package com.github.indrawadan.metricimperialconverter.service;

import com.github.indrawadan.metricimperialconverter.model.ConversionRequest;
import com.github.indrawadan.metricimperialconverter.model.ConversionRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConversionRuleServiceImplTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ConversionRuleServiceImpl conversionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddConversionRule_Success() {
        // Prepare data
        ConversionRule rule = new ConversionRule("meter", "feet", 3.28084);

        // Mock the database query
        when(jdbcTemplate.update(anyString(), anyString(), anyString(), anyDouble())).thenReturn(1);

        // Call the method
        conversionService.addConversionRule(rule);

        // Verify the database query is called with the correct arguments
        String expectedSql = "INSERT INTO conversion_rules (source_unit, target_unit, conversion_rate) VALUES (?, ?, ?)";
        String expectedSourceUnit = "meter";
        String expectedTargetUnit = "feet";
        double expectedConversionRate = 3.28084;
        verify(jdbcTemplate).update(eq(expectedSql), eq(expectedSourceUnit), eq(expectedTargetUnit), eq(expectedConversionRate));
    }

    @Test
    public void testAddConversionRule_InvalidRule() {
        // Prepare an invalid conversion rule with an empty source unit
        final ConversionRule rule1 = new ConversionRule("", "feet", 3.28084);

        // Call the method and expect it to throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> conversionService.addConversionRule(rule1));

        // Prepare an invalid conversion rule with a null target unit
        final ConversionRule rule2 = new ConversionRule("meter", null, 3.28084);

        // Call the method and expect it to throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> conversionService.addConversionRule(rule2));

        // Prepare an invalid conversion rule with a negative conversion rate
        final ConversionRule rule3 = new ConversionRule("meter", "feet", -3.28084);

        // Call the method and expect it to throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> conversionService.addConversionRule(rule3));

        // Verify that jdbcTemplate.update() is not called for any invalid rule
        verify(jdbcTemplate, never()).update(anyString(), anyString(), anyString(), anyDouble());
    }

    @Test
    public void testConvert_Success() {
        // Prepare data
        ConversionRule rule = new ConversionRule("meter", "feet", 3.28084);
        ConversionRequest request = new ConversionRequest("meter", "feet", 5.0);

        // Mock the database query to return the conversion rate
        when(jdbcTemplate.queryForObject(anyString(), eq(Double.class), anyString(), anyString()))
                .thenReturn(rule.getConversionRate());

        // Call the method
        double convertedValue = conversionService.convert(request);

        // Verify the converted value
        double expectedConvertedValue = request.getValue() * rule.getConversionRate();
        assertEquals(expectedConvertedValue, convertedValue, 0.001);
    }

    @Test
    public void testConvert_ConversionRuleNotFound() {
        // Prepare data
        ConversionRequest request = new ConversionRequest("meter", "feet", 5.0);

        // Mock the database query to return null (conversion rule not found)
        when(jdbcTemplate.queryForObject(anyString(), eq(Double.class), anyString(), anyString()))
                .thenThrow(EmptyResultDataAccessException.class);

        // Call the method and expect it to throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> conversionService.convert(request));
    }
}