package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Vehicle;
import com.mycompany.myapp.repository.VehicleRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Vehicle.
 */
@RestController
@RequestMapping("/api")
public class VehicleResource {

    private final Logger log = LoggerFactory.getLogger(VehicleResource.class);
        
    @Inject
    private VehicleRepository vehicleRepository;
    
    /**
     * POST  /vehicles : Create a new vehicle.
     *
     * @param vehicle the vehicle to create
     * @return the ResponseEntity with status 201 (Created) and with body the new vehicle, or with status 400 (Bad Request) if the vehicle has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/vehicles",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Vehicle> createVehicle(@RequestBody Vehicle vehicle) throws URISyntaxException {
        log.debug("REST request to save Vehicle : {}", vehicle);
        if (vehicle.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("vehicle", "idexists", "A new vehicle cannot already have an ID")).body(null);
        }
        Vehicle result = vehicleRepository.save(vehicle);
        return ResponseEntity.created(new URI("/api/vehicles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("vehicle", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /vehicles : Updates an existing vehicle.
     *
     * @param vehicle the vehicle to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated vehicle,
     * or with status 400 (Bad Request) if the vehicle is not valid,
     * or with status 500 (Internal Server Error) if the vehicle couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/vehicles",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Vehicle> updateVehicle(@RequestBody Vehicle vehicle) throws URISyntaxException {
        log.debug("REST request to update Vehicle : {}", vehicle);
        if (vehicle.getId() == null) {
            return createVehicle(vehicle);
        }
        Vehicle result = vehicleRepository.save(vehicle);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("vehicle", vehicle.getId().toString()))
            .body(result);
    }

    /**
     * GET  /vehicles : get all the vehicles.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of vehicles in body
     */
    @RequestMapping(value = "/vehicles",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Vehicle> getAllVehicles() {
        log.debug("REST request to get all Vehicles");
        List<Vehicle> vehicles = vehicleRepository.findAll();
        return vehicles;
    }

    /**
     * GET  /vehicles/:id : get the "id" vehicle.
     *
     * @param id the id of the vehicle to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the vehicle, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/vehicles/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Vehicle> getVehicle(@PathVariable Long id) {
        log.debug("REST request to get Vehicle : {}", id);
        Vehicle vehicle = vehicleRepository.findOne(id);
        return Optional.ofNullable(vehicle)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /vehicles/:id : delete the "id" vehicle.
     *
     * @param id the id of the vehicle to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/vehicles/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        log.debug("REST request to delete Vehicle : {}", id);
        vehicleRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("vehicle", id.toString())).build();
    }

}
