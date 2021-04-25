package hqr.o365.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hqr.o365.domain.TaUser;
import hqr.o365.service.RegUser;

@Controller
public class RegCtrl {
	
	@Autowired
	private RegUser su;
	
	@RequestMapping(value = "/reg")
	public String regUser(@RequestParam(name="userid") String userid,@RequestParam(name="pwd") String pwd) {
		TaUser user = new TaUser();
		user.setUserId(userid);
		user.setPasswd(pwd);
		su.save(user);
		
		return "login";
	}
}
