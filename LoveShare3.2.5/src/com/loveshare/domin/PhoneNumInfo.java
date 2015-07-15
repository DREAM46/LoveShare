package com.loveshare.domin;

public class PhoneNumInfo {

	private String city;
	private String province;
	private String supplier;

	public PhoneNumInfo() {

	}

	public String getCity() {
		return city;
	}

	public String getProvince() {
		return province;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	@Override
	public String toString() {
		return province + city + supplier;
	}

}
