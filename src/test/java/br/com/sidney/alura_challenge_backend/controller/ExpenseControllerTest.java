package br.com.sidney.alura_challenge_backend.controller;

import br.com.sidney.alura_challenge_backend.dto.ExpenseRequest;
import br.com.sidney.alura_challenge_backend.dto.ExpenseResponse;
import br.com.sidney.alura_challenge_backend.mocks.WithMockAdmin;
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
import org.springframework.test.web.servlet.MockMvc;

import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ExpenseController.class)
@WithMockAdmin
@DisplayName("Expense Controller REST Endpoint Testing With MockMvc")
class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExpenseService expenseService;
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final List<ExpenseResponse> expenses = new ArrayList<>();
    private static ExpenseRequest expenseRequest;

    @BeforeAll
    static void setup() {
        expenseRequest = ExpenseRequest.builder()
                .date("11/08/2022")
                .value("985.56")
                .description("Cleaning products")
                .description("Dwelling house")
                .build();

        ExpenseResponse expense = new ExpenseResponse();
        expense.setId("1");
        expense.setDate("30/08/2022");
        expense.setValue("65000.00");
        expense.setDescription("Fuel expense");
        expense.setCategory("Transport");
        expenses.add(expense);

        expense = new ExpenseResponse();
        expense.setId("2");
        expense.setDate("11/08/2022");
        expense.setValue("985.56");
        expense.setDescription("Cleaning products");
        expense.setCategory("Dwelling house");
        expenses.add(expense);

        expense = new ExpenseResponse();
        expense.setId("3");
        expense.setDate("11/08/2022");
        expense.setValue("1085.56");
        expense.setDescription("Cloud Microservices Course");
        expense.setCategory("Education");
        expenses.add(expense);

        expense = new ExpenseResponse();
        expense.setId("4");
        expense.setDate("11/12/2022");
        expense.setValue("345.00");
        expense.setDescription("Java Streams Course");
        expense.setCategory("Education");
        expenses.add(expense);
    }


    @Test
    @DisplayName("When save is successful then return the HTTP status code CREATED (201)")
    void whenExpenseValid_thenCreated() throws Exception {
        ExpenseResponse response = expenses.get(1);
        when(expenseService.register(any(ExpenseRequest.class))).thenReturn(response);
        mockMvc.perform(post("/api/v1/expenses")
                        .content(mapper.writeValueAsString(expenseRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.description", Matchers.equalTo("Cleaning products")))
                        .andExpect(jsonPath("$.category", Matchers.equalTo("Dwelling house")));
    }

    @Test
    @DisplayName("Shouldn't create a new expense and return the HTTP status code BAD REQUEST (400)")
    void whenDescriptionExpenseIsNull_thenTBadRequest() throws Exception {
       mockMvc.perform(post("/api/v1/expenses")
                        .content(mapper.writeValueAsString(expenseRequest.builder().description(null).build()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Shouldn't create a new expense and return the HTTP status code BAD REQUEST (400)")
    void whenValueExpenseIsNull_thenTBadRequest() throws Exception {
       mockMvc.perform(post("/api/v1/expenses")
                        .content(mapper.writeValueAsString(expenseRequest.builder().value(null).build()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Shouldn't create a new expense and return the HTTP status code BAD REQUEST (400)")
    void whenDateExpenseIsNull_thenTBadRequest() throws Exception {
       mockMvc.perform(post("/api/v1/expenses")
                        .content(mapper.writeValueAsString(expenseRequest.builder().date(null).build()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return the HTTP status code OK (200) and expense list")
    void getAll() throws Exception {
        when(expenseService.getAll(Optional.empty())).thenReturn(expenses);

        mockMvc.perform(get("/api/v1/expenses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(4)))
                .andExpect(jsonPath("$[0].description", Matchers.equalTo("Fuel expense")));
    }

    @Test
    @DisplayName("Should return the HTTP status code OK (200) and expense match list")
    void getAllByDescription() throws Exception {
        String param = "microservices";
        List<ExpenseResponse> filteredList = expenses.stream()
                .filter(expense -> expense.getDescription().toLowerCase().contains(param.toLowerCase()))
                .collect(Collectors.toList());
        when(expenseService.getAll(Optional.of(param))).thenReturn(filteredList);

        mockMvc.perform(get("/api/v1/expenses?description=microservices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].description", Matchers.equalTo("Cloud Microservices Course")));
    }

    @Test
    @DisplayName("Should return the HTTP status code OK (200) and expense by id")
    void findById() throws Exception {
        when(expenseService.findById("3")).thenReturn(Optional.of(expenses.get(2)));

        mockMvc.perform(get("/api/v1/expenses/{id}", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo("3")))
                .andExpect(jsonPath("$.description", Matchers.equalTo("Cloud Microservices Course")));
    }

    @Test
    @DisplayName("Should delete expense by id")
    void deleteById() throws Exception {
        doNothing().when(expenseService).delete("1");

        mockMvc.perform(delete("/api/v1/expenses/{id}", "1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should update expense")
    void update() throws Exception {
        expenseRequest = ExpenseRequest.builder()
                .description("BMW")
                .date("30/08/2022")
                .value("65000.00")
                .build();

        ExpenseResponse response = new ExpenseResponse();
        response.setId("1");
        response.setDescription("BMW");
        response.setDate("30/08/2022");
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

    @Test
    @DisplayName("Should return expense list for the same month")
    void findByMonth() throws Exception {
        Integer year = 2022, month = 12;

        List<ExpenseResponse> matchers = expenses.stream()
                .filter(expense ->
                        expense.getDate().substring(3, 5).contains(String.valueOf(Month.DECEMBER.getValue()))
                                && expense.getDate().substring(6).contains(Year.of(year).toString()))
                .collect(Collectors.toList());

        when(expenseService.findByMonth(year, month)).thenReturn(matchers);
        mockMvc.perform(get("/api/v1/expenses/{year}/{month}", year, month))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)));
    }

    @Test
    @DisplayName("Should return the HTTP status code OK (400)")
    void whenSaveWithIncorrectDate_thenBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/expenses")
                        .content(mapper.writeValueAsString(ExpenseRequest.builder()
                                .description("iFood")
                                .value("95.50")
                                .date("22/08/22").build()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
    }
}