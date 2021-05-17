package hqr.o365.ctrl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LicenseCtrl {

	@RequestMapping(value = {"/tabs/license.html"})
	public String dummy() {
		return "tabs/license";
	}
}
