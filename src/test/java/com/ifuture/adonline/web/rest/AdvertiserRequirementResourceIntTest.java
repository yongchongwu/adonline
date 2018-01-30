package com.ifuture.adonline.web.rest;

import com.ifuture.adonline.AdonlineApp;

import com.ifuture.adonline.domain.AdvertiserRequirement;
import com.ifuture.adonline.repository.AdvertiserRequirementRepository;
import com.ifuture.adonline.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AdvertiserRequirementResource REST controller.
 *
 * @see AdvertiserRequirementResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdonlineApp.class)
public class AdvertiserRequirementResourceIntTest {

    private static final Long DEFAULT_MASTER_ID = 1L;
    private static final Long UPDATED_MASTER_ID = 2L;

    private static final Double DEFAULT_MIN_AMOUNT = 1D;
    private static final Double UPDATED_MIN_AMOUNT = 2D;

    private static final String DEFAULT_MEID = "AAAAAAAAAA";
    private static final String UPDATED_MEID = "BBBBBBBBBB";

    private static final String DEFAULT_ORDER_NO = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_NO = "BBBBBBBBBB";

    private static final String DEFAULT_WUID = "AAAAAAAAAA";
    private static final String UPDATED_WUID = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_DATE = "AAAAAAAAAA";
    private static final String UPDATED_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_TIME = "AAAAAAAAAA";
    private static final String UPDATED_TIME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CLICKED = false;
    private static final Boolean UPDATED_CLICKED = true;

    private static final Boolean DEFAULT_OVER_AVG_AMOUNT = false;
    private static final Boolean UPDATED_OVER_AVG_AMOUNT = true;

    private static final String DEFAULT_OS = "AAAAAAAAAA";
    private static final String UPDATED_OS = "BBBBBBBBBB";

    private static final String DEFAULT_NETWORK = "AAAAAAAAAA";
    private static final String UPDATED_NETWORK = "BBBBBBBBBB";

    private static final String DEFAULT_SEX = "AAAAAAAAAA";
    private static final String UPDATED_SEX = "BBBBBBBBBB";

    private static final String DEFAULT_TRADE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TRADE_TYPE = "BBBBBBBBBB";

    @Autowired
    private AdvertiserRequirementRepository advertiserRequirementRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAdvertiserRequirementMockMvc;

