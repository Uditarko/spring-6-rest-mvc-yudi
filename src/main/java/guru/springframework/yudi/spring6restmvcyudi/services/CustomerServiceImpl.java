package guru.springframework.yudi.spring6restmvcyudi.services;

import guru.springframework.yudi.spring6restmvcyudi.model.Customer;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService{

    private Map<UUID, Customer> customerMap;

    public CustomerServiceImpl() {
        Customer customer1 = Customer.builder()
                .id(UUID.randomUUID())
                .name("Customer 1")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Customer customer2 = Customer.builder()
                .id(UUID.randomUUID())
                .name("Customer 2")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Customer customer3 = Customer.builder()
                .id(UUID.randomUUID())
                .name("Customer 3")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        customerMap = new HashMap<>();
        customerMap.put(customer1.getId(), customer1);
        customerMap.put(customer2.getId(), customer2);
        customerMap.put(customer3.getId(), customer3);
    }

    @Override
    public Optional<Customer> getCustomerById(UUID id) {
        return Optional.of(customerMap.get(id));
    }

    @Override
    public List<Customer> listCustomers() {
        return new ArrayList<>(customerMap.values());
    }

    /**
     * @param customer 
     * @return
     */
    @Override
    public Customer saveNewCustomer(Customer customer) {
        Customer savedNewCustomer = Customer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .name(customer.getName())
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        customerMap.put(savedNewCustomer.getId(),savedNewCustomer);
        return savedNewCustomer;
    }

    /**
     * @param id
     * @param customer
     */
    @Override
    public void updateCustomerById(UUID id, Customer customer) {
        Optional<Customer> existing = getCustomerById(id);
        if(existing.isPresent()) {
            existing.get().setName(customer.getName());
            existing.get().setVersion(existing.get().getVersion() + 1);
            existing.get().setUpdateDate(LocalDateTime.now());
        }
    }

    /**
     * @param customerId
     */
    @Override
    public void deleteById(UUID customerId) {
        customerMap.remove(customerId);
    }

    /**
     * @param customerId
     * @param customer
     */
    @Override
    public void patchById(UUID customerId, Customer customer) {
        Optional<Customer> existing = getCustomerById(customerId);
        if(existing.isPresent()) {
            if (StringUtils.hasText(customer.getName())) {
                existing.get().setName(customer.getName());
            }
        }
    }
}
