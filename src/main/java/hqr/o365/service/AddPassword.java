package hqr.o365.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
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
public class AddPassword {
	private RestTemplate restTemplate = new RestTemplate();
	
	@Autowired
	private ValidateAppInfo vai;
	
	@Autowired
	private TaOfficeInfoRepo repo;
	
	@Value("${UA}")
    private String ua;
	
	@CacheEvict(value="cacheOfficeInfo", allEntries = true)
	public HashMap<String, String> add(int seqNo, String tenantId, String appId, String secretId) {
		HashMap<String, String> map = new HashMap<String, String>();
		
		vai.checkAndGet(tenantId, appId, secretId);
		String accessToken = vai.getAccessToken();
		
		if(!"".equals(accessToken)) {
			String endpoint = "https://graph.microsoft.com/v1.0/applications?$select=id,appId";
			HttpHeaders headers = new HttpHeaders();
			headers.set(HttpHeaders.USER_AGENT, ua);
			headers.add("Authorization", "Bearer "+accessToken);
			String body="";
			HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
			try {
				ResponseEntity<String> response= restTemplate.exchange(endpoint, HttpMethod.GET, requestEntity, String.class);
				
				if(response.getStatusCodeValue()==200) {
					JSONObject jo2 = JSON.parseObject(response.getBody());
					JSONArray ja = jo2.getJSONArray("value");
					String objId = "";
					String tmpAppId = "";
					for (Object object : ja) {
						JSONObject jb = (JSONObject)object;
						objId = jb.getString("id");
						tmpAppId = jb.getString("appId");
						if(tmpAppId.equals(appId)) {
							System.out.println("found it "+tmpAppId);
							break;
						}
					}
					
					endpoint = "https://graph.microsoft.com/v1.0/applications/"+objId+"/addPassword";
					headers.setContentType(MediaType.APPLICATION_JSON);
					String json = "{\"passwordCredential\": {\"displayName\": \""+new Date().getTime()+"\",\"endDateTime\": \"2099-12-31T06:30:23.9074306Z\" }}";
					HttpEntity<String> requestEntity2 = new HttpEntity<String>(json, headers);
					ResponseEntity<String> response2= restTemplate.postForEntity(endpoint, requestEntity2, String.class);
					
					if(response2.getStatusCodeValue()==200) {
						JSONObject jo3 = JSON.parseObject(response2.getBody());
						String newSecretId = jo3.getString("secretText");
						Optional<TaOfficeInfo> opt = repo.findById(seqNo);
						if(opt.isPresent()) {
							TaOfficeInfo ta = opt.get();
							ta.setSecretId(newSecretId);
							repo.saveAndFlush(ta);
							map.put("status", "0");
							map.put("message", "succ");
						}
						else {
							map.put("status", "1");
							map.put("message", "Invalid seqNo "+ seqNo);
						}
					}
					else {
						map.put("status", "1");
						map.put("message", response2.getBody());
					}
				}
				else {
					map.put("status", "1");
					map.put("message", response.getBody());
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				map.put("status", "2");
				map.put("message", e.toString());
			}
		}
		else {
			map.put("status", "1");
			map.put("message", "Fail to get the token ");
		}
		
		return map;
	}
	
}
