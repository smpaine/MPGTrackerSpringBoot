package com.nameniap.mpgtracker.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.annotation.Immutable;

@Entity
@Table(name = "yearlystats")
@Immutable
public class YearlyStats {
	
	@EmbeddedId
	private YearlyStatsId yearlyStatsId;
	
	@Column(name = "vid", insertable = false, updatable = false)
	private int vid;
	
	@Column(name = "year", insertable = false, updatable = false)
	private int year;

	@Column(name = "gallons")
	private double gallons;
	
	@Column(name = "totalcost")
	private double totalCost;
	
	@Column(name = "costpergallon")
	private double costPerGallon;
	
	@Column(name = "costpermile")
	private double costPerMile;
	
	@Column(name = "mpg")
	private double mpg;
	
	@Column(name = "miles")
	private int miles;
	
	@Column(name = "g100m")
	private double g100m;
	
	/*
	public YearlyStatsId getYearlyStatsId() {
		return yearlyStatsId;
	}
	
	public void setYearlyStatsId(YearlyStatsId yearlyStatsId) {
		this.yearlyStatsId = yearlyStatsId;
	}
	*/
	
	public YearlyStatsId getId() {
		//return new YearlyStatsId(vid, year);
		return yearlyStatsId;
	}
	
	public void setId(YearlyStatsId yearlyStatsId) {
		//this.vid = id.getVid();
		//this.year = id.getYear();
		this.yearlyStatsId = yearlyStatsId;
	}
	
	public int getVid() {
		return vid;
	}
	
	public void setVid(int vid) {
		this.vid = vid;
	}
	
	public int getYear() {
		return year;
	}
	
	public void setYear(int year) {
		this.year = year;
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

	public double getCostPerGallon() {
		return costPerGallon;
	}

	public void setCostPerGallon(double costPerGallon) {
		this.costPerGallon = costPerGallon;
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

	public int getMiles() {
		return miles;
	}

	public void setMiles(int miles) {
		this.miles = miles;
	}
	
	public double getG100m() {
		return g100m;
	}
	
	public void setG100m(double g100m) {
		this.g100m = g100m;
	}
	
}
