package guru.springframework.yudi.spring6restmvcyudi.services;

import guru.springframework.yudi.spring6restmvcyudi.model.BeerCSVRecord;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BeerCsvServiceImplTest {
    BeerCsvService beerCsvService = new BeerCsvServiceImpl();
    @Test
    void testConvertCSV() throws FileNotFoundException {
        List<BeerCSVRecord> recs = beerCsvService.convertCSV(ResourceUtils.getFile("classpath:csvdata/beers.csv"));
        System.out.println(recs.size());
        assertThat(recs.size()).isGreaterThan(0);
    }

}