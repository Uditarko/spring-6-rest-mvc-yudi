package guru.springframework.yudi.spring6restmvcyudi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.yudi.spring6restmvcyudi.entities.Beer;
import guru.springframework.yudi.spring6restmvcyudi.model.BeerDTO;
import guru.springframework.yudi.spring6restmvcyudi.model.BeerStyle;
import guru.springframework.yudi.spring6restmvcyudi.repositories.BeerRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static guru.springframework.yudi.spring6restmvcyudi.controllers.BeerController.BEER_PATH_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@Slf4j
@SpringBootTest
        /* Will not wire up web context but will wire up everything else opposite to test splices*/
class BeerControllerIT {

    @Autowired
    BeerController beerController;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    WebApplicationContext wac;

    @Autowired
    ObjectMapper objectMapper;

    MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void testListBeers() {
        List<BeerDTO> beers = beerController.listBeers(null, null, null);
        assertThat(beers.size()).isEqualTo(2413);
    }

    @Test
    void testListBeersByName() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                        .queryParam("beerName", "%IPA%"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(336)));
    }

    @Test
    void testListBeersByBeerStyle() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                        .queryParam("beerStyle", "IPA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(548)));
    }

    @Test
    void testListBeersByBeerNameAndBeerStyleAndShowInventory() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                        .queryParam("beerName", "%IPA%")
                        .queryParam("beerStyle", "IPA")
                        .queryParam("showInventory", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(310))).andExpect(jsonPath("$.[0].quantityOnHand").value(IsNull.notNullValue()));
    }

    @Test
    void testListBeersByBeerNameAndBeerStyleAndNotShowInventory() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                        .queryParam("beerName", "%IPA%")
                        .queryParam("beerStyle", "IPA")
                        .queryParam("showInventory", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(310))).andExpect(jsonPath("$.[0].quantityOnHand").value(IsNull.nullValue()));
    }

    @Rollback
    @Transactional
    @Test
    void testEmptyListBeers() {
        beerRepository.deleteAll();
        List<BeerDTO> beers = beerController.listBeers(null, null, null);
        assertThat(beers.size()).isEqualTo(0);
    }

    @Test
    void getBeerByIdFound() {
        BeerDTO foundBeer = beerController.getBeerById(beerRepository.findAll().getFirst().getId());
        assertThat(foundBeer.getId()).isNotNull();
    }

    @Rollback
    @Transactional
    @Test
    void getBeerByIdNotFound() {
        beerRepository.deleteAll();
        assertThrows(NotFoundException.class, () -> beerController.getBeerById(UUID.randomUUID()));
    }

    @Test
    @Transactional
    @Rollback
    void testSavedNewBeer() {
        ResponseEntity responseEntity = beerController.saveNewBeer(BeerDTO.builder().beerName("New Beer").build());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String id = responseEntity.getHeaders().getLocation().getPath().split("/")[4].strip();
        System.out.println(id);
        Optional<Beer> savedBeer = beerRepository.findById(UUID.fromString(id));
        assertThat(savedBeer.isPresent()).isTrue();
        assertThat(savedBeer.get().getBeerName()).isEqualTo("New Beer");
    }

    @Test
    @Transactional
    @Rollback
    void updateBeerByIdWhenExists() {
        Beer beerToUpdate = beerRepository.findAll().getFirst();
        BeerDTO inBeerDto = BeerDTO.builder()
                .beerName("Updated")
                .beerStyle(BeerStyle.GOSE)
                .id(null)
                .version(null)
                .build();
        ResponseEntity responseEntity = beerController.updateBeerById(beerToUpdate.getId(), inBeerDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        /*Not required to fetch again as the test is Transactional*/
        assertThat(beerToUpdate.getBeerName()).isEqualTo(inBeerDto.getBeerName());
        assertThat(beerToUpdate.getBeerStyle()).isEqualTo(BeerStyle.GOSE);
        assertThat(beerToUpdate.getUpc()).isNull();

    }

    @Test
    @Transactional
    @Rollback
    void updateBeerByIdWhenNotExists() {
        beerRepository.deleteAll();
        BeerDTO inBeerDto = BeerDTO.builder()
                .beerName("Updated")
                .beerStyle(BeerStyle.GOSE)
                .id(null)
                .version(null)
                .build();
        assertThrows(NotFoundException.class, () -> beerController.updateBeerById(UUID.randomUUID(), inBeerDto));
    }

    @Test
    @Transactional
    @Rollback
    public void testdeleteByIdFound() {
        Beer beer = beerRepository.findAll().getFirst();
        ResponseEntity responseEntity = beerController.deleteBeerById(beer.getId());
        assertThat(beerRepository.findById(beer.getId()).isPresent()).isFalse();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @Transactional
    @Rollback
    public void testdeleteByIdNotFound() {
        Beer beer = beerRepository.findAll().getFirst();
        beerRepository.deleteById(beer.getId());
        assertThrows(NotFoundException.class, () -> beerController.deleteBeerById(beer.getId()));
    }

    @Test
    @Transactional
    @Rollback
    void testPatchBeerByIdSuccess() {
        Beer existing = beerRepository.findAll().getFirst();
        BeerDTO update = BeerDTO.builder().beerName("Patched").build();
        ResponseEntity responseEntity = beerController.patchBeerById(existing.getId(), update);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(existing.getBeerName()).isEqualTo("Patched");
        assertThat(existing.getUpc()).isNotNull();
    }

    @Test
    @Transactional
    @Rollback
    void testPatchBeerByIdNotFound() {
        beerRepository.deleteAll();
        BeerDTO update = BeerDTO.builder().beerName("Patched").build();
        assertThrows(NotFoundException.class, () -> beerController.patchBeerById(UUID.randomUUID(), update));
    }

    /* from here the tests are written with web context*/
    @Test
    void testPatchBeerByIdBadName() throws Exception {
        Beer existing = beerRepository.findAll().getFirst();
        Map<String, String> valueMap = new HashMap<>();
        valueMap.put("beerName", existing.getBeerName() + " patched1234567890123456789012345678901234567890123456789012345678901234567890");

        MvcResult mvcResult = mockMvc.perform(patch(BEER_PATH_ID, existing.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(valueMap)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)))
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }
}