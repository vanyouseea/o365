package hqr.o365.ctrl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import hqr.o365.service.GetOfficeInfo;

@RestController
public class GetConfigCtrl {
	
	@Autowired
	private GetOfficeInfo gi;
	
	@RequestMapping(value = {"/getConfig"})
	public String dummy() {
		return gi.getAllOfficeInfo();
	}
}
