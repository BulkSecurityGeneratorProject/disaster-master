package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Unit;
import com.mycompany.myapp.repository.UnitRepository;
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
 * REST controller for managing Unit.
 */
@RestController
@RequestMapping("/api")
public class UnitResource {

    private final Logger log = LoggerFactory.getLogger(UnitResource.class);
        
    @Inject
    private UnitRepository unitRepository;
    
    /**
     * POST  /units : Create a new unit.
     *
     * @param unit the unit to create
     * @return the ResponseEntity with status 201 (Created) and with body the new unit, or with status 400 (Bad Request) if the unit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/units",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Unit> createUnit(@RequestBody Unit unit) throws URISyntaxException {
        log.debug("REST request to save Unit : {}", unit);
        if (unit.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("unit", "idexists", "A new unit cannot already have an ID")).body(null);
        }
        Unit result = unitRepository.save(unit);
        return ResponseEntity.created(new URI("/api/units/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("unit", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /units : Updates an existing unit.
     *
     * @param unit the unit to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated unit,
     * or with status 400 (Bad Request) if the unit is not valid,
     * or with status 500 (Internal Server Error) if the unit couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/units",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Unit> updateUnit(@RequestBody Unit unit) throws URISyntaxException {
        log.debug("REST request to update Unit : {}", unit);
        if (unit.getId() == null) {
            return createUnit(unit);
        }
        Unit result = unitRepository.save(unit);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("unit", unit.getId().toString()))
            .body(result);
    }

    /**
     * GET  /units : get all the units.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of units in body
     */
    @RequestMapping(value = "/units",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Unit> getAllUnits() {
        log.debug("REST request to get all Units");
        List<Unit> units = unitRepository.findAll();
        return units;
    }

    /**
     * GET  /units/:id : get the "id" unit.
     *
     * @param id the id of the unit to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the unit, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/units/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Unit> getUnit(@PathVariable Long id) {
        log.debug("REST request to get Unit : {}", id);
        Unit unit = unitRepository.findOne(id);
        return Optional.ofNullable(unit)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /units/:id : delete the "id" unit.
     *
     * @param id the id of the unit to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/units/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteUnit(@PathVariable Long id) {
        log.debug("REST request to delete Unit : {}", id);
        unitRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("unit", id.toString())).build();
    }

}
