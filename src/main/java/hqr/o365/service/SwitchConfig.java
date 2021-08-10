package hqr.o365.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import hqr.o365.dao.TaOfficeInfoRepo;
import hqr.o365.domain.TaOfficeInfo;

@Service
public class SwitchConfig {
	
	@Autowired
	private TaOfficeInfoRepo repo;
	
	@CacheEvict(value= {"cacheOfficeInfo","cacheOrg","cacheLicense","cacheRoleUser","cacheOfficeUser"}, allEntries = true)
	public boolean updateConfig(TaOfficeInfo enti) {
		boolean flag = false;
		try {
			repo.updateAllNo();
			repo.save(enti);
			flag = true;
		}
		catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}
	
}
