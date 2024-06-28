package guru.springframework.yudi.spring6restmvcyudi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.yudi.spring6restmvcyudi.model.Customer;
import guru.springframework.yudi.spring6restmvcyudi.services.CustomerService;
import guru.springframework.yudi.spring6restmvcyudi.services.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static guru.springframework.yudi.spring6restmvcyudi.controllers.CustomerController.PATH_CUSTOMER;
import static guru.springframework.yudi.spring6restmvcyudi.controllers.CustomerController.PATH_CUSTOMER_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CustomerService customerService;

    @Autowired
    ObjectMapper objectMapper;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<Customer> customerArgumentCaptor;

    CustomerServiceImpl customerServiceImpl;

    @BeforeEach
    void setUp() {
        customerServiceImpl = new CustomerServiceImpl();
    }

    @Test
    void getCustomerById() throws Exception {
        Customer testCustomer = customerServiceImpl.listCustomers().getFirst();

        given(customerService.getCustomerById(any(UUID.class))).willReturn(testCustomer);

        mockMvc.perform(get(PATH_CUSTOMER_ID, testCustomer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testCustomer.getId().toString())))
                .andExpect(jsonPath("$.name", is(testCustomer.getName())));
    }

    @Test
    void getCustomerByIdNotFound() throws Exception {
        given(customerService.getCustomerById(any(UUID.class))).willThrow(NotFoundException.class);

        mockMvc.perform(get(PATH_CUSTOMER_ID, UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testListCustomers() throws Exception {
        given(customerService.listCustomers()).willReturn(customerServiceImpl.listCustomers());

        mockMvc.perform(get(PATH_CUSTOMER).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(customerServiceImpl.listCustomers().size())));
    }

    @Test
    void testSaveNewCustomer() throws Exception {
        Customer customer = customerServiceImpl.listCustomers().getFirst();
        customer.setId(null);
        customer.setVersion(null);
        customer.setUpdateDate(null);
        customer.setCreatedDate(null);

        given(customerService.saveNewCustomer(customer)).willReturn(customerServiceImpl.listCustomers().get(1));

        mockMvc.perform(post(PATH_CUSTOMER)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void testUpdateCustomerById() throws Exception {
        Customer customer = customerServiceImpl.listCustomers().getFirst();

        mockMvc.perform(put(PATH_CUSTOMER_ID, customer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isNoContent());

        verify(customerService).updateCustomerById(customer.getId(), customer);
    }

    @Test
    void testDeleteCustomerById() throws Exception {
        Customer customer = customerServiceImpl.listCustomers().getFirst();

        mockMvc.perform(delete(PATH_CUSTOMER_ID, customer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(customerService).deleteById(uuidArgumentCaptor.capture());
        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(customer.getId());
    }

    @Test
    void testPatchCustomerById() throws Exception {
        Customer toUpdate = customerServiceImpl.listCustomers().getFirst();

        Map<String, String> updateMap = new HashMap<>();
        updateMap.put("name", toUpdate.getName() + "patched");

        mockMvc.perform(patch(PATH_CUSTOMER_ID, toUpdate.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateMap)))
                .andExpect(status().isNoContent());

        verify(customerService).patchById(uuidArgumentCaptor.capture(), customerArgumentCaptor.capture());
        assertThat(uuidArgumentCaptor.getValue().toString()).isEqualTo(toUpdate.getId().toString());
        assertThat(customerArgumentCaptor.getValue().getName()).isEqualTo(toUpdate.getName() + "patched");
    }

}