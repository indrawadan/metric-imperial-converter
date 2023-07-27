package com.github.indrawadan.metricimperialconverter.service;

import com.github.indrawadan.metricimperialconverter.model.ConversionRequest;
import com.github.indrawadan.metricimperialconverter.model.ConversionRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.Before;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
@RunWith(MockitoJUnitRunner.class)
public class ConversionRuleServiceImplTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ConversionRuleServiceImpl conversionRuleService;


    @Test
    public void testAddConversionRule_Success() {
        // Prepare data
        ConversionRule rule = new ConversionRule("meter", "feet", 3.28084, "x * c", true);

        // Mock the jdbcTemplate update method
        when(jdbcTemplate.update(anyString(), any(), any(), anyDouble(), anyString(), anyBoolean())).thenReturn(1);

        // Call the service method
        conversionRuleService.addConversionRule(rule);

        // Verify that the jdbcTemplate update method was called with the correct parameters
        verify(jdbcTemplate, times(1)).update(
                eq("INSERT INTO conversion_rules (source_unit, target_unit, formula_constant, formula, is_metric_to_imperial) VALUES (?, ?, ?, ?, ?)"),
                eq(rule.getSourceUnit()), eq(rule.getTargetUnit()), eq(rule.getFormulaConstant()), eq(rule.getFormula()), eq(rule.isMetricToImperial())
        );
    }

    @Test
    public void testAddConversionRule_InvalidSourceUnit() {
        // Prepare an invalid conversion rule with an empty source unit
        ConversionRule rule = new ConversionRule("", "feet", 3.28084, "x * c", true);

        // Call the service method and verify the exception
        assertThrows(IllegalArgumentException.class, () -> conversionRuleService.addConversionRule(rule));
    }

    @Test
    public void testAddConversionRule_InvalidTargetUnit() {
        // Prepare an invalid conversion rule with an empty target unit
        ConversionRule rule = new ConversionRule("meter", "", 3.28084, "x * c", true);

        // Call the service method and verify the exception
        assertThrows(IllegalArgumentException.class, () -> conversionRuleService.addConversionRule(rule));
    }

    @Test
    public void testAddConversionRule_NegativeConversionRate() {
        // Prepare an invalid conversion rule with a negative conversion rate
        ConversionRule rule = new ConversionRule("meter", "feet", -3.28084, "x * c", true);

        // Call the service method and verify the exception
        assertThrows(IllegalArgumentException.class, () -> conversionRuleService.addConversionRule(rule));
    }

    @Test
    public void testConvert_Success() {
        // Prepare data
        ConversionRequest request = new ConversionRequest("meter", "feet", 5.0);
        ConversionRule rule = new ConversionRule("meter", "feet", 3.28084, "x * c", true);

        // Mock the JdbcTemplate behavior
        when(jdbcTemplate.queryForObject(any(String.class), any(Object[].class), any(RowMapper.class)))
                .thenReturn(rule);

        // Call the service method
        double convertedValue = conversionRuleService.convert(request);

        // Verify the converted value
        assertEquals(16.4042, convertedValue, 0.0001);
        // Verify that the jdbcTemplate queryForObject method was called
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), any(Object[].class), any(RowMapper.class));
    }


    @Test
    public void testConvert_ConversionRuleNotFound() {
        // Prepare data
        ConversionRequest request = new ConversionRequest("meter", "feet", 5.0);

        // Mock the JdbcTemplate behavior to throw EmptyResultDataAccessException
        when(jdbcTemplate.queryForObject(any(String.class), any(Object[].class), any(RowMapper.class)))
                .thenThrow(new EmptyResultDataAccessException(1));

        // Call the service method and verify the exception
        assertThrows(IllegalArgumentException.class, () -> conversionRuleService.convert(request));
    }

    @Test
    public void testConvert_ExceptionInQueryForObject() {
        // Prepare data
        ConversionRequest request = new ConversionRequest("meter", "feet", 5.0);

        // Mock the behavior of JdbcTemplate queryForObject method to throw an exception
        when(jdbcTemplate.queryForObject(any(String.class), any(Object[].class), any(RowMapper.class))).thenThrow(new RuntimeException());

        // Call the service method and verify the exception
        assertThrows(IllegalArgumentException.class, () -> conversionRuleService.convert(request));
    }

    @Test
    public void testEvaluateFormula_Success() {
        // Prepare data
        String formula = "x * c";
        double formulaConstant = 3.28084;
        double value = 5.0;

        // Call the method
        double result = conversionRuleService.evaluateFormula(formula, formulaConstant, value);

        // Verify the result
        assertEquals(16.4042, result, 0.0001);
    }

    @Test
    public void testEvaluateFormula_InvalidFormula() {
        // Prepare data with invalid formula
        String formula = "x *"; // Missing constant 'c'

        // Call the service method and verify the exception
        assertThrows(IllegalArgumentException.class, () -> conversionRuleService.evaluateFormula(formula, 3.28084, 5.0));
    }

}