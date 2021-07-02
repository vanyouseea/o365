package hqr.o365.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import hqr.o365.dao.TaMasterCdRepo;
import hqr.o365.dao.TaOfficeInfoRepo;
import hqr.o365.domain.LicenseInfo;
import hqr.o365.domain.TaMasterCd;
import hqr.o365.domain.TaOfficeInfo;

@Service
public class GetLicenseInfo {
	private RestTemplate restTemplate = new RestTemplate();
	
	@Autowired
	private TaOfficeInfoRepo repo;
	
	@Autowired
	private ValidateAppInfo vai;
	
	@Autowired
	private TaMasterCdRepo tmc;
	
	@Value("${UA}")
    private String ua;

	public HashMap<String, Object> getLicenses() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<LicenseInfo> ll = new ArrayList<LicenseInfo>();
		HashMap jsonTmp = new HashMap();
		
		List<TaOfficeInfo> list = repo.getSelectedApp();
		if(list!=null&&list.size()>0) {
			TaOfficeInfo ta = list.get(0);
			String accessToken = "";
			if(vai.checkAndGet(ta.getTenantId(), ta.getAppId(), ta.getSecretId())) {
				accessToken = vai.getAccessToken();
			}
			
			if(!"".equals(accessToken)) {
				String endpoint = "https://graph.microsoft.com/v1.0/subscribedSkus?$select=capabilityStatus,skuid,skuPartNumber,consumedUnits,prepaidUnits";
				HttpHeaders headers = new HttpHeaders();
				headers.set(HttpHeaders.USER_AGENT, ua);
				headers.add("Authorization", "Bearer "+accessToken);
				String body="";
				HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
				try {
					ResponseEntity<String> response= restTemplate.exchange(endpoint, HttpMethod.GET, requestEntity, String.class);
					if(response.getStatusCodeValue()==200) {
						JSONObject jo = JSON.parseObject(response.getBody());
						JSONArray ja = jo.getJSONArray("value");
						for (Object object : ja) {
							JSONObject jb = (JSONObject)object;
							String capabilityStatus = jb.getString("capabilityStatus");
							String skuId = jb.getString("skuId");
							String skuPartNumber = jb.getString("skuPartNumber");
							String skuIdDesc = skuPartNumber;
							Optional<TaMasterCd> slfp = tmc.findById(skuId);
							if(slfp.isPresent()) {
								skuIdDesc = slfp.get().getDecode();
							}
							String consumedUnits = jb.getString("consumedUnits");
							String prepaidUnits = jb.getJSONObject("prepaidUnits").getString("enabled");
							LicenseInfo li = new LicenseInfo();
							li.setCapabilityStatus(capabilityStatus);
							li.setSkuId(skuId);
							li.setSkuPartNumber(skuPartNumber);
							li.setSkuIdDesc(skuIdDesc);
							li.setConsumedUnits(consumedUnits);
							li.setPrepaidUnits(prepaidUnits);
							ll.add(li);
						}
						
						jsonTmp.put("total", ll.size());
						jsonTmp.put("rows", ll);
						
						map.put("status", "0");
						map.put("message", JSON.toJSON(jsonTmp).toString());
						map.put("licenseVo", ll);
					}
					else {
						map.put("status", "1");
						map.put("message", "fail to connect to microsoft");
					}
				}
				catch (Exception e) {
					e.printStackTrace();
					map.put("status", "1");
					map.put("message", "无法获得正确的状态或无法连接到microsoft");
				}
			}
			else {
				map.put("status", "1");
				map.put("message", "获取token失败，请确认全局的有效性");
			}
		}
		else {
			map.put("status", "1");
			map.put("message", "请先选择一个全局");
		}
		return map;
	}
	
}
