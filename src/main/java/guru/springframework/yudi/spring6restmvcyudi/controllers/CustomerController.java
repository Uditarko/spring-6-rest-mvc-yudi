package guru.springframework.yudi.spring6restmvcyudi.controllers;

import guru.springframework.yudi.spring6restmvcyudi.model.Customer;
import guru.springframework.yudi.spring6restmvcyudi.services.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping(path = "/api/v1/customer")
public class CustomerController {
    private final CustomerService customerService;
    @RequestMapping(path = "/{customerId}", method = RequestMethod.GET)
    public Customer getCustomerById(@PathVariable("customerId") UUID id){
        log.debug("In CustomerController --> getCustomerById(UUID id)");
        return customerService.getCustomerById(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Customer> listCustomers(){
        return customerService.listCustomers();
    }
}
