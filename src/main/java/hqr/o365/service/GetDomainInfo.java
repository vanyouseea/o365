package hqr.o365.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import hqr.o365.dao.TaOfficeInfoRepo;
import hqr.o365.domain.TaOfficeInfo;


@Service
public class GetDomainInfo {
	private RestTemplate restTemplate = new RestTemplate();
	
	@Autowired
	private TaOfficeInfoRepo repo;
	
	@Autowired
	private ValidateAppInfo vai;
	
	@Value("${UA}")
    private String ua;

	@Cacheable(value="cacheDomain")
	public String getDomains(){
		String json = "[]";
		List<ComboboxDo> ll = new ArrayList<ComboboxDo>();
		
		List<TaOfficeInfo> list = repo.findBySelected("是");
		if(list!=null&&list.size()>0) {
			TaOfficeInfo ta = list.get(0);
			String accessToken = "";
			if(vai.checkAndGet(ta.getTenantId(), ta.getAppId(), ta.getSecretId())) {
				accessToken = vai.getAccessToken();
			}
			if(!"".equals(accessToken)) {
				String endpoint = "https://graph.microsoft.com/v1.0/domains?$select=id,isVerified";
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
						int j = 0;
						for (int i=0;i<ja.size();i++) {
							JSONObject jb = (JSONObject)ja.get(i);
							String domain = jb.getString("id");
							String status = jb.getString("isVerified");
							if("false".equals(status)) {
								continue;
							}
							else {
								j++;
							}
							ComboboxDo cbm = new ComboboxDo();
							cbm.setId(j);
							if(j==1) {
								cbm.setSelected(true);
							}
							cbm.setText("@"+domain);
							ll.add(cbm);
						}
						json = JSON.toJSONString(ll);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return json;
	}
	
	class ComboboxDo {
		private int id;
		private String text;
		private boolean selected = false;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		public boolean isSelected() {
			return selected;
		}
		public void setSelected(boolean selected) {
			this.selected = selected;
		}
	}
	
}
