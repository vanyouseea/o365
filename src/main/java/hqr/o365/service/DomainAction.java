package hqr.o365.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException.BadRequest;
import org.springframework.web.client.RestTemplate;

import hqr.o365.dao.TaOfficeInfoRepo;
import hqr.o365.domain.TaOfficeInfo;

@Service
public class DomainAction {
	
	@Autowired
	private TaOfficeInfoRepo repo;
	
	@Autowired
	private GetDomainInfo2 gdi2;
	
	@Autowired
	private ValidateAppInfo vai;
	
	private RestTemplate restTemplate = new RestTemplate();
	
	@Value("${UA}")
    private String ua;
	
	@CacheEvict(value= {"cacheDomain"}, allEntries = true)
	public boolean createDomain(String domain) {
		boolean res = false;
		
		String allDomains = gdi2.getAllDomains();
		if(allDomains.indexOf(domain+" - unverified")>=0) {
			System.out.println("old unverified domain, skip create domain action");
			return true;
		}
		
		List<TaOfficeInfo> list = repo.getSelectedApp();
		if(list!=null&&list.size()>0) {
			TaOfficeInfo ta = list.get(0);
			String accessToken = "";
			if(vai.checkAndGet(ta.getTenantId(), ta.getAppId(), ta.getSecretId())) {
				accessToken = vai.getAccessToken();
			}
			
			if(!"".equals(accessToken)) {
				String endpoint = "https://graph.microsoft.com/v1.0/domains";
				HttpHeaders headers = new HttpHeaders();
				headers.set(HttpHeaders.USER_AGENT, ua);
				headers.add("Authorization", "Bearer "+accessToken);
				headers.setContentType(MediaType.APPLICATION_JSON);
				
				String json="{\"id\": \""+domain+"\"}";
				System.out.println(json);
				
				HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
				try {
					ResponseEntity<String> response= restTemplate.postForEntity(endpoint, requestEntity, String.class);

					if(response.getStatusCodeValue()==201) {
						res = true;
						System.out.println("Created domain "+domain+" done");
					}
				}
				catch (Exception e) {
					e.printStackTrace();
					if(e instanceof BadRequest) {
						
					}
				}
			}
		}
		
		return res;
	}
	
	public String verificationDnsRecords(String domain) {
		String dnsRecords = "fail";
		
		List<TaOfficeInfo> list = repo.getSelectedApp();
		if(list!=null&&list.size()>0) {
			TaOfficeInfo ta = list.get(0);
			String accessToken = "";
			if(vai.checkAndGet(ta.getTenantId(), ta.getAppId(), ta.getSecretId())) {
				accessToken = vai.getAccessToken();
			}
			
			if(!"".equals(accessToken)) {
				String endpoint = "https://graph.microsoft.com/v1.0/domains/scmw.org/verificationDnsRecords";
				HttpHeaders headers = new HttpHeaders();
				headers.set(HttpHeaders.USER_AGENT, ua);
				headers.add("Authorization", "Bearer "+accessToken);
				headers.setContentType(MediaType.APPLICATION_JSON);
				
				String json="";
				
				HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
				try {
					ResponseEntity<String> response= restTemplate.exchange(endpoint, HttpMethod.GET, requestEntity, String.class);

					if(response.getStatusCodeValue()==200) {
						dnsRecords = response.getBody();
						System.out.println("Get verfiy dnsrecord:"+dnsRecords);
					}
				}
				catch (Exception e) {
					System.out.println(e.toString());
				}
			}
		}
		
		return dnsRecords;
	}
	
	@CacheEvict(value= {"cacheDomain"}, allEntries = true)
	public String verifyDomain(String domain) {
		String res = "fail";
		
		String arr[] = domain.split(" - ");
		
		List<TaOfficeInfo> list = repo.getSelectedApp();
		if(list!=null&&list.size()>0) {
			TaOfficeInfo ta = list.get(0);
			String accessToken = "";
			if(vai.checkAndGet(ta.getTenantId(), ta.getAppId(), ta.getSecretId())) {
				accessToken = vai.getAccessToken();
			}
			
			if(!"".equals(accessToken)) {
				String endpoint = "https://graph.microsoft.com/v1.0/domains/"+arr[0]+"/verify";
				HttpHeaders headers = new HttpHeaders();
				headers.set(HttpHeaders.USER_AGENT, ua);
				headers.add("Authorization", "Bearer "+accessToken);
				headers.setContentType(MediaType.APPLICATION_JSON);
				
				String json="";
				
				HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
				try {
					ResponseEntity<String> response= restTemplate.postForEntity(endpoint, requestEntity, String.class);

					if(response.getStatusCodeValue()==200) {
						res = "succ";
						System.out.println("domain "+domain+" verified");
					}
				}
				catch (Exception e) {
					System.out.println(e.toString());
				}
			}
		}
		
		return res;
	}
	
	@CacheEvict(value= {"cacheDomain"}, allEntries = true)
	public boolean deleteDomain(String domain) {
		boolean res = false;
		
		String arr[] = domain.split(" - ");
		if(arr[0]==null||arr[0].endsWith(".onmicrosoft.com")) {
			return false;
		}
		
		List<TaOfficeInfo> list = repo.getSelectedApp();
		if(list!=null&&list.size()>0) {
			TaOfficeInfo ta = list.get(0);
			String accessToken = "";
			if(vai.checkAndGet(ta.getTenantId(), ta.getAppId(), ta.getSecretId())) {
				accessToken = vai.getAccessToken();
			}
			
			if(!"".equals(accessToken)) {
				String endpoint = "https://graph.microsoft.com/v1.0/domains/"+arr[0]+"/forceDelete";
				HttpHeaders headers = new HttpHeaders();
				headers.set(HttpHeaders.USER_AGENT, ua);
				headers.add("Authorization", "Bearer "+accessToken);
				headers.setContentType(MediaType.APPLICATION_JSON);
				
				String json="";
				
				HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
				try {
					ResponseEntity<String> response= restTemplate.postForEntity(endpoint, requestEntity, String.class);

					if(response.getStatusCodeValue()==204) {
						res = true;
						System.out.println("Deleted domain "+domain+" done");
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return res;
	}
	
}
