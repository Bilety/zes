package com.example.seeker;

public class Place {

	private String name;
	private String street;
	private double latitude;
	private double longitude;

	public Place(String name, String street, double latitude, double longitude) {
		super();
		this.name = name;
		this.street = street;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		return "Place [name=" + name + ", street=" + street + ", latitude="
				+ latitude + ", longitude=" + longitude + "]";
	}

}
