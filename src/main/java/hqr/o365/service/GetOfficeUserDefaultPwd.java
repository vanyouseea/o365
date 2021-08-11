package hqr.o365.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import hqr.o365.dao.TaMasterCdRepo;
import hqr.o365.domain.TaMasterCd;

@Service
public class GetOfficeUserDefaultPwd {

	@Autowired
	private TaMasterCdRepo tmc;
	
	@Cacheable(value="cacheDefaultPwd")
	public String getDefaultPwd() {
		String pwd = "Mjj@1234";
		
		Optional<TaMasterCd> opt = tmc.findById("DEFAULT_PASSWORD");
		if(opt.isPresent()) {
			pwd = opt.get().getCd();
		}
		
		return pwd;
	}
	
}
