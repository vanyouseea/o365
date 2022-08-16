package hqr.o365.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import hqr.o365.dao.TaInviteInfoRepo;
import hqr.o365.dao.TaMasterCdRepo;
import hqr.o365.dao.TaOfficeInfoRepo;
import hqr.o365.domain.TaInviteInfo;
import hqr.o365.domain.TaMasterCd;
import hqr.o365.domain.TaOfficeInfo;

@Service
public class MassCreateInviteCd {
	
	@Autowired
	private TaOfficeInfoRepo repo;
	
	@Autowired
	private ValidateAppInfo vai;
	
	@Autowired
	private TaInviteInfoRepo tii;
	
	@Autowired
	private TaMasterCdRepo tmc;
	
	@Value("${UA}")
    private String ua;
	
	private SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
	
	@CacheEvict(value="cacheInviteInfo", allEntries = true)
	public String create(int count, String licenses, String startDt, String endDt, String domain) {
		String result = "";
		
		//get info
		List<TaOfficeInfo> list = repo.findBySelected("是");
		if(list!=null&&list.size()>0) {
			TaOfficeInfo ta = list.get(0);
			String accessToken = "";
			if(vai.checkAndGet(ta.getTenantId(), ta.getAppId(), ta.getSecretId())) {
				accessToken = vai.getAccessToken();
			}
			
			if(!"".equals(accessToken)) {
				for(int i=0;i<count;i++) {
					TaInviteInfo enti = new TaInviteInfo();
					enti.setInviteId(UUID.randomUUID().toString());
					if(startDt!=null) {
						try {
							enti.setStartDt(yyyyMMdd.parse(startDt));
						} catch (ParseException e) {}
					}
					if(endDt!=null) {
						try {
							enti.setEndDt(yyyyMMdd.parse(endDt));
						} catch (ParseException e) {}
					}
					enti.setInviteStatus("1");
					enti.setSeqNo(ta.getSeqNo());
					enti.setLicenses(licenses);
					
					String arrLicen[] = licenses.split(",");
					String licensesCn = "";
					for (String license : arrLicen) {
						Optional<TaMasterCd> opt = tmc.findById(license);
						if(opt.isPresent()) {
							licensesCn = licensesCn + opt.get().getDecode() + ",";
						}
						else {
							licensesCn = licensesCn + license + ",";
						}
					}
					if(licensesCn.length()>=1) {
						licensesCn = licensesCn.substring(0, licensesCn.length()-1);
					}
					enti.setLicensesCn(licensesCn);
					enti.setSuffix(domain);
					tii.save(enti);
				}
				tii.flush();
				result = "成功创建"+count+"个邀请码";
			}
			else {
				result = "获取token失败，请确认全局的有效性";
			}
		}
		else {
			result = "请先选择一个全局";
		}
		
		return result;
	}
	
}
