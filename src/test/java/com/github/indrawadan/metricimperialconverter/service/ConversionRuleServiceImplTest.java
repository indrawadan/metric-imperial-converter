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
import org.springframework.jdbc.core.RowMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ConversionRuleServiceImplTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ConversionRuleServiceImpl conversionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddConversionRule_Success() {
        // Prepare data
        ConversionRule rule = new ConversionRule("meter", "feet", 3.28084, "x * c", true);

        // Mock the database query
        when(jdbcTemplate.update(anyString(), anyString(), anyString(), anyDouble(), anyString(), anyBoolean()))
                .thenReturn(1);

        // Call the method
        conversionService.addConversionRule(rule);

        // Verify the database query is called with the correct arguments
        String expectedSql = "INSERT INTO conversion_rules (source_unit, target_unit, formula_constant, formula, is_metric_to_imperial) VALUES (?, ?, ?, ?, ?)";
        String expectedSourceUnit = "meter";
        String expectedTargetUnit = "feet";
        double expectedFormulaConstant = 3.28084;
        String expectedFormula = "x * c";
        boolean expectedIsMetricToImperial = true;
        verify(jdbcTemplate).update(expectedSql, expectedSourceUnit, expectedTargetUnit, expectedFormulaConstant, expectedFormula, expectedIsMetricToImperial);
    }

    @Test
    public void testAddConversionRule_InvalidRule() {
        // Prepare an invalid conversion rule with an empty source unit
        final ConversionRule rule1 = new ConversionRule("", "feet", 3.28084, "x * c", true);

        // Call the method and expect it to throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> conversionService.addConversionRule(rule1));

        // Prepare an invalid conversion rule with an empty target unit
        final ConversionRule rule2 = new ConversionRule("meter", "", 3.28084, "x * c", true);

        // Call the method and expect it to throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> conversionService.addConversionRule(rule2));

        // Prepare an invalid conversion rule with a negative formula constant
        final ConversionRule rule3 = new ConversionRule("meter", "feet", -3.28084, "x * c", true);

        // Call the method and expect it to throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> conversionService.addConversionRule(rule3));
    }

    @Test
    public void testConvert_Success_MetricToImperial() {
        // Prepare data
        ConversionRule rule = new ConversionRule("meter", "feet", 3.28084, "x * c", true);
        ConversionRequest request = new ConversionRequest("meter", "feet", 5.0);

        // Mock the database query to return the conversion rule
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(rule);

        // Call the method
        double convertedValue = conversionService.convert(request);

        // Verify the converted value
        double expectedConvertedValue = request.getValue() * rule.getFormulaConstant();
        assertEquals(expectedConvertedValue, convertedValue, 0.001);
    }

    @Test
    public void testConvert_Success_ImperialToMetric() {
        // Prepare data
        ConversionRule rule = new ConversionRule("feet", "meter", 0.3048, "x * c", false);
        ConversionRequest request = new ConversionRequest("feet", "meter", 10.0);

        // Mock the database query to return the conversion rule
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(rule);

        // Call the method
        double convertedValue = conversionService.convert(request);

        // Verify the converted value
        double expectedConvertedValue = request.getValue() * rule.getFormulaConstant();
        assertEquals(expectedConvertedValue, convertedValue, 0.001);
    }

    @Test
    public void testConvert_ConversionRuleNotFound() {
        // Prepare data
        ConversionRequest request = new ConversionRequest("meter", "feet", 5.0);

        // Mock the database query to return null (conversion rule not found)
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenThrow(EmptyResultDataAccessException.class);

        // Call the method and expect it to throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> conversionService.convert(request));
    }
}