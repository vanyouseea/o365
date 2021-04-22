package hqr.o365.ctrl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexCtrl {
	@RequestMapping(value = {"/","/index.html"})
	public String index() {
		System.out.println("asdasd");
		return "index";
	}
}
