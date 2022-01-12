package com.nameniap.mpgtracker.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
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
    	return this.vehicles.findAll(Sort.by(new Order(Direction.ASC, "sortkey")));
	}
    
    @GetMapping("/api/vehicles/{vehicleId}")
	Optional<Vehicle> getUser(@PathVariable int vehicleId) {
    	return this.vehicles.findById(vehicleId);
	}
	
	@GetMapping("/api/vehicles/findVehicle/{vehicleName}")
	Vehicle getVehicle(@PathVariable String vehicleName) {
		return this.vehicles.findByNameOrderBySortkey(vehicleName);
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
