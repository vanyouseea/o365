package hqr.o365.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import hqr.o365.dao.TaOfficeInfoRepo;
import hqr.o365.domain.TaOfficeInfo;

@Service
public class GetOfficeInfo {
	
	@Autowired
	private TaOfficeInfoRepo repo;
	
	@Value("${UA}")
    private String ua;

	@Cacheable(value="cacheOfficeInfo")
	public String getAllOfficeInfo(int intRows, int intPage) {
		long total = repo.count();
		
		Page<TaOfficeInfo> pages = repo.findAll(PageRequest.of(intPage - 1, intRows));
		
		HashMap map = new HashMap();
		map.put("total", total);
		map.put("rows", pages.getContent());
		
		return JSON.toJSON(map).toString();
	}
	
}
