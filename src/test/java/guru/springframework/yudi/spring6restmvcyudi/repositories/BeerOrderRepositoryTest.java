package guru.springframework.yudi.spring6restmvcyudi.repositories;

import guru.springframework.yudi.spring6restmvcyudi.entities.BeerOrder;
import guru.springframework.yudi.spring6restmvcyudi.entities.Customer;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BeerOrderRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BeerOrderRepository beerOrderRepository;

    Customer testCustomer;

    @BeforeEach
    void setUp(){
        testCustomer = customerRepository.findAll().getFirst();
    }

    @Test
    @Transactional
    void testAssociation(){
        BeerOrder testOrder = BeerOrder.builder().customer(testCustomer).customerRef("Test Customer").build();
        testOrder =beerOrderRepository.save(testOrder);
        assertThat(testOrder.getCustomer().getName()).isEqualTo(testCustomer.getName());
    }
}