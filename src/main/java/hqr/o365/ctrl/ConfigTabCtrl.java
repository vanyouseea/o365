package hqr.o365.ctrl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import hqr.o365.domain.TaOfficeInfo;
import hqr.o365.service.DeleteOfficeInfo;
import hqr.o365.service.GetOfficeInfo;
import hqr.o365.service.SaveOfficeInfo;

@Controller
public class ConfigTabCtrl {
	
	@Autowired
	private GetOfficeInfo gi;
	
	@Autowired
	private SaveOfficeInfo si;
	
	@Autowired
	private DeleteOfficeInfo di;
	
	@RequestMapping(value = {"/tabs/config.html"})
	public String dummy() {
		return "tabs/config";
	}
	
	@ResponseBody
	@RequestMapping(value = {"/getConfig"})
	public String getConfig() {
		return gi.getAllOfficeInfo();
	}
	
	@ResponseBody
	@RequestMapping(value = {"/saveConfig"}, method = RequestMethod.POST)
	public boolean save(@RequestParam(name="seqNo") int seqNo, 
			@RequestParam(name="userid") String userid, 
			@RequestParam(name="passwd") String passwd, 
			@RequestParam(name="tenantId") String tenantId, 
			@RequestParam(name="appId") String appId , 
			@RequestParam(name="secretId") String secretId, 
			@RequestParam(name="remarks") String remarks) {
		TaOfficeInfo ti = new TaOfficeInfo();
		//seqNo==-1 -> insert; seqNo!=-1 -> update
		if(seqNo!=-1) {
			ti.setSeqNo(seqNo);
		}
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
	
	@ResponseBody
	@RequestMapping(value = {"/delConfig"}, method = RequestMethod.POST)
	public boolean delete(@RequestParam(name="seqNo") int seqNo) {
		System.out.println("SeqNo:"+seqNo);
		return di.delete(seqNo);
	}
	
}
