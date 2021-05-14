package hqr.o365.ctrl;

import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import hqr.o365.domain.TaOfficeInfo;
import hqr.o365.service.AddPassword;
import hqr.o365.service.DeleteOfficeInfo;
import hqr.o365.service.GetOfficeInfo;
import hqr.o365.service.SaveOfficeInfo;
import hqr.o365.service.SwitchConfig;
import hqr.o365.service.ValidateAppInfo;

@Controller
public class ConfigTabCtrl {
	
	@Autowired
	private GetOfficeInfo gi;
	
	@Autowired
	private SaveOfficeInfo si;
	
	@Autowired
	private DeleteOfficeInfo di;
	
	@Autowired
	private ValidateAppInfo vai;
	
	@Autowired
	private SwitchConfig sc;
	
	@Autowired
	private AddPassword ap;
	
	@RequestMapping(value = {"/tabs/config.html"})
	public String dummy() {
		return "tabs/config";
	}
	
	@ResponseBody
	@RequestMapping(value = {"/getConfig"})
	public String getConfig(String page, String rows) {
		int intPage = 1;
		int intRows = 10;
		try {
			intPage = Integer.valueOf(page);
		}
		catch (Exception e) {
			System.out.println("Invalid page, force it to 1");
		}
		try {
			intRows = Integer.valueOf(rows);
		}
		catch (Exception e) {
			System.out.println("Invalid row, force it to 10");
		}
		
		return gi.getAllOfficeInfo(intRows, intPage);
	}
	
	@ResponseBody
	@RequestMapping(value = {"/saveConfig"}, method = RequestMethod.POST)
	public boolean save(@RequestParam(name="seqNo") int seqNo, 
			@RequestParam(name="userid") String userid, 
			@RequestParam(name="passwd") String passwd, 
			@RequestParam(name="tenantId") String tenantId, 
			@RequestParam(name="appId") String appId , 
			@RequestParam(name="secretId") String secretId, 
			@RequestParam(name="remarks") String remarks,
			@RequestParam(name="selected") String selected) {
		TaOfficeInfo ti = new TaOfficeInfo();
		System.out.println("seqNo is "+ seqNo);
		//seqNo==-1 -> insert; seqNo!=-1 -> update
		if(seqNo!=-1) {
			ti.setSeqNo(seqNo);
		}
		else {
			ti.setCreateDt(new Date());
		}
		ti.setUserId(userid);
		ti.setPasswd(passwd);
		ti.setTenantId(tenantId);
		ti.setAppId(appId);
		ti.setSecretId(secretId);
		ti.setRemarks(remarks);
		ti.setLastUpdateDt(new Date());
		ti.setLastUpdateId("mjj");
		ti.setSelected(selected);
		
		return si.save(ti);
		
	}
	
	@ResponseBody
	@RequestMapping(value = {"/delConfig"}, method = RequestMethod.POST)
	public boolean delete(@RequestParam(name="seqNo") int seqNo) {
		System.out.println("SeqNo:"+seqNo);
		return di.delete(seqNo);
	}
	
	@ResponseBody
	@RequestMapping(value = {"/valiateAppInfo"}, method = RequestMethod.POST)
	public boolean validate(@RequestParam(name="tenantId") String tenantId,@RequestParam(name="appId") String appId,@RequestParam(name="secretId") String secretId) {
		System.out.println("tenantId"+tenantId);
		return vai.check(tenantId, appId, secretId);
	}
	
	@ResponseBody
	@RequestMapping(value = {"/switchConfig"}, method = RequestMethod.POST)
	public boolean switchConfig(@RequestParam(name="seqNo") int seqNo, 
			@RequestParam(name="userid") String userid, 
			@RequestParam(name="passwd") String passwd, 
			@RequestParam(name="tenantId") String tenantId, 
			@RequestParam(name="appId") String appId , 
			@RequestParam(name="secretId") String secretId, 
			@RequestParam(name="remarks") String remarks,
			@RequestParam(name="selected") String selected) {
		
		TaOfficeInfo ti = new TaOfficeInfo();
		//seqNo==-1 -> insert; seqNo!=-1 -> update
		if(seqNo!=-1) {
			ti.setSeqNo(seqNo);
		}
		else {
			ti.setCreateDt(new Date());
		}
		ti.setUserId(userid);
		ti.setPasswd(passwd);
		ti.setTenantId(tenantId);
		ti.setAppId(appId);
		ti.setSecretId(secretId);
		ti.setRemarks(remarks);
		ti.setLastUpdateDt(new Date());
		ti.setLastUpdateId("mjj");
		ti.setSelected("是");
		
		return sc.updateConfig(ti);
	}
	
	@ResponseBody
	@RequestMapping(value = {"/addPassword"}, method = RequestMethod.POST)
	public String addPassword(@RequestParam(name="seqNo") int seqNo, 
			@RequestParam(name="tenantId") String tenantId, 
			@RequestParam(name="appId") String appId , 
			@RequestParam(name="secretId") String secretId) {
		
		HashMap<String, String> map = ap.add(seqNo, tenantId, appId, secretId);
		String status = map.get("status");
		
		if("0".equals(status)) {
			return "已成功添加了一个新的密钥";
		}
		else {
			return map.get("message");
		}
	}
	
}
