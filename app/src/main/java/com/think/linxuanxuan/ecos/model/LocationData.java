package com.think.linxuanxuan.ecos.model;

public class LocationData {

	public String latitude;
	public String longitude;
	
	public String locCity;
	public String locDistrict;
	public String locStreet;
	
	public LocationData(){}
	
	public LocationData(String latitude, String longitude,String locCity, 
			String locDistrict, String locStreet) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		
		this.locCity = locCity;
		this.locDistrict = locDistrict;
		this.locStreet = locStreet;
	}

	public String getLocCity() {
		return locCity;
	}

	public void setLocCity(String locCity) {
		this.locCity = locCity;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLocDistrict() {
		return locDistrict;
	}

	public void setLocDistrict(String locDistrict) {
		this.locDistrict = locDistrict;
	}

	public String getLocStreet() {
		return locStreet;
	}

	public void setgetLocStreet(String locStreet) {
		this.locStreet = locStreet;
	}


	@Override
	public String toString() {
		return "LocationData{" +
				"latitude='" + latitude + '\'' +
				", longitude='" + longitude + '\'' +
				", locCity='" + locCity + '\'' +
				", locDistrict='" + locDistrict + '\'' +
				", locStreet='" + locStreet + '\'' +
				'}';
	}
}
