package guru.springframework.yudi.spring6restmvcyudi.services;

import guru.springframework.yudi.spring6restmvcyudi.model.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    Customer getCustomerById(UUID id);

    List<Customer> listCustomers();
}
