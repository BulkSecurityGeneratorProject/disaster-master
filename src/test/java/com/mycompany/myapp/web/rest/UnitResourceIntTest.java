package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterApp;
import com.mycompany.myapp.domain.Unit;
import com.mycompany.myapp.repository.UnitRepository;

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
 * Test class for the UnitResource REST controller.
 *
 * @see UnitResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JhipsterApp.class)
@WebAppConfiguration
@IntegrationTest
public class UnitResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private UnitRepository unitRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restUnitMockMvc;

    private Unit unit;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UnitResource unitResource = new UnitResource();
        ReflectionTestUtils.setField(unitResource, "unitRepository", unitRepository);
        this.restUnitMockMvc = MockMvcBuilders.standaloneSetup(unitResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        unit = new Unit();
        unit.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createUnit() throws Exception {
        int databaseSizeBeforeCreate = unitRepository.findAll().size();

        // Create the Unit

        restUnitMockMvc.perform(post("/api/units")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(unit)))
                .andExpect(status().isCreated());

        // Validate the Unit in the database
        List<Unit> units = unitRepository.findAll();
        assertThat(units).hasSize(databaseSizeBeforeCreate + 1);
        Unit testUnit = units.get(units.size() - 1);
        assertThat(testUnit.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllUnits() throws Exception {
        // Initialize the database
        unitRepository.saveAndFlush(unit);

        // Get all the units
        restUnitMockMvc.perform(get("/api/units?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(unit.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getUnit() throws Exception {
        // Initialize the database
        unitRepository.saveAndFlush(unit);

        // Get the unit
        restUnitMockMvc.perform(get("/api/units/{id}", unit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(unit.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUnit() throws Exception {
        // Get the unit
        restUnitMockMvc.perform(get("/api/units/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUnit() throws Exception {
        // Initialize the database
        unitRepository.saveAndFlush(unit);
        int databaseSizeBeforeUpdate = unitRepository.findAll().size();

        // Update the unit
        Unit updatedUnit = new Unit();
        updatedUnit.setId(unit.getId());
        updatedUnit.setName(UPDATED_NAME);

        restUnitMockMvc.perform(put("/api/units")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedUnit)))
                .andExpect(status().isOk());

        // Validate the Unit in the database
        List<Unit> units = unitRepository.findAll();
        assertThat(units).hasSize(databaseSizeBeforeUpdate);
        Unit testUnit = units.get(units.size() - 1);
        assertThat(testUnit.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteUnit() throws Exception {
        // Initialize the database
        unitRepository.saveAndFlush(unit);
        int databaseSizeBeforeDelete = unitRepository.findAll().size();

        // Get the unit
        restUnitMockMvc.perform(delete("/api/units/{id}", unit.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Unit> units = unitRepository.findAll();
        assertThat(units).hasSize(databaseSizeBeforeDelete - 1);
    }
}
