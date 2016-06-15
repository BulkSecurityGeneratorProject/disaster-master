package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Fleet;
import com.mycompany.myapp.repository.FleetRepository;
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
 * REST controller for managing Fleet.
 */
@RestController
@RequestMapping("/api")
public class FleetResource {

    private final Logger log = LoggerFactory.getLogger(FleetResource.class);
        
    @Inject
    private FleetRepository fleetRepository;
    
    /**
     * POST  /fleets : Create a new fleet.
     *
     * @param fleet the fleet to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fleet, or with status 400 (Bad Request) if the fleet has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/fleets",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Fleet> createFleet(@RequestBody Fleet fleet) throws URISyntaxException {
        log.debug("REST request to save Fleet : {}", fleet);
        if (fleet.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("fleet", "idexists", "A new fleet cannot already have an ID")).body(null);
        }
        Fleet result = fleetRepository.save(fleet);
        return ResponseEntity.created(new URI("/api/fleets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("fleet", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fleets : Updates an existing fleet.
     *
     * @param fleet the fleet to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fleet,
     * or with status 400 (Bad Request) if the fleet is not valid,
     * or with status 500 (Internal Server Error) if the fleet couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/fleets",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Fleet> updateFleet(@RequestBody Fleet fleet) throws URISyntaxException {
        log.debug("REST request to update Fleet : {}", fleet);
        if (fleet.getId() == null) {
            return createFleet(fleet);
        }
        Fleet result = fleetRepository.save(fleet);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("fleet", fleet.getId().toString()))
            .body(result);
    }

    /**
     * GET  /fleets : get all the fleets.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of fleets in body
     */
    @RequestMapping(value = "/fleets",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Fleet> getAllFleets() {
        log.debug("REST request to get all Fleets");
        List<Fleet> fleets = fleetRepository.findAll();
        return fleets;
    }

    /**
     * GET  /fleets/:id : get the "id" fleet.
     *
     * @param id the id of the fleet to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fleet, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/fleets/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Fleet> getFleet(@PathVariable Long id) {
        log.debug("REST request to get Fleet : {}", id);
        Fleet fleet = fleetRepository.findOne(id);
        return Optional.ofNullable(fleet)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /fleets/:id : delete the "id" fleet.
     *
     * @param id the id of the fleet to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/fleets/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFleet(@PathVariable Long id) {
        log.debug("REST request to delete Fleet : {}", id);
        fleetRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("fleet", id.toString())).build();
    }

}
