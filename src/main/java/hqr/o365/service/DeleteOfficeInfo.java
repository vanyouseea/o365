package hqr.o365.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hqr.o365.dao.TaOfficeInfoRepo;

@Service
public class DeleteOfficeInfo {
	@Autowired
	private TaOfficeInfoRepo repo;
	
	public boolean delete(int id) {
		try {
			repo.deleteById(id);
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}
}
