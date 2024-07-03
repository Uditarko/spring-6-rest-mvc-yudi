package guru.springframework.yudi.spring6restmvcyudi.repositories;

import guru.springframework.yudi.spring6restmvcyudi.entities.Beer;
import guru.springframework.yudi.spring6restmvcyudi.model.BeerStyle;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testSaveBeer() {
        Beer savedBeer = beerRepository.save(Beer.builder()
                .beerName("My Beer")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("9846573")
                .price(BigDecimal.TEN)
                .build());
        beerRepository.flush();
        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isNotNull();
    }

    @Test
    void testSaveBeerFail() {
        beerRepository.save(Beer.builder()
                .build());
        assertThrows(ConstraintViolationException.class, () -> beerRepository.flush());
    }

    @Test
    void testSaveBeerNameTooLong() {
        assertThrows(ConstraintViolationException.class,() -> {
            beerRepository.save(Beer.builder()
                    .beerName("My Beer aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
                    .beerStyle(BeerStyle.PALE_ALE)
                    .upc("9846573")
                    .price(BigDecimal.TEN)
                    .build());
            beerRepository.flush();
        });
    }

}