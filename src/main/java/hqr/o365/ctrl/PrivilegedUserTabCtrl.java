package hqr.o365.ctrl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import hqr.o365.service.GetOfficeRoleUser;

@Controller
public class PrivilegedUserTabCtrl {
	
	@Autowired
	private GetOfficeRoleUser goru;
	
	@RequestMapping(value = {"/tabs/privilegedUser.html"})
	public String dummy() {
		return "tabs/privilegedUser";
	}
	
	@ResponseBody
	@RequestMapping(value = {"/getOfficeRoleUser"})
	public String getOfficeRoleUser() {
		HashMap<String, String> map = goru.getRoleUsers();
		return map.get("message");
	}
	
}
