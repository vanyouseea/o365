package hqr.o365.service;

import java.util.ArrayList;
import java.util.HashMap;
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

import hqr.o365.dao.TaMasterCdRepo;
import hqr.o365.dao.TaOfficeInfoRepo;
import hqr.o365.domain.OfficeUser;
import hqr.o365.domain.PrivilegedUser;
import hqr.o365.domain.TaMasterCd;
import hqr.o365.domain.TaOfficeInfo;

@Service
public class GetOfficeRoleUser {
	
	private RestTemplate restTemplate = new RestTemplate();

	@Autowired
	private TaMasterCdRepo tmc;
	
	@Autowired
	private TaOfficeInfoRepo repo;
	
	@Autowired
	private ValidateAppInfo vai;
	
	@Value("${UA}")
    private String ua;
	
	@Cacheable(value="cacheRoleUser")
	public HashMap<String, String> getRoleUsers(){
		HashMap<String, String> map = new HashMap<String, String>();
		List<PrivilegedUser> ll = new ArrayList<PrivilegedUser>();
		HashMap jsonTmp = new HashMap();
		
		List<TaOfficeInfo> list = repo.findBySelected("是");
		if(list!=null&&list.size()>0) {
			TaOfficeInfo ta = list.get(0);
			String accessToken = "";
			if(vai.checkAndGet(ta.getTenantId(), ta.getAppId(), ta.getSecretId())) {
				accessToken = vai.getAccessToken();
			}
			
			if(!"".equals(accessToken)) {
				//List<TaMasterCd> roleList = tmc.getSearchRoles();
				List<TaMasterCd> roleList = tmc.findByKeyTyStartsWith("SEARCH_ROLE_");
				int total = 0;
				if(roleList!=null&&roleList.size()>0) {
					for (TaMasterCd taMasterCd : roleList) {
						String roleId = taMasterCd.getCd();
						String endpoint = "https://graph.microsoft.com/v1.0/directoryRoles/roleTemplateId="+roleId+"/members?$select=accountEnabled,usageLocation,id,userPrincipalName,displayName";
						HttpHeaders headers = new HttpHeaders();
						headers.set(HttpHeaders.USER_AGENT, ua);
						headers.add("Authorization", "Bearer "+accessToken);
						String body = "";
						HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
						try {
							ResponseEntity<String> response= restTemplate.exchange(endpoint, HttpMethod.GET, requestEntity, String.class);
							if(response.getStatusCodeValue()==200) {
								JSONObject jo = JSON.parseObject(response.getBody());
								JSONArray ja = jo.getJSONArray("value");
								
								for (Object object : ja) {
									PrivilegedUser pu = new PrivilegedUser();
									
									JSONObject jb = (JSONObject)object;
									String accountEnabled = jb.getString("accountEnabled");
									String usageLocation = jb.getString("usageLocation");
									String uid = jb.getString("id");
									String userPrincipalName = jb.getString("userPrincipalName");
									String displayName = jb.getString("displayName");
									String roleName = taMasterCd.getDecode();
									pu.setAccountEnabled(accountEnabled);
									pu.setUsageLocation(usageLocation);
									pu.setId(uid);
									pu.setUserPrincipalName(userPrincipalName);
									pu.setDisplayName(displayName);
									pu.setRoleName(roleName);
									ll.add(pu);
									
									total ++;
								}
							}
							else {
								//ignoer other error
							}
							
							map.put("status", "0");
							jsonTmp.put("total", String.valueOf(total));
							jsonTmp.put("rows", ll);
							map.put("message", JSON.toJSON(jsonTmp).toString());
						}
						catch (Exception e) {
							//ignoer other exception
						}
					}
				}
				else {
					map.put("status", "0");
					jsonTmp.put("total", String.valueOf(total));
					jsonTmp.put("rows", ll);
					map.put("message", JSON.toJSON(jsonTmp).toString());
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
