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

import cn.hutool.core.util.URLUtil;

/**
 POST https://login.microsoftonline.com/48350e78-b3d8-4148-a266-34b602b1d51a/oauth2/v2.0/token
 Media Type: application/x-www-form-urlencoded
 Body:
 client_id=f8048a1a-06b9-477f-1fe7-ae0e1d5cd082
 &client_secret=G2zGRO.Q7-7Mxm8fBW1f4QxRy0_-x-1l.L
 &grant_type=client_credentials
 &scope=https://graph.microsoft.com/.default
 */

@Service
public class ValidateAppInfo {
	
	private RestTemplate restTemplate = new RestTemplate();
	
	private String accessToken = "";
	
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	@Value("${UA}")
    private String ua;

	public boolean checkAndGet(String tenantId, String appId, String secretId) {
		boolean flag = false;
		
		String endpoint = "https://login.microsoftonline.com/" + tenantId + "/oauth2/v2.0/token";
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.USER_AGENT, ua);
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		String json = "client_id="+URLUtil.encodeQuery(appId)+"&client_secret="+URLUtil.encodeQuery(secretId)+"&grant_type=client_credentials&scope=https://graph.microsoft.com/.default";
		HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);

		try{
			ResponseEntity<String> response= restTemplate.postForEntity(endpoint, requestEntity, String.class);
			if(response.getStatusCodeValue()==200) {
				JSONObject jo = JSON.parseObject(response.getBody());
				accessToken = jo.getString("access_token");
				flag = true;
			}
			else {
				System.out.println("failed:" + response.getBody());
				accessToken = "";
				flag = false;
			}
		} catch (Exception e) {
			//e.printStackTrace();
			accessToken = "";
			flag = false;
		}
		
		return flag;
	}
	
}
