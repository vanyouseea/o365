package hqr.o365.ctrl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import hqr.o365.service.GetLicenseInfo;

@Controller
public class LicenseTabCtrl {
	
	@Autowired
	private GetLicenseInfo gli;
	
	@ResponseBody
	@RequestMapping(value = {"/getLicense"}, method = RequestMethod.POST)
	public String getLicense() {
		HashMap<String, String> map = gli.getLicenses();
		String status = map.get("status");
		return map.get("message");
	}
}
