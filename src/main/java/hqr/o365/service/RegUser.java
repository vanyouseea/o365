package hqr.o365.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hqr.o365.dao.TaUserRepo;
import hqr.o365.domain.TaUser;

@Service
public class RegUser {
	
	@Autowired
	private TaUserRepo tur;
	
	public void save(TaUser user) {
		tur.save(user);
	}
}
