package hqr.o365.ctrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.hutool.http.server.HttpServerRequest;
import hqr.o365.domain.LicenseInfo;
import hqr.o365.service.CreateOfficeUser;
import hqr.o365.service.DeleteOfficeUser;
import hqr.o365.service.GetDomainInfo;
import hqr.o365.service.GetLicenseInfo;
import hqr.o365.service.GetOfficeUser;
import hqr.o365.service.GetOfficeUserRole;
import hqr.o365.service.UpdateOfficeUser;
import hqr.o365.service.UpdateOfficeUserRole;

@Controller
public class UserTabCtrl {
	
	@Autowired
	private GetOfficeUser gou;
	
	@Autowired
	private GetLicenseInfo gli;
	
	@Autowired
	private GetDomainInfo gdi;
	
	@Autowired
	private CreateOfficeUser cou;
	
	@Autowired
	private DeleteOfficeUser dou;
	
	@Autowired
	private GetOfficeUserRole gour;
	
	@Autowired
	private UpdateOfficeUser uou;
	
	@Autowired
	private UpdateOfficeUserRole uour;
	
	@RequestMapping(value = {"/tabs/user.html"})
	public String dummy() {
		return "tabs/user";
	}
	
	@RequestMapping(value = {"tabs/dialogs/createUser.html"})
	public String dummyCreateUser(HttpServletRequest req) {
		Object tmp2 = req.getSession().getAttribute("licenseVo");
		if(tmp2==null) {
			HashMap<String, Object> map2 = gli.getLicenses();
			List<LicenseInfo> vo = new ArrayList<LicenseInfo>();
			Object obj = map2.get("licenseVo");
			if(obj!=null) {
				vo = (List<LicenseInfo>)obj;
			}
			req.getSession().setAttribute("licenseVo", vo);
		}
		else {
			System.out.println("licenseVo already exist,skip to get");
		}
		return "tabs/dialogs/createUser";
	}
	
	@ResponseBody
	@RequestMapping(value = {"/getDomains"})
	public String getDomains(HttpServletRequest req) {
		Object tmp = req.getSession().getAttribute("domainVo");
		if(tmp==null) {
			String json = gdi.getDomains();
			req.getSession().setAttribute("domainVo", json);
			return json;
		}
		else {
			System.out.println("domainVo already exist,use old one");
			return (String)tmp;
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value = {"/getOfficeUser"})
	public String getOfficeUser(String page, String rows) {
		int intPage = 1;
		int intRows = 100;
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
			System.out.println("Invalid row, force it to 100");
		}
		
		HashMap<String,String> map = gou.getUsers(intPage, intRows);
		
		return map.get("message");
	}
	
	@ResponseBody
	@RequestMapping(value = {"/createOfficeUser"}, method = RequestMethod.POST)
	public String createUser(@RequestParam(name="mailNickname") String mailNickname,
			@RequestParam(name="userPrincipalName") String userPrincipalName,
			@RequestParam(name="displayName") String displayName,
			@RequestParam(name="licenses") String licenses) {
		
		HashMap<String, String> map = cou.createCommonUser(mailNickname, userPrincipalName, displayName, licenses);
		
		return map.get("message");
	}
	
	@ResponseBody
	@RequestMapping(value = {"/deleteOfficeUser"}, method = RequestMethod.POST)
	public boolean deleteUser(@RequestParam(name="uid") String uid) {
		return dou.deleteUser(uid);
	}
	
	@ResponseBody
	@RequestMapping(value = {"/getOfficeUserDtls"}, method = RequestMethod.POST)
	public String getOfficeUserDtls(@RequestParam(name="uid") String uid) {
		return gour.getRole(uid);
	}
	
	@ResponseBody
	@RequestMapping(value = {"/updateOfficeUser"}, method = RequestMethod.POST)
	public boolean patchUser(@RequestParam(name="uid") String uid, @RequestParam(name="accountEnabled") String accountEnabled) {
		return uou.patchOfficeUser(uid, accountEnabled);
	}
	
	@ResponseBody
	@RequestMapping(value = {"/updateOfficeUserRole"}, method = RequestMethod.POST)
	public boolean patchUserRole(@RequestParam(name="uid") String uid, @RequestParam(name="action") String action) {
		return uour.update(uid, action);
	}
}
