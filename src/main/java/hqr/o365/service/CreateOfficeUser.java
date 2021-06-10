package hqr.o365.service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;

import hqr.o365.dao.TaMasterCdRepo;
import hqr.o365.dao.TaOfficeInfoRepo;
import hqr.o365.domain.OfficeUser;
import hqr.o365.domain.TaMasterCd;
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
public class CreateOfficeUser {
	private RestTemplate restTemplate = new RestTemplate();
	
	@Autowired
	private TaOfficeInfoRepo repo;
	
	@Autowired
	private ValidateAppInfo vai;
	
	@Autowired
	private TaMasterCdRepo tmr;
	
	@Autowired
	private GetOrganizationInfo goi;
	
	@Value("${UA}")
    private String ua;
	
	public HashMap<String, String> createCommonUser(String mailNickname, String userPrincipalName, String displayName, String licenses, String userPwd){
		HashMap<String, String> map = new HashMap<String, String>();
		String forceInd = "Y";
		Optional<TaMasterCd> opt = tmr.findById("FORCE_CHANGE_PASSWORD");
		if(opt.isPresent()) {
			TaMasterCd cd = opt.get();
			forceInd = cd.getCd();
		}

		OfficeUser ou = new OfficeUser();
		ou.setMailNickname(mailNickname);
		ou.setUserPrincipalName(userPrincipalName);
		ou.setDisplayName(displayName);
		ou.getPasswordProfile().setPassword(userPwd);
		if(!"Y".equals(forceInd)) {
			ou.getPasswordProfile().setForceChangePasswordNextSignIn(false);
		}
		String message = "";
		
		//get info
		List<TaOfficeInfo> list = repo.getSelectedApp();
		if(list!=null&&list.size()>0) {
			TaOfficeInfo ta = list.get(0);
			String accessToken = "";
			if(vai.checkAndGet(ta.getTenantId(), ta.getAppId(), ta.getSecretId())) {
				accessToken = vai.getAccessToken();
			}
			
			if(!"".equals(accessToken)) {
				//set usage location
				ou.setUsageLocation(goi.getUsageLocation(accessToken));
				String createUserJson = JSON.toJSONString(ou);
				
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
						message = "成功创建用户"+ou.getUserPrincipalName()+"<br>";
						map.put("status", "0");
						map.put("message", message);
						System.out.println( "成功创建用户："+ou.getUserPrincipalName());
						
						if(licenses!=null&&!"".equals(licenses)) {
							Thread.sleep(200);
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
										message = message + "成功分配订阅："+license + "<br>";
										map.put("message", message);
									}
									else {
										message = message + "分配订阅："+license+" 时失败<br>";
										map.put("message", message);
									}
								}
								catch (Exception e) {
									e.printStackTrace();
									map.put("message", message + "无法分配订阅 "+e.toString());
								}
							}
						}
					}
					else {
						map.put("status", "1");
						map.put("message", "失败，创建用户未能获得预期的返回值201");
					}
				}
				catch (Exception e) {
					e.printStackTrace();
					map.put("status", "1");
					map.put("message", "无法创建用户 "+e.toString());
				}
			}
			else {
				map.put("status", "1");
				map.put("message", "获取token失败，请确认全局的有效性");
			}
		}
		else {
			map.put("status", "1");
			map.put("message", "请先选择一个全局");
		}
		
		return map;
	}
	
}
