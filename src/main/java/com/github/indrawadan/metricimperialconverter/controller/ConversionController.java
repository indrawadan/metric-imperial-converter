package com.github.indrawadan.metricimperialconverter.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.github.indrawadan.metricimperialconverter.service.ConversionService;
import com.github.indrawadan.metricimperialconverter.model.ConversionRule;
import com.github.indrawadan.metricimperialconverter.model.ConversionRequest;
@RestController
@RequestMapping("/api/conversion")
public class ConversionController {
    @Autowired
    private ConversionService conversionService;

    @Autowired
    public ConversionController() {
    }

    @PostMapping("/add")
    public ResponseEntity<String> addConversionRule(@RequestBody ConversionRule rule) {
        try {
            conversionService.addConversionRule(rule);
            return new ResponseEntity<>("Conversion rule added successfully!", HttpStatus.CREATED);
        }
        catch(IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/convert")
    public ResponseEntity<String> convertValue(@RequestBody ConversionRequest conversionRequest) {
        try
        {
            double convertedValue = conversionService.convert(conversionRequest);
            return ResponseEntity.status(HttpStatus.OK).body(String.valueOf(convertedValue));
        }
        catch(IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
