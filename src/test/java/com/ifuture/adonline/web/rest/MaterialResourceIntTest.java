package com.ifuture.adonline.web.rest;

import com.ifuture.adonline.AdonlineApp;

import com.ifuture.adonline.domain.Material;
import com.ifuture.adonline.repository.MaterialRepository;
import com.ifuture.adonline.service.MaterialService;
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
 * Test class for the MaterialResource REST controller.
 *
 * @see MaterialResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdonlineApp.class)
public class MaterialResourceIntTest {

    private static final String DEFAULT_POSITION = "AAAAAAAAAA";
    private static final String UPDATED_POSITION = "BBBBBBBBBB";

    private static final String DEFAULT_TAGS = "AAAAAAAAAA";
    private static final String UPDATED_TAGS = "BBBBBBBBBB";

    private static final String DEFAULT_START_DATE = "AAAAAAAAAA";
    private static final String UPDATED_START_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_END_DATE = "AAAAAAAAAA";
    private static final String UPDATED_END_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_IMG_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMG_URL = "BBBBBBBBBB";

    private static final String DEFAULT_HREF_URL = "AAAAAAAAAA";
    private static final String UPDATED_HREF_URL = "BBBBBBBBBB";

    private static final Double DEFAULT_CUSTOMER_PRICE = 1D;
    private static final Double UPDATED_CUSTOMER_PRICE = 2D;

    private static final String DEFAULT_COPY = "AAAAAAAAAA";
    private static final String UPDATED_COPY = "BBBBBBBBBB";

    private static final String DEFAULT_COLLECT_DATE = "AAAAAAAAAA";
    private static final String UPDATED_COLLECT_DATE = "BBBBBBBBBB";

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMaterialMockMvc;

    private Material material;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MaterialResource materialResource = new MaterialResource(materialService);
        this.restMaterialMockMvc = MockMvcBuilders.standaloneSetup(materialResource)
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
    public static Material createEntity(EntityManager em) {
        Material material = new Material()
            .position(DEFAULT_POSITION)
            .tags(DEFAULT_TAGS)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .imgUrl(DEFAULT_IMG_URL)
            .hrefUrl(DEFAULT_HREF_URL)
            .customerPrice(DEFAULT_CUSTOMER_PRICE)
            .copy(DEFAULT_COPY)
            .collectDate(DEFAULT_COLLECT_DATE);
        return material;
    }

    @Before
    public void initTest() {
        material = createEntity(em);
    }

