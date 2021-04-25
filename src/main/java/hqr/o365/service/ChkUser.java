package hqr.o365.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hqr.o365.dao.TaUserRepo;

@Service
public class ChkUser {
	@Autowired
	private TaUserRepo tup;
	
	public String checkCanReg(String userid) {
		int cnt = tup.chkUserId(userid);
		if(cnt>0) {
			return "N";
		}
		else{
			return "Y";
		}
	}
	
	public String checkCredential(String userid, String pwd) {
		int cnt = tup.checkCredential(userid, pwd);
		if(cnt>0) {
			return "Y";
		}
		else {
			return "N";
		}
	}
	
}
