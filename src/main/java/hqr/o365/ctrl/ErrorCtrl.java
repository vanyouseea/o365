package hqr.o365.ctrl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorCtrl {
	
	@RequestMapping(value = {"/error/401.html"})
	public String err401() {
		return "error/401";
	}
	
}
