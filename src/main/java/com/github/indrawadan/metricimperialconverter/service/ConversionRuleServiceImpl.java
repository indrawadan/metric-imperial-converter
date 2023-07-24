package com.github.indrawadan.metricimperialconverter.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.Optional;
import org.springframework.util.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import com.github.indrawadan.metricimperialconverter.model.ConversionRule;
import com.github.indrawadan.metricimperialconverter.model.ConversionRequest;
@Service
public class ConversionRuleServiceImpl implements ConversionService {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ConversionRuleServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addConversionRule(ConversionRule rule) {
        if (!StringUtils.hasLength(rule.getSourceUnit()) || !StringUtils.hasLength(rule.getTargetUnit())) {
            throw new IllegalArgumentException("Source unit and target unit must not be empty.");
        }

        if (rule.getConversionRate() <= 0) {
            throw new IllegalArgumentException("Conversion rate must be a positive value.");
        }
        String sql = "INSERT INTO conversion_rules (source_unit, target_unit, conversion_rate) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, rule.getSourceUnit(), rule.getTargetUnit(), rule.getConversionRate());
    }


    public double convert(ConversionRequest conversionRequest) {
        String sourceUnit = conversionRequest.getSourceUnit();
        String targetUnit = conversionRequest.getTargetUnit();

        // fetch conversion rates from the database based on the source and target units
        String sql = "SELECT conversion_rate FROM conversion_rules WHERE source_unit = ? AND target_unit = ?";
        Double conversionRate;
        try {
            conversionRate = jdbcTemplate.queryForObject(sql, Double.class, sourceUnit, targetUnit);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("Conversion rule not found for the given units.");
        }

        // calculate and return the converted value
        return conversionRequest.getValue() * Optional.ofNullable(conversionRate).orElse(1.0);

    }
}
