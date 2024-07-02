package guru.springframework.yudi.spring6restmvcyudi.controllers;

import guru.springframework.yudi.spring6restmvcyudi.model.CustomerDTO;
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
    public CustomerDTO getCustomerById(@PathVariable("customerId") UUID id) {
        log.debug("In CustomerController --> getCustomerById(UUID id)");
        return customerService.getCustomerById(id).orElseThrow(NotFoundException::new);
    }

    @RequestMapping(path = PATH_CUSTOMER, method = RequestMethod.GET)
    public List<CustomerDTO> listCustomers() {
        return customerService.listCustomers();
    }

    @PostMapping(path = PATH_CUSTOMER)
    public ResponseEntity saveNewCustomer(@RequestBody CustomerDTO customerDTO) {
        CustomerDTO savedNewCustomerDTO = customerService.saveNewCustomer(customerDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/customer/" + savedNewCustomerDTO.getId().toString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping(path = PATH_CUSTOMER_ID)
    public ResponseEntity updateCustomerById(@PathVariable("customerId") UUID customerId, @RequestBody CustomerDTO customerDTO) {
        if(customerService.updateCustomerById(customerId, customerDTO).isEmpty()){
            throw new NotFoundException();
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = PATH_CUSTOMER_ID)
    public ResponseEntity deleteCustomerById(@PathVariable("customerId") UUID customerId) {
        if(!customerService.deleteById(customerId)){
            throw new NotFoundException();
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(path = PATH_CUSTOMER_ID)
    public ResponseEntity patchCustomerById(@PathVariable("customerId") UUID customerId, @RequestBody CustomerDTO customerDTO) {
        if(customerService.patchById(customerId, customerDTO).isEmpty()){
            throw new NotFoundException();
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
