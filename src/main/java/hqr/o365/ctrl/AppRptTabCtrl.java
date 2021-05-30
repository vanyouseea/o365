package hqr.o365.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import hqr.o365.service.GetAppRptService;
import hqr.o365.service.ScanAppStatusService;

@Controller
public class AppRptTabCtrl {

	@Autowired
	private GetAppRptService gars;
	
	@Autowired
	private ScanAppStatusService sass;
	
	
	@RequestMapping(value = {"/tabs/apprpt.html"})
	public String dummy() {
		return "tabs/apprpt";
	}
	
	@ResponseBody
	@RequestMapping(value = {"/getAppRpt"})
	public String getOverallRpt(String page, String rows) {
		int intPage = 1;
		int intRows = 10;
		try {
			intPage = Integer.valueOf(page);
		}
		catch (Exception e) {
			System.out.println("Invalid page, force it to 1");
		}
		try {
			intRows = Integer.valueOf(rows);
		}
		catch (Exception e) {
			System.out.println("Invalid row, force it to 10");
		}
		
		return gars.getSysRpt(intRows, intPage);
	}
	
	@ResponseBody
	@RequestMapping(value = {"/getAppRptManaully"})
	public boolean getOverallRptManaully(String page, String rows) {
		sass.execute("M");
		return true;
	}
	
}
