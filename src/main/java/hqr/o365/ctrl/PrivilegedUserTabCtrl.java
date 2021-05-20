package hqr.o365.ctrl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PrivilegedUserTabCtrl {
	@RequestMapping(value = {"/tabs/privilegedUser.html"})
	public String dummy() {
		return "tabs/privilegedUser";
	}
}
