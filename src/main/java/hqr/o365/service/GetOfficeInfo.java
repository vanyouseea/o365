package hqr.o365.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import hqr.o365.dao.TaOfficeInfoRepo;
import hqr.o365.domain.TaOfficeInfo;

@Service
public class GetOfficeInfo {
	
	@Autowired
	private TaOfficeInfoRepo repo;
	
	public String getAllOfficeInfo() {
		long total = repo.count();
		List<TaOfficeInfo> rows = new ArrayList<TaOfficeInfo>();
		if(total>0) {
			rows = repo.findAll();
		}
		
		HashMap map = new HashMap();
		map.put("total", total);
		map.put("rows", rows);
		
		return JSON.toJSON(map).toString();
	}
	
}
