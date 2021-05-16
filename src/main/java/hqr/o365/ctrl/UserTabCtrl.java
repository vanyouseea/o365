package hqr.o365.ctrl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import hqr.o365.service.GetOfficeUser;

@Controller
public class UserTabCtrl {
	
	@Autowired
	private GetOfficeUser gou;
	
	@RequestMapping(value = {"/tabs/user.html"})
	public String dummy() {
		return "tabs/user";
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
