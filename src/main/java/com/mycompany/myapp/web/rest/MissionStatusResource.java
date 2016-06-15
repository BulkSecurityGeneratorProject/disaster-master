package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.MissionStatus;
import com.mycompany.myapp.repository.MissionStatusRepository;
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
 * REST controller for managing MissionStatus.
 */
@RestController
@RequestMapping("/api")
public class MissionStatusResource {

    private final Logger log = LoggerFactory.getLogger(MissionStatusResource.class);
        
    @Inject
    private MissionStatusRepository missionStatusRepository;
    
    /**
     * POST  /mission-statuses : Create a new missionStatus.
     *
     * @param missionStatus the missionStatus to create
     * @return the ResponseEntity with status 201 (Created) and with body the new missionStatus, or with status 400 (Bad Request) if the missionStatus has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/mission-statuses",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MissionStatus> createMissionStatus(@RequestBody MissionStatus missionStatus) throws URISyntaxException {
        log.debug("REST request to save MissionStatus : {}", missionStatus);
        if (missionStatus.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("missionStatus", "idexists", "A new missionStatus cannot already have an ID")).body(null);
        }
        MissionStatus result = missionStatusRepository.save(missionStatus);
        return ResponseEntity.created(new URI("/api/mission-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("missionStatus", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /mission-statuses : Updates an existing missionStatus.
     *
     * @param missionStatus the missionStatus to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated missionStatus,
     * or with status 400 (Bad Request) if the missionStatus is not valid,
     * or with status 500 (Internal Server Error) if the missionStatus couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/mission-statuses",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MissionStatus> updateMissionStatus(@RequestBody MissionStatus missionStatus) throws URISyntaxException {
        log.debug("REST request to update MissionStatus : {}", missionStatus);
        if (missionStatus.getId() == null) {
            return createMissionStatus(missionStatus);
        }
        MissionStatus result = missionStatusRepository.save(missionStatus);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("missionStatus", missionStatus.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mission-statuses : get all the missionStatuses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of missionStatuses in body
     */
    @RequestMapping(value = "/mission-statuses",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<MissionStatus> getAllMissionStatuses() {
        log.debug("REST request to get all MissionStatuses");
        List<MissionStatus> missionStatuses = missionStatusRepository.findAll();
        return missionStatuses;
    }

    /**
     * GET  /mission-statuses/:id : get the "id" missionStatus.
     *
     * @param id the id of the missionStatus to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the missionStatus, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/mission-statuses/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MissionStatus> getMissionStatus(@PathVariable Long id) {
        log.debug("REST request to get MissionStatus : {}", id);
        MissionStatus missionStatus = missionStatusRepository.findOne(id);
        return Optional.ofNullable(missionStatus)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /mission-statuses/:id : delete the "id" missionStatus.
     *
     * @param id the id of the missionStatus to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/mission-statuses/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMissionStatus(@PathVariable Long id) {
        log.debug("REST request to delete MissionStatus : {}", id);
        missionStatusRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("missionStatus", id.toString())).build();
    }

}
