package hqr.o365.ctrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.hutool.http.server.HttpServerRequest;
import hqr.o365.domain.LicenseInfo;
import hqr.o365.service.GetLicenseInfo;
import hqr.o365.service.GetOfficeUser;

@Controller
public class UserTabCtrl {
	
	@Autowired
	private GetOfficeUser gou;
	
	@Autowired
	private GetLicenseInfo gli;
	
	@RequestMapping(value = {"/tabs/user.html"})
	public String dummy() {
		return "tabs/user";
	}
	
	@RequestMapping(value = {"tabs/dialogs/createUser.html"})
	public String dummyCreateUser(HttpServletRequest req) {
		Object tmp = req.getSession().getAttribute("licenseVo");
		if(tmp==null) {
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
	
}
