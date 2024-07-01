package guru.springframework.yudi.spring6restmvcyudi.controllers;

import guru.springframework.yudi.spring6restmvcyudi.entities.Beer;
import guru.springframework.yudi.spring6restmvcyudi.model.BeerDTO;
import guru.springframework.yudi.spring6restmvcyudi.model.BeerStyle;
import guru.springframework.yudi.spring6restmvcyudi.repositories.BeerRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@SpringBootTest
/* Will not wire up web context but will wire up everything else opposite to test splices*/
class BeerControllerIT {

    @Autowired
    BeerController beerController;

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testListBeers() {
        List<BeerDTO> beers = beerController.listBeers();
        assertThat(beers.size()).isEqualTo(3);
    }

    @Rollback
    @Transactional
    @Test
    void testEmptyListBeers() {
        beerRepository.deleteAll();
        List<BeerDTO> beers = beerController.listBeers();
        assertThat(beers.size()).isEqualTo(0);
    }

    @Test
    void getBeerByIdFound() {
        BeerDTO foundBeer  = beerController.getBeerById(beerRepository.findAll().getFirst().getId());
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
}