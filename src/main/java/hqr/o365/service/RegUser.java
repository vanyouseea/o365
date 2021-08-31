package hqr.o365.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import hqr.o365.dao.TaMasterCdRepo;
import hqr.o365.dao.TaUserRepo;
import hqr.o365.domain.TaMasterCd;
import hqr.o365.domain.TaUser;

@Service
public class RegUser {
	
	@Autowired
	private TaUserRepo tur;
	
	@Autowired
	private TaMasterCdRepo tmc;
	
	@CacheEvict(value={"cacheTaUser","cacheGlobalInd"}, allEntries = true)
	public void save(TaUser user) {
		tur.save(user);
		//stop reg the admin user
		Optional<TaMasterCd> opt = tmc.findById("GLOBAL_REG");
		if(opt.isPresent()) {
			TaMasterCd pd = opt.get();
			pd.setCd("N");
			tmc.save(pd);
		}
		else {
			TaMasterCd pd = new TaMasterCd();
			pd.setKeyTy("GLOBAL_REG");
			pd.setCd("N");
			pd.setDecode("turn on the function to register the admin user");
			pd.setCreateDt(new Date());
			pd.setStartDt(new Date());
			pd.setEndDt(null);
			pd.setLastUpdateId("o365");
			pd.setLastUpdateDt(new Date());
			tmc.saveAndFlush(pd);
		}
	}
}
