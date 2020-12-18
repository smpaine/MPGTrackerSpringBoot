package com.nameniap.mpgtracker.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "vehicles")
public class Vehicle extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3796468217380910840L;

	@Column(name = "name")
    @NotEmpty
	private String name;
	
	@Column(name = "make")
    @NotEmpty
	private String make;
	
	@Column(name = "model")
    @NotEmpty
	private String model;
	
	@Column(name = "year")
    @NotNull
	private int year;
	
	@Column(name = "purchased")
    @NotNull
	private Date purchased;
	
	@Column(name = "sortkey")
	private int sortkey;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public Date getPurchased() {
		return purchased;
	}

	public void setPurchased(Date purchased) {
		this.purchased = purchased;
	}

	public int getSortkey() {
		return sortkey;
	}

	public void setSortkey(int sortkey) {
		this.sortkey = sortkey;
	}
	
}
