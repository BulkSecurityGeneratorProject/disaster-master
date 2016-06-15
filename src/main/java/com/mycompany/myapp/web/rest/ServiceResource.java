package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Service;
import com.mycompany.myapp.repository.ServiceRepository;
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
 * REST controller for managing Service.
 */
@RestController
@RequestMapping("/api")
public class ServiceResource {

    private final Logger log = LoggerFactory.getLogger(ServiceResource.class);
        
    @Inject
    private ServiceRepository serviceRepository;
    
    /**
     * POST  /services : Create a new service.
     *
     * @param service the service to create
     * @return the ResponseEntity with status 201 (Created) and with body the new service, or with status 400 (Bad Request) if the service has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/services",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Service> createService(@RequestBody Service service) throws URISyntaxException {
        log.debug("REST request to save Service : {}", service);
        if (service.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("service", "idexists", "A new service cannot already have an ID")).body(null);
        }
        Service result = serviceRepository.save(service);
        return ResponseEntity.created(new URI("/api/services/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("service", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /services : Updates an existing service.
     *
     * @param service the service to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated service,
     * or with status 400 (Bad Request) if the service is not valid,
     * or with status 500 (Internal Server Error) if the service couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/services",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Service> updateService(@RequestBody Service service) throws URISyntaxException {
        log.debug("REST request to update Service : {}", service);
        if (service.getId() == null) {
            return createService(service);
        }
        Service result = serviceRepository.save(service);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("service", service.getId().toString()))
            .body(result);
    }

    /**
     * GET  /services : get all the services.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of services in body
     */
    @RequestMapping(value = "/services",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Service> getAllServices() {
        log.debug("REST request to get all Services");
        List<Service> services = serviceRepository.findAll();
        return services;
    }

    /**
     * GET  /services/:id : get the "id" service.
     *
     * @param id the id of the service to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the service, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/services/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Service> getService(@PathVariable Long id) {
        log.debug("REST request to get Service : {}", id);
        Service service = serviceRepository.findOne(id);
        return Optional.ofNullable(service)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /services/:id : delete the "id" service.
     *
     * @param id the id of the service to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/services/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        log.debug("REST request to delete Service : {}", id);
        serviceRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("service", id.toString())).build();
    }

}
