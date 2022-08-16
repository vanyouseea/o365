package hqr.o365.service;

import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import hqr.o365.dao.TaInviteInfoRepo;
import hqr.o365.domain.TaInviteInfo;


@Service
public class GetInviteInfo {
	@Autowired
	private TaInviteInfoRepo repo;
	
	@Value("${UA}")
    private String ua;
	
	@Cacheable(value="cacheInviteInfo")
	public String getAllInviteInfo(int intRows, int intPage) {
		long total = repo.count();
		
		Page<TaInviteInfo> pages = repo.findAll(PageRequest.of(intPage-1, intRows));
		
		HashMap map = new HashMap();
		map.put("total", total);
		map.put("rows", pages.getContent());
		
		return JSON.toJSON(map).toString();
	}
	
}
