package guru.springframework.yudi.spring6restmvcyudi.services;

import guru.springframework.yudi.spring6restmvcyudi.model.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    Optional<Customer> getCustomerById(UUID id);

    List<Customer> listCustomers();

    Customer saveNewCustomer(Customer customer);

    void updateCustomerById(UUID id, Customer customer);

    void deleteById(UUID customerId);

    void patchById(UUID customerId, Customer customer);
}
