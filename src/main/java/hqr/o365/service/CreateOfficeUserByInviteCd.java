package hqr.o365.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

import com.alibaba.fastjson.JSON;

import hqr.o365.dao.TaInviteInfoRepo;
import hqr.o365.dao.TaOfficeInfoRepo;
import hqr.o365.domain.OfficeUser;
import hqr.o365.domain.TaInviteInfo;
import hqr.o365.domain.TaOfficeInfo;

/*
 * {
	  "addLicenses": [
	    {
	      "disabledPlans": [ ],
	      "skuId": "guid",
	    }
	  ],
	  "removeLicenses": [ ]
   }
 */
@Service
public class CreateOfficeUserByInviteCd {
	private RestTemplate restTemplate = new RestTemplate();
	
	@Autowired
	private TaOfficeInfoRepo repo;
	
	@Autowired
	private ValidateAppInfo vai;
	
	@Autowired
	private TaInviteInfoRepo tii;
	
	@Autowired
	private GetOrganizationInfo goi;
	
	@Value("${UA}")
    private String ua;
	
	@CacheEvict(value= {"cacheOfficeUser","cacheInviteInfo","cacheOfficeUserSearch"}, allEntries = true)
	public String createCommonUser(String mailNickname, String displayName, String inviteCd, String password){
		String resultMsg = "失败";
		Optional<TaInviteInfo> opt = tii.findById(inviteCd);
		if(opt.isPresent()) {
			TaInviteInfo tiiDo = opt.get();
			
			Date currentDt = new Date();
			Date startDt = tiiDo.getStartDt();
			Date endDt = tiiDo.getEndDt();
			
			if(startDt!=null&&startDt.after(currentDt)) {
				resultMsg = "此邀请码尚未生效";
				return resultMsg;
			}
			
			if(endDt!=null&&endDt.before(currentDt)) {
				resultMsg = "此邀请码已过期";
				return resultMsg;
			}
			
			if("1".equals(tiiDo.getInviteStatus())){
				//update the invite code to in-progress
				tiiDo.setInviteStatus("2");
				tii.save(tiiDo);
				
				Optional<TaOfficeInfo> opt1 = repo.findById(tiiDo.getSeqNo());
				if(opt1.isPresent()) {
					//get info
					TaOfficeInfo ta = opt1.get();
					String accessToken = "";
					if(vai.checkAndGet(ta.getTenantId(), ta.getAppId(), ta.getSecretId())) {
						accessToken = vai.getAccessToken();
					}
					
					if(!"".equals(accessToken)) {
						String userPrincipalName = mailNickname + tiiDo.getSuffix();
						OfficeUser ou = new OfficeUser();
						ou.setMailNickname(mailNickname);
						ou.setUserPrincipalName(userPrincipalName);
						ou.setDisplayName(displayName);
						ou.getPasswordProfile().setPassword(password);
						ou.getPasswordProfile().setForceChangePasswordNextSignIn(true);
						//set usage location
						ou.setUsageLocation(goi.getUsageLocation(accessToken));
						String createUserJson = JSON.toJSONString(ou);
						
						//1. create user
						String endpoint = "https://graph.microsoft.com/v1.0/users";
						HttpHeaders headers = new HttpHeaders();
						headers.set(HttpHeaders.USER_AGENT, ua);
						headers.add("Authorization", "Bearer "+accessToken);
						headers.setContentType(MediaType.APPLICATION_JSON);
						HttpEntity<String> requestEntity = new HttpEntity<String>(createUserJson, headers);
						try {
							ResponseEntity<String> response= restTemplate.postForEntity(endpoint, requestEntity, String.class);
							if(response.getStatusCodeValue()==201) {
								response.getBody();
								System.out.println( "成功创建用户："+ou.getUserPrincipalName());
								Thread.sleep(500);
							}
							else {
								tiiDo.setResult("创建用户出错 1");
								tiiDo.setInviteStatus("4");
								tii.save(tiiDo);
								resultMsg = "创建用户出错";
								return resultMsg;
							}
						}
						catch (Exception e) {
							if(e instanceof BadRequest) {
								String responeBody = ((BadRequest) e).getResponseBodyAsString();
								System.out.println(responeBody);
								if(responeBody.indexOf("same value")>=0) {
									tiiDo.setResult("此前缀已存在，请选择一个另外的前缀");
									tiiDo.setInviteStatus("1");
									tii.save(tiiDo);
									resultMsg = "此前缀已存在，请选择一个另外的前缀";
									return resultMsg;
								}
								else {
									tiiDo.setResult("创建用户出错 2");
									tiiDo.setInviteStatus("4");
									tii.save(tiiDo);
									resultMsg = "创建用户出错";
									return resultMsg;
								}
							}
							else {
								tiiDo.setResult("创建用户出错 3");
								tiiDo.setInviteStatus("4");
								tii.save(tiiDo);
								resultMsg = "创建用户出错";
								return resultMsg;
							}
						}
						
						//2. assign license
						String licenses = tiiDo.getLicenses();
						if(licenses!=null&&!"".equals(licenses)) {
							System.out.println("开始分配订阅："+licenses);
							String acs [] = licenses.split(",");
							for (String license : acs) {
								String licenseJson = "{\"addLicenses\": [{\"disabledPlans\": [],\"skuId\": \""+license+"\",}],\"removeLicenses\": [ ]}";
								
								endpoint = "https://graph.microsoft.com/v1.0/users/"+ou.getUserPrincipalName()+"/assignLicense";
								
								HttpHeaders headers2 = new HttpHeaders();
								headers2.set(HttpHeaders.USER_AGENT, ua);
								headers2.add("Authorization", "Bearer "+accessToken);
								headers2.setContentType(MediaType.APPLICATION_JSON);
								HttpEntity<String> requestEntity2 = new HttpEntity<String>(licenseJson, headers2);
								
								try {
									ResponseEntity<String> response2= restTemplate.postForEntity(endpoint, requestEntity2, String.class);
									if(response2.getStatusCodeValue()==200) {
										response2.getBody();
										tiiDo.setResult(ou.getUserPrincipalName()+"|"+password);
										tiiDo.setInviteStatus("3");
										tii.save(tiiDo);
										resultMsg = "0|"+ou.getUserPrincipalName();
									}
									else {
										tiiDo.setResult("分配订阅时出错 1");
										tiiDo.setInviteStatus("4");
										tii.save(tiiDo);
										resultMsg = "分配订阅时出错";
										return resultMsg;
									}
								}
								catch (Exception e) {
									tiiDo.setResult("分配订阅时出错 2");
									tiiDo.setInviteStatus("4");
									tii.save(tiiDo);
									resultMsg = "分配订阅时出错";
									return resultMsg;
								}
							}
						}
						else {
							tiiDo.setResult(ou.getUserPrincipalName()+"|"+password);
							tiiDo.setInviteStatus("3");
							tii.save(tiiDo);
							resultMsg = "0|"+ou.getUserPrincipalName();
						}
					}
					else {
						tiiDo.setResult("无效的全局");
						tiiDo.setInviteStatus("4");
						tii.save(tiiDo);
						resultMsg = "无效的全局";
					}
				}
				else {
					tiiDo.setResult("不存在的全局");
					tiiDo.setInviteStatus("4");
					tii.save(tiiDo);
					resultMsg = "不存在的全局";
				}
			}
			else if("2".equals(tiiDo.getInviteStatus())){
				resultMsg = "此邀请码正被使用中";
			}
			else if("3".equals(tiiDo.getInviteStatus())){
				resultMsg = "此邀请码已使用";
			}
			else if("4".equals(tiiDo.getInviteStatus())){
				resultMsg = "此邀请码使用出现错误";
			}
			else {
				resultMsg = "无效的邀请码状态";
			}
		}
		else {
			resultMsg = "无效的邀请码";
		}
		
		return resultMsg;
	}
}
