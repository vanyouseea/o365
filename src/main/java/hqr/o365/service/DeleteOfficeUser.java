package hqr.o365.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import hqr.o365.dao.TaOfficeInfoRepo;
import hqr.o365.domain.TaOfficeInfo;

@Service
public class DeleteOfficeUser {
	
	private RestTemplate restTemplate = new RestTemplate();
	
	@Autowired
	private TaOfficeInfoRepo repo;
	
	@Autowired
	private ValidateAppInfo vai;
	
	@Value("${UA}")
    private String ua;

	@CacheEvict(value= {"cacheOfficeUser","cacheOfficeUserSearch","cacheLicense"}, allEntries = true)
	public HashMap<String, int[]> deleteUser(String uids) {
		String uidArr[] = uids.split(",");
		int succ = 0;
		int fail = 0;
		//get info
		List<TaOfficeInfo> list = repo.findBySelected("是");
		if(list!=null&&list.size()>0) {
			TaOfficeInfo ta = list.get(0);
			String accessToken = "";
			if(vai.checkAndGet(ta.getTenantId(), ta.getAppId(), ta.getSecretId())) {
				accessToken = vai.getAccessToken();
			}
			if(!"".equals(accessToken)) {
				for (String uid : uidArr) {
					String endpoint = "https://graph.microsoft.com/v1.0/users/"+uid;
					HttpHeaders headers = new HttpHeaders();
					headers.set(HttpHeaders.USER_AGENT, ua);
					String body = "";
					headers.add("Authorization", "Bearer "+accessToken);
					HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
					try {
						ResponseEntity<String> response = restTemplate.exchange(endpoint, HttpMethod.DELETE, requestEntity, String.class);
						if(response.getStatusCodeValue()==204) {
							succ ++;
						}
						else {
							fail ++;
						}
					}
					catch (Exception e) {
						e.printStackTrace();
						fail ++;
					}
				}
			}
			else {
				succ = 0;
				fail = uidArr.length;
			}
		}
		else {
			succ = 0;
			fail = uidArr.length;
		}
		
		HashMap<String, int[]> map = new HashMap<String, int[]>();
		int [] overall = new int[] {succ, fail};
		map.put("delete_res", overall);
		return map;
	}
	
}
