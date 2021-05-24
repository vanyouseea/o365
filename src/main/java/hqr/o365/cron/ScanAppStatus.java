package hqr.o365.cron;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import hqr.o365.dao.TaAppRptRepo;
import hqr.o365.dao.TaOfficeInfoRepo;
import hqr.o365.domain.TaAppRpt;
import hqr.o365.domain.TaOfficeInfo;


public class ScanAppStatus {
	
	@Autowired
	private TaOfficeInfoRepo toi;
	
	@Autowired
	private TaAppRptRepo tar;
	
	public void scan() {
		//clean up the report table
		tar.deleteAll();
		tar.flush();
		
		//copy the tenant id from ta_office_info to ta_app_rpt
		List<TaOfficeInfo> list =  toi.findAll();
		if(list!=null) {
			for (int i = 0; i < list.size(); i++) {
				TaOfficeInfo tt = list.get(i);
				TaAppRpt rptEnti = new TaAppRpt();
				rptEnti.setTenantId(tt.getTenantId());
				rptEnti.setAppId(tt.getAppId());
				rptEnti.setSecretId(tt.getSecretId());
				tar.saveAndFlush(rptEnti);
			}
		}
	}
	
}
