package hqr.o365.domain;

public class LicenseInfo {
	private String capabilityStatus ="";
	private String skuId = "";
	private String skuPartNumber = "";
	private String skuIdDesc = "";
	private String consumedUnits = "0";
	//enable only
	private String prepaidUnits = "0";
	public String getCapabilityStatus() {
		return capabilityStatus;
	}
	public void setCapabilityStatus(String capabilityStatus) {
		this.capabilityStatus = capabilityStatus;
	}
	public String getSkuId() {
		return skuId;
	}
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	public String getConsumedUnits() {
		return consumedUnits;
	}
	public void setConsumedUnits(String consumedUnits) {
		this.consumedUnits = consumedUnits;
	}
	public String getPrepaidUnits() {
		return prepaidUnits;
	}
	public void setPrepaidUnits(String prepaidUnits) {
		this.prepaidUnits = prepaidUnits;
	}
	public String getSkuPartNumber() {
		return skuPartNumber;
	}
	public void setSkuPartNumber(String skuPartNumber) {
		this.skuPartNumber = skuPartNumber;
	}
	public String getSkuIdDesc() {
		return skuIdDesc;
	}
	public void setSkuIdDesc(String skuIdDesc) {
		this.skuIdDesc = skuIdDesc;
	}
}
