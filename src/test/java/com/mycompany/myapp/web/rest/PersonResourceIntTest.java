package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterApp;
import com.mycompany.myapp.domain.Person;
import com.mycompany.myapp.repository.PersonRepository;

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
 * Test class for the PersonResource REST controller.
 *
 * @see PersonResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JhipsterApp.class)
@WebAppConfiguration
@IntegrationTest
public class PersonResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_ADDRESS = "AAAAA";
    private static final String UPDATED_ADDRESS = "BBBBB";
    private static final String DEFAULT_BSN = "AAAAA";
    private static final String UPDATED_BSN = "BBBBB";
    private static final String DEFAULT_FIRST_NAME = "AAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBB";
    private static final String DEFAULT_LAST_NAME = "AAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBB";

    private static final LocalDate DEFAULT_DATE_BORN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_BORN = LocalDate.now(ZoneId.systemDefault());

    private static final ZonedDateTime DEFAULT_DATE_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_CREATED_STR = dateTimeFormatter.format(DEFAULT_DATE_CREATED);

    private static final ZonedDateTime DEFAULT_DATE_LAST_EDITED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE_LAST_EDITED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_LAST_EDITED_STR = dateTimeFormatter.format(DEFAULT_DATE_LAST_EDITED);

    @Inject
    private PersonRepository personRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPersonMockMvc;

    private Person person;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PersonResource personResource = new PersonResource();
        ReflectionTestUtils.setField(personResource, "personRepository", personRepository);
        this.restPersonMockMvc = MockMvcBuilders.standaloneSetup(personResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        person = new Person();
        person.setAddress(DEFAULT_ADDRESS);
        person.setBsn(DEFAULT_BSN);
        person.setFirstName(DEFAULT_FIRST_NAME);
        person.setLastName(DEFAULT_LAST_NAME);
        person.setDateBorn(DEFAULT_DATE_BORN);
        person.setDateCreated(DEFAULT_DATE_CREATED);
        person.setDateLastEdited(DEFAULT_DATE_LAST_EDITED);
    }

    @Test
    @Transactional
    public void createPerson() throws Exception {
        int databaseSizeBeforeCreate = personRepository.findAll().size();

        // Create the Person

        restPersonMockMvc.perform(post("/api/people")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(person)))
                .andExpect(status().isCreated());

        // Validate the Person in the database
        List<Person> people = personRepository.findAll();
        assertThat(people).hasSize(databaseSizeBeforeCreate + 1);
        Person testPerson = people.get(people.size() - 1);
        assertThat(testPerson.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testPerson.getBsn()).isEqualTo(DEFAULT_BSN);
        assertThat(testPerson.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testPerson.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testPerson.getDateBorn()).isEqualTo(DEFAULT_DATE_BORN);
        assertThat(testPerson.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testPerson.getDateLastEdited()).isEqualTo(DEFAULT_DATE_LAST_EDITED);
    }

    @Test
    @Transactional
    public void checkDateCreatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = personRepository.findAll().size();
        // set the field null
        person.setDateCreated(null);

        // Create the Person, which fails.

        restPersonMockMvc.perform(post("/api/people")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(person)))
                .andExpect(status().isBadRequest());

        List<Person> people = personRepository.findAll();
        assertThat(people).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPeople() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the people
        restPersonMockMvc.perform(get("/api/people?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(person.getId().intValue())))
                .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
                .andExpect(jsonPath("$.[*].bsn").value(hasItem(DEFAULT_BSN.toString())))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
                .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
                .andExpect(jsonPath("$.[*].dateBorn").value(hasItem(DEFAULT_DATE_BORN.toString())))
                .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED_STR)))
                .andExpect(jsonPath("$.[*].dateLastEdited").value(hasItem(DEFAULT_DATE_LAST_EDITED_STR)));
    }

    @Test
    @Transactional
    public void getPerson() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get the person
        restPersonMockMvc.perform(get("/api/people/{id}", person.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(person.getId().intValue()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.bsn").value(DEFAULT_BSN.toString()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.dateBorn").value(DEFAULT_DATE_BORN.toString()))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED_STR))
            .andExpect(jsonPath("$.dateLastEdited").value(DEFAULT_DATE_LAST_EDITED_STR));
    }

    @Test
    @Transactional
    public void getNonExistingPerson() throws Exception {
        // Get the person
        restPersonMockMvc.perform(get("/api/people/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePerson() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);
        int databaseSizeBeforeUpdate = personRepository.findAll().size();

        // Update the person
        Person updatedPerson = new Person();
        updatedPerson.setId(person.getId());
        updatedPerson.setAddress(UPDATED_ADDRESS);
        updatedPerson.setBsn(UPDATED_BSN);
        updatedPerson.setFirstName(UPDATED_FIRST_NAME);
        updatedPerson.setLastName(UPDATED_LAST_NAME);
        updatedPerson.setDateBorn(UPDATED_DATE_BORN);
        updatedPerson.setDateCreated(UPDATED_DATE_CREATED);
        updatedPerson.setDateLastEdited(UPDATED_DATE_LAST_EDITED);

        restPersonMockMvc.perform(put("/api/people")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPerson)))
                .andExpect(status().isOk());

        // Validate the Person in the database
        List<Person> people = personRepository.findAll();
        assertThat(people).hasSize(databaseSizeBeforeUpdate);
        Person testPerson = people.get(people.size() - 1);
        assertThat(testPerson.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testPerson.getBsn()).isEqualTo(UPDATED_BSN);
        assertThat(testPerson.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testPerson.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testPerson.getDateBorn()).isEqualTo(UPDATED_DATE_BORN);
        assertThat(testPerson.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testPerson.getDateLastEdited()).isEqualTo(UPDATED_DATE_LAST_EDITED);
    }

    @Test
    @Transactional
    public void deletePerson() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);
        int databaseSizeBeforeDelete = personRepository.findAll().size();

        // Get the person
        restPersonMockMvc.perform(delete("/api/people/{id}", person.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Person> people = personRepository.findAll();
        assertThat(people).hasSize(databaseSizeBeforeDelete - 1);
    }
}
