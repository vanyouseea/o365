package hqr.o365.service;

import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
		
		Page<TaAppRpt> pages = tar.findAll(PageRequest.of(intPage-1, intRows));
		
		HashMap map = new HashMap();
		map.put("total", total);
		map.put("rows", pages.getContent());
		
		return JSON.toJSON(map).toString();
		
	}
}
