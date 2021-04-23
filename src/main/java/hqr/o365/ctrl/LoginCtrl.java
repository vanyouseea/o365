package hqr.o365.ctrl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginCtrl {

	@RequestMapping(value = "/login")
	public String goLogin() {
		System.out.println("Go to login");
		return "login";
	}
	
}
