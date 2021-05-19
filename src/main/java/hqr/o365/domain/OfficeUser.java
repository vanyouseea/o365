package hqr.o365.domain;

import java.util.ArrayList;
import java.util.List;

/*
 * {
	    "accountEnabled": "true",
	    "displayName": "Du Yong5",
	    "mailNickname": "duyong5",
	    "passwordPolicies": "DisablePasswordExpiration, DisableStrongPassword",
	    "passwordProfile": {
	        "password": "123",
	        "forceChangePasswordNextSignIn": false
	    },
	    "userPrincipalName": "11@11.com",
	    "usageLocation": "CN"
	}
 */

public class OfficeUser {
	private String id;
	private String accountEnabled="true";
	private String displayName;
	private String mailNickname;
	private String passwordPolicies = "DisablePasswordExpiration, DisableStrongPassword";
	private PasswordProfile passwordProfile = new PasswordProfile();
	private String userPrincipalName;
	private String usageLocation="CN";
	private List<String> assignedLicenses = new ArrayList<String>();
	public String getAccountEnabled() {
		return accountEnabled;
	}
	public void setAccountEnabled(String accountEnabled) {
		this.accountEnabled = accountEnabled;
	}
	public String getUsageLocation() {
		return usageLocation;
	}
	public void setUsageLocation(String usageLocation) {
		this.usageLocation = usageLocation;
	}
	public String getUserPrincipalName() {
		return userPrincipalName;
	}
	public void setUserPrincipalName(String userPrincipalName) {
		this.userPrincipalName = userPrincipalName;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getMailNickname() {
		return mailNickname;
	}
	public void setMailNickname(String mailNickname) {
		this.mailNickname = mailNickname;
	}
	public String getPasswordPolicies() {
		return passwordPolicies;
	}
	public void setPasswordPolicies(String passwordPolicies) {
		this.passwordPolicies = passwordPolicies;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public PasswordProfile getPasswordProfile() {
		return passwordProfile;
	}
	public void setPasswordProfile(PasswordProfile passwordProfile) {
		this.passwordProfile = passwordProfile;
	}
	public List<String> getAssignedLicenses() {
		return assignedLicenses;
	}
	public void setAssignedLicenses(List<String> assignedLicenses) {
		this.assignedLicenses = assignedLicenses;
	}
	public class PasswordProfile{
		private String password;
		private boolean forceChangePasswordNextSignIn = true;
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public boolean isForceChangePasswordNextSignIn() {
			return forceChangePasswordNextSignIn;
		}
		public void setForceChangePasswordNextSignIn(boolean forceChangePasswordNextSignIn) {
			this.forceChangePasswordNextSignIn = forceChangePasswordNextSignIn;
		}
	}
	
}
