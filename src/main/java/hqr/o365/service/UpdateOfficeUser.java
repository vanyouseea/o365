package hqr.o365.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import hqr.o365.dao.TaOfficeInfoRepo;
import hqr.o365.domain.TaOfficeInfo;

@Service
public class UpdateOfficeUser {
	//need use new restTemplate to support the PATCH method
	private RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
	
	@Autowired
	private TaOfficeInfoRepo repo;
	
	@Autowired
	private ValidateAppInfo vai;
	
	public boolean patchOfficeUser(String uid, String accountEnabled) {
		boolean flag = false;
		List<TaOfficeInfo> list = repo.getSelectedApp();
		if(list!=null&&list.size()>0) {
			TaOfficeInfo ta = list.get(0);
			String accessToken = "";
			if(vai.checkAndGet(ta.getTenantId(), ta.getAppId(), ta.getSecretId())) {
				accessToken = vai.getAccessToken();
			}
			
			if(!"".equals(accessToken)) {
				String endpoint = "https://graph.microsoft.com/v1.0/users/"+uid;
				HttpHeaders headers = new HttpHeaders();
				headers.add("Authorization", "Bearer "+accessToken);
				headers.setContentType(MediaType.APPLICATION_JSON);
				String body = "{\"accountEnabled\": \""+accountEnabled+"\"}";
				HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
				try {
					ResponseEntity<String> response= restTemplate.exchange(endpoint, HttpMethod.PATCH, requestEntity, String.class);
					if(response.getStatusCodeValue()==204) {
						flag =true;
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return flag;
	}
	
}
