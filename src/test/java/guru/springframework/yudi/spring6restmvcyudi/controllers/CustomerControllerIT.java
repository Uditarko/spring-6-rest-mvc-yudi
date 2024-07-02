package guru.springframework.yudi.spring6restmvcyudi.controllers;

import guru.springframework.yudi.spring6restmvcyudi.entities.Customer;
import guru.springframework.yudi.spring6restmvcyudi.model.CustomerDTO;
import guru.springframework.yudi.spring6restmvcyudi.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
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


    @Test
    @Transactional
    @Rollback
    void testSavedNewCustomer() {
        ResponseEntity responseEntity = customerController.saveNewCustomer(CustomerDTO.builder().name("New Customer").build());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String id = responseEntity.getHeaders().getLocation().getPath().split("/")[4].strip();
        System.out.println(id);
        Optional<Customer> savedCustomer = customerRepository.findById(UUID.fromString(id));
        assertThat(savedCustomer.isPresent()).isTrue();
        assertThat(savedCustomer.get().getName()).isEqualTo("New Customer");
    }

    @Test
    @Transactional
    @Rollback
    void updateCustomerByIdWhenExists() {
        Customer customerToUpdate = customerRepository.findAll().getFirst();
        CustomerDTO inCustomerDto = CustomerDTO.builder()
                .name("Updated")
                .id(null)
                .version(null)
                .build();
        ResponseEntity responseEntity = customerController.updateCustomerById(customerToUpdate.getId(), inCustomerDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        /*Not required to fetch again as the test is Transactional*/
        assertThat(customerToUpdate.getName()).isEqualTo(inCustomerDto.getName());
    }

    @Test
    @Transactional
    @Rollback
    void updateCustomerByIdWhenNotExists() {
        customerRepository.deleteAll();
        CustomerDTO inCustomerDto = CustomerDTO.builder()
                .name("Updated")
                .id(null)
                .version(null)
                .build();
        assertThrows(NotFoundException.class, () -> customerController.updateCustomerById(UUID.randomUUID(), inCustomerDto));
    }

    @Test
    @Transactional
    @Rollback
    public void testDeleteByIdFound() {
        Customer customer = customerRepository.findAll().getFirst();
        ResponseEntity responseEntity = customerController.deleteCustomerById(customer.getId());
        assertThat(customerRepository.findById(customer.getId()).isPresent()).isFalse();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @Transactional
    @Rollback
    public void testDeleteByIdNotFound() {
        Customer customer = customerRepository.findAll().getFirst();
        customerRepository.deleteById(customer.getId());
        assertThrows(NotFoundException.class, () -> customerController.deleteCustomerById(customer.getId()));
    }

    @Test
    @Transactional
    @Rollback
    void testPatchCustomerByIdSuccess() {
        Customer existing = customerRepository.findAll().getFirst();
        CustomerDTO update = CustomerDTO.builder().name("Patched").build();
        ResponseEntity responseEntity = customerController.patchCustomerById(existing.getId(), update);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(existing.getName()).isEqualTo("Patched");
    }

    @Test
    @Transactional
    @Rollback
    void testPatchCustomerByIdNotFound(){
        customerRepository.deleteAll();
        CustomerDTO update = CustomerDTO.builder().name("Patched").build();
        assertThrows(NotFoundException.class, () -> customerController.patchCustomerById(UUID.randomUUID(), update));
    }
}