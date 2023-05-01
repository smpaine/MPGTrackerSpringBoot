package com.nameniap.mpgtracker.model;

import java.io.Serializable;
import java.util.Objects;

public class YearlyStatsId implements Serializable {

	private static final long serialVersionUID = -4126714403537777902L;

	private int vid;
	
	private int year;
	
	public YearlyStatsId() {
		// Default no-args constructor
	};
	
	public YearlyStatsId(int vid, int year) {
		this.vid = vid;
		this.year = year;
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

	@Override
	public int hashCode() {
		return Objects.hash(vid, year);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		YearlyStatsId other = (YearlyStatsId) obj;
		return vid == other.vid && year == other.year;
	}
	
	@Override
	public String toString() {
		return "vid: " + vid + ", year: " + year;
	}
	
	
}
