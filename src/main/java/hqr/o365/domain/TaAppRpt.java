package hqr.o365.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TaAppRpt {
	@Id
	private int seqNo;
	private String tenantId;
	private String appId;
	private String secretId;
	private Date rptDt;
	private String totalUser;
	private int totalGlobalAdmin = 0;
	private int enableGlobalAdmin = 0;
	private int disableGloablAdmin = 0;
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
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
	public int getTotalGlobalAdmin() {
		return totalGlobalAdmin;
	}
	public void setTotalGlobalAdmin(int totalGlobalAdmin) {
		this.totalGlobalAdmin = totalGlobalAdmin;
	}
	public int getEnableGlobalAdmin() {
		return enableGlobalAdmin;
	}
	public void setEnableGlobalAdmin(int enableGlobalAdmin) {
		this.enableGlobalAdmin = enableGlobalAdmin;
	}
	public int getDisableGloablAdmin() {
		return disableGloablAdmin;
	}
	public void setDisableGloablAdmin(int disableGloablAdmin) {
		this.disableGloablAdmin = disableGloablAdmin;
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
	public int getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}
}
