package com.github.indrawadan.metricimperialconverter.model;

public class ConversionRule {
    private String sourceUnit;
    private String targetUnit;
    private double conversionRate;

    public ConversionRule() {
    }

    public ConversionRule(String sourceUnit, String targetUnit, double conversionRate) {
        this.sourceUnit = sourceUnit;
        this.targetUnit = targetUnit;
        this.conversionRate = conversionRate;
    }

    public String getSourceUnit() {
        return sourceUnit;
    }

    public void setSourceUnit(String sourceUnit) {
        this.sourceUnit = sourceUnit;
    }

    public String getTargetUnit() {
        return targetUnit;
    }

    public void setTargetUnit(String targetUnit) {
        this.targetUnit = targetUnit;
    }

    public double getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(double conversionRate) {
        this.conversionRate = conversionRate;
    }
}