    private AdvertiserRequirement advertiserRequirement;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AdvertiserRequirementResource advertiserRequirementResource = new AdvertiserRequirementResource(advertiserRequirementRepository);
        this.restAdvertiserRequirementMockMvc = MockMvcBuilders.standaloneSetup(advertiserRequirementResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdvertiserRequirement createEntity(EntityManager em) {
        AdvertiserRequirement advertiserRequirement = new AdvertiserRequirement()
            .masterId(DEFAULT_MASTER_ID)
            .minAmount(DEFAULT_MIN_AMOUNT)
            .meid(DEFAULT_MEID)
            .orderNo(DEFAULT_ORDER_NO)
            .wuid(DEFAULT_WUID)
            .city(DEFAULT_CITY)
            .category(DEFAULT_CATEGORY)
            .date(DEFAULT_DATE)
            .time(DEFAULT_TIME)
            .clicked(DEFAULT_CLICKED)
            .overAvgAmount(DEFAULT_OVER_AVG_AMOUNT)
            .os(DEFAULT_OS)
            .network(DEFAULT_NETWORK)
            .sex(DEFAULT_SEX)
            .tradeType(DEFAULT_TRADE_TYPE);
        return advertiserRequirement;
    }

    @Before
    public void initTest() {
        advertiserRequirement = createEntity(em);
    }

    @Test
    @Transactional
    public void createAdvertiserRequirement() throws Exception {
        int databaseSizeBeforeCreate = advertiserRequirementRepository.findAll().size();

        // Create the AdvertiserRequirement
        restAdvertiserRequirementMockMvc.perform(post("/api/advertiser-requirements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(advertiserRequirement)))
            .andExpect(status().isCreated());

        // Validate the AdvertiserRequirement in the database
        List<AdvertiserRequirement> advertiserRequirementList = advertiserRequirementRepository.findAll();
        assertThat(advertiserRequirementList).hasSize(databaseSizeBeforeCreate + 1);
        AdvertiserRequirement testAdvertiserRequirement = advertiserRequirementList.get(advertiserRequirementList.size() - 1);
        assertThat(testAdvertiserRequirement.getMasterId()).isEqualTo(DEFAULT_MASTER_ID);
        assertThat(testAdvertiserRequirement.getMinAmount()).isEqualTo(DEFAULT_MIN_AMOUNT);
        assertThat(testAdvertiserRequirement.getMeid()).isEqualTo(DEFAULT_MEID);
        assertThat(testAdvertiserRequirement.getOrderNo()).isEqualTo(DEFAULT_ORDER_NO);
        assertThat(testAdvertiserRequirement.getWuid()).isEqualTo(DEFAULT_WUID);
        assertThat(testAdvertiserRequirement.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testAdvertiserRequirement.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testAdvertiserRequirement.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testAdvertiserRequirement.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testAdvertiserRequirement.isClicked()).isEqualTo(DEFAULT_CLICKED);
        assertThat(testAdvertiserRequirement.isOverAvgAmount()).isEqualTo(DEFAULT_OVER_AVG_AMOUNT);
        assertThat(testAdvertiserRequirement.getOs()).isEqualTo(DEFAULT_OS);
        assertThat(testAdvertiserRequirement.getNetwork()).isEqualTo(DEFAULT_NETWORK);
        assertThat(testAdvertiserRequirement.getSex()).isEqualTo(DEFAULT_SEX);
        assertThat(testAdvertiserRequirement.getTradeType()).isEqualTo(DEFAULT_TRADE_TYPE);
    }

    @Test
    @Transactional
    public void createAdvertiserRequirementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = advertiserRequirementRepository.findAll().size();

        // Create the AdvertiserRequirement with an existing ID
        advertiserRequirement.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdvertiserRequirementMockMvc.perform(post("/api/advertiser-requirements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(advertiserRequirement)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<AdvertiserRequirement> advertiserRequirementList = advertiserRequirementRepository.findAll();
        assertThat(advertiserRequirementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkMasterIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = advertiserRequirementRepository.findAll().size();
        // set the field null
        advertiserRequirement.setMasterId(null);

        // Create the AdvertiserRequirement, which fails.

        restAdvertiserRequirementMockMvc.perform(post("/api/advertiser-requirements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(advertiserRequirement)))
            .andExpect(status().isBadRequest());

        List<AdvertiserRequirement> advertiserRequirementList = advertiserRequirementRepository.findAll();
        assertThat(advertiserRequirementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAdvertiserRequirements() throws Exception {
        // Initialize the database
        advertiserRequirementRepository.saveAndFlush(advertiserRequirement);

        // Get all the advertiserRequirementList
        restAdvertiserRequirementMockMvc.perform(get("/api/advertiser-requirements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(advertiserRequirement.getId().intValue())))
            .andExpect(jsonPath("$.[*].masterId").value(hasItem(DEFAULT_MASTER_ID.intValue())))
            .andExpect(jsonPath("$.[*].minAmount").value(hasItem(DEFAULT_MIN_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].meid").value(hasItem(DEFAULT_MEID.toString())))
            .andExpect(jsonPath("$.[*].orderNo").value(hasItem(DEFAULT_ORDER_NO.toString())))
            .andExpect(jsonPath("$.[*].wuid").value(hasItem(DEFAULT_WUID.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.toString())))
            .andExpect(jsonPath("$.[*].clicked").value(hasItem(DEFAULT_CLICKED.booleanValue())))
            .andExpect(jsonPath("$.[*].overAvgAmount").value(hasItem(DEFAULT_OVER_AVG_AMOUNT.booleanValue())))
            .andExpect(jsonPath("$.[*].os").value(hasItem(DEFAULT_OS.toString())))
            .andExpect(jsonPath("$.[*].network").value(hasItem(DEFAULT_NETWORK.toString())))
            .andExpect(jsonPath("$.[*].sex").value(hasItem(DEFAULT_SEX.toString())))
            .andExpect(jsonPath("$.[*].tradeType").value(hasItem(DEFAULT_TRADE_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getAdvertiserRequirement() throws Exception {
        // Initialize the database
        advertiserRequirementRepository.saveAndFlush(advertiserRequirement);

        // Get the advertiserRequirement
        restAdvertiserRequirementMockMvc.perform(get("/api/advertiser-requirements/{id}", advertiserRequirement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(advertiserRequirement.getId().intValue()))
            .andExpect(jsonPath("$.masterId").value(DEFAULT_MASTER_ID.intValue()))
            .andExpect(jsonPath("$.minAmount").value(DEFAULT_MIN_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.meid").value(DEFAULT_MEID.toString()))
            .andExpect(jsonPath("$.orderNo").value(DEFAULT_ORDER_NO.toString()))
            .andExpect(jsonPath("$.wuid").value(DEFAULT_WUID.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME.toString()))
            .andExpect(jsonPath("$.clicked").value(DEFAULT_CLICKED.booleanValue()))
            .andExpect(jsonPath("$.overAvgAmount").value(DEFAULT_OVER_AVG_AMOUNT.booleanValue()))
            .andExpect(jsonPath("$.os").value(DEFAULT_OS.toString()))
            .andExpect(jsonPath("$.network").value(DEFAULT_NETWORK.toString()))
            .andExpect(jsonPath("$.sex").value(DEFAULT_SEX.toString()))
            .andExpect(jsonPath("$.tradeType").value(DEFAULT_TRADE_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAdvertiserRequirement() throws Exception {
        // Get the advertiserRequirement
        restAdvertiserRequirementMockMvc.perform(get("/api/advertiser-requirements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAdvertiserRequirement() throws Exception {
        // Initialize the database
        advertiserRequirementRepository.saveAndFlush(advertiserRequirement);
        int databaseSizeBeforeUpdate = advertiserRequirementRepository.findAll().size();

        // Update the advertiserRequirement
        AdvertiserRequirement updatedAdvertiserRequirement = advertiserRequirementRepository.findOne(advertiserRequirement.getId());
        updatedAdvertiserRequirement
            .masterId(UPDATED_MASTER_ID)
            .minAmount(UPDATED_MIN_AMOUNT)
            .meid(UPDATED_MEID)
            .orderNo(UPDATED_ORDER_NO)
            .wuid(UPDATED_WUID)
            .city(UPDATED_CITY)
            .category(UPDATED_CATEGORY)
            .date(UPDATED_DATE)
            .time(UPDATED_TIME)
            .clicked(UPDATED_CLICKED)
            .overAvgAmount(UPDATED_OVER_AVG_AMOUNT)
            .os(UPDATED_OS)
            .network(UPDATED_NETWORK)
            .sex(UPDATED_SEX)
            .tradeType(UPDATED_TRADE_TYPE);

        restAdvertiserRequirementMockMvc.perform(put("/api/advertiser-requirements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAdvertiserRequirement)))
            .andExpect(status().isOk());

        // Validate the AdvertiserRequirement in the database
        List<AdvertiserRequirement> advertiserRequirementList = advertiserRequirementRepository.findAll();
        assertThat(advertiserRequirementList).hasSize(databaseSizeBeforeUpdate);
        AdvertiserRequirement testAdvertiserRequirement = advertiserRequirementList.get(advertiserRequirementList.size() - 1);
        assertThat(testAdvertiserRequirement.getMasterId()).isEqualTo(UPDATED_MASTER_ID);
        assertThat(testAdvertiserRequirement.getMinAmount()).isEqualTo(UPDATED_MIN_AMOUNT);
        assertThat(testAdvertiserRequirement.getMeid()).isEqualTo(UPDATED_MEID);
        assertThat(testAdvertiserRequirement.getOrderNo()).isEqualTo(UPDATED_ORDER_NO);
        assertThat(testAdvertiserRequirement.getWuid()).isEqualTo(UPDATED_WUID);
        assertThat(testAdvertiserRequirement.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testAdvertiserRequirement.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testAdvertiserRequirement.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testAdvertiserRequirement.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testAdvertiserRequirement.isClicked()).isEqualTo(UPDATED_CLICKED);
        assertThat(testAdvertiserRequirement.isOverAvgAmount()).isEqualTo(UPDATED_OVER_AVG_AMOUNT);
        assertThat(testAdvertiserRequirement.getOs()).isEqualTo(UPDATED_OS);
        assertThat(testAdvertiserRequirement.getNetwork()).isEqualTo(UPDATED_NETWORK);
        assertThat(testAdvertiserRequirement.getSex()).isEqualTo(UPDATED_SEX);
        assertThat(testAdvertiserRequirement.getTradeType()).isEqualTo(UPDATED_TRADE_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingAdvertiserRequirement() throws Exception {
        int databaseSizeBeforeUpdate = advertiserRequirementRepository.findAll().size();

        // Create the AdvertiserRequirement

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAdvertiserRequirementMockMvc.perform(put("/api/advertiser-requirements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(advertiserRequirement)))
            .andExpect(status().isCreated());

        // Validate the AdvertiserRequirement in the database
        List<AdvertiserRequirement> advertiserRequirementList = advertiserRequirementRepository.findAll();
        assertThat(advertiserRequirementList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAdvertiserRequirement() throws Exception {
        // Initialize the database
        advertiserRequirementRepository.saveAndFlush(advertiserRequirement);
        int databaseSizeBeforeDelete = advertiserRequirementRepository.findAll().size();

        // Get the advertiserRequirement
        restAdvertiserRequirementMockMvc.perform(delete("/api/advertiser-requirements/{id}", advertiserRequirement.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AdvertiserRequirement> advertiserRequirementList = advertiserRequirementRepository.findAll();
        assertThat(advertiserRequirementList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdvertiserRequirement.class);
        AdvertiserRequirement advertiserRequirement1 = new AdvertiserRequirement();
        advertiserRequirement1.setId(1L);
        AdvertiserRequirement advertiserRequirement2 = new AdvertiserRequirement();
        advertiserRequirement2.setId(advertiserRequirement1.getId());
        assertThat(advertiserRequirement1).isEqualTo(advertiserRequirement2);
        advertiserRequirement2.setId(2L);
        assertThat(advertiserRequirement1).isNotEqualTo(advertiserRequirement2);
        advertiserRequirement1.setId(null);
        assertThat(advertiserRequirement1).isNotEqualTo(advertiserRequirement2);
    }
}
