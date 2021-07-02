package hqr.o365.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TaAppRpt {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int seqNo;
	private String tenantId;
	private String appId;
	private String secretId;
	private String remarks;
	private Date rptDt;
	private String totalUser;
	private String totalGlobalAdmin ;
	private String enableGlobalAdmin ;
	private String disableGloablAdmin ;
	private String spo;
	public int getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getSecretId() {
		return secretId;
	}
	public void setSecretId(String secretId) {
		this.secretId = secretId;
	}
	public Date getRptDt() {
		return rptDt;
	}
	public void setRptDt(Date rptDt) {
		this.rptDt = rptDt;
	}
	public String getTotalUser() {
		return totalUser;
	}
	public void setTotalUser(String totalUser) {
		this.totalUser = totalUser;
	}
	public String getTotalGlobalAdmin() {
		return totalGlobalAdmin;
	}
	public void setTotalGlobalAdmin(String totalGlobalAdmin) {
		this.totalGlobalAdmin = totalGlobalAdmin;
	}
	public String getEnableGlobalAdmin() {
		return enableGlobalAdmin;
	}
	public void setEnableGlobalAdmin(String enableGlobalAdmin) {
		this.enableGlobalAdmin = enableGlobalAdmin;
	}
	public String getDisableGloablAdmin() {
		return disableGloablAdmin;
	}
	public void setDisableGloablAdmin(String disableGloablAdmin) {
		this.disableGloablAdmin = disableGloablAdmin;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getSpo() {
		return spo;
	}
	public void setSpo(String spo) {
		this.spo = spo;
	}
}
