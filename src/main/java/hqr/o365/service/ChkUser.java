package hqr.o365.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hqr.o365.dao.TaUserRepo;
import hqr.o365.domain.TaUser;

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
	
	public TaUser checkCredential(String userid, String pwd) {
		List<TaUser> users = tup.checkCredential(userid, pwd);
		if(users!=null && users.size()>0) {
			return users.get(0);
		}
		else {
			return null;
		}
	}
	
}
