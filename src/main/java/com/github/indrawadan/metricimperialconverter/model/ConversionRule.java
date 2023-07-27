package com.github.indrawadan.metricimperialconverter.model;

import java.util.Objects;

public class ConversionRule {
    private String sourceUnit;
    private String targetUnit;
    private Double formulaConstant;
    private String formula;

    private boolean isMetricToImperial;

    public ConversionRule() {
    }

    public ConversionRule(String sourceUnit, String targetUnit,Double formulaConstant,String formula,boolean isMetricToImperial ) {
        this.sourceUnit = sourceUnit;
        this.targetUnit = targetUnit;
        this.formulaConstant = formulaConstant;
        this.formula=formula;
        this.isMetricToImperial=isMetricToImperial;
    }

    public String getSourceUnit() {
        return sourceUnit;
    }

    public String getTargetUnit() {
        return targetUnit;
    }

    public Double getFormulaConstant() {
        return formulaConstant;
    }

    public String getFormula() {
        return formula;
    }

    public boolean isMetricToImperial() {
        return isMetricToImperial;
    }

    public void setMetricToImperial(boolean metricToImperial) {
        isMetricToImperial = metricToImperial;
    }

    public void setSourceUnit(String sourceUnit) {
        this.sourceUnit = sourceUnit;
    }

    public void setTargetUnit(String targetUnit) {
        this.targetUnit = targetUnit;
    }

    public void setFormulaConstant(Double formulaConstant) {
        this.formulaConstant = formulaConstant;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    @Override
    public String toString() {
        return "ConversionRule{" +
                "sourceUnit='" + sourceUnit + '\'' +
                ", targetUnit='" + targetUnit + '\'' +
                ", formulaConstant=" + formulaConstant +
                ", formula='" + formula + '\'' +
                ", isMetricToImperial=" + isMetricToImperial +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceUnit, targetUnit, formulaConstant, formula, isMetricToImperial);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ConversionRule that = (ConversionRule) obj;
        return isMetricToImperial == that.isMetricToImperial &&
                Objects.equals(sourceUnit, that.sourceUnit) &&
                Objects.equals(targetUnit, that.targetUnit) &&
                Objects.equals(formulaConstant, that.formulaConstant) &&
                Objects.equals(formula, that.formula);
    }
}
