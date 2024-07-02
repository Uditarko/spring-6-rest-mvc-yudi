package guru.springframework.yudi.spring6restmvcyudi.services;

import guru.springframework.yudi.spring6restmvcyudi.mappers.CustomerMapper;
import guru.springframework.yudi.spring6restmvcyudi.model.CustomerDTO;
import guru.springframework.yudi.spring6restmvcyudi.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class CustomerServiceJPA implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    /**
     * @param id
     * @return
     */
    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        return Optional.ofNullable(customerMapper.customerToCustomerDto(
                customerRepository.findById(id).orElse(null)));
    }

    /**
     * @return
     */
    @Override
    public List<CustomerDTO> listCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::customerToCustomerDto)
                .collect(Collectors.toList());
    }

    /**
     * @param customerDTO
     * @return
     */
    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO customerDTO) {
        return customerMapper.customerToCustomerDto(
                customerRepository.save(customerMapper.customerDtoToCustomer(customerDTO)));
    }

    /**
     * @param id
     * @param customerDTO
     * @return
     */
    @Override
    public Optional<CustomerDTO> updateCustomerById(UUID id, CustomerDTO customerDTO) {
        AtomicReference<Optional<CustomerDTO>> returnCustomer = new AtomicReference<>();
        customerRepository.findById(id).ifPresentOrElse(
                (elem) -> {
                    elem.setName(customerDTO.getName());
                    elem.setUpdateDate(LocalDateTime.now());
                    returnCustomer.set(Optional.of(customerMapper.customerToCustomerDto(customerRepository.save(elem))));
                },
                () -> {
                    returnCustomer.set(Optional.empty());
                }
        );
        return returnCustomer.get();
    }

    /**
     * @param customerId
     * @return
     */
    @Override
    public Boolean deleteById(UUID customerId) {
        if (customerRepository.existsById(customerId)) {
            customerRepository.deleteById(customerId);
            return true;
        } else return false;
    }

    /**
     * @param customerId
     * @param customerDTO
     * @return
     */
    @Override
    public Optional<CustomerDTO> patchById(UUID customerId, CustomerDTO customerDTO) {
        AtomicReference<Optional<CustomerDTO>> returnCustomer = new AtomicReference<>();
        customerRepository.findById(customerId).ifPresentOrElse(
                (elem) -> {
                    if (StringUtils.hasText(customerDTO.getName())) {
                        elem.setName(customerDTO.getName());
                    }
                    returnCustomer.set(Optional.of(customerMapper.customerToCustomerDto(customerRepository.save(elem))));
                },
                () -> {
                    returnCustomer.set(Optional.empty());
                }
        );
        return returnCustomer.get();
    }
}
