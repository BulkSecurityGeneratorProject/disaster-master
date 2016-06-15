package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterApp;
import com.mycompany.myapp.domain.Mission;
import com.mycompany.myapp.repository.MissionRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the MissionResource REST controller.
 *
 * @see MissionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JhipsterApp.class)
@WebAppConfiguration
@IntegrationTest
public class MissionResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final Boolean DEFAULT_IS_ACCEPTED = false;
    private static final Boolean UPDATED_IS_ACCEPTED = true;
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_NOTES = "AAAAA";
    private static final String UPDATED_NOTES = "BBBBB";

    private static final ZonedDateTime DEFAULT_DATE_STARTED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE_STARTED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_STARTED_STR = dateTimeFormatter.format(DEFAULT_DATE_STARTED);

    private static final ZonedDateTime DEFAULT_DATE_COMPLETED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE_COMPLETED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_COMPLETED_STR = dateTimeFormatter.format(DEFAULT_DATE_COMPLETED);

    @Inject
    private MissionRepository missionRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMissionMockMvc;

    private Mission mission;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MissionResource missionResource = new MissionResource();
        ReflectionTestUtils.setField(missionResource, "missionRepository", missionRepository);
        this.restMissionMockMvc = MockMvcBuilders.standaloneSetup(missionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        mission = new Mission();
        mission.setIsAccepted(DEFAULT_IS_ACCEPTED);
        mission.setName(DEFAULT_NAME);
        mission.setNotes(DEFAULT_NOTES);
        mission.setDateStarted(DEFAULT_DATE_STARTED);
        mission.setDateCompleted(DEFAULT_DATE_COMPLETED);
    }

    @Test
    @Transactional
    public void createMission() throws Exception {
        int databaseSizeBeforeCreate = missionRepository.findAll().size();

        // Create the Mission

        restMissionMockMvc.perform(post("/api/missions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(mission)))
                .andExpect(status().isCreated());

        // Validate the Mission in the database
        List<Mission> missions = missionRepository.findAll();
        assertThat(missions).hasSize(databaseSizeBeforeCreate + 1);
        Mission testMission = missions.get(missions.size() - 1);
        assertThat(testMission.isIsAccepted()).isEqualTo(DEFAULT_IS_ACCEPTED);
        assertThat(testMission.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMission.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testMission.getDateStarted()).isEqualTo(DEFAULT_DATE_STARTED);
        assertThat(testMission.getDateCompleted()).isEqualTo(DEFAULT_DATE_COMPLETED);
    }

    @Test
    @Transactional
    public void getAllMissions() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missions
        restMissionMockMvc.perform(get("/api/missions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(mission.getId().intValue())))
                .andExpect(jsonPath("$.[*].isAccepted").value(hasItem(DEFAULT_IS_ACCEPTED.booleanValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
                .andExpect(jsonPath("$.[*].dateStarted").value(hasItem(DEFAULT_DATE_STARTED_STR)))
                .andExpect(jsonPath("$.[*].dateCompleted").value(hasItem(DEFAULT_DATE_COMPLETED_STR)));
    }

    @Test
    @Transactional
    public void getMission() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get the mission
        restMissionMockMvc.perform(get("/api/missions/{id}", mission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(mission.getId().intValue()))
            .andExpect(jsonPath("$.isAccepted").value(DEFAULT_IS_ACCEPTED.booleanValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()))
            .andExpect(jsonPath("$.dateStarted").value(DEFAULT_DATE_STARTED_STR))
            .andExpect(jsonPath("$.dateCompleted").value(DEFAULT_DATE_COMPLETED_STR));
    }

    @Test
    @Transactional
    public void getNonExistingMission() throws Exception {
        // Get the mission
        restMissionMockMvc.perform(get("/api/missions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMission() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);
        int databaseSizeBeforeUpdate = missionRepository.findAll().size();

        // Update the mission
        Mission updatedMission = new Mission();
        updatedMission.setId(mission.getId());
        updatedMission.setIsAccepted(UPDATED_IS_ACCEPTED);
        updatedMission.setName(UPDATED_NAME);
        updatedMission.setNotes(UPDATED_NOTES);
        updatedMission.setDateStarted(UPDATED_DATE_STARTED);
        updatedMission.setDateCompleted(UPDATED_DATE_COMPLETED);

        restMissionMockMvc.perform(put("/api/missions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMission)))
                .andExpect(status().isOk());

        // Validate the Mission in the database
        List<Mission> missions = missionRepository.findAll();
        assertThat(missions).hasSize(databaseSizeBeforeUpdate);
        Mission testMission = missions.get(missions.size() - 1);
        assertThat(testMission.isIsAccepted()).isEqualTo(UPDATED_IS_ACCEPTED);
        assertThat(testMission.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMission.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testMission.getDateStarted()).isEqualTo(UPDATED_DATE_STARTED);
        assertThat(testMission.getDateCompleted()).isEqualTo(UPDATED_DATE_COMPLETED);
    }

    @Test
    @Transactional
    public void deleteMission() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);
        int databaseSizeBeforeDelete = missionRepository.findAll().size();

        // Get the mission
        restMissionMockMvc.perform(delete("/api/missions/{id}", mission.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Mission> missions = missionRepository.findAll();
        assertThat(missions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
