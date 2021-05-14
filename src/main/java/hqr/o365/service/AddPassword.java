package hqr.o365.service;

import java.util.HashMap;
import java.util.Optional;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import hqr.o365.dao.TaOfficeInfoRepo;
import hqr.o365.domain.TaOfficeInfo;
import hqr.o365.util.Brower;

@Service
public class AddPassword {

	@Autowired
	private TaOfficeInfoRepo repo;
	
	public HashMap<String, String> add(int seqNo, String tenantId, String appId, String secretId) {
		HashMap<String, String> map = new HashMap<String, String>();
		
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
					endpoint = "https://graph.microsoft.com/v1.0/applications?$select=id,appId";
					HttpGet get = new HttpGet(endpoint);
					get.setConfig(Brower.getRequestConfig());
					get.setHeader("Authorization", accessToken);
					try(CloseableHttpResponse cl2 = httpclient.execute(get,httpClientContext);) {
						if(cl2.getStatusLine().getStatusCode()==200) {
							JSONObject jo2 = JSON.parseObject(EntityUtils.toString(cl2.getEntity()));
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
							HttpPost post2 = new HttpPost(endpoint);
							post2.setConfig(Brower.getRequestConfig());
							post2.setHeader("Authorization", accessToken);
							
							json = "{\"passwordCredential\": {\"displayName\": \"mjj\",\"endDateTime\": \"2099-12-31T06:30:23.9074306Z\" }}";
							post2.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
							try(CloseableHttpResponse cl3 = httpclient.execute(post2,httpClientContext);) {
								if(cl3.getStatusLine().getStatusCode()==200) {
									JSONObject jo3 = JSON.parseObject(EntityUtils.toString(cl3.getEntity()));
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
									map.put("message", EntityUtils.toString(cl3.getEntity()));
								}
							}
						}
						else {
							map.put("status", "1");
							map.put("message", EntityUtils.toString(cl2.getEntity()));
						}
					}
				}
				else {
					map.put("status", "1");
					map.put("message", "Fail to get the token");
				}
			}
			else {
				map.put("status", "1");
				map.put("message", EntityUtils.toString(cl.getEntity()));
			}
			httpclient.close();
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", "2");
			map.put("message", "Fail to connect to microsoft");
		}
		
		return map;
	}
	
}
