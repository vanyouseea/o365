package hqr.o365.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import hqr.o365.dao.TaUserRepo;

@Service
public class ChkUser {
	@Autowired
	private TaUserRepo tup;
	
	@Cacheable(value="cacheTaUser")
	public String checkCanReg(String userid) {
		int cnt = tup.chkUserId(userid);
		if(cnt>0) {
			return "N";
		}
		else{
			return "Y";
		}
	}
}
