package com.github.indrawadan.metricimperialconverter.controller;

import com.github.indrawadan.metricimperialconverter.model.ConversionRequest;
import com.github.indrawadan.metricimperialconverter.model.ConversionRule;
import com.github.indrawadan.metricimperialconverter.service.ConversionService;

import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import static org.mockito.Mockito.*;

class ConversionControllerTest {

    @Mock
    private ConversionService conversionService;

    @InjectMocks
    private ConversionController conversionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddConversionRule_Success() throws Exception {
        // data
        ConversionRule rule = new ConversionRule("meter", "feet", 3.28084,0,null);

        // mock conversion service
        doNothing().when(conversionService).addConversionRule(any(ConversionRule.class));

        // Call the endpoint
        ResponseEntity<String> response = conversionController.addConversionRule(rule);

        // Verify the response
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Conversion rule added successfully!", response.getBody());
    }

    @Test
    public void testConvertValue_Success() {
        //  data
        ConversionRequest request = new ConversionRequest("meter", "feet", 5.0);

        // mock conversion service
        when(conversionService.convert(any(ConversionRequest.class))).thenReturn(16.4042);

        // API call
        ResponseEntity<String> response = conversionController.convertValue(request);

        // response verification
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("16.4042", response.getBody());
    }

    @Test
    public void testConvertValue_ConversionRuleNotFound() {
        // data
        ConversionRequest request = new ConversionRequest("meter", "feet", 5.0);

        // Mock the conversion service to throw IllegalArgumentException
        when(conversionService.convert(any(ConversionRequest.class)))
                .thenThrow(new IllegalArgumentException("Conversion rule not found for the given units."));

        // call the endpoint
        ResponseEntity<String> response = conversionController.convertValue(request);

        // verify the response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Conversion rule not found for the given units.", response.getBody());
    }

}