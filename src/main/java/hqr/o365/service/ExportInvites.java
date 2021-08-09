package hqr.o365.service;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hqr.o365.dao.TaInviteInfoRepo;
import hqr.o365.domain.TaInviteInfo;

@Service
public class ExportInvites {
	
	@Autowired
	private TaInviteInfoRepo tii;
	
	public boolean exportInvite(){
		boolean flag = false;
		try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("export_invite_info.csv"), "GB2312"))){
			bw.write("邀请码,订阅,生效时间,失效时间,状态,全局编号,邮件后缀,结果"+System.getProperty("line.separator"));
			
			List<TaInviteInfo> list = tii.findAll();
			for (TaInviteInfo en : list) {
				StringBuilder sb = new StringBuilder();
				sb.append(en.getInviteId()).append(",");
				sb.append(en.getLicensesCn()).append(",");
				sb.append(en.getStartDt()).append(",");
				sb.append(en.getEndDt()).append(",");
				sb.append(en.getInviteStatus()).append(",");
				sb.append(en.getSeqNo()).append(",");
				sb.append(en.getSuffix()).append(",");
				sb.append(en.getResult()).append(System.getProperty("line.separator"));
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
