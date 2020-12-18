package com.nameniap.mpgtracker.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.nameniap.mpgtracker.model.MPG;
import com.nameniap.mpgtracker.model.Mileage;

@Service
public class MileageService {
	
	private final SimpleDateFormat sdf;
	
	private final Calendar cal;
	
	public MileageService() {
		this.sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.cal = Calendar.getInstance();
	}

	public MPG convertMileageToMPG(Mileage mileage) {
		MPG mpg = new MPG();
		mpg.setId(mileage.getId());
		mpg.setMileage(mileage.getMileage());
		mpg.setGallons(mileage.getGallons());
		mpg.setTotalCost(mileage.getTotalCost());
		mpg.setTimestamp(sdf.format(mileage.getTimestamp()));
		mpg.setVid(mileage.getVid());
		return mpg;
	}
	
	public Mileage convertMPGToMileage(MPG mpg)  {
		Mileage mileage = new Mileage();
		mileage.setId(mpg.getId());
		mileage.setMileage(mpg.getMileage());
		mileage.setGallons(mpg.getGallons());
		mileage.setTotalCost(mpg.getTotalCost());
		Date timeStamp = null;
		try {
			if (mpg.getTimestamp() != null && mpg.getTimestamp().trim().length() > 0) {
				timeStamp = sdf.parse(mpg.getTimestamp());
			} else {
				timeStamp = cal.getTime();
			}
		} catch (ParseException pe) {
			timeStamp = cal.getTime();
		}
		mileage.setTimestamp(timeStamp);
		mileage.setVid(mpg.getVid());
		return mileage;
	}

}
