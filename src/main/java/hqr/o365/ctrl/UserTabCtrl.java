package hqr.o365.ctrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import hqr.o365.domain.LicenseInfo;
import hqr.o365.service.CreateOfficeUser;
import hqr.o365.service.DeleteOfficeUser;
import hqr.o365.service.GetDomainInfo;
import hqr.o365.service.GetLicenseInfo;
import hqr.o365.service.GetOfficeUser;
import hqr.o365.service.GetOfficeUserByKeyWord;
import hqr.o365.service.GetOfficeUserDefaultPwd;
import hqr.o365.service.GetOfficeUserRole;
import hqr.o365.service.MassCreateOfficeUser;
import hqr.o365.service.UpdateOfficeUser;
import hqr.o365.service.UpdateOfficeUserRole;

@Controller
public class UserTabCtrl {
	
	@Autowired
	private GetOfficeUser gou;
	
	@Autowired
	private GetOfficeUserByKeyWord goubk;
	
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
	
	@Autowired
	private GetOfficeUserDefaultPwd goud;
	
	@Autowired
	private MassCreateOfficeUser mcou;
	
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
	
	@RequestMapping(value = {"tabs/dialogs/massCreateUser.html"})
	public String dummyCreateUser2(HttpServletRequest req) {
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
		return "tabs/dialogs/massCreateUser";
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
	public String getOfficeUser(String page, String rows, HttpServletRequest req) {
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
		HashMap<String,String> map = new HashMap<String,String>();
		
		Object obj = req.getSession().getAttribute("keyword");
		
		if(obj==null) {
			map = gou.getUsers(intPage, intRows);
		}
		else {
			String keyword = (String)obj;
			System.out.println("keyword is "+keyword);
			if(!"".equals(keyword)) {
				map = goubk.getUsers(intPage, intRows, keyword);
			}
			else {
				map = gou.getUsers(intPage, intRows);
			}
		}
		return map.get("message");
	}
	
	@ResponseBody
	@RequestMapping(value = {"/createOfficeUser"}, method = RequestMethod.POST)
	public String createUser(@RequestParam(name="mailNickname") String mailNickname,
			@RequestParam(name="userPrincipalName") String userPrincipalName,
			@RequestParam(name="displayName") String displayName,
			@RequestParam(name="licenses") String licenses,
			@RequestParam(name="userPwd") String userPwd) {
		
		HashMap<String, String> map = cou.createCommonUser(mailNickname, userPrincipalName, displayName, licenses, userPwd);
		
		return map.get("message");
	}	
	
	@ResponseBody
	@RequestMapping(value = {"/massCreateOfficeUser"}, method = RequestMethod.POST)
	public String massCreateUser(@RequestParam(name="prefix") String prefix,
			@RequestParam(name="domain") String domain,
			@RequestParam(name="count") String countStr,
			@RequestParam(name="licenses") String licenses,
			@RequestParam(name="userPwd") String userPwd,
			@RequestParam(name="strategy") String strategy) {
		
		int count = 10;
		try {
			count = Integer.parseInt(countStr);
		}
		catch (Exception e) {}
		
		HashMap<String, int[]> map = mcou.createCommonUser(prefix, domain, licenses, userPwd, count, strategy);
		int [] overall1 = map.get("user_res");
		int [] overall2 = map.get("license_res");
		
		StringBuilder sb = new StringBuilder();
		sb.append("用户创建成功:"+overall1[1]+"<br>");
		sb.append("用户创建失败:"+overall1[2]+"<br>");
		sb.append("订阅分配成功:"+overall2[1]+"<br>");
		sb.append("订阅分配失败:"+overall2[2]+"<br>");
		
		return sb.toString();
	}
	
	@ResponseBody
	@RequestMapping(value = {"/deleteOfficeUser"}, method = RequestMethod.POST)
	public String deleteUser(@RequestParam(name="uids") String uids) {
		HashMap<String, int[]> map = dou.deleteUser(uids);
		int [] overall = map.get("delete_res");
		
		StringBuilder sb = new StringBuilder();
		sb.append("用户删除成功:"+overall[0]+"<br>");
		sb.append("用户删除失败:"+overall[1]+"<br>");
		
		return sb.toString();
	}
	
	@ResponseBody
	@RequestMapping(value = {"/getOfficeUserDtls"}, method = RequestMethod.POST)
	public String getOfficeUserDtls(@RequestParam(name="uid") String uid) {
		return gour.getRole(uid);
	}
	
	@ResponseBody
	@RequestMapping(value = {"/updateOfficeUser"}, method = RequestMethod.POST)
	public String patchUser(@RequestParam(name="uids") String uids, @RequestParam(name="accountEnabled") String accountEnabled) {
		HashMap<String, int[]> map = uou.patchOfficeUser(uids, accountEnabled);
		
		int [] overall = map.get("status_res");
		
		StringBuilder sb = new StringBuilder();
		if("true".equals(accountEnabled)) {
			sb.append("用户启用成功:"+overall[0]+"<br>");
			sb.append("用户启用失败:"+overall[1]+"<br>");
		}
		else {
			sb.append("用户禁用成功:"+overall[0]+"<br>");
			sb.append("用户禁用失败:"+overall[1]+"<br>");
		}
		
		return sb.toString();
	}
	
	@ResponseBody
	@RequestMapping(value = {"/updateOfficeUserRole"}, method = RequestMethod.POST)
	public boolean patchUserRole(@RequestParam(name="uid") String uid, @RequestParam(name="action") String action) {
		return uour.update(uid, action);
	}
	
	@ResponseBody
	@RequestMapping(value = {"/saveKeyWord"}, method = RequestMethod.POST)
	public void saveKeywordInSession(HttpServletRequest req, String keyword) {
		req.getSession().setAttribute("keyword", keyword);
	}
	
	@ResponseBody
	@RequestMapping(value = {"/getDefaultPwd"}, method = RequestMethod.GET)
	public String getDefaultPassword() {
		return goud.getDefaultPwd();
	}
	
}