    @Test
    @Transactional
    public void createMaterial() throws Exception {
        int databaseSizeBeforeCreate = materialRepository.findAll().size();

        // Create the Material
        restMaterialMockMvc.perform(post("/api/materials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(material)))
            .andExpect(status().isCreated());

        // Validate the Material in the database
        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeCreate + 1);
        Material testMaterial = materialList.get(materialList.size() - 1);
        assertThat(testMaterial.getPosition()).isEqualTo(DEFAULT_POSITION);
        assertThat(testMaterial.getTags()).isEqualTo(DEFAULT_TAGS);
        assertThat(testMaterial.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testMaterial.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testMaterial.getImgUrl()).isEqualTo(DEFAULT_IMG_URL);
        assertThat(testMaterial.getHrefUrl()).isEqualTo(DEFAULT_HREF_URL);
        assertThat(testMaterial.getCustomerPrice()).isEqualTo(DEFAULT_CUSTOMER_PRICE);
        assertThat(testMaterial.getCopy()).isEqualTo(DEFAULT_COPY);
        assertThat(testMaterial.getCollectDate()).isEqualTo(DEFAULT_COLLECT_DATE);
    }

    @Test
    @Transactional
    public void createMaterialWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = materialRepository.findAll().size();

        // Create the Material with an existing ID
        material.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaterialMockMvc.perform(post("/api/materials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(material)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkPositionIsRequired() throws Exception {
        int databaseSizeBeforeTest = materialRepository.findAll().size();
        // set the field null
        material.setPosition(null);

        // Create the Material, which fails.

        restMaterialMockMvc.perform(post("/api/materials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(material)))
            .andExpect(status().isBadRequest());

        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTagsIsRequired() throws Exception {
        int databaseSizeBeforeTest = materialRepository.findAll().size();
        // set the field null
        material.setTags(null);

        // Create the Material, which fails.

        restMaterialMockMvc.perform(post("/api/materials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(material)))
            .andExpect(status().isBadRequest());

        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = materialRepository.findAll().size();
        // set the field null
        material.setStartDate(null);

        // Create the Material, which fails.

        restMaterialMockMvc.perform(post("/api/materials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(material)))
            .andExpect(status().isBadRequest());

        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = materialRepository.findAll().size();
        // set the field null
        material.setEndDate(null);

        // Create the Material, which fails.

        restMaterialMockMvc.perform(post("/api/materials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(material)))
            .andExpect(status().isBadRequest());

        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkImgUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = materialRepository.findAll().size();
        // set the field null
        material.setImgUrl(null);

        // Create the Material, which fails.

        restMaterialMockMvc.perform(post("/api/materials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(material)))
            .andExpect(status().isBadRequest());

        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHrefUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = materialRepository.findAll().size();
        // set the field null
        material.setHrefUrl(null);

        // Create the Material, which fails.

        restMaterialMockMvc.perform(post("/api/materials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(material)))
            .andExpect(status().isBadRequest());

        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCustomerPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = materialRepository.findAll().size();
        // set the field null
        material.setCustomerPrice(null);

        // Create the Material, which fails.

        restMaterialMockMvc.perform(post("/api/materials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(material)))
            .andExpect(status().isBadRequest());

        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCollectDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = materialRepository.findAll().size();
        // set the field null
        material.setCollectDate(null);

        // Create the Material, which fails.

        restMaterialMockMvc.perform(post("/api/materials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(material)))
            .andExpect(status().isBadRequest());

        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMaterials() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList
        restMaterialMockMvc.perform(get("/api/materials?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(material.getId().intValue())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION.toString())))
            .andExpect(jsonPath("$.[*].tags").value(hasItem(DEFAULT_TAGS.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].imgUrl").value(hasItem(DEFAULT_IMG_URL.toString())))
            .andExpect(jsonPath("$.[*].hrefUrl").value(hasItem(DEFAULT_HREF_URL.toString())))
            .andExpect(jsonPath("$.[*].customerPrice").value(hasItem(DEFAULT_CUSTOMER_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].copy").value(hasItem(DEFAULT_COPY.toString())))
            .andExpect(jsonPath("$.[*].collectDate").value(hasItem(DEFAULT_COLLECT_DATE.toString())));
    }

    @Test
    @Transactional
    public void getMaterial() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get the material
        restMaterialMockMvc.perform(get("/api/materials/{id}", material.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(material.getId().intValue()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION.toString()))
            .andExpect(jsonPath("$.tags").value(DEFAULT_TAGS.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.imgUrl").value(DEFAULT_IMG_URL.toString()))
            .andExpect(jsonPath("$.hrefUrl").value(DEFAULT_HREF_URL.toString()))
            .andExpect(jsonPath("$.customerPrice").value(DEFAULT_CUSTOMER_PRICE.doubleValue()))
            .andExpect(jsonPath("$.copy").value(DEFAULT_COPY.toString()))
            .andExpect(jsonPath("$.collectDate").value(DEFAULT_COLLECT_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMaterial() throws Exception {
        // Get the material
        restMaterialMockMvc.perform(get("/api/materials/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMaterial() throws Exception {
        // Initialize the database
        materialService.save(material);

        int databaseSizeBeforeUpdate = materialRepository.findAll().size();

        // Update the material
        Material updatedMaterial = materialRepository.findOne(material.getId());
        updatedMaterial
            .position(UPDATED_POSITION)
            .tags(UPDATED_TAGS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .imgUrl(UPDATED_IMG_URL)
            .hrefUrl(UPDATED_HREF_URL)
            .customerPrice(UPDATED_CUSTOMER_PRICE)
            .copy(UPDATED_COPY)
            .collectDate(UPDATED_COLLECT_DATE);

        restMaterialMockMvc.perform(put("/api/materials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMaterial)))
            .andExpect(status().isOk());

        // Validate the Material in the database
        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeUpdate);
        Material testMaterial = materialList.get(materialList.size() - 1);
        assertThat(testMaterial.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testMaterial.getTags()).isEqualTo(UPDATED_TAGS);
        assertThat(testMaterial.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testMaterial.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testMaterial.getImgUrl()).isEqualTo(UPDATED_IMG_URL);
        assertThat(testMaterial.getHrefUrl()).isEqualTo(UPDATED_HREF_URL);
        assertThat(testMaterial.getCustomerPrice()).isEqualTo(UPDATED_CUSTOMER_PRICE);
        assertThat(testMaterial.getCopy()).isEqualTo(UPDATED_COPY);
        assertThat(testMaterial.getCollectDate()).isEqualTo(UPDATED_COLLECT_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingMaterial() throws Exception {
        int databaseSizeBeforeUpdate = materialRepository.findAll().size();

        // Create the Material

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMaterialMockMvc.perform(put("/api/materials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(material)))
            .andExpect(status().isCreated());

        // Validate the Material in the database
        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMaterial() throws Exception {
        // Initialize the database
        materialService.save(material);

        int databaseSizeBeforeDelete = materialRepository.findAll().size();

        // Get the material
        restMaterialMockMvc.perform(delete("/api/materials/{id}", material.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Material.class);
        Material material1 = new Material();
        material1.setId(1L);
        Material material2 = new Material();
        material2.setId(material1.getId());
        assertThat(material1).isEqualTo(material2);
        material2.setId(2L);
        assertThat(material1).isNotEqualTo(material2);
        material1.setId(null);
        assertThat(material1).isNotEqualTo(material2);
    }
}
