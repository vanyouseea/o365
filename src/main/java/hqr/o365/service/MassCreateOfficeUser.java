package hqr.o365.service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
public class MassCreateOfficeUser {
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
	
	private final String SEED = "0123456789abcdefghijklmnopqrstuvwxyz";
	
	private SecureRandom ran = new SecureRandom();
	
	@CacheEvict(value= {"cacheOfficeUser","cacheOfficeUserSearch", "cacheLicense"}, allEntries = true)
	public HashMap<String, int[]> createCommonUser(String prefix, String domain, String licenses, String userPwd, int count, String strategy){
		String forceInd = "Y";
		Optional<TaMasterCd> opt = tmr.findById("FORCE_CHANGE_PASSWORD");
		if(opt.isPresent()) {
			TaMasterCd cd = opt.get();
			forceInd = cd.getCd();
		}
		
		int userSucc = 0;
		int userFail = 0; 
		
		int licenseSucc = 0;
		int licenseFail = 0;
		
		//get info
		List<TaOfficeInfo> list = repo.findBySelected("是");
		if(list!=null&&list.size()>0) {
			TaOfficeInfo ta = list.get(0);
			String accessToken = "";
			if(vai.checkAndGet(ta.getTenantId(), ta.getAppId(), ta.getSecretId())) {
				accessToken = vai.getAccessToken();
			}
			
			if(!"".equals(accessToken)) {
				//sample user
				OfficeUser ou = new OfficeUser();
				ou.setMailNickname("");
				ou.setUserPrincipalName("");
				ou.setDisplayName("");
				ou.getPasswordProfile().setPassword(userPwd);
				if(!"Y".equals(forceInd)) {
					ou.getPasswordProfile().setForceChangePasswordNextSignIn(false);
				}
				//set usage location
				ou.setUsageLocation(goi.getUsageLocation(accessToken));
				
				List<String> userList = new ArrayList<String>();
				//create users
				for(int massCount=0; massCount<count; massCount++) {
					//st1 = gen 5 random character
					//st2 = gen by sequence
					StringBuilder sb = new StringBuilder();
					if("st1".equals(strategy)) {
						char c1 = SEED.charAt(ran.nextInt(SEED.length()-1));
						char c2 = SEED.charAt(ran.nextInt(SEED.length()-1));
						char c3 = SEED.charAt(ran.nextInt(SEED.length()-1));
						char c4 = SEED.charAt(ran.nextInt(SEED.length()-1));
						char c5 = SEED.charAt(ran.nextInt(SEED.length()-1));
						sb.append(prefix).append(c1).append(c2).append(c3).append(c4).append(c5);
					}
					else if("st2".equals(strategy)) {
						String addon = padLeft(String.valueOf(massCount), '0', String.valueOf(count).length());
						sb.append(prefix).append(addon);
					}
					else {
						String addon = padLeft(String.valueOf(massCount), '0', String.valueOf(count).length());
						sb.append(prefix).append(addon);
					}
					String mailNickname = sb.toString();
					String userPrincipalName = mailNickname + domain;
					String displayName = sb.toString();
					
					System.out.println("mailNickname:"+mailNickname+"\t"+userPrincipalName);
					
					ou.setMailNickname(mailNickname);
					ou.setUserPrincipalName(userPrincipalName);
					ou.setDisplayName(displayName);
					
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
							userSucc ++;
							System.out.println( "成功创建用户："+ou.getUserPrincipalName());
							userList.add(ou.getUserPrincipalName());
						}
						else {
							userFail++;
							if(licenses!=null&&!"".equals(licenses)) {
								String acs [] = licenses.split(",");
								for (String license : acs) {
									licenseFail++;
								}
							}
						}
					}
					catch (Exception e) {
						e.printStackTrace();
						userFail++;
						if(licenses!=null&&!"".equals(licenses)) {
							String acs [] = licenses.split(",");
							for (String license : acs) {
								licenseFail++;
							}
						}
					}
				}
				
				if(licenses!=null&&!"".equals(licenses)) {
					try {
						Thread.sleep(400);
					}
					catch (Exception e) {}
					System.out.println("开始分配订阅："+licenses);
					for(String userId : userList) {
						String acs [] = licenses.split(",");
						for (String license : acs) {
							String licenseJson = "{\"addLicenses\": [{\"disabledPlans\": [],\"skuId\": \""+license+"\",}],\"removeLicenses\": [ ]}";
							
							String endpoint = "https://graph.microsoft.com/v1.0/users/"+userId+"/assignLicense";
							
							HttpHeaders headers2 = new HttpHeaders();
							headers2.set(HttpHeaders.USER_AGENT, ua);
							headers2.add("Authorization", "Bearer "+accessToken);
							headers2.setContentType(MediaType.APPLICATION_JSON);
							HttpEntity<String> requestEntity2 = new HttpEntity<String>(licenseJson, headers2);
							
							try {
								ResponseEntity<String> response2= restTemplate.postForEntity(endpoint, requestEntity2, String.class);
								if(response2.getStatusCodeValue()==200) {
									response2.getBody();
									licenseSucc ++;
								}
								else {
									licenseFail++;
								}
							}
							catch (Exception e) {
								licenseFail++;
							}
						}
						Optional<TaMasterCd> cgfu =  tmr.findById("CREATE_GRP_FOR_USER");
						if(cgfu.isPresent()) {
							if("Y".equals(cgfu.get().getCd())) {
								try {
									Thread.sleep(200);
								}
								catch (Exception e) {}
								createGroupForUser(userId.split("@")[0], userId, accessToken);
							}
						}
					}
				}
				
			}
			else {
				userFail = count;
			}
		}
		else {
			userFail = count;
		}
		
		HashMap<String, int[]> map = new HashMap<String, int[]>();
		int [] overall1 = new int[] {userSucc+userFail, userSucc, userFail};
		int [] overall2 = new int[] {licenseSucc+licenseFail, licenseSucc, licenseFail};
		map.put("user_res", overall1);
		map.put("license_res", overall2);
		
		return map;
	}
	
	private String padLeft(String in, char pad, int size) {
        StringBuffer str = in==null?new StringBuffer(""):new StringBuffer(in.trim());
        int a = str.length();
        str.ensureCapacity(size);
        a = size - a;
        while((a--) > 0)
            str.insert(0,pad);
        return str.toString();
    }
	
	/*
	 * Json for create group (when create group, SP site will be created automatically later)
		{
		    "expirationDateTime": null,
		    "groupTypes": [
		        "Unified"
		    ],
		    "mailEnabled": false,
		    "mailNickname": mailNickname,
		    "securityEnabled": false,
		    "visibility": "private",
			"owners@odata.bind": [
				"https://graph.microsoft.com/v1.0/users/userPrincipalName"
			]
		}
	 */
	public void createGroupForUser(String mailNickname, String userPrincipalName, String accessToken) {
		String json = "{\"displayName\": \""+mailNickname+"\",\"expirationDateTime\": null,\"groupTypes\": [\"Unified\"],\"mailEnabled\": false,\"mailNickname\": \""+mailNickname+"\",\"securityEnabled\": false,\"visibility\": \"private\",\"owners@odata.bind\": [\"https://graph.microsoft.com/v1.0/users/"+userPrincipalName+"\"]}";
		
		String endpoint = "https://graph.microsoft.com/v1.0/groups";
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.USER_AGENT, ua);
		headers.add("Authorization", "Bearer "+accessToken);
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
		try {
			ResponseEntity<String> response = restTemplate.postForEntity(endpoint, requestEntity, String.class);
			if(response.getStatusCodeValue()==201) {
				System.out.println("成功创建Group:"+mailNickname);
				response.getBody();
			}
			else {
				System.out.println("fail to create the group for user "+userPrincipalName);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
