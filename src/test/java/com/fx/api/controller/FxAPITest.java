package com.fx.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fx.api.service.FxService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(FxAPI.class)
class FxAPITest {

  @Autowired private MockMvc mockMvc;

  @MockBean private FxService fxService;

  @MockBean protected MockHttpSession session;

  @Test
  void testGetExchangeRate() throws Exception {
    String jsonResponse = "{\"data\":{\"USD\":{\"value\":1.2}}}";
    when(fxService.getExchangeRates(any(List.class), anyString()))
        .thenReturn(Mono.just(jsonResponse));

    mockMvc
        .perform(
            get("/exchange-api/exchange-rate")
                .param("requiredCurrencyCodeList", "USD")
                .param("currencyCode", "EUR")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json("{\"USD\":{\"value\":1.2}}"));
  }

  @Test
  void testConvert() throws Exception {
    when(fxService.convertRates(anyString(), anyString(), any(Double.class))).thenReturn(120.0);

    mockMvc
        .perform(
            get("/exchange-api/convert")
                .param("requiredCurrencyCode", "USD")
                .param("currencyCode", "EUR")
                .param("amount", "100")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string("120.0"));
  }

  @Test
  void testGetExchangeRate_InvalidInput() throws Exception {

    mockMvc
        .perform(
            get("/exchange-api/exchange-rate")
                .param("requiredCurrencyCodeList", "INVALID")
                .param("currencyCode", "EUR")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testConvert_InvalidInput() throws Exception {

    mockMvc
        .perform(
            get("/exchange-api/convert")
                .param("requiredCurrencyCode", "INVALID")
                .param("currencyCode", "EUR")
                .param("amount", "100")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }
}
