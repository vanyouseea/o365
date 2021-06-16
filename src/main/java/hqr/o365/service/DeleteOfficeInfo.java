package hqr.o365.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hqr.o365.dao.TaOfficeInfoRepo;

@Service
public class DeleteOfficeInfo {
	@Autowired
	private TaOfficeInfoRepo repo;
	
	public HashMap<String, int[]> delete(String seqNos) {
		String seqArr[] = seqNos.split(",");
		int succ = 0;
		int fail = 0;
		for (String seq : seqArr) {
			try {
				repo.deleteById(Integer.valueOf(seq));
				succ ++;
			}
			catch (Exception e) {
				fail ++;
			}
		}
		
		HashMap<String, int[]> map = new HashMap<String, int[]>();
		int [] overall = new int[] {succ, fail};
		map.put("delete_res", overall);
		
		return map;
	}
}
