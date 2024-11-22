package guru.springframework.yudi.spring6restmvcyudi.bootstrap;

import guru.springframework.yudi.spring6restmvcyudi.entities.Beer;
import guru.springframework.yudi.spring6restmvcyudi.entities.Customer;
import guru.springframework.yudi.spring6restmvcyudi.mappers.BeerMapper;
import guru.springframework.yudi.spring6restmvcyudi.mappers.CustomerMapper;
import guru.springframework.yudi.spring6restmvcyudi.model.BeerCSVRecord;
import guru.springframework.yudi.spring6restmvcyudi.model.BeerDTO;
import guru.springframework.yudi.spring6restmvcyudi.model.BeerStyle;
import guru.springframework.yudi.spring6restmvcyudi.model.CustomerDTO;
import guru.springframework.yudi.spring6restmvcyudi.repositories.BeerRepository;
import guru.springframework.yudi.spring6restmvcyudi.repositories.CustomerRepository;
import guru.springframework.yudi.spring6restmvcyudi.services.BeerCsvService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static guru.springframework.yudi.spring6restmvcyudi.model.BeerStyle.IPA;
import static guru.springframework.yudi.spring6restmvcyudi.model.BeerStyle.PALE_ALE;

@Component
@AllArgsConstructor
public class BootStrapData implements CommandLineRunner {
    @Autowired
    BeerMapper beerMapper;

    @Autowired
    CustomerMapper customerMapper;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BeerCsvService beerCsvService;

    /**
     * @param args
     * @throws Exception
     */
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        beerRepository.saveAll(createBeers());
        loadCsvData();
        customerRepository.saveAll(createCustomers());
    }

    private void loadCsvData() throws FileNotFoundException {
        if(beerRepository.count()<25)
        {
            List<BeerCSVRecord> recs = beerCsvService.convertCSV(ResourceUtils.getFile("classpath:csvdata/beers.csv"));
            recs.forEach(beerCSVRecord -> {
                BeerStyle beerStyle = switch (beerCSVRecord.getStyle()) {
                    case "American Pale Lager" -> BeerStyle.LAGER;
                    case "American Pale Ale (APA)", "American Black Ale", "Belgian Dark Ale", "American Blonde Ale" ->
                            BeerStyle.ALE;
                    case "American IPA", "American Double / Imperial IPA", "Belgian IPA" -> BeerStyle.IPA;
                    case "American Porter" -> BeerStyle.PORTER;
                    case "Oatmeal Stout", "American Stout" -> BeerStyle.STOUT;
                    case "Saison / Farmhouse Ale" -> BeerStyle.SAISON;
                    case "Fruit / Vegetable Beer", "Winter Warmer", "Berliner Weissbier" -> BeerStyle.WHEAT;
                    case "English Pale Ale" -> BeerStyle.PALE_ALE;
                    default -> BeerStyle.PILSNER;
                };

                beerRepository.save(Beer.builder()
                        .beerName(StringUtils.abbreviate(beerCSVRecord.getBeer(), 50))
                        .beerStyle(beerStyle)
                        .price(BigDecimal.TEN)
                        .upc(beerCSVRecord.getRow().toString())
                        .quantityOnHand(beerCSVRecord.getCount())
                        .build());
            });
        }
    }

    List<Beer> createBeers() {
        BeerDTO beerDTO1 = BeerDTO.builder()
                .beerName("Galaxy Cat")
                .beerStyle(PALE_ALE)
                .upc("12356")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        BeerDTO beerDTO2 = BeerDTO.builder()
                .beerName("Crank")
                .beerStyle(PALE_ALE)
                .upc("12356222")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(392)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        BeerDTO beerDTO3 = BeerDTO.builder()
                .beerName("Sunshine City")
                .beerStyle(IPA)
                .upc("12356")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(144)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        ArrayList<Beer> beerEntities = new ArrayList<>();
        beerEntities.add(beerMapper.beerDtoToBeer(beerDTO1));
        beerEntities.add(beerMapper.beerDtoToBeer(beerDTO2));
        beerEntities.add(beerMapper.beerDtoToBeer(beerDTO3));

        return beerEntities;
    }

    List<Customer> createCustomers(){
        CustomerDTO customerDTO1 = CustomerDTO.builder()
                .name("Customer 1")
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        CustomerDTO customerDTO2 = CustomerDTO.builder()
                .name("Customer 2")
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        CustomerDTO customerDTO3 = CustomerDTO.builder()
                .name("Customer 3")
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        ArrayList<Customer> customerEntities = new ArrayList<>();
        customerEntities.add(customerMapper.customerDtoToCustomer(customerDTO1));
        customerEntities.add(customerMapper.customerDtoToCustomer(customerDTO2));
        customerEntities.add(customerMapper.customerDtoToCustomer(customerDTO3));

        return customerEntities;
    }
}
