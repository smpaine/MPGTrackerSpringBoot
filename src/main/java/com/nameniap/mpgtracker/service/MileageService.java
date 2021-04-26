package com.nameniap.mpgtracker.service;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.nameniap.mpgtracker.model.MPG;
import com.nameniap.mpgtracker.model.Mileage;

@Service
public class MileageService {
	
	public MileageService() {
	}

	public MPG convertMileageToMPG(Mileage mileage) {
		MPG mpg = new MPG();
		mpg.setId(mileage.getId());
		mpg.setMileage(mileage.getMileage());
		mpg.setGallons(mileage.getGallons());
		mpg.setTotalCost(mileage.getTotalCost());
		mpg.setTimestamp(mileage.getTimestamp().getTime());
		mpg.setVid(mileage.getVid());
		return mpg;
	}
	
	public Mileage convertMPGToMileage(MPG mpg)  {
		Calendar cal = Calendar.getInstance();
		Mileage mileage = new Mileage();
		mileage.setId(mpg.getId());
		mileage.setMileage(mpg.getMileage());
		mileage.setGallons(mpg.getGallons());
		mileage.setTotalCost(mpg.getTotalCost());
		Date timeStamp = null;
		if (mpg.getTimestamp() > 0) {
			timeStamp = new Date(mpg.getTimestamp());
		} else {
			timeStamp = cal.getTime();
		}
		mileage.setTimestamp(timeStamp);
		mileage.setVid(mpg.getVid());
		return mileage;
	}

}
