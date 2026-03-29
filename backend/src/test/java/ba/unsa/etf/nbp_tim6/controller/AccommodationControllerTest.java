package ba.unsa.etf.nbp_tim6.controller;

import ba.unsa.etf.nbp_tim6.model.Accommodation;
import ba.unsa.etf.nbp_tim6.service.abstraction.AccommodationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccommodationController.class)
public class AccommodationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccommodationService accommodationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllAccommodations() throws Exception {
        when(accommodationService.getAllAccommodations()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/accommodations"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    public void testCreateAccommodation() throws Exception {
        Accommodation acc = new Accommodation();
        acc.setName("Test Hotel");
        acc.setPricePerNight(new BigDecimal("100.00"));

        mockMvc.perform(post("/api/accommodations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(acc)))
                .andExpect(status().isOk())
                .andExpect(content().string("Accommodation created!"));
    }

    @Test
    public void testGetAccommodationById() throws Exception {
        Accommodation acc = new Accommodation();
        acc.setName("Target Hotel");
        when(accommodationService.getAccommodationById(1)).thenReturn(acc);

        mockMvc.perform(get("/api/accommodations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Target Hotel"));
    }
}
