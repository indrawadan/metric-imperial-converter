package com.github.indrawadan.metricimperialconverter.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import com.github.indrawadan.metricimperialconverter.model.ConversionRule;
import com.github.indrawadan.metricimperialconverter.model.ConversionRequest;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

@Service
public class ConversionRuleServiceImpl{
    private  JdbcTemplate jdbcTemplate;


    @Autowired
    public ConversionRuleServiceImpl() {
    }

    public void addConversionRule(ConversionRule rule) {
        if (!StringUtils.hasLength(rule.getSourceUnit()) || !StringUtils.hasLength(rule.getTargetUnit())) {
            throw new IllegalArgumentException("Source unit and target unit must not be empty.");
        }

        if (rule.getFormulaConstant() < 0) {
            throw new IllegalArgumentException("Conversion rate must be a non-negative value.");
        }
        try {
            String sql = "INSERT INTO conversion_rules (source_unit, target_unit, formula_constant, formula, is_metric_to_imperial) VALUES (?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, rule.getSourceUnit(), rule.getTargetUnit(), rule.getFormulaConstant(), rule.getFormula(), rule.isMetricToImperial());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("An error occurred during the conversion process.");
        }

    }


    public double convert(ConversionRequest conversionRequest) {
        String sourceUnit = conversionRequest.getSourceUnit();
        String targetUnit = conversionRequest.getTargetUnit();

        try {
            // Fetch conversion rule from the database based on the source and target units
            String sql = "SELECT formula, formula_constant, is_metric_to_imperial FROM conversion_rules WHERE source_unit = ? AND target_unit = ?";
            ConversionRule rule = jdbcTemplate.queryForObject(sql, new Object[]{sourceUnit, targetUnit},
                    (rs, rowNum) -> {
                        ConversionRule conversionRule = new ConversionRule();
                        conversionRule.setFormula(rs.getString("formula"));
                        conversionRule.setFormulaConstant(rs.getDouble("formula_constant"));
                        conversionRule.setMetricToImperial(rs.getBoolean("is_metric_to_imperial"));
                        return conversionRule;
                    });

            if (rule == null) {
                throw new IllegalArgumentException("Conversion rule not found for the given units.");
            }

            // Calculate and return the converted value using the formula from the database
            String formula = rule.getFormula();
            double formulaConstant = rule.getFormulaConstant();
            double value = conversionRequest.getValue();

            return evaluateFormula(formula, formulaConstant, value);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("Conversion rule not found for the given units.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("An error occurred during the conversion process.");
        }
    }


    private double evaluateFormula(String formula, double formulaConstant, double value) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        try {
            engine.put("x", value);
            engine.put("c", formulaConstant);
            Object result = engine.eval(formula);
            if (result instanceof Number) {
                return ((Number) result).doubleValue();
            }
        } catch (ScriptException e) {
            throw new IllegalArgumentException("Invalid formula or error occurred while evaluating the formula.");
        }
        throw new IllegalArgumentException("Invalid formula or error occurred while evaluating the formula.");
    }
}
