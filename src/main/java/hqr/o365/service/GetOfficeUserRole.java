package hqr.o365.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import hqr.o365.dao.TaMasterCdRepo;
import hqr.o365.dao.TaOfficeInfoRepo;
import hqr.o365.domain.TaMasterCd;
import hqr.o365.domain.TaOfficeInfo;

@Service
public class GetOfficeUserRole {
	private RestTemplate restTemplate = new RestTemplate();
	
	@Autowired
	private TaOfficeInfoRepo repo;
	
	@Autowired
	private ValidateAppInfo vai;
	
	@Autowired
	private TaMasterCdRepo tmc;
	
	public String getRole(String uid) {
		String role = "";
		List<TaOfficeInfo> list = repo.getSelectedApp();
		if(list!=null&&list.size()>0) {
			TaOfficeInfo ta = list.get(0);
			String accessToken = "";
			if(vai.checkAndGet(ta.getTenantId(), ta.getAppId(), ta.getSecretId())) {
				accessToken = vai.getAccessToken();
			}
			
			if(!"".equals(accessToken)) {
				String endpoint = "https://graph.microsoft.com/v1.0/directoryObjects/"+uid+"/getMemberObjects";
				HttpHeaders headers = new HttpHeaders();
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
							Optional<TaMasterCd> opt = tmc.findById(roleId.toString());
							if(opt.isPresent()) {
								TaMasterCd tm = opt.get();
								role = role + tm.getCd() + "<br>";
							}
							else {
								role = role + roleId + "<br>";
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
