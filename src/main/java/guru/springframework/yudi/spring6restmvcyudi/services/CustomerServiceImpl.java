package guru.springframework.yudi.spring6restmvcyudi.services;

import guru.springframework.yudi.spring6restmvcyudi.model.CustomerDTO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService{

    private Map<UUID, CustomerDTO> customerMap;

    public CustomerServiceImpl() {
        CustomerDTO customerDTO1 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("Customer 1")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        CustomerDTO customerDTO2 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("Customer 2")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        CustomerDTO customerDTO3 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("Customer 3")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        customerMap = new HashMap<>();
        customerMap.put(customerDTO1.getId(), customerDTO1);
        customerMap.put(customerDTO2.getId(), customerDTO2);
        customerMap.put(customerDTO3.getId(), customerDTO3);
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        return Optional.of(customerMap.get(id));
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        return new ArrayList<>(customerMap.values());
    }

    /**
     * @param customerDTO
     * @return
     */
    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO customerDTO) {
        CustomerDTO savedNewCustomerDTO = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .name(customerDTO.getName())
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        customerMap.put(savedNewCustomerDTO.getId(), savedNewCustomerDTO);
        return savedNewCustomerDTO;
    }

    /**
     * @param id
     * @param customerDTO
     * @return
     */
    @Override
    public Optional<CustomerDTO> updateCustomerById(UUID id, CustomerDTO customerDTO) {
        Optional<CustomerDTO> existing = getCustomerById(id);
        if(existing.isPresent()) {
            existing.get().setName(customerDTO.getName());
            existing.get().setVersion(existing.get().getVersion() + 1);
            existing.get().setUpdateDate(LocalDateTime.now());
        }
        return existing;
    }

    /**
     * @param customerId
     * @return
     */
    @Override
    public Boolean deleteById(UUID customerId) {
        customerMap.remove(customerId);
        return true;
    }

    /**
     * @param customerId
     * @param customerDTO
     * @return
     */
    @Override
    public Optional<CustomerDTO> patchById(UUID customerId, CustomerDTO customerDTO) {
        Optional<CustomerDTO> existing = getCustomerById(customerId);
        if(existing.isPresent()) {
            if (StringUtils.hasText(customerDTO.getName())) {
                existing.get().setName(customerDTO.getName());
            }
        }
        return existing;
    }
}
