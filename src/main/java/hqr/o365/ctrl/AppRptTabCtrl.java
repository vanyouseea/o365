package hqr.o365.ctrl;

import java.io.File;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import hqr.o365.service.GetAppRptService;
import hqr.o365.service.GetExchangeRpt;
import hqr.o365.service.GetOnedriveRpt;
import hqr.o365.service.ScanAppStatusService;

@Controller
public class AppRptTabCtrl {

	@Autowired
	private GetAppRptService gars;
	
	@Autowired
	private ScanAppStatusService sass;
	
	@Autowired
	private GetExchangeRpt ger;
	
	@Autowired
	private GetOnedriveRpt gor;
	
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
	@RequestMapping(value= {"/getExchangeRpt"})
	public ResponseEntity<FileSystemResource> getExchangeRpt(int seqNo){
		ger.execute(seqNo);
		return export(new File("exchange_details.csv"));
	}
	
	@ResponseBody
	@RequestMapping(value= {"/getOnedriveRpt"})
	public ResponseEntity<FileSystemResource> getOnedriveRpt(int seqNo){
		gor.execute(seqNo);
		return export(new File("onedrive_details.csv"));
	}
	
	public ResponseEntity<FileSystemResource> export(File file) { 
		HttpHeaders headers = new HttpHeaders();
	    headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
	    headers.add("Content-Disposition", "attachment; filename="+file.getName());
	    headers.add("Pragma", "no-cache");
	    headers.add("Expires", "0");
	    headers.add("Last-Modified", new Date().toString());
	    headers.add("ETag", String.valueOf(System.currentTimeMillis()));
	 
	    return ResponseEntity.ok().headers(headers) .contentLength(file.length()) .contentType(MediaType.parseMediaType("text/csv")) .body(new FileSystemResource(file));
	}
	
	@ResponseBody
	@RequestMapping(value = {"/getAppRptManaully"})
	public boolean getOverallRptManaully(String page, String rows) {
		sass.execute("M");
		return true;
	}
	
}
