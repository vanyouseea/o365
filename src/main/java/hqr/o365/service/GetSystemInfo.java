package hqr.o365.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import hqr.o365.dao.TaMasterCdRepo;
import hqr.o365.domain.TaMasterCd;

@Service
public class GetSystemInfo {
	
	@Autowired
	private TaMasterCdRepo tmc;
	
	@Cacheable(value="cacheSysInfo")
	public String getAllSystemInfo() {
		long total = tmc.count();
		List<TaMasterCd> rows = new ArrayList<TaMasterCd>();
		if(total>0) {
			rows = tmc.findAll();
		}
		
		HashMap map = new HashMap();
		map.put("total", total);
		map.put("rows", rows);
		
		return JSON.toJSON(map).toString();
	}
	
}
