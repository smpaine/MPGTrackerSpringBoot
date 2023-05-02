package com.nameniap.mpgtracker.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nameniap.mpgtracker.model.MPG;
import com.nameniap.mpgtracker.model.Mileage;
import com.nameniap.mpgtracker.model.Vehicle;
import com.nameniap.mpgtracker.model.YearlyStats;
import com.nameniap.mpgtracker.model.YearlyStatsId;
import com.nameniap.mpgtracker.repository.MileageRepository;
import com.nameniap.mpgtracker.repository.VehicleRepository;
import com.nameniap.mpgtracker.repository.YearlyStatsRepository;
import com.nameniap.mpgtracker.service.MileageService;

@RestController
public class MileageController {
	
	Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private MileageRepository mileages;
	
	@Autowired
	private VehicleRepository vehicles;
	
	@Autowired
	private MileageService mileageService;
	
	@Autowired
	private YearlyStatsRepository yearlyStats;

	@GetMapping("/api/mileages")
	List<Mileage> getAllMileages() {
		return this.mileages.findAll(Sort.by(new Order(Direction.ASC, "timestamp")));
	}

	@GetMapping("/api/mileages/{mileageId}")
	Optional<Mileage> getMileage(@PathVariable int mileageId) {
		return this.mileages.findById(mileageId);
	}

	@GetMapping("/api/mileages/vehicle/{vehicleId}")
	List<MPG> getMileagesByVehicle(@PathVariable int vehicleId) {
		List<Mileage> mileages = this.mileages.findByVidOrderByTimestamp(vehicleId);
		List<MPG> mpgs = new ArrayList<MPG>();

		Vehicle vehicle = this.vehicles.findById(vehicleId).get();
		
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
	
	@GetMapping("/api/mileages/vehicle/stats")
	List<YearlyStats> getAllStats() {
		List<YearlyStats> stats = yearlyStats.findAll();
		
		/*
		for (YearlyStats aStat : stats) {
			//logger.info("stat.id: " + aStat.getYearlyStatsId().toString());
			logger.info("stat.id: " + aStat.getId().toString());
		}
		*/
		
		return stats;
	}
	
	@GetMapping("/api/mileages/vehicle/stats/{vehicleId}")
	List<YearlyStats> getStatsByVehicle(@PathVariable int vehicleId) {
		List<YearlyStats> stats = yearlyStats.findByVidOrderByYearDesc(vehicleId);

		/*
		for (YearlyStats aStat : stats) {
			logger.info("stat.id: " + aStat.getId().toString());
		}
		*/
		
		return stats;
	}
	
	@GetMapping("/api/mileages/vehicle/stats/{vehicleId}/{year}")
	Optional<YearlyStats> getStatsByVehicleByYear(@PathVariable int vehicleId, @PathVariable int year) {
		YearlyStatsId id = new YearlyStatsId(vehicleId, year);
		logger.debug("id: " + id.toString());
		Optional<YearlyStats> stats = yearlyStats.findByYearlyStatsId(id);
		
		return stats;
	}
	
}
