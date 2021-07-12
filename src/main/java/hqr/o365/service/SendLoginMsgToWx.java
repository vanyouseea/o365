package hqr.o365.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import hqr.o365.dao.TaMasterCdRepo;
import hqr.o365.domain.TaMasterCd;

@Service
public class SendLoginMsgToWx {
	
	private RestTemplate restTemplate = new RestTemplate();
	
	@Autowired
	private TaMasterCdRepo tmc;
	
	public boolean sendMsg(String remoteIP) {
		boolean status = false;
		
		String corpId = "";
		String corpSecret = "";
		String agentId = "";
		
		Optional<TaMasterCd> opt1 = tmc.findById("WX_CORPID");
		Optional<TaMasterCd> opt2 = tmc.findById("WX_CORPSECRET");
		Optional<TaMasterCd> opt3 = tmc.findById("WX_AGENTID");
		if(opt1.isPresent()&&opt2.isPresent()&&opt3.isPresent()) {
			corpId = opt1.get().getCd();
			corpSecret = opt2.get().getCd();
			agentId = opt3.get().getCd();
		}

		if(corpId!=null&&corpSecret!=null&&agentId!=null&&!"".equals(corpId)&&!"".equals(corpSecret)&&!"".equals(agentId)) {
			String endpoint1 = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid="+corpId+"&corpsecret="+corpSecret;
			HttpHeaders headers1 = new HttpHeaders();
			String body1="";
			HttpEntity<String> requestEntity1 = new HttpEntity<String>(body1, headers1);
			try {
				ResponseEntity<String> response1 = restTemplate.exchange(endpoint1, HttpMethod.GET, requestEntity1, String.class);
				JSONObject jo = JSON.parseObject(response1.getBody());
		    	String errcode = jo.get("errcode").toString();
		    	if("0".equals(errcode)) {
		    		String token = (String)jo.get("access_token");
		    		
					String endpoint2 = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token="+token+"&debug=1";
					HttpHeaders headers2 = new HttpHeaders();
					headers2.setContentType(MediaType.APPLICATION_JSON);
					String content = "用户(IP:"+remoteIP+")请求登录O365管理系统，如果批准请在60秒内回复Y，如果这不是你已知的登录请求那么可能你的密码已泄露，请忽略或回复N";
					//agentId=1000002
					String body2 = "{\"touser\": \"@all\",\"msgtype\" : \"text\",\"agentid\" : "+agentId+",\"text\" : {\"content\" : \""+content+"\"},\"enable_duplicate_check\" : 1, \"duplicate_check_interval\" : 3}";

					HttpEntity<String> requestEntity2 = new HttpEntity<String>(body2, headers2);
					ResponseEntity<String> response2 = restTemplate.exchange(endpoint2, HttpMethod.POST, requestEntity2, String.class);
		    		
					JSONObject jo2 = JSON.parseObject(response2.getBody());
			    	String errcode2 = jo2.get("errcode").toString();
			    	if("0".equals(errcode2)) {
			    		//init the user reply keyty in DB
			    		TaMasterCd enti = new TaMasterCd();
			    		enti.setKeyTy("USER_RESPONSE");
			    		enti.setLastUpdateId("o365");
			    		enti.setLastUpdateDt(new Date());
			    		tmc.saveAndFlush(enti);
			    		
			    		System.out.println("msg push to user successfully, wait 60s for user reply");
			    		status = true;
			    	}
			    	else {
			    		System.out.println("failed to push the msg "+jo2.get("errmsg"));
			    	}
		    	}
			}
			catch (Exception e) {
				System.out.println("failed to push the msg2 "+e);
			}
		}

		return status;
	}
	
}
