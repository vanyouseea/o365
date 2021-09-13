package hqr.o365.service;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import hqr.o365.dao.TaAppRptRepo;
import hqr.o365.domain.TaAppRpt;

@Service
public class GetOnedriveRpt {
	
	private RestTemplate restTemplate = new RestTemplate();
	
	@Autowired
	private TaAppRptRepo tarr;
	
	@Autowired
	private ValidateAppInfo vai;
	
	@Value("${UA}")
    private String ua;
	
	public boolean execute(int seqNo) {
		boolean flag = false;
		Optional<TaAppRpt> opt = tarr.findById(seqNo);
		if(opt.isPresent()) {
			TaAppRpt taAppRpt = opt.get();
			String accessToken = "";
			if(vai.checkAndGet(taAppRpt.getTenantId(), taAppRpt.getAppId(), taAppRpt.getSecretId())) {
				accessToken = vai.getAccessToken();
			}
			if(!"".equals(accessToken)) {
				String endpoint = "https://graph.microsoft.com/v1.0/reports/getOneDriveUsageAccountDetail(period='D30')";
				HttpHeaders headers = new HttpHeaders();
				headers.set(HttpHeaders.USER_AGENT, ua);
				headers.add("Authorization", "Bearer "+accessToken);
				String body="";
				HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
				try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("onedrive_details.csv"), "GB2312"))) {
					ResponseEntity<String> response= restTemplate.exchange(endpoint, HttpMethod.GET, requestEntity, String.class);
					if(response.getStatusCodeValue()==200) {
						String text = response.getBody();
						bw.write(text);
						bw.flush();
						flag = true;
					}
				}
				catch (Exception e) {
				}
			}
		}
		return flag;
	}
}
