package com.github.indrawadan.metricimperialconverter.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
        String sql = "INSERT INTO conversion_rules (source_unit, target_unit, conversion_rate, constant, offset_action) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, rule.getSourceUnit(), rule.getTargetUnit(), rule.getConversionRate(), rule.getConstant(), rule.getOffsetAction());
    }


    public double convert(ConversionRequest conversionRequest) {
        String sourceUnit = conversionRequest.getSourceUnit();
        String targetUnit = conversionRequest.getTargetUnit();

        // Fetch conversion rules from the database based on the source and target units
        String sql = "SELECT conversion_rate, constant, offset_action FROM conversion_rules WHERE source_unit = ? AND target_unit = ?";
        ConversionRule conversionRule;
        try {
            conversionRule = jdbcTemplate.queryForObject(sql, new Object[]{sourceUnit, targetUnit},
                    (rs, rowNum) -> {
                        ConversionRule rule = new ConversionRule();
                        rule.setConversionRate(rs.getDouble("conversion_rate"));
                        rule.setConstant(rs.getDouble("constant"));
                        rule.setOffsetAction(rs.getString("offset_action"));
                        return rule;
                    });
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("Conversion rule not found for the given units.");
        }

        if (conversionRule == null) {
            throw new IllegalArgumentException("Conversion rule not found for the given units.");
        }

        // Calculate and return the converted value
        double convertedValue = conversionRequest.getValue() * conversionRule.getConversionRate();

        if ("add".equalsIgnoreCase(conversionRule.getOffsetAction())) {
            convertedValue += conversionRule.getConstant();
        } else if ("subtract".equalsIgnoreCase(conversionRule.getOffsetAction())) {
            convertedValue -= conversionRule.getConstant();
        }
        return convertedValue;
    }
}
