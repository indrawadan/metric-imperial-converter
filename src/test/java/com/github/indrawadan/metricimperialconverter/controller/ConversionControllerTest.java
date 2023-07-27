package com.github.indrawadan.metricimperialconverter.controller;


import com.github.indrawadan.metricimperialconverter.model.ConversionRule;
import com.github.indrawadan.metricimperialconverter.service.ConversionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConversionControllerTest {

    @Mock
    private ConversionService conversionService;

    @InjectMocks
    private ConversionController conversionController;

    @Test
    public void testAddConversionRule_Success() {
        // Prepare data
        ConversionRule rule = new ConversionRule("meter", "feet", 3.28084, "x * c", true);

        // Mock the conversion service behavior
        doNothing().when(conversionService).addConversionRule(any(ConversionRule.class));

        // Call the API
        ResponseEntity<String> response = conversionController.addConversionRule(rule);

        // Verify the response status and message
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Conversion rule added successfully!", response.getBody());

        // Verify that the service method was called
        verify(conversionService, times(1)).addConversionRule(rule);
    }

   /* @Test
    public void testAddConversionRule_InvalidInput() {
        // Prepare an invalid conversion rule with an empty source unit
        ConversionRule rule = new ConversionRule("", "feet", 3.28084, "x * c", true);

        // Call the API
        ResponseEntity<String> response = conversionController.addConversionRule(rule);

        // Verify the response status and error message
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Source unit and target unit must not be empty.", response.getBody());

        // Verify that the service method was not called
        verify(conversionService, never()).addConversionRule(rule);
    }*/

    /*@Test
    public void testConvertValue_Success() {
        // Prepare data
        ConversionRequest request = new ConversionRequest("meter", "feet", 5.0);

        // Mock the conversion service behavior
        when(conversionService.convert(any(ConversionRequest.class))).thenReturn(16.4042);

        // Call the API
        ResponseEntity<String> response = conversionController.convertValue(request);

        // Verify the response status and converted value
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("16.4042", response.getBody());

        // Verify that the service method was called
        verify(conversionService, times(1)).convert(request);
    }

    @Test
    public void testConvertValue_InvalidConversionRule() {
        // Prepare data
        ConversionRequest request = new ConversionRequest("meter", "feet", 5.0);

        // Mock the conversion service to throw IllegalArgumentException
        when(conversionService.convert(any(ConversionRequest.class)))
                .thenThrow(new IllegalArgumentException("Conversion rule not found for the given units."));

        // Call the API
        ResponseEntity<String> response = conversionController.convertValue(request);

        // Verify the response status and error message
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Conversion rule not found for the given units.", response.getBody());

        // Verify that the service method was called
        verify(conversionService, times(1)).convert(request);
    }

    @Test
    public void testConvertValue_InternalServerError() {
        // Prepare data
        ConversionRequest request = new ConversionRequest("meter", "feet", 5.0);

        // Mock the conversion service to throw an exception
        when(conversionService.convert(any(ConversionRequest.class))).thenThrow(new RuntimeException("Internal Server Error"));

        // Call the API
        ResponseEntity<String> response = conversionController.convertValue(request);

        // Verify the response status and error message
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(null, response.getBody());

        // Verify that the service method was called
        verify(conversionService, times(1)).convert(request);
    }*/
}
