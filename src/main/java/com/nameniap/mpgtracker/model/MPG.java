package com.nameniap.mpgtracker.model;

public class MPG {

	/**
	 * 
	 */
	private static final long serialVersionUID = 750700760297878183L;
	
	private int id;

	private int mileage;

	private double gallons;
	
	private double totalCost;
	
	private String timestamp;
	
	private String name;
	
	private double costPerGallon;
	
	private int vid;
	
	private double costPerMile;
	
	private double mpg;
	
	private int miles;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public int getMileage() {
		return mileage;
	}

	public void setMileage(int mileage) {
		this.mileage = mileage;
	}

	public double getGallons() {
		return gallons;
	}

	public void setGallons(double gallons) {
		this.gallons = gallons;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}

	public double getCostPerGallon() {
		return costPerGallon;
	}

	public void setCostPerGallon(double costPerGallon) {
		this.costPerGallon = costPerGallon;
	}

	public int getVid() {
		return vid;
	}

	public void setVid(int vid) {
		this.vid = vid;
	}

	public double getCostPerMile() {
		return costPerMile;
	}

	public void setCostPerMile(double costPerMile) {
		this.costPerMile = costPerMile;
	}

	public double getMpg() {
		return mpg;
	}

	public void setMpg(double mpg) {
		this.mpg = mpg;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getMiles() {
		return miles;
	}

	public void setMiles(int miles) {
		this.miles = miles;
	}
	
}
