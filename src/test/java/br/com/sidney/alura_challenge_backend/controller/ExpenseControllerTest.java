package br.com.sidney.alura_challenge_backend.controller;

import br.com.sidney.alura_challenge_backend.dto.ExpenseRequest;
import br.com.sidney.alura_challenge_backend.dto.ExpenseResponse;
import br.com.sidney.alura_challenge_backend.service.ExpenseService;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = {ExpenseController.class})
@DisplayName("Expense Controller REST Endpoint Testing With MockMvc")
class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExpenseService expenseService;
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final List<ExpenseResponse> incomes = new ArrayList<>();
    private static ExpenseRequest expenseRequest;

    @BeforeAll
    static void setup() {
        expenseRequest = ExpenseRequest.builder()
                .date("11/08/22 12:08")
                .value("985.56")
                .description("Cleaning products")
                .build();

        ExpenseResponse expense = new ExpenseResponse();
        expense.setId("1");
        expense.setDate("30/08/22 10:08");
        expense.setValue("65000.00");
        expense.setDescription("Fuel expense");
        expense.setCategory("Transport");
        incomes.add(expense);

        expense = new ExpenseResponse();
        expense.setId("2");
        expense.setDate("11/08/22 12:08");
        expense.setValue("985.56");
        expense.setDescription("Cleaning products");
        expense.setCategory("Dwelling house");
        incomes.add(expense);

        expense = new ExpenseResponse();
        expense.setId("3");
        expense.setDate("11/08/22 12:08");
        expense.setValue("1085.56");
        expense.setDescription("Cloud Microservices Course");
        expense.setCategory("Education");
        incomes.add(expense);
    }


    @Test
    @DisplayName("When save is successful then return the HTTP status code CREATED (201)")
    void whenExpenseValid_thenCreated() throws Exception {
        ExpenseResponse response = incomes.get(1);
        when(expenseService.register(any(ExpenseRequest.class))).thenReturn(response);
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/expenses")
                        .content(mapper.writeValueAsString(expenseRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.description", Matchers.equalTo("Cleaning products")))
                        .andExpect(jsonPath("$.category", Matchers.equalTo("Dwelling house")))
                .andReturn();
        String location = mvcResult.getResponse().getHeader("location");
        assertNotNull(location);
    }

    @Test
    @DisplayName("Shouldn't create a new income and return the HTTP status code BAD REQUEST (400)")
    void whenDescriptionExpenseIsNull_thenTBadRequest() throws Exception {
       mockMvc.perform(post("/api/v1/expenses")
                        .content(mapper.writeValueAsString(expenseRequest.builder().description(null).build()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();
    }

    @Test
    @DisplayName("Shouldn't create a new income and return the HTTP status code BAD REQUEST (400)")
    void whenValueExpenseIsNull_thenTBadRequest() throws Exception {
       mockMvc.perform(post("/api/v1/expenses")
                        .content(mapper.writeValueAsString(expenseRequest.builder().value(null).build()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();
    }

    @Test
    @DisplayName("Shouldn't create a new income and return the HTTP status code BAD REQUEST (400)")
    void whenDateExpenseIsNull_thenTBadRequest() throws Exception {
       mockMvc.perform(post("/api/v1/expenses")
                        .content(mapper.writeValueAsString(expenseRequest.builder().date(null).build()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();
    }

    @Test
    @DisplayName("Should return the HTTP status code OK (200) and income list")
    void getAll() throws Exception {
        when(expenseService.getAll()).thenReturn(incomes);

        mockMvc.perform(get("/api/v1/expenses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[0].description", Matchers.equalTo("Fuel expense")));
    }

    @Test
    @DisplayName("Should return the HTTP status code OK (200) and income by id")
    void findById() throws Exception {
        when(expenseService.findById("3")).thenReturn(Optional.of(incomes.get(2)));

        mockMvc.perform(get("/api/v1/expenses/{id}", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo("3")))
                .andExpect(jsonPath("$.description", Matchers.equalTo("Cloud Microservices Course")));
    }

    @Test
    @DisplayName("Should delete income by id")
    void deleteById() throws Exception {
        doNothing().when(expenseService).delete("1");

        mockMvc.perform(delete("/api/v1/expenses/{id}", "1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should update income")
    void update() throws Exception {
        expenseRequest = ExpenseRequest.builder()
                .description("BMW")
                .date("30/08/22 10:08")
                .value("65000.00")
                .build();

        ExpenseResponse response = new ExpenseResponse();
        response.setId("1");
        response.setDescription("BMW");
        response.setDate("30/08/22 10:08");
        response.setValue("65000.00");

        when(expenseService.update("1", expenseRequest)).thenReturn(response);

        mockMvc.perform(put("/api/v1/expenses/{id}", "1")
                .content(mapper.writeValueAsString(expenseRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo("1")))
                .andExpect(jsonPath("$.value", Matchers.equalTo("65000.00")))
                .andExpect(jsonPath("$.description", Matchers.equalTo("BMW")));
    }
}