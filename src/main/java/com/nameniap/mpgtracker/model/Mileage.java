package com.nameniap.mpgtracker.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "mileages")
public class Mileage extends BaseEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4784357786051538166L;

	@Column(name = "mileage")
    @NotNull
	private int mileage;
	
	@Column(name = "gallons")
    @NotNull
	private double gallons;

	@Column(name = "totalcost")
    @NotNull
	private double totalCost;
	
	@Column(name = "timestamp")
	private Date timestamp;
	
	@Column(name = "vid")
	private int vid;

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

	public double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public int getVid() {
		return vid;
	}

	public void setVid(int vid) {
		this.vid = vid;
	}
	
}
