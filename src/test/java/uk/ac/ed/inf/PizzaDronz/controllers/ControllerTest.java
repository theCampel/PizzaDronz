package uk.ac.ed.inf.PizzaDronz.controllers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import static org.hamcrest.number.BigDecimalCloseTo.closeTo;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;

@WebMvcTest(Controller.class)
class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    //private static final DecimalFormat df = new DecimalFormat("#.########");
    private static final BigDecimal error = BigDecimal.valueOf(0.00015);

    // Test isAlive
    @Test
    public void testIsAlive() throws Exception {
        mockMvc.perform(get("/isAlive"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)));
    }

    // Test distanceTo with a valid request
    @Test
    public void testDistanceTo_ValidRequest() throws Exception {
        BigDecimal expectedDistance = BigDecimal.valueOf(0.00469176);

        String json = "{\"position1\":{\"lng\":-3.188267,\"lat\":55.944154},\"position2\":{\"lng\":-3.192473,\"lat\":55.946233}}";
        mockMvc.perform(post("/distanceTo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(closeTo(expectedDistance, error))));
    }

    // Test distanceTo with an invalid request with missing positions
    @Test
    public void testDistanceTo_InvalidRequest_MissingPosition() throws Exception {
        String json = "{\"position1\":{\"lng\":-3.188267,\"lat\":55.944154},\"position2\":{\"lng\":-3.192473}}";
        mockMvc.perform(post("/distanceTo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    // Test distanceTo with an invalid request with invalid coordinates
    @Test
    public void testDistanceTo_InvalidRequest_InvalidCoordinates() throws Exception {
        String json = "{\"position1\":{\"lng\":200.0,\"lat\":55.944154},\"position2\":{\"lng\":-3.192473,\"lat\":-91.946233}}";
        mockMvc.perform(post("/distanceTo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    // Test distanceTo with the same coordinates
    @Test
    public void testDistanceTo_SameCoordinates() throws Exception {
        String json = "{\"position1\":{\"lng\":-3.188267,\"lat\":55.944154},\"position2\":{\"lng\":-3.188267,\"lat\":55.944154}}";
        mockMvc.perform(post("/distanceTo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(0.0)));
    }

    // Test isCloseTo with a valid request where the distance is less than 0.00015
    @Test
    public void testIsCloseTo_ValidRequest() throws Exception {
        String json = "{\"position1\":{\"lng\":-3.188267,\"lat\":55.944154},\"position2\":{\"lng\":-3.188267,\"lat\":55.944154}}";
        mockMvc.perform(post("/isCloseTo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)));
    }

    // Test isCloseTo with a valid request where the distance is greater than 0.00015
    @Test
    public void testIsCloseTo_ValidRequest_False() throws Exception {
        String json = "{\"position1\":{\"lng\":-3.188267,\"lat\":55.944154},\"position2\":{\"lng\":-3.192473,\"lat\":55.946233}}";
        mockMvc.perform(post("/isCloseTo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(false)));
    }

    // Test isCloseTo with an invalid request with missing positions
    @Test
    public void testIsCloseTo_InvalidRequest_MissingPosition() throws Exception {
        String json = "{\"position1\":{\"lng\":-3.188267,\"lat\":55.944154},\"position2\":{\"lng\":-3.192473}}";
        mockMvc.perform(post("/isCloseTo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }
}
