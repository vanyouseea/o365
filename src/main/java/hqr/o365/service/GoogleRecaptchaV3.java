package hqr.o365.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Service
public class GoogleRecaptchaV3 {
	
	private RestTemplate restTemplate = new RestTemplate();
	
	@Value("${UA}")
    private String ua;
	
	public boolean verify(String token) {
		boolean res = false;
		
		String endpoint = "https://www.google.com/recaptcha/api/siteverify?secret=6LcN96MZAAAAAKTsXbR831q6ELKtm6qA3XlXed6J&response="+token;
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.USER_AGENT, ua);
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<String> requestEntity = new HttpEntity<String>("", headers);

		try{
			ResponseEntity<String> response= restTemplate.postForEntity(endpoint, requestEntity, String.class);
			System.out.println("Google response:"+response.getBody());
			if(response.getStatusCodeValue()==200) {
				JSONObject jo = JSON.parseObject(response.getBody());
				res = jo.getBoolean("success");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return res;
	}
	
}
