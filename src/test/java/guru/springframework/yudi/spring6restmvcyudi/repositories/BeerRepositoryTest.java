package guru.springframework.yudi.spring6restmvcyudi.repositories;

import guru.springframework.yudi.spring6restmvcyudi.bootstrap.BootStrapData;
import guru.springframework.yudi.spring6restmvcyudi.entities.Beer;
import guru.springframework.yudi.spring6restmvcyudi.mappers.BeerMapperImpl;
import guru.springframework.yudi.spring6restmvcyudi.mappers.CustomerMapperImpl;
import guru.springframework.yudi.spring6restmvcyudi.model.BeerStyle;
import guru.springframework.yudi.spring6restmvcyudi.services.BeerCsvServiceImpl;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Import({BootStrapData.class, BeerCsvServiceImpl.class, BeerMapperImpl.class, CustomerMapperImpl.class})
class BeerRepositoryTest {

    public static final String BEER_NAME = "%IPA%";
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
        assertThrows(ConstraintViolationException.class, () -> {
            beerRepository.save(Beer.builder()
                    .beerName("My Beer aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
                    .beerStyle(BeerStyle.PALE_ALE)
                    .upc("9846573")
                    .price(BigDecimal.TEN)
                    .build());
            beerRepository.flush();
        });
    }

    @Test
    void testFindAllByBeerNameIsLikeIgnoreCase() {
        List<Beer> list = beerRepository.findAllByBeerNameIsLikeIgnoreCase(BEER_NAME);

        assertThat(list.size()).isEqualTo(336);
    }

    @Test
    void testFindAllByBeerStyle() {
        List<Beer> list = beerRepository.findAllByBeerStyle(BeerStyle.IPA);

        assertThat(list.size()).isEqualTo(548);
    }

    @Test
    void testFindAllByBeerNameIsLikeIgnoreCaseAndBeerStyle(){
        List<Beer> list = beerRepository.findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle(BEER_NAME, BeerStyle.IPA);
        assertThat(list.size()).isEqualTo(310);
    }
}