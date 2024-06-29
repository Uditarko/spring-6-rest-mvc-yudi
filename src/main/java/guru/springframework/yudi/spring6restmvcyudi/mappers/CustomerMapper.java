package guru.springframework.yudi.spring6restmvcyudi.mappers;

import guru.springframework.yudi.spring6restmvcyudi.entities.Customer;
import guru.springframework.yudi.spring6restmvcyudi.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {
    Customer customerDtoToCustomer(CustomerDTO dto);
    CustomerDTO customerToCustomerDto(Customer customer);
}
