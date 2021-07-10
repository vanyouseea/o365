package hqr.o365.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hqr.o365.dao.TaMasterCdRepo;
import hqr.o365.domain.TaMasterCd;

@Service
public class UpdateSystemInfo {

	@Autowired
	private TaMasterCdRepo tmc;
	
	@Autowired
	private ScanAppStatusService genRpt;
	
	public boolean updateInfo(String keyTy, String cd, String decode) {
		boolean flag = false;
		System.out.println("key_ty :"+keyTy);
		Optional<TaMasterCd> opt = tmc.findById(keyTy);
		//can find -> update
		if(opt.isPresent()) {
			TaMasterCd enti = opt.get();
			enti.setCd(cd);
			enti.setDecode(decode);
			tmc.saveAndFlush(enti);
			if("GEN_APP_RPT_CRON".equals(enti.getKeyTy())) {
				genRpt.setCron(enti.getCd());
			}
			flag = true;
		}
		//can find -> insert
		else {
			TaMasterCd enti = new TaMasterCd();
			enti.setKeyTy(keyTy);
			enti.setCd(cd);
			enti.setDecode(decode);
			enti.setLastUpdateId("o365");
			enti.setLastUpdateDt(new Date());
			tmc.saveAndFlush(enti);
			flag = true;
		}
		return flag;
	}
	
}
