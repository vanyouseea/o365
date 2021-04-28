package hqr.o365.ctrl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ConfigTabCtrl {
	@RequestMapping(value = {"/tabs/config.html"})
	public String dummy() {
		return "tabs/config";
	}
}
