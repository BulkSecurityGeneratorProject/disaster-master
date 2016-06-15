package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterApp;
import com.mycompany.myapp.domain.MissionStatus;
import com.mycompany.myapp.repository.MissionStatusRepository;

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
 * Test class for the MissionStatusResource REST controller.
 *
 * @see MissionStatusResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JhipsterApp.class)
@WebAppConfiguration
@IntegrationTest
public class MissionStatusResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private MissionStatusRepository missionStatusRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMissionStatusMockMvc;

    private MissionStatus missionStatus;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MissionStatusResource missionStatusResource = new MissionStatusResource();
        ReflectionTestUtils.setField(missionStatusResource, "missionStatusRepository", missionStatusRepository);
        this.restMissionStatusMockMvc = MockMvcBuilders.standaloneSetup(missionStatusResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        missionStatus = new MissionStatus();
        missionStatus.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createMissionStatus() throws Exception {
        int databaseSizeBeforeCreate = missionStatusRepository.findAll().size();

        // Create the MissionStatus

        restMissionStatusMockMvc.perform(post("/api/mission-statuses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(missionStatus)))
                .andExpect(status().isCreated());

        // Validate the MissionStatus in the database
        List<MissionStatus> missionStatuses = missionStatusRepository.findAll();
        assertThat(missionStatuses).hasSize(databaseSizeBeforeCreate + 1);
        MissionStatus testMissionStatus = missionStatuses.get(missionStatuses.size() - 1);
        assertThat(testMissionStatus.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllMissionStatuses() throws Exception {
        // Initialize the database
        missionStatusRepository.saveAndFlush(missionStatus);

        // Get all the missionStatuses
        restMissionStatusMockMvc.perform(get("/api/mission-statuses?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(missionStatus.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getMissionStatus() throws Exception {
        // Initialize the database
        missionStatusRepository.saveAndFlush(missionStatus);

        // Get the missionStatus
        restMissionStatusMockMvc.perform(get("/api/mission-statuses/{id}", missionStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(missionStatus.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMissionStatus() throws Exception {
        // Get the missionStatus
        restMissionStatusMockMvc.perform(get("/api/mission-statuses/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMissionStatus() throws Exception {
        // Initialize the database
        missionStatusRepository.saveAndFlush(missionStatus);
        int databaseSizeBeforeUpdate = missionStatusRepository.findAll().size();

        // Update the missionStatus
        MissionStatus updatedMissionStatus = new MissionStatus();
        updatedMissionStatus.setId(missionStatus.getId());
        updatedMissionStatus.setName(UPDATED_NAME);

        restMissionStatusMockMvc.perform(put("/api/mission-statuses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMissionStatus)))
                .andExpect(status().isOk());

        // Validate the MissionStatus in the database
        List<MissionStatus> missionStatuses = missionStatusRepository.findAll();
        assertThat(missionStatuses).hasSize(databaseSizeBeforeUpdate);
        MissionStatus testMissionStatus = missionStatuses.get(missionStatuses.size() - 1);
        assertThat(testMissionStatus.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteMissionStatus() throws Exception {
        // Initialize the database
        missionStatusRepository.saveAndFlush(missionStatus);
        int databaseSizeBeforeDelete = missionStatusRepository.findAll().size();

        // Get the missionStatus
        restMissionStatusMockMvc.perform(delete("/api/mission-statuses/{id}", missionStatus.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<MissionStatus> missionStatuses = missionStatusRepository.findAll();
        assertThat(missionStatuses).hasSize(databaseSizeBeforeDelete - 1);
    }
}
