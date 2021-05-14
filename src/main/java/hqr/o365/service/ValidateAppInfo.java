package hqr.o365.service;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import hqr.o365.util.Brower;

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
	
	public boolean check(String tenantId, String appId, String secretId) {
		boolean flag = false;
		
		String endpoint = "https://login.microsoftonline.com/" + tenantId + "/oauth2/v2.0/token";
		//open browser
		CloseableHttpClient httpclient = Brower.getCloseableHttpClient();
		//html context
		HttpClientContext httpClientContext = Brower.getHttpClientContext();
		
		HttpPost post = new HttpPost(endpoint);
		post.setConfig(Brower.getRequestConfig());
		
		String json = "client_id="+appId+"&client_secret="+secretId+"&grant_type=client_credentials&scope=https://graph.microsoft.com/.default";
		post.setEntity(new StringEntity(json, ContentType.APPLICATION_FORM_URLENCODED));
		
		try(CloseableHttpResponse cl = httpclient.execute(post,httpClientContext);) {
			if(cl.getStatusLine().getStatusCode()==200) {
				JSONObject jo = JSON.parseObject(EntityUtils.toString(cl.getEntity()));
				
				String accessToken = jo.getString("access_token");
				if(accessToken!=null) {
					flag = true;
					System.out.println("accessToken:"+accessToken);
				}
				else {
					flag = false;
				}
			}
			else {
				System.out.println("failed:" + EntityUtils.toString(cl.getEntity()));
			}
			httpclient.close();
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		
		return flag;
	}
	
}
