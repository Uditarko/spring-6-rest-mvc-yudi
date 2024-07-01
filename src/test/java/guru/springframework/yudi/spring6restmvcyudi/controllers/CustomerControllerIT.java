package guru.springframework.yudi.spring6restmvcyudi.controllers;

import guru.springframework.yudi.spring6restmvcyudi.model.CustomerDTO;
import guru.springframework.yudi.spring6restmvcyudi.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CustomerControllerIT {

    @Autowired
    CustomerController customerController;

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void testListCustomers() {
        List<CustomerDTO> customerDTOS = customerController.listCustomers();
        assertThat(customerDTOS.size()).isEqualTo(customerRepository.count());
    }

    @Test
    @Transactional
    @Rollback
    void testEmptyListCustomers() {
        customerRepository.deleteAll();
        List<CustomerDTO> customerDTOS = customerController.listCustomers();
        assertThat(customerDTOS.size()).isEqualTo(0);
    }

    @Test
    void testGetCustomerByIdFound() {
        CustomerDTO found = customerController.getCustomerById(customerRepository.findAll().getFirst().getId());
        assertThat(found).isNotNull();
        assertThat(found.getId()).isNotNull();
        assertThat(found.getVersion()).isNotNull();
    }

    @Test
    @Transactional
    @Rollback
    void testGetCustomerByIdNotFound() {
        customerRepository.deleteAll();
        assertThrows(NotFoundException.class, () -> customerController.getCustomerById(UUID.randomUUID()));
    }
}