package hqr.o365.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import hqr.o365.dao.TaMasterCdRepo;
import hqr.o365.domain.TaMasterCd;

/**
 * Get the organization default location
 * When creating user use organization location, if not found or invalid, then use US location.
 */

@Service
public class GetOrganizationInfo {
	
	private RestTemplate restTemplate = new RestTemplate();
	
	@Autowired
	private TaMasterCdRepo tmc;
	
	@Value("${UA}")
    private String ua;
	
	@Cacheable(value="cacheOrg")
	public String getUsageLocation(String accessToken) {
		String location = "US";
		
		Optional<TaMasterCd> opt = tmc.findById("CUSTOM_USAGE_LOCATION_IND");
		if(opt.isPresent()) {
			TaMasterCd config = opt.get();
			if("Y".equals(config.getCd())){
				//get custom location from db
				Optional<TaMasterCd> opt2 = tmc.findById("DEFAULT_USAGE_LOCATION");
				if(opt2.isPresent()) {
					TaMasterCd config2 = opt2.get();
					location = config2.getCd();
				}
			}
			else {
				//use organization's location
				String endpoint = "https://graph.microsoft.com/v1.0/organization?$select=countryLetterCode";
				HttpHeaders headers = new HttpHeaders();
				headers.set(HttpHeaders.USER_AGENT, ua);
				headers.add("Authorization", "Bearer "+accessToken);
				String body="";
				HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
				try {
					ResponseEntity<String> response= restTemplate.exchange(endpoint, HttpMethod.GET, requestEntity, String.class);
					JSONObject jo = JSON.parseObject(response.getBody());
					JSONArray ja = jo.getJSONArray("value");
					//get 1st one
					JSONObject jb = (JSONObject)ja.get(0);
					location = jb.getString("countryLetterCode");
				}
				catch (Exception e) {
					System.out.println("Fail to get usagelocation, use US as default");
				}
			}
		}
		System.out.println("Final location is "+location);
		return location;
	}
	
}
