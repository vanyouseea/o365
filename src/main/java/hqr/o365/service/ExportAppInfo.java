package hqr.o365.service;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hqr.o365.dao.TaOfficeInfoRepo;
import hqr.o365.domain.TaOfficeInfo;

@Service
public class ExportAppInfo {
	
	@Autowired
	private TaOfficeInfoRepo toi;
	
	public boolean exportApp(){
		boolean flag = false;
		try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("export.csv"), "GB2312"))){
			bw.write("userid,password,tenantid,appid,secretid,remarks"+System.getProperty("line.separator"));
			
			List<TaOfficeInfo> list = toi.findAll();
			for (TaOfficeInfo en : list) {
				StringBuilder sb = new StringBuilder();
				sb.append(en.getUserId()).append(",");
				sb.append(en.getPasswd()).append(",");
				sb.append(en.getTenantId()).append(",");
				sb.append(en.getAppId()).append(",");
				sb.append(en.getSecretId()).append(",");
				sb.append(en.getRemarks()).append(System.getProperty("line.separator"));
				bw.write(sb.toString());
			}
			
			bw.flush();
			bw.close();
			flag = true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
}
