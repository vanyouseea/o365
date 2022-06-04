package hqr.o365.service;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
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

@Service
public class ValidateCfConfig {
	
	@Autowired
	private TaMasterCdRepo tmc;
	
	private RestTemplate restTemplate;
	
	@Value("${UA}")
    private String ua;
	
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
	
	public boolean validateCf() {
		boolean res = false;
		
		Optional<TaMasterCd> opt1 = tmc.findById("CF_AUTH_EMAIL");
		Optional<TaMasterCd> opt2 = tmc.findById("CF_AUTH_KEY");

		if(opt1.isPresent()&&opt2.isPresent()) {
			TaMasterCd en1 = opt1.get();
			TaMasterCd en2 = opt2.get();
			if(en1.getCd()!=null&&!"".equals(en1.getCd())&&en2.getCd()!=null&&!"".equals(en2.getCd())) {
				initlRestTemplate();
				String cfAuthEmail = en1.getCd();
				String cfAuthKey = en2.getCd();
				
				String endpoint = "https://api.cloudflare.com/client/v4/user";
				HttpHeaders headers = new HttpHeaders();
				headers.set(HttpHeaders.USER_AGENT, ua);
				headers.add("X-Auth-Email", cfAuthEmail);
				headers.add("X-Auth-Key", cfAuthKey);
				headers.add("Content-Type", "application/json");
				String body="";
				HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
				try {
					ResponseEntity<String> response= restTemplate.exchange(endpoint, HttpMethod.GET, requestEntity, String.class);
					JSONObject jo = JSON.parseObject(response.getBody());
					boolean flag = jo.getBoolean("success");
					if(flag) {
						res = flag;
					}
					else {
						JSONArray ja = jo.getJSONArray("errors");
						System.out.println("Error to retrieve user:"+ja);
					}
				}
				catch (Exception e) {
					System.out.println("CF_AUTH_EMAIL or CF_AUTH_KEY does not correct");
				}
			}
			else {
				System.out.println("CF_AUTH_EMAIL or CF_AUTH_KEY value is null");
			}
		}
		else {
			System.out.println("CF_AUTH_EMAIL or CF_AUTH_KEY do not exist");
		}
		
		return res;
	}
	
}
