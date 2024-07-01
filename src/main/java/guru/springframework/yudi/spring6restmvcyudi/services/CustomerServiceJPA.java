package guru.springframework.yudi.spring6restmvcyudi.services;

import guru.springframework.yudi.spring6restmvcyudi.mappers.CustomerMapper;
import guru.springframework.yudi.spring6restmvcyudi.model.CustomerDTO;
import guru.springframework.yudi.spring6restmvcyudi.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class CustomerServiceJPA implements CustomerService{

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    /**
     * @param id 
     * @return
     */
    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        return Optional.empty();
    }

    /**
     * @return 
     */
    @Override
    public List<CustomerDTO> listCustomers() {
        return List.of();
    }

    /**
     * @param customerDTO 
     * @return
     */
    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO customerDTO) {
        return null;
    }

    /**
     * @param id 
     * @param customerDTO
     */
    @Override
    public void updateCustomerById(UUID id, CustomerDTO customerDTO) {

    }

    /**
     * @param customerId 
     */
    @Override
    public void deleteById(UUID customerId) {

    }

    /**
     * @param customerId 
     * @param customerDTO
     */
    @Override
    public void patchById(UUID customerId, CustomerDTO customerDTO) {

    }
}
