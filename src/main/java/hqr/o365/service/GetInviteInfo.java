package hqr.o365.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	
	public String getAllInviteInfo(int intRows, int intPage) {
		long total = repo.count();
		List<TaInviteInfo> rows = new ArrayList<TaInviteInfo>();
		if(total>0) {
			rows = repo.getInviteInfos(intRows * (intPage - 1), intRows * intPage );
		}
		
		HashMap map = new HashMap();
		map.put("total", total);
		map.put("rows", rows);
		return JSON.toJSON(map).toString();
	}
	
}
