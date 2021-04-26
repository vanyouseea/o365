package hqr.o365.ctrl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DummyCtrl {
	
	@RequestMapping(value = {"/dummy"})
	public String dummy() {
		return "dummy info <b>123</b>";
	}
}
