package hqr.o365.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import hqr.o365.dao.TaUserRepo;
import hqr.o365.domain.TaUser;

@Service
public class ChkUser {
	@Autowired
	private TaUserRepo tup;
	
	@Cacheable(value="cacheTaUser")
	public String checkCanReg(String userid) {
		TaUser user = tup.findByUserId(userid);
		if(user!=null) {
			return "N";
		}
		else{
			return "Y";
		}
	}
}
