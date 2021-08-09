package hqr.o365.ctrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import hqr.o365.domain.LicenseInfo;
import hqr.o365.service.CreateOfficeUserByInviteCd;
import hqr.o365.service.DeleteInviteInfo;
import hqr.o365.service.GetInviteInfo;
import hqr.o365.service.GetLicenseInfo;
import hqr.o365.service.MassCreateInviteCd;

@Controller
public class InviteTabCtrl {
	
	@Autowired
	private GetInviteInfo gii;
	
	@Autowired
	private GetLicenseInfo gli;
	
	@Autowired
	private MassCreateInviteCd mci;
	
	@Autowired
	private DeleteInviteInfo dii;
	
	@Autowired
	private CreateOfficeUserByInviteCd coubi;
	
	@RequestMapping(value = {"/tabs/invite.html"})
	public String dummy() {
		return "tabs/invite";
	}
	
	@RequestMapping(value = {"/tabs/dialogs/createInviteCd.html"})
	public String dummy2(HttpServletRequest req) {
		Object tmp2 = req.getSession().getAttribute("licenseVo");
		if(tmp2==null) {
			HashMap<String, Object> map2 = gli.getLicenses();
			List<LicenseInfo> vo = new ArrayList<LicenseInfo>();
			Object obj = map2.get("licenseVo");
			if(obj!=null) {
				vo = (List<LicenseInfo>)obj;
			}
			req.getSession().setAttribute("licenseVo", vo);
		}
		else {
			System.out.println("licenseVo already exist,skip to get");
		}
		return "tabs/dialogs/createInviteCd";
	}
	
	@RequestMapping(value = {"/refer.html"})
	public String dummy3() {
		return "refer";
	}
	
	@ResponseBody
	@RequestMapping(value = {"/getInvite"})
	public String getInvite(String page, String rows) {
		int intPage = 1;
		int intRows = 10;
		try {
			intPage = Integer.valueOf(page);
		}
		catch (Exception e) {
			System.out.println("Invalid page, force it to 1");
		}
		try {
			intRows = Integer.valueOf(rows);
		}
		catch (Exception e) {
			System.out.println("Invalid row, force it to 10");
		}
		
		return gii.getAllInviteInfo(intRows, intPage);
	}
	
	@ResponseBody
	@RequestMapping(value = {"/massCreateInviteCd"})
	public String massCreateInviteCd(@RequestParam(name="count") String countStr,
			@RequestParam(name="licenses") String licenses,
			@RequestParam(name="domain") String domain,
			@RequestParam(name="startDt",required = false) String startDt,
			@RequestParam(name="endDt",required = false) String endDt) {
		
		System.out.println("startDt:"+startDt+" endDt:"+endDt);
		int count = 10;
		try {
			count = Integer.parseInt(countStr);
		}
		catch (Exception e) {}
		
		return mci.create(count, licenses, startDt, endDt, domain);
	}
	
	@ResponseBody
	@RequestMapping(value = {"/delInviteCds"})
	public String deleteInviteInfo(@RequestParam(name="uuids") String uuids) {
		dii.deleteInviteCd(uuids);
		return "已删除";
	}
	
	@ResponseBody
	@RequestMapping(value = {"/createUserByInviteCd"})
	public String createUserByInviteCd(@RequestParam(name="mailNickname") String mailNickname,
			@RequestParam(name="displayName") String displayName,
			@RequestParam(name="inviteCd") String inviteCd,
			@RequestParam(name="password") String password) {

		return coubi.createCommonUser(mailNickname, displayName, inviteCd, password);
	}
	
}
