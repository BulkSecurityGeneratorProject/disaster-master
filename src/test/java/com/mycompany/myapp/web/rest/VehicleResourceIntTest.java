package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterApp;
import com.mycompany.myapp.domain.Vehicle;
import com.mycompany.myapp.repository.VehicleRepository;

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
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the VehicleResource REST controller.
 *
 * @see VehicleResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JhipsterApp.class)
@WebAppConfiguration
@IntegrationTest
public class VehicleResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_BRAND = "AAAAA";
    private static final String UPDATED_BRAND = "BBBBB";
    private static final String DEFAULT_LICENSE_PLATE = "AAAAA";
    private static final String UPDATED_LICENSE_PLATE = "BBBBB";
    private static final String DEFAULT_VEHICLE_TYPE = "AAAAA";
    private static final String UPDATED_VEHICLE_TYPE = "BBBBB";

    private static final LocalDate DEFAULT_DATE_CREATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATED = LocalDate.now(ZoneId.systemDefault());

    private static final ZonedDateTime DEFAULT_DATE_LAST_ACCESSED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE_LAST_ACCESSED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_LAST_ACCESSED_STR = dateTimeFormatter.format(DEFAULT_DATE_LAST_ACCESSED);

    @Inject
    private VehicleRepository vehicleRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restVehicleMockMvc;

    private Vehicle vehicle;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        VehicleResource vehicleResource = new VehicleResource();
        ReflectionTestUtils.setField(vehicleResource, "vehicleRepository", vehicleRepository);
        this.restVehicleMockMvc = MockMvcBuilders.standaloneSetup(vehicleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        vehicle = new Vehicle();
        vehicle.setBrand(DEFAULT_BRAND);
        vehicle.setLicensePlate(DEFAULT_LICENSE_PLATE);
        vehicle.setVehicleType(DEFAULT_VEHICLE_TYPE);
        vehicle.setDateCreated(DEFAULT_DATE_CREATED);
        vehicle.setDateLastAccessed(DEFAULT_DATE_LAST_ACCESSED);
    }

    @Test
    @Transactional
    public void createVehicle() throws Exception {
        int databaseSizeBeforeCreate = vehicleRepository.findAll().size();

        // Create the Vehicle

        restVehicleMockMvc.perform(post("/api/vehicles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(vehicle)))
                .andExpect(status().isCreated());

        // Validate the Vehicle in the database
        List<Vehicle> vehicles = vehicleRepository.findAll();
        assertThat(vehicles).hasSize(databaseSizeBeforeCreate + 1);
        Vehicle testVehicle = vehicles.get(vehicles.size() - 1);
        assertThat(testVehicle.getBrand()).isEqualTo(DEFAULT_BRAND);
        assertThat(testVehicle.getLicensePlate()).isEqualTo(DEFAULT_LICENSE_PLATE);
        assertThat(testVehicle.getVehicleType()).isEqualTo(DEFAULT_VEHICLE_TYPE);
        assertThat(testVehicle.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testVehicle.getDateLastAccessed()).isEqualTo(DEFAULT_DATE_LAST_ACCESSED);
    }

    @Test
    @Transactional
    public void getAllVehicles() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicles
        restVehicleMockMvc.perform(get("/api/vehicles?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(vehicle.getId().intValue())))
                .andExpect(jsonPath("$.[*].brand").value(hasItem(DEFAULT_BRAND.toString())))
                .andExpect(jsonPath("$.[*].licensePlate").value(hasItem(DEFAULT_LICENSE_PLATE.toString())))
                .andExpect(jsonPath("$.[*].vehicleType").value(hasItem(DEFAULT_VEHICLE_TYPE.toString())))
                .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
                .andExpect(jsonPath("$.[*].dateLastAccessed").value(hasItem(DEFAULT_DATE_LAST_ACCESSED_STR)));
    }

    @Test
    @Transactional
    public void getVehicle() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);

        // Get the vehicle
        restVehicleMockMvc.perform(get("/api/vehicles/{id}", vehicle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(vehicle.getId().intValue()))
            .andExpect(jsonPath("$.brand").value(DEFAULT_BRAND.toString()))
            .andExpect(jsonPath("$.licensePlate").value(DEFAULT_LICENSE_PLATE.toString()))
            .andExpect(jsonPath("$.vehicleType").value(DEFAULT_VEHICLE_TYPE.toString()))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.dateLastAccessed").value(DEFAULT_DATE_LAST_ACCESSED_STR));
    }

    @Test
    @Transactional
    public void getNonExistingVehicle() throws Exception {
        // Get the vehicle
        restVehicleMockMvc.perform(get("/api/vehicles/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVehicle() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);
        int databaseSizeBeforeUpdate = vehicleRepository.findAll().size();

        // Update the vehicle
        Vehicle updatedVehicle = new Vehicle();
        updatedVehicle.setId(vehicle.getId());
        updatedVehicle.setBrand(UPDATED_BRAND);
        updatedVehicle.setLicensePlate(UPDATED_LICENSE_PLATE);
        updatedVehicle.setVehicleType(UPDATED_VEHICLE_TYPE);
        updatedVehicle.setDateCreated(UPDATED_DATE_CREATED);
        updatedVehicle.setDateLastAccessed(UPDATED_DATE_LAST_ACCESSED);

        restVehicleMockMvc.perform(put("/api/vehicles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedVehicle)))
                .andExpect(status().isOk());

        // Validate the Vehicle in the database
        List<Vehicle> vehicles = vehicleRepository.findAll();
        assertThat(vehicles).hasSize(databaseSizeBeforeUpdate);
        Vehicle testVehicle = vehicles.get(vehicles.size() - 1);
        assertThat(testVehicle.getBrand()).isEqualTo(UPDATED_BRAND);
        assertThat(testVehicle.getLicensePlate()).isEqualTo(UPDATED_LICENSE_PLATE);
        assertThat(testVehicle.getVehicleType()).isEqualTo(UPDATED_VEHICLE_TYPE);
        assertThat(testVehicle.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testVehicle.getDateLastAccessed()).isEqualTo(UPDATED_DATE_LAST_ACCESSED);
    }

    @Test
    @Transactional
    public void deleteVehicle() throws Exception {
        // Initialize the database
        vehicleRepository.saveAndFlush(vehicle);
        int databaseSizeBeforeDelete = vehicleRepository.findAll().size();

        // Get the vehicle
        restVehicleMockMvc.perform(delete("/api/vehicles/{id}", vehicle.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Vehicle> vehicles = vehicleRepository.findAll();
        assertThat(vehicles).hasSize(databaseSizeBeforeDelete - 1);
    }
}
