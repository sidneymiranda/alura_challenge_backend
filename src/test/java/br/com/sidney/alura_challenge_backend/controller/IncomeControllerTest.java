package br.com.sidney.alura_challenge_backend.controller;

import br.com.sidney.alura_challenge_backend.dto.IncomeRequest;
import br.com.sidney.alura_challenge_backend.dto.IncomeResponse;
import br.com.sidney.alura_challenge_backend.service.IncomeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = {IncomeController.class})
@DisplayName("Income Controller REST Endpoint Testing With MockMvc")
class IncomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IncomeService incomeService;
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final List<IncomeResponse> incomes = new ArrayList<>();
    private static IncomeRequest incomeRequest;

    @BeforeAll
    static void setup() {
        incomeRequest = IncomeRequest.builder()
                .date("11/08/22 12:08")
                .value("985.56")
                .description("Salary")
                .build();

        IncomeResponse income = new IncomeResponse();
        income.setId("1");
        income.setDate("30/08/22 10:08");
        income.setValue("65000.00");
        income.setDescription("Car sold");
        incomes.add(income);

        income = new IncomeResponse();
        income.setId("2");
        income.setDate("11/08/22 12:08");
        income.setValue("985.56");
        income.setDescription("Salary");
        incomes.add(income);

        income = new IncomeResponse();
        income.setId("3");
        income.setDate("11/08/22 12:08");
        income.setValue("1085.56");
        income.setDescription("Service provided");
        incomes.add(income);

        income = new IncomeResponse();
        income.setId("2");
        income.setDate("71/11/22 11:08");
        income.setValue("2985.56");
        income.setDescription("Salary");
        incomes.add(income);
    }


    @Test
    @DisplayName("When save is successful then return the HTTP status code CREATED (201)")
    void register() throws Exception {
        IncomeResponse response = incomes.get(1);
        when(incomeService.register(any(IncomeRequest.class))).thenReturn(response);
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/incomes")
                        .content(mapper.writeValueAsString(incomeRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.description", Matchers.equalTo("Salary")))
                .andReturn();
        String location = mvcResult.getResponse().getHeader("location");
        assertNotNull(location);
    }

    @Test
    @DisplayName("Shouldn't create a new income and return the HTTP status code BAD REQUEST (400)")
    void whenDescriptionIncomeIsNull_thenTBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/incomes")
                        .content(mapper.writeValueAsString(incomeRequest.builder().description(null).build()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Shouldn't create a new income and return the HTTP status code BAD REQUEST (400)")
    void whenValueIncomeIsNull_thenTBadRequest() throws Exception {
       mockMvc.perform(post("/api/v1/incomes")
                        .content(mapper.writeValueAsString(incomeRequest.builder().value(null).build()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Shouldn't create a new income and return the HTTP status code BAD REQUEST (400)")
    void whenDateIncomeIsNull_thenTBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/incomes")
                        .content(mapper.writeValueAsString(incomeRequest.builder().date(null).build()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return the HTTP status code OK (200) and income match list")
    void getAllByDescription() throws Exception {
        String param = "salary";
        List<IncomeResponse> filteredList = incomes.stream()
                .filter(income -> income.getDescription().toLowerCase().contains(param.toLowerCase()))
                .collect(Collectors.toList());

        when(incomeService.getAll(Optional.of(param))).thenReturn(filteredList);

        mockMvc.perform(get("/api/v1/incomes?description=salary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)));
    }

    @Test
    @DisplayName("Should return the HTTP status code OK (200) and income list")
    void getAll() throws Exception {
        when(incomeService.getAll(Optional.empty())).thenReturn(incomes);

        mockMvc.perform(get("/api/v1/incomes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(4)))
                .andExpect(jsonPath("$[0].description", Matchers.equalTo("Car sold")));
    }

    @Test
    @DisplayName("Should return the HTTP status code OK (200) and income by id")
    void findById() throws Exception {
        when(incomeService.findById("3")).thenReturn(Optional.of(incomes.get(2)));

        mockMvc.perform(get("/api/v1/incomes/{id}", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo("3")))
                .andExpect(jsonPath("$.description", Matchers.equalTo("Service provided")));
    }

    @Test
    @DisplayName("Should delete income by id")
    void deleteById() throws Exception {
        doNothing().when(incomeService).delete("1");

        mockMvc.perform(delete("/api/v1/incomes/{id}", "1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should update income")
    void update() throws Exception {
        incomeRequest = IncomeRequest.builder()
                .description("BMW")
                .date("30/08/22 10:08")
                .value("65000.00")
                .build();

        IncomeResponse response = new IncomeResponse();
        response.setId("1");
        response.setDescription("BMW");
        response.setDate("30/08/22 10:08");
        response.setValue("65000.00");

        when(incomeService.update("1", incomeRequest)).thenReturn(response);

        mockMvc.perform(put("/api/v1/incomes/{id}", "1")
                .content(mapper.writeValueAsString(incomeRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo("1")))
                .andExpect(jsonPath("$.value", Matchers.equalTo("65000.00")))
                .andExpect(jsonPath("$.description", Matchers.equalTo("BMW")));
    }
}