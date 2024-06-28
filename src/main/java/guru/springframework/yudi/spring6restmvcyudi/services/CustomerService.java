package guru.springframework.yudi.spring6restmvcyudi.services;

import guru.springframework.yudi.spring6restmvcyudi.model.CustomerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    Optional<CustomerDTO> getCustomerById(UUID id);

    List<CustomerDTO> listCustomers();

    CustomerDTO saveNewCustomer(CustomerDTO customerDTO);

    void updateCustomerById(UUID id, CustomerDTO customerDTO);

    void deleteById(UUID customerId);

    void patchById(UUID customerId, CustomerDTO customerDTO);
}
