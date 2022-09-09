package br.com.sidney.alura_challenge_backend.controller;

import br.com.sidney.alura_challenge_backend.dto.CategoryResponse;
import br.com.sidney.alura_challenge_backend.dto.ResumeResponse;
import br.com.sidney.alura_challenge_backend.enums.Category;
import br.com.sidney.alura_challenge_backend.mocks.WithMockAdmin;
import br.com.sidney.alura_challenge_backend.service.ResumeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ResumeController.class)
@WithMockAdmin
@ActiveProfiles("dev")
@DisplayName("Resume Controller REST Endpoint Testing With MockMvc")

class ResumeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ResumeService service;

    @Test
    @DisplayName("Should return a resume of year month")
    void whenGetResume_thenResumeMonth() throws Exception {
        List<CategoryResponse> totalByCategory = List.of(
            new CategoryResponse(Category.FOOD, BigDecimal.valueOf(1250.0)),
            new CategoryResponse(Category.HEALTH, BigDecimal.valueOf(350.0)),
            new CategoryResponse(Category.EDUCATION, BigDecimal.valueOf(2000.0)),
            new CategoryResponse(Category.OTHERS, BigDecimal.valueOf(8900.0))
        );

        BigDecimal amountIncome = BigDecimal.valueOf(8000.0);
        BigDecimal amountExpense = BigDecimal.valueOf(4490.0);
        BigDecimal endBalance = amountIncome.subtract(amountExpense);

        String year = "2022", month = "08";

        ResumeResponse resume = ResumeResponse.builder()
                .amountIncome(amountIncome)
                .amountExpense(amountExpense)
                .endBalance(endBalance)
                .totalByCategory(totalByCategory)
                .build();

        when(service.getResume(any(Integer.class), any(Integer.class))).thenReturn(resume);

        mockMvc
                .perform(get("/api/v1/resume/{year}/{month}", year, month)
                .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("SUPER_USER"))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
    }
}