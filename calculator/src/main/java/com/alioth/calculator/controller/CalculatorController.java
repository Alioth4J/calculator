package com.alioth.calculator.controller;

import com.alioth.calculator.service.CalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 计算器 Controller
 * @author Alioth
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
