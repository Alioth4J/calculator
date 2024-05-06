package com.alioth4j.calculator.controller;

import com.alioth4j.calculator.service.CalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 计算器 Controller
 * @author Alioth4J
 */
@Controller
@RequestMapping("/calculator")
public class CalculatorController {

    @Autowired
    private CalculatorService calculatorService;

    @GetMapping("/index")
    public String calculator() {
        return "calculator";
    }

    @PostMapping("/calculate")
    @ResponseBody
    public String calculate(@RequestParam String expression) {
        String result = calculatorService.calculate(expression);
        return result;
    }
}
