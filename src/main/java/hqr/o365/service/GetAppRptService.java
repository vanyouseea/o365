package hqr.o365.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import hqr.o365.dao.TaAppRptRepo;
import hqr.o365.domain.TaAppRpt;

@Service
public class GetAppRptService {
	
	@Autowired
	private TaAppRptRepo tar;
	
	@Cacheable(value="cacheApprpt")
	public String getSysRpt(int intRows, int intPage) {
		long total = tar.count();
		List<TaAppRpt> rows = new ArrayList<TaAppRpt>();
		if(total>0) {
			rows = tar.getSysRpt(intRows * (intPage - 1), intRows * intPage );
		}
		
		HashMap map = new HashMap();
		map.put("total", total);
		map.put("rows", rows);
		
		return JSON.toJSON(map).toString();
		
	}
}
