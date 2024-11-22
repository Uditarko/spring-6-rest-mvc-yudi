package guru.springframework.yudi.spring6restmvcyudi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.yudi.spring6restmvcyudi.model.BeerDTO;
import guru.springframework.yudi.spring6restmvcyudi.services.BeerService;
import guru.springframework.yudi.spring6restmvcyudi.services.BeerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static guru.springframework.yudi.spring6restmvcyudi.controllers.BeerController.BEER_PATH;
import static guru.springframework.yudi.spring6restmvcyudi.controllers.BeerController.BEER_PATH_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BeerService beerService;

    @Autowired
    ObjectMapper objectMapper;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<BeerDTO> beerArgumentCaptor;

    BeerServiceImpl beerServiceImpl;

    @BeforeEach
    public void setUp() {
        beerServiceImpl = new BeerServiceImpl();
    }

    @Test
    void getBeerById() throws Exception {
        BeerDTO testBeerDTO = beerServiceImpl.listBeers(null, null, null).getFirst();

        given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.ofNullable(testBeerDTO));

        assert testBeerDTO != null;
        mockMvc.perform(get(BEER_PATH_ID, UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testBeerDTO.getId().toString())))
                .andExpect(jsonPath("$.beerName", is(testBeerDTO.getBeerName())));
    }

    @Test
    void getBeerByIdNotFound() throws Exception {

        given(beerService.getBeerById(any(UUID.class))).willThrow(NotFoundException.class);

        mockMvc.perform(get(BEER_PATH_ID, UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testListBeers() throws Exception {
        given(beerService.listBeers(null, null, null)).willReturn(beerServiceImpl.listBeers(null, null, null));

        mockMvc.perform(get(BEER_PATH)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void testSaveNewBeer() throws Exception {
        BeerDTO beerDTO = beerServiceImpl.listBeers(null, null, null).getFirst();
        beerDTO.setCreatedDate(null);
        beerDTO.setId(null);
        beerDTO.setVersion(null);
        beerDTO.setUpdateDate(null);
        given(beerService.saveNewBeer(beerDTO)).willReturn(beerServiceImpl.listBeers(null, null, null).get(1));

        mockMvc.perform(post(BEER_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void testSaveNewBeerWithNullBeerName() throws Exception {
        BeerDTO beerDTO = beerServiceImpl.listBeers(null, null, null).getFirst();
        beerDTO.setBeerName(null);
        beerDTO.setCreatedDate(null);
        beerDTO.setId(null);
        beerDTO.setVersion(null);
        beerDTO.setUpdateDate(null);
        System.out.println(beerDTO);
        given(beerService.saveNewBeer(beerDTO)).willReturn(beerServiceImpl.listBeers(null, null, null).get(1));

        MvcResult mvcResult = mockMvc.perform(post(BEER_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(2)))
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testSaveNewBeerWithAllNull() throws Exception {
        BeerDTO beerDTO = beerServiceImpl.listBeers(null, null, null).getFirst();
        beerDTO.setBeerName(null);
        beerDTO.setBeerStyle(null);
        beerDTO.setUpc(null);
        beerDTO.setPrice(BigDecimal.ZERO);
        beerDTO.setCreatedDate(null);
        beerDTO.setId(null);
        beerDTO.setVersion(null);
        beerDTO.setUpdateDate(null);
        System.out.println(beerDTO);
        given(beerService.saveNewBeer(beerDTO)).willReturn(beerServiceImpl.listBeers(null, null, null).get(1));

        MvcResult mvcResult = mockMvc.perform(post(BEER_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(6)))
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testUpdateBeerById() throws Exception {
        BeerDTO beerDTO = beerServiceImpl.listBeers(null, null, null).getFirst();

        given(beerService.updateBeerById(any(), any())).willReturn(Optional.of(beerDTO));

        mockMvc.perform(put(BEER_PATH_ID, beerDTO.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerDTO)))
                .andExpect(status().isNoContent());

        verify(beerService).updateBeerById(beerDTO.getId(), beerDTO);
    }

    @Test
    void testUpdateBeerByIdWithReqAttrMissing() throws Exception {
        BeerDTO beerDTO = beerServiceImpl.listBeers(null, null, null).getFirst();
        beerDTO.setBeerName(null);
        beerDTO.setBeerStyle(null);
        beerDTO.setUpc(null);
        beerDTO.setPrice(BigDecimal.ZERO);

        given(beerService.updateBeerById(any(), any())).willReturn(Optional.of(beerDTO));

        MvcResult mvcResult = mockMvc.perform(put(BEER_PATH_ID, beerDTO.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(6)))
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
        verify(beerService, times(0)).updateBeerById(beerDTO.getId(), beerDTO);
    }

    @Test
    void testDeleteBeerById() throws Exception {
        BeerDTO beerDTO = beerServiceImpl.listBeers(null, null, null).getFirst();
        given(beerService.deleteById(any())).willReturn(true);
        mockMvc.perform(delete(BEER_PATH_ID, beerDTO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(beerService).deleteById(uuidArgumentCaptor.capture());
        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(beerDTO.getId());
    }

    @Test
    void testPatchBeerById() throws Exception {
        BeerDTO toUpdateDTO = beerServiceImpl.listBeers(null, null, null).getFirst();

        Map<String, String> valueMap = new HashMap<>();
        valueMap.put("beerName", toUpdateDTO.getBeerName() + " patched");
        given(beerService.patchById(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture())).willReturn(Optional.of(toUpdateDTO));
        mockMvc.perform(patch(BEER_PATH_ID, toUpdateDTO.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(valueMap)))
                .andExpect(status().isNoContent());

        verify(beerService).patchById(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());
        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(toUpdateDTO.getId());
        assertThat(beerArgumentCaptor.getValue().getBeerName()).isEqualTo(toUpdateDTO.getBeerName() + " patched");
    }
}