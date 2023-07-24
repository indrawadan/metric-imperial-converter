package com.github.indrawadan.metricimperialconverter.model;

public class ConversionRequest {
    private double value;
    private String sourceUnit;
    private String targetUnit;

    public ConversionRequest() {
    }
    public ConversionRequest(String sourceUnit, String targetUnit,double value) {
        this.sourceUnit = sourceUnit;
        this.targetUnit = targetUnit;
        this.value = value;
    }
    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getSourceUnit() {
        return sourceUnit;
    }

    /*public void setSourceUnit(String sourceUnit) {
        this.sourceUnit = sourceUnit;
    }*/

    public String getTargetUnit() {
        return targetUnit;
    }

/*    public void setTargetUnit(String targetUnit) {
        this.targetUnit = targetUnit;
    }*/
}
