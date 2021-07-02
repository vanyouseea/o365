package hqr.o365.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hqr.o365.dao.TaMasterCdRepo;

@Service
public class DeleteSystemInfo {
	@Autowired
	private TaMasterCdRepo tmc;
	
	public boolean deletePk(String keyTy) {
		boolean status = false;
		try {
			tmc.deleteById(keyTy);
			status= true;
		}
		catch (Exception e) {
			status = false;
		}
		return status;
	}
}
