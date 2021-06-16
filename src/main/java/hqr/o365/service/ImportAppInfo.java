package hqr.o365.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import hqr.o365.dao.TaOfficeInfoRepo;
import hqr.o365.domain.TaOfficeInfo;

@Service
public class ImportAppInfo {
	
	@Autowired
	private TaOfficeInfoRepo toi;
	
	public HashMap<String, int[]> importApp(MultipartFile file){
		int succ = 0;
		int fail = 0;

		//userid(opt),password(opt),tenantid,appid,secretid,remarks(opt)
		//,,6d742637-e5a7-4cf1-b632-12d36d3d0240,ff8680df-2137-4d0f-8173-cf6c1f878771,uofkvqf5Riretg~F5W-2z.J.J6h0N1.mWi,
		try(BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), "GB2312"));){
			String cont = "";
			int count = 0;
			while((cont=br.readLine())!=null) {
				//skip header
				count++;
				if(count==1) {
					continue;
				}
				
				String []appInfos = cont.split(",");
				if(appInfos.length>=5) {
					TaOfficeInfo enti = new TaOfficeInfo();
					if(appInfos.length==5) {
						enti.setUserId(appInfos[0]);
						enti.setPasswd(appInfos[1]);
						enti.setTenantId(appInfos[2]);
						enti.setAppId(appInfos[3]);
						enti.setSecretId(appInfos[4]);
						enti.setRemarks("");
					}
					else if(appInfos.length>5){
						enti.setUserId(appInfos[0]);
						enti.setPasswd(appInfos[1]);
						enti.setTenantId(appInfos[2]);
						enti.setAppId(appInfos[3]);
						enti.setSecretId(appInfos[4]);
						enti.setRemarks(appInfos[5]);
					}
					toi.saveAndFlush(enti);
					succ++;
				}
				else {
					fail++;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		HashMap<String, int[]> map = new HashMap<String, int[]>();
		int [] overall = new int[] {succ+fail, succ, fail};
		map.put("import_res", overall);
		
		return map;
	}
	
}
