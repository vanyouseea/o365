package hqr.o365.ctrl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import hqr.o365.domain.LicenseInfo;
import hqr.o365.domain.TaOfficeInfo;
import hqr.o365.service.AddPassword;
import hqr.o365.service.DeleteOfficeInfo;
import hqr.o365.service.ExportAppInfo;
import hqr.o365.service.GetDomainInfo;
import hqr.o365.service.GetLicenseInfo;
import hqr.o365.service.GetOfficeInfo;
import hqr.o365.service.ImportAppInfo;
import hqr.o365.service.SaveOfficeInfo;
import hqr.o365.service.ScanAppStatusServiceForOne;
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
	
	@Autowired
	private GetLicenseInfo gli;
	
	@Autowired
	private GetDomainInfo gdi;
	
	@Autowired
	private ImportAppInfo iai;
	
	@Autowired
	private ExportAppInfo eai;
	
	@Autowired
	private ScanAppStatusServiceForOne s41;
	
	@RequestMapping(value = {"/tabs/config.html"})
	public String dummy() {
		return "tabs/config";
	}
	
	@RequestMapping(value = {"/tabs/dialogs/import.html"})
	public String dummy2() {
		return "tabs/dialogs/import";
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
	public String delete(@RequestParam(name="seqNos") String seqNos) {
		HashMap<String, int[]> map = di.delete(seqNos);
		int [] overall = map.get("delete_res");
		
		StringBuilder sb = new StringBuilder();
		sb.append("配置删除成功:"+overall[0]+"<br>");
		sb.append("配置删除失败:"+overall[1]+"<br>");
		
		return sb.toString();
	}
	
	@ResponseBody
	@RequestMapping(value = {"/valiateAppInfo"}, method = RequestMethod.POST)
	public boolean validate(@RequestParam(name="tenantId") String tenantId,@RequestParam(name="appId") String appId,@RequestParam(name="secretId") String secretId) {
		return vai.checkAndGet(tenantId, appId, secretId);
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
			@RequestParam(name="selected") String selected, 
			HttpServletRequest req) {
		
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
		
		boolean flag = sc.updateConfig(ti);
		if(flag) {
			//change license
			HashMap<String, Object> map2 = gli.getLicenses();
			List<LicenseInfo> vo = new ArrayList<LicenseInfo>();
			Object obj = map2.get("licenseVo");
			if(obj!=null) {
				vo = (List<LicenseInfo>)obj;
			}
			req.getSession().setAttribute("licenseVo", vo);
			
			//change domains
			String domainVo = gdi.getDomains();
			req.getSession().setAttribute("domainVo", domainVo);
			
		}
		else {
			//invalid info, update the licenseVo & domain Vo to null
			req.getSession().setAttribute("licenseVo", null);
			req.getSession().setAttribute("domainVo", "[]");
		}
		return flag;
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
	
	@ResponseBody
	@RequestMapping(value = {"/getAppRptForOne"}, method = RequestMethod.POST)
	public String getOverallRptForOne(@RequestParam(name="seqNos") String seqNos) {
		s41.execute(seqNos);
		return "报告已生成，请前往[Office总览报告]中查看结果";
	}
	
	@ResponseBody
	@RequestMapping(value = {"/importApps"}, method = RequestMethod.POST)
	public String uploadApps(@RequestParam("fileNm") MultipartFile file) {
		HashMap<String, int[]> map = iai.importApp(file);
		int [] overall = map.get("import_res");
		
		StringBuilder sb = new StringBuilder();
		sb.append("导入成功:"+overall[1]+"条<br>");
		sb.append("导入失败:"+overall[2]+"条<br>");
		return sb.toString();
	}
	
	@RequestMapping(value = {"/exportApps"}, method = RequestMethod.GET)
	public ResponseEntity<FileSystemResource> exportApps(){
		eai.exportApp();
		
		return export(new File("export_app_info.csv"));
	}
	
	public ResponseEntity<FileSystemResource> export(File file) { 
		HttpHeaders headers = new HttpHeaders();
	    headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
	    headers.add("Content-Disposition", "attachment; filename=export_app_info.csv");
	    headers.add("Pragma", "no-cache");
	    headers.add("Expires", "0");
	    headers.add("Last-Modified", new Date().toString());
	    headers.add("ETag", String.valueOf(System.currentTimeMillis()));
	 
	    return ResponseEntity.ok().headers(headers) .contentLength(file.length()) .contentType(MediaType.parseMediaType("text/csv")) .body(new FileSystemResource(file));
	}
	
}
