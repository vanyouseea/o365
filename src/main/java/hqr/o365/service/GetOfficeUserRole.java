package hqr.o365.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import hqr.o365.dao.TaOfficeInfoRepo;
import hqr.o365.domain.TaOfficeInfo;

@Service
public class GetOfficeUserRole {
	private RestTemplate restTemplate = new RestTemplate();
	
	@Autowired
	private TaOfficeInfoRepo repo;
	
	@Autowired
	private ValidateAppInfo vai;
	
	@Value("${UA}")
    private String ua;

	@Cacheable(value="cacheUserRole")
	public String getRole(String uid) {
		String role = "";
		List<TaOfficeInfo> list = repo.findBySelected("是");
		if(list!=null&&list.size()>0) {
			TaOfficeInfo ta = list.get(0);
			String accessToken = "";
			if(vai.checkAndGet(ta.getTenantId(), ta.getAppId(), ta.getSecretId())) {
				accessToken = vai.getAccessToken();
			}
			
			if(!"".equals(accessToken)) {
				String endpoint = "https://graph.microsoft.com/v1.0/directoryObjects/"+uid+"/getMemberObjects";
				HttpHeaders headers = new HttpHeaders();
				headers.set(HttpHeaders.USER_AGENT, ua);
				headers.add("Authorization", "Bearer "+accessToken);
				headers.setContentType(MediaType.APPLICATION_JSON);
				String body = "{\"securityEnabledOnly\": true}";
				HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
				try {
					ResponseEntity<String> response= restTemplate.postForEntity(endpoint, requestEntity, String.class);
					if(response.getStatusCodeValue()==200) {
						JSONObject jo = JSON.parseObject(response.getBody());
						JSONArray ja = jo.getJSONArray("value");
						for (Object roleId : ja) {
							endpoint = "https://graph.microsoft.com/v1.0/directoryRoles/"+roleId.toString()+"?$select=displayName";
							HttpHeaders headers2 = new HttpHeaders();
							headers2.set(HttpHeaders.USER_AGENT, ua);
							headers2.add("Authorization", "Bearer "+accessToken);
							String body2 = "{\"securityEnabledOnly\": true}";
							HttpEntity<String> requestEntity2 = new HttpEntity<String>(body2, headers2);
							try {
								ResponseEntity<String> response2 = restTemplate.exchange(endpoint, HttpMethod.GET, requestEntity2, String.class);
								if(response2.getStatusCodeValue()==200) {
									JSONObject jo2 = JSON.parseObject(response2.getBody());
									role = role + jo2.getString("displayName") + ",";
								}
							}
							catch (Exception e) {
								e.printStackTrace();
							}
						}
						if(!"".equals(role)) {
							String str = role.substring(role.length()-1);
							if(",".equals(str)) {
								role = role.substring(0, role.length()-1);
							}
						}
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return role;
	}
	
}
