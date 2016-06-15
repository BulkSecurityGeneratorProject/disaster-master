package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Mission;
import com.mycompany.myapp.repository.MissionRepository;
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
 * REST controller for managing Mission.
 */
@RestController
@RequestMapping("/api")
public class MissionResource {

    private final Logger log = LoggerFactory.getLogger(MissionResource.class);
        
    @Inject
    private MissionRepository missionRepository;
    
    /**
     * POST  /missions : Create a new mission.
     *
     * @param mission the mission to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mission, or with status 400 (Bad Request) if the mission has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/missions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Mission> createMission(@RequestBody Mission mission) throws URISyntaxException {
        log.debug("REST request to save Mission : {}", mission);
        if (mission.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("mission", "idexists", "A new mission cannot already have an ID")).body(null);
        }
        Mission result = missionRepository.save(mission);
        return ResponseEntity.created(new URI("/api/missions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("mission", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /missions : Updates an existing mission.
     *
     * @param mission the mission to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mission,
     * or with status 400 (Bad Request) if the mission is not valid,
     * or with status 500 (Internal Server Error) if the mission couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/missions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Mission> updateMission(@RequestBody Mission mission) throws URISyntaxException {
        log.debug("REST request to update Mission : {}", mission);
        if (mission.getId() == null) {
            return createMission(mission);
        }
        Mission result = missionRepository.save(mission);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("mission", mission.getId().toString()))
            .body(result);
    }

    /**
     * GET  /missions : get all the missions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of missions in body
     */
    @RequestMapping(value = "/missions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Mission> getAllMissions() {
        log.debug("REST request to get all Missions");
        List<Mission> missions = missionRepository.findAll();
        return missions;
    }

    /**
     * GET  /missions/:id : get the "id" mission.
     *
     * @param id the id of the mission to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mission, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/missions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Mission> getMission(@PathVariable Long id) {
        log.debug("REST request to get Mission : {}", id);
        Mission mission = missionRepository.findOne(id);
        return Optional.ofNullable(mission)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /missions/:id : delete the "id" mission.
     *
     * @param id the id of the mission to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/missions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMission(@PathVariable Long id) {
        log.debug("REST request to delete Mission : {}", id);
        missionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("mission", id.toString())).build();
    }

}
