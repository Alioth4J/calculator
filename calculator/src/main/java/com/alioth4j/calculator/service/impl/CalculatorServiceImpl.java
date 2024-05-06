package com.alioth4j.calculator.service.impl;

import com.alioth4j.calculator.service.CalculatorService;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 计算 Service 实现类
 * @author Alioth4J
 */
@Service
public class CalculatorServiceImpl implements CalculatorService {

    private static final double EPSILON = 0.000001;

    @Override
    public String calculate(String expression) {
        List<String> postfixExpression = convertExpression(expression);
        Deque<Double> stack = new ArrayDeque<>();
        for (String token : postfixExpression) {
            if (Character.isDigit(token.charAt(0))) {
                stack.push(Double.parseDouble(token));
            } else {
                double num2 = stack.pop();
                if ("!".equals(token)) {
                    stack.push(Math.abs(num2) < EPSILON ? 1.0 : 0.0);
                } else {
                    double num1 = stack.pop();
                    switch (token) {
                        case "+":
                            stack.push(num1 + num2);
                            break;
                        case "-":
                            stack.push(num1 - num2);
                            break;
                        case "*":
                            stack.push(num1 * num2);
                            break;
                        case "/":
                            stack.push(num1 / num2);
                            break;
                        case "&&":
                            stack.push((Math.abs(num1) < EPSILON && Math.abs(num2) < EPSILON) ? 1.0 : 0.0);
                            break;
                        case "||":
                            stack.push((Math.abs(num1) > EPSILON || Math.abs(num2) > EPSILON) ? 1.0 : 0.0);
                            break;
                    }
                }
            }
        }
        return stack.pop().toString();
    }

    private List<String> convertExpression(String expression) {
        List<String> postfixExpression = new ArrayList<>();
        Deque<String> stack = new ArrayDeque<>();
        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);
            if (Character.isDigit(ch) || ch == '.') {
                StringBuilder num = new StringBuilder();
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    num.append(expression.charAt(i));
                    i++;
                }
                i--;
                postfixExpression.add(num.toString());
            } else if (ch == '(') {
                stack.push("" + ch);
            } else if (ch == ')') {
                while (!stack.isEmpty() && !"(".equals(stack.peek())) {
                    postfixExpression.add(stack.pop());
                }
                stack.pop();
            } else {
                while (!stack.isEmpty() && precedence("" + ch) <= precedence(stack.peek())) {
                    postfixExpression.add(stack.pop());
                }
                if (ch == '&' || ch == '|') {
                    stack.push("" + ch + ch);
                    i++;
                } else {
                    stack.push("" + ch);
                }
            }
        }
        while (!stack.isEmpty()) {
            postfixExpression.add(stack.pop());
        }
        return postfixExpression;
    }

    private int precedence(String op) {
        return switch (op) {
            case "+", "-" -> 1;
            case "*", "/" -> 2;
            case "!" -> 3;
            default -> 0;
        };
    }

}
