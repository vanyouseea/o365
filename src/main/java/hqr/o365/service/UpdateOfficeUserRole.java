package hqr.o365.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import hqr.o365.dao.TaMasterCdRepo;
import hqr.o365.dao.TaOfficeInfoRepo;
import hqr.o365.domain.TaMasterCd;
import hqr.o365.domain.TaOfficeInfo;

@Service
public class UpdateOfficeUserRole {
	//need use new restTemplate to support the PATCH method
	private RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
	
	@Autowired
	private TaOfficeInfoRepo repo;
	
	@Autowired
	private ValidateAppInfo vai;
	
	@Autowired
	private TaMasterCdRepo tmc;
	
	public boolean update(String uid, String action) {
		boolean flag = false;
		
		//init the role
		String roleId = "7d6ae5b8-219a-432f-ac24-4c3054921c50";
		Optional<TaMasterCd> top1 = tmc.findById("DEFAULT_ADMIN_ROLE_ID");
		if(top1.isPresent()) {
			roleId = top1.get().getCd();
		}
		
		List<TaOfficeInfo> list = repo.getSelectedApp();
		if(list!=null&&list.size()>0) {
			TaOfficeInfo ta = list.get(0);
			String accessToken = "";
			if(vai.checkAndGet(ta.getTenantId(), ta.getAppId(), ta.getSecretId())) {
				accessToken = vai.getAccessToken();
			}
			if(!"".equals(accessToken)) {
				String endpoint = "";
				//grant
				if("P".equals(action)) {
					endpoint = "https://graph.microsoft.com/v1.0/directoryRoles/roleTemplateId="+roleId+"/members/$ref";
					HttpHeaders headers = new HttpHeaders();
					headers.add("Authorization", "Bearer "+accessToken);
					headers.setContentType(MediaType.APPLICATION_JSON);
					String body = "{\"@odata.id\": \"https://graph.microsoft.com/v1.0/directoryObjects/"+uid+"\"}";
					HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
					try {
						ResponseEntity<String> response= restTemplate.postForEntity(endpoint, requestEntity, String.class);
						if(response.getStatusCodeValue()==204) {
							flag = true;
						}
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
				//revoke
				else {
					endpoint = "https://graph.microsoft.com/v1.0/directoryRoles/roleTemplateId="+roleId+"/members/"+uid+"/$ref";
					HttpHeaders headers = new HttpHeaders();
					headers.add("Authorization", "Bearer "+accessToken);
					headers.setContentType(MediaType.APPLICATION_JSON);
					String body = "";
					HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
					try {
						ResponseEntity<String> response= restTemplate.exchange(endpoint, HttpMethod.DELETE, requestEntity, String.class);
						if(response.getStatusCodeValue()==204) {
							flag = true;
						}
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		return flag;
	}
}
