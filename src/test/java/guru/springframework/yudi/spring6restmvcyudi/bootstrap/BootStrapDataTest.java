package guru.springframework.yudi.spring6restmvcyudi.bootstrap;

import guru.springframework.yudi.spring6restmvcyudi.TestMapperConfiguration;
import guru.springframework.yudi.spring6restmvcyudi.mappers.BeerMapper;
import guru.springframework.yudi.spring6restmvcyudi.mappers.CustomerMapper;
import guru.springframework.yudi.spring6restmvcyudi.repositories.BeerRepository;
import guru.springframework.yudi.spring6restmvcyudi.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {TestMapperConfiguration.class})
class BootStrapDataTest {
    @Autowired
    BeerRepository beerRepository;

    @Autowired
    CustomerRepository customerRepository;

    BootStrapData bootStrapData;

    @Autowired
    CustomerMapper customerMapper;

    @Autowired
    BeerMapper beerMapper;

    @BeforeEach
    void setUp(){
        bootStrapData = new BootStrapData(beerMapper, customerMapper, beerRepository, customerRepository);
    }

    @Test
    void testRun() {
        assertThat(beerRepository.count()).isEqualTo(3);
        assertThat(customerRepository.count()).isEqualTo(3);

    }
}