package hqr.o365.domain;

public class PrivilegedUser {
	private String id;
	private String userPrincipalName;
	private String accountEnabled="true";
	private String displayName;
	private String usageLocation;
	private String roleName;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAccountEnabled() {
		return accountEnabled;
	}
	public void setAccountEnabled(String accountEnabled) {
		this.accountEnabled = accountEnabled;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getUsageLocation() {
		return usageLocation;
	}
	public void setUsageLocation(String usageLocation) {
		this.usageLocation = usageLocation;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getUserPrincipalName() {
		return userPrincipalName;
	}
	public void setUserPrincipalName(String userPrincipalName) {
		this.userPrincipalName = userPrincipalName;
	}
}
