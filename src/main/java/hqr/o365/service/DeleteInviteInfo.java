package hqr.o365.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import hqr.o365.dao.TaInviteInfoRepo;

@Service
public class DeleteInviteInfo {
	
	@Autowired
	private TaInviteInfoRepo tii;
	
	@CacheEvict(value="cacheInviteInfo", allEntries = true)
	public void deleteInviteCd(String cds) {
		String uuids[] = cds.split(",");
		for (String uuid : uuids) {
			try {
				tii.deleteById(uuid);
				tii.flush();
			}
			catch (Exception e) {}
		}
	}
}
