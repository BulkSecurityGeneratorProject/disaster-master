package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterApp;
import com.mycompany.myapp.domain.Fleet;
import com.mycompany.myapp.repository.FleetRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the FleetResource REST controller.
 *
 * @see FleetResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JhipsterApp.class)
@WebAppConfiguration
@IntegrationTest
public class FleetResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private FleetRepository fleetRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFleetMockMvc;

    private Fleet fleet;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FleetResource fleetResource = new FleetResource();
        ReflectionTestUtils.setField(fleetResource, "fleetRepository", fleetRepository);
        this.restFleetMockMvc = MockMvcBuilders.standaloneSetup(fleetResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        fleet = new Fleet();
        fleet.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createFleet() throws Exception {
        int databaseSizeBeforeCreate = fleetRepository.findAll().size();

        // Create the Fleet

        restFleetMockMvc.perform(post("/api/fleets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fleet)))
                .andExpect(status().isCreated());

        // Validate the Fleet in the database
        List<Fleet> fleets = fleetRepository.findAll();
        assertThat(fleets).hasSize(databaseSizeBeforeCreate + 1);
        Fleet testFleet = fleets.get(fleets.size() - 1);
        assertThat(testFleet.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllFleets() throws Exception {
        // Initialize the database
        fleetRepository.saveAndFlush(fleet);

        // Get all the fleets
        restFleetMockMvc.perform(get("/api/fleets?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(fleet.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getFleet() throws Exception {
        // Initialize the database
        fleetRepository.saveAndFlush(fleet);

        // Get the fleet
        restFleetMockMvc.perform(get("/api/fleets/{id}", fleet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(fleet.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFleet() throws Exception {
        // Get the fleet
        restFleetMockMvc.perform(get("/api/fleets/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFleet() throws Exception {
        // Initialize the database
        fleetRepository.saveAndFlush(fleet);
        int databaseSizeBeforeUpdate = fleetRepository.findAll().size();

        // Update the fleet
        Fleet updatedFleet = new Fleet();
        updatedFleet.setId(fleet.getId());
        updatedFleet.setName(UPDATED_NAME);

        restFleetMockMvc.perform(put("/api/fleets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedFleet)))
                .andExpect(status().isOk());

        // Validate the Fleet in the database
        List<Fleet> fleets = fleetRepository.findAll();
        assertThat(fleets).hasSize(databaseSizeBeforeUpdate);
        Fleet testFleet = fleets.get(fleets.size() - 1);
        assertThat(testFleet.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteFleet() throws Exception {
        // Initialize the database
        fleetRepository.saveAndFlush(fleet);
        int databaseSizeBeforeDelete = fleetRepository.findAll().size();

        // Get the fleet
        restFleetMockMvc.perform(delete("/api/fleets/{id}", fleet.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Fleet> fleets = fleetRepository.findAll();
        assertThat(fleets).hasSize(databaseSizeBeforeDelete - 1);
    }
}
