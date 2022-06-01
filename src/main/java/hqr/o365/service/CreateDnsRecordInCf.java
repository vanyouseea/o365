package hqr.o365.service;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Optional;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpHost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import hqr.o365.dao.TaMasterCdRepo;
import hqr.o365.domain.TaMasterCd;

/**
 *
POST https://api.cloudflare.com/client/v4/zones/${zone_id}/dns_records
JSON:
{
	"type":"A",
	"name":"cgi",
	"content":"2.3.4.5",
	"ttl":3600,
	"proxied":false
}
 *
 */

@Service
public class CreateDnsRecordInCf {
	@Autowired
	private TaMasterCdRepo tmc;
	
	@Value("${UA}")
    private String ua;
	
	private RestTemplate restTemplate;
	
	public void initlRestTemplate() {
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
					return true;
				}
			}).build();
			
			SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
			CloseableHttpClient httpClient;
			
			Optional<TaMasterCd> opt = tmc.findById("HTTP_PROXY");
			if(opt.isPresent()) {
				TaMasterCd enti = opt.get();
				if("".equals( enti.getCd() ) ) {
					httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
				}
				else {
					try {
						String proxy[] = enti.getCd().split(":");
						httpClient = HttpClients.custom().setSSLSocketFactory(csf).setProxy(new HttpHost(proxy[0], Integer.parseInt(proxy[1]))).build();
					}
					catch (Exception e) {
						System.out.println("invalid proxy "+enti.getCd());
						httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
					}
				}
			}
			else {
				httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
			}

			HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

			requestFactory.setHttpClient(httpClient);
			restTemplate = new RestTemplate(requestFactory);
			restTemplate.setErrorHandler(new ResponseErrorHandler() {
				@Override
				public boolean hasError(ClientHttpResponse response) throws IOException {
					return false;
				}
				@Override
				public void handleError(ClientHttpResponse response) throws IOException {}
			});
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String create(String zoneId, String type, String ttl, String content, String cfAuthEmail, String cfAuthKey) {
		String res = "fail";
		initlRestTemplate();
		
		String endpoint = "https://api.cloudflare.com/client/v4/zones/"+zoneId+"/dns_records";
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.USER_AGENT, ua);
		headers.add("X-Auth-Email", cfAuthEmail);
		headers.add("X-Auth-Key", cfAuthKey);
		headers.add("Content-Type", "application/json");
		String json = "{\"type\":\""+type+"\",\"name\":\""+zoneId+"\",\"content\":\""+content+"\",\"ttl\":"+ttl+"}";
		System.out.println("CreateDnsJson:"+json);
		HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
		try {
			ResponseEntity<String> response= restTemplate.exchange(endpoint, HttpMethod.POST, requestEntity, String.class);
			if(response.getStatusCodeValue()==200) {
				//200 Succ
				/*
				 * {
					   "result":    {
					      "id": "9f97efd780a539071a0358c247d121d6",
					      "zone_id": "78fa80a60e4a64c23c4bafe62d75670a",
					      "zone_name": "baidu.com",
					      "name": "cgi2.baidu.com",
						  ......
					   },
					   "success": true,
					   "errors": [],
					   "messages": []
					}
				 * 
				 */
				System.out.println("Created DNS record in CF:"+response.getBody());
				res = "succ";
			}
			else {
				//400 Fail
				JSONObject jo = JSON.parseObject(response.getBody());
				JSONArray ja = jo.getJSONArray("errors");
				JSONObject erroObj = (JSONObject)ja.get(0);
				System.out.println(erroObj.getString("message"));
			}
		}
		catch (Exception e) {
			System.out.println("fail to connect to CF:"+e.toString());
		}
		
		return res;
	}
	
}
