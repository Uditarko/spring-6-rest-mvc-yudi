package guru.springframework.yudi.spring6restmvcyudi.controllers;

import guru.springframework.yudi.spring6restmvcyudi.model.Customer;
import guru.springframework.yudi.spring6restmvcyudi.services.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CustomerController {
    public static final String PATH_CUSTOMER = "/api/v1/customer";
    public static final String PATH_CUSTOMER_ID = PATH_CUSTOMER + "/{customerId}";
    private final CustomerService customerService;

    @RequestMapping(path = PATH_CUSTOMER_ID, method = RequestMethod.GET)
    public Customer getCustomerById(@PathVariable("customerId") UUID id) {
        log.debug("In CustomerController --> getCustomerById(UUID id)");
        return customerService.getCustomerById(id);
    }

    @RequestMapping(path = PATH_CUSTOMER, method = RequestMethod.GET)
    public List<Customer> listCustomers() {
        return customerService.listCustomers();
    }

    @PostMapping(path = PATH_CUSTOMER)
    public ResponseEntity saveNewCustomer(@RequestBody Customer customer) {
        Customer savedNewCustomer = customerService.saveNewCustomer(customer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/customer/" + savedNewCustomer.getId().toString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping(path = PATH_CUSTOMER_ID)
    public ResponseEntity updateCustomerById(@PathVariable("customerId") UUID customerId, @RequestBody Customer customer) {
        customerService.updateCustomerById(customerId, customer);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = PATH_CUSTOMER_ID)
    public ResponseEntity deleteCustomerById(@PathVariable("customerId") UUID customerId) {
        customerService.deleteById(customerId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(path = PATH_CUSTOMER_ID)
    public ResponseEntity patchCustomerById(@PathVariable("customerId") UUID customerId, @RequestBody Customer customer) {
        customerService.patchById(customerId, customer);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
