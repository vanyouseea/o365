package hqr.o365.ctrl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hqr.o365.domain.TaOfficeInfo;
import hqr.o365.service.SaveOfficeInfo;

@RestController
public class SaveConfigCtrl {
	
	@Autowired
	private SaveOfficeInfo si;
	
	@RequestMapping(value = {"/saveConfig"}, method = RequestMethod.POST)
	public boolean save(@RequestParam(name="userid") String userid, 
			@RequestParam(name="passwd") String passwd, 
			@RequestParam(name="tenantId") String tenantId, 
			@RequestParam(name="appId") String appId , 
			@RequestParam(name="secretId") String secretId, 
			@RequestParam(name="remarks") String remarks) {
		TaOfficeInfo ti = new TaOfficeInfo();
		ti.setUserId(userid);
		ti.setPasswd(passwd);
		ti.setTenantId(tenantId);
		ti.setAppId(appId);
		ti.setSecretId(secretId);
		ti.setRemarks(remarks);
		ti.setCreateDt(new Date());
		ti.setLastUpdateDt(new Date());
		ti.setLastUpdateId("mjj");
		
		return si.save(ti);
		
	}
	
}
