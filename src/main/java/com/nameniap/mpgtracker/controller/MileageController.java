package com.nameniap.mpgtracker.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nameniap.mpgtracker.model.MPG;
import com.nameniap.mpgtracker.model.Mileage;
import com.nameniap.mpgtracker.model.Vehicle;
import com.nameniap.mpgtracker.repository.MileageRepository;
import com.nameniap.mpgtracker.repository.VehicleRepository;
import com.nameniap.mpgtracker.service.MileageService;

@RestController
public class MileageController {

	@Autowired
	private MileageRepository mileages;
	
	@Autowired
	private VehicleRepository vehicles;
	
	@Autowired
	private MileageService mileageService;

	@GetMapping("/api/mileages")
	List<Mileage> getAllMileages() {
		return this.mileages.getAllMileages();
	}

	@GetMapping("/api/mileages/{mileageId}")
	Mileage getMileage(@PathVariable int mileageId) {
		return this.mileages.findById(mileageId);
	}

	@GetMapping("/api/mileages/vehicle/{vehicleId}")
	List<MPG> getMileagesByVehicle(@PathVariable int vehicleId) {
		List<Mileage> mileages = this.mileages.findByVehicleId(vehicleId);
		List<MPG> mpgs = new ArrayList<MPG>();

		Vehicle vehicle = this.vehicles.findById(vehicleId);
		
		if (mileages.size() > 0) {
			double mpg = 0.0;
			int milesDriven = 0;
			int lastMileage = 0;

			for (Mileage aMileage : mileages) {
				MPG aMPG = mileageService.convertMileageToMPG(aMileage);

				if (aMileage.getGallons() > 0) {
					mpg = (aMileage.getMileage() - lastMileage) / aMileage.getGallons();
					milesDriven = aMileage.getMileage() - lastMileage;
				} else {
					mpg = 0.00;
					milesDriven = 0;
				}

				aMPG.setMpg(mpg);
				aMPG.setMiles(milesDriven);
				aMPG.setName(vehicle.getName());

				if (milesDriven > 0 && aMileage.getTotalCost() > 0 && aMileage.getGallons() > 0) {
					aMPG.setCostPerMile(aMPG.getTotalCost() / aMPG.getMiles());
					aMPG.setCostPerGallon(aMileage.getTotalCost() / aMileage.getGallons());
				} else {
					aMPG.setCostPerGallon(0.00);
					aMPG.setCostPerMile(0.00);
				}

				lastMileage = aMileage.getMileage();

				mpgs.add(aMPG);
			}

			Collections.reverse(mpgs);

		}

		return mpgs;
	}

	@PutMapping("/api/mileages/vehicle/{vehicleId}")
	MPG saveMileage(@PathVariable int vehicleId, @RequestBody MPG mpgToSave) {
		Mileage mileage = mileageService.convertMPGToMileage(mpgToSave);
		mileages.save(mileage);
		return mileageService.convertMileageToMPG(mileage);
	}
	
}
