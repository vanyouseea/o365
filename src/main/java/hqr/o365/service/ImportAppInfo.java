package hqr.o365.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImportAppInfo {
	
	public HashMap<String, int[]> importData(MultipartFile file){
		int succ = 0;
		int fail = 0;
		
		//userid(opt),password(opt),tenantid,appid,secretid,remarks(opt)
		//,,6d742637-e5a7-4cf1-b632-12d36d3d0240,ff8680df-2137-4d0f-8173-cf6c1f878771,uofkvqf5Riretg~F5W-2z.J.J6h0N1.mWi,
		try(BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()));){
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
					if(appInfos.length==5) {
						
					}
					else if(appInfos.length>5){
						
					}
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
