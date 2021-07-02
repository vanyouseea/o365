package hqr.o365.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import hqr.o365.service.DeleteSystemInfo;
import hqr.o365.service.GetSystemInfo;
import hqr.o365.service.ResetSystemInfo;
import hqr.o365.service.UpdateSystemInfo;

@Controller
public class SystemTabCtrl {
	
	@Autowired
	private GetSystemInfo gsi;
	
	@Autowired
	private DeleteSystemInfo dsi;
	
	@Autowired
	private UpdateSystemInfo usi;
	
	@Autowired
	private ResetSystemInfo rsi;
	
	@RequestMapping(value = {"/tabs/system.html"})
	public String dummy() {
		return "tabs/system";
	}
	
	@ResponseBody
	@RequestMapping(value = {"/getSystemInfo"}, method = RequestMethod.POST)
	public String getSystemInfo() {
		return gsi.getAllSystemInfo();
	}	
	
	@ResponseBody
	@RequestMapping(value = {"/deleteSystemInfo"}, method = RequestMethod.POST)
	public boolean deleteSystemInfo(@RequestParam(name="keyTy") String keyTy) {
		return dsi.deletePk(keyTy);
	}
	
	@ResponseBody
	@RequestMapping(value = {"/resetSystemInfo"}, method = RequestMethod.POST)
	public boolean resetSystemInfo() {
		return rsi.executeSql();
	}
	
	@ResponseBody
	@RequestMapping(value = {"/updateSystemInfo"}, method = RequestMethod.POST)
	public boolean updateSystemInfo(@RequestParam(name="keyTy") String keyTy,
			@RequestParam(name="cd") String cd,
			@RequestParam(name="decode") String decode) {
		return usi.updateInfo(keyTy, cd, decode);
	}
}
