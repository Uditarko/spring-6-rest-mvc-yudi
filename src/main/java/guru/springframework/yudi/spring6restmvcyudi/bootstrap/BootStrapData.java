package guru.springframework.yudi.spring6restmvcyudi.bootstrap;

import guru.springframework.yudi.spring6restmvcyudi.entities.Beer;
import guru.springframework.yudi.spring6restmvcyudi.entities.Customer;
import guru.springframework.yudi.spring6restmvcyudi.mappers.BeerMapper;
import guru.springframework.yudi.spring6restmvcyudi.mappers.CustomerMapper;
import guru.springframework.yudi.spring6restmvcyudi.model.BeerDTO;
import guru.springframework.yudi.spring6restmvcyudi.model.CustomerDTO;
import guru.springframework.yudi.spring6restmvcyudi.repositories.BeerRepository;
import guru.springframework.yudi.spring6restmvcyudi.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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

    /**
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        beerRepository.saveAll(createBeers());
        customerRepository.saveAll(createCustomers());
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
