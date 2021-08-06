package hqr.o365.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TaInviteInfo {
	@Id
	private String inviteId;
	private String licenses;
	private String licensesCn;
	private Date startDt;
	private Date endDt;
	/*
	 * 1 - Enabled
	 * 2 - In-progress
	 * 3 - Success
	 * 4 - Failure
	 */
	private String inviteStatus = "1";
	//Global App seqNo
	private int seqNo;
	private String suffix;
	private String result;
	public String getInviteId() {
		return inviteId;
	}
	public void setInviteId(String inviteId) {
		this.inviteId = inviteId;
	}
	public String getLicenses() {
		return licenses;
	}
	public void setLicenses(String licenses) {
		this.licenses = licenses;
	}
	public String getLicensesCn() {
		return licensesCn;
	}
	public void setLicensesCn(String licensesCn) {
		this.licensesCn = licensesCn;
	}
	public Date getStartDt() {
		return startDt;
	}
	public void setStartDt(Date startDt) {
		this.startDt = startDt;
	}
	public Date getEndDt() {
		return endDt;
	}
	public void setEndDt(Date endDt) {
		this.endDt = endDt;
	}
	public String getInviteStatus() {
		return inviteStatus;
	}
	public void setInviteStatus(String inviteStatus) {
		this.inviteStatus = inviteStatus;
	}
	public int getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
}
