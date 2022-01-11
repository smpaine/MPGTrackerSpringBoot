package com.nameniap.mpgtracker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nameniap.mpgtracker.model.Vehicle;
import com.nameniap.mpgtracker.repository.VehicleRepository;

@RestController
public class VehicleController {

	@Autowired
    private VehicleRepository vehicles;
    
    @GetMapping("/api/vehicles")
	List<Vehicle> getAllVehicles() {
    	return this.vehicles.getAllVehicles();
	}
    
    @GetMapping("/api/vehicles/{vehicleId}")
	Vehicle getUser(@PathVariable int vehicleId) {
    	return this.vehicles.findById(vehicleId);
	}
	
	@GetMapping("/api/vehicles/findVehicle/{vehicleName}")
	Vehicle getVehicle(@PathVariable String vehicleName) {
		return this.vehicles.findVehicle(vehicleName);
	}
	
	@PutMapping("/api/vehicles")
	Vehicle addVehicle(@RequestBody Vehicle newVehicle) {
		this.vehicles.save(newVehicle);
		return newVehicle;
	}
	
	@PostMapping("/api/vehicles")
	Vehicle updateVehicle(@RequestBody Vehicle vehicleToUpdate) {
		this.vehicles.save(vehicleToUpdate);
		return vehicleToUpdate;
	}

}
