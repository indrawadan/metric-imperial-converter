package com.github.indrawadan.metricimperialconverter.service;
import com.github.indrawadan.metricimperialconverter.model.ConversionRule;
import com.github.indrawadan.metricimperialconverter.model.ConversionRequest;
public interface ConversionService {
    void addConversionRule(ConversionRule rule);
    double convert(ConversionRequest conversionRequest);
}
