package com.alioth.calculator.service.impl;

import com.alioth.calculator.service.CalculatorService;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 计算 Service 实现类
 * @author Alioth
 */
@Service
public class CalculatorServiceImpl implements CalculatorService {

    @Override
    public String calculate(String expression) {
        List<String> postfixExpression = convertExpression(expression);
        Deque<Integer> stack = new ArrayDeque<>();
        for (String token : postfixExpression) {
            if (Character.isDigit(token.charAt(0))) {
                stack.push(Integer.parseInt(token));
            } else {
                int num2 = stack.pop();
                if ("!".equals(token)) {
                    stack.push(num2 == 0 ? 1 : 0);
                } else {
                    int num1 = stack.pop();
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
                            stack.push((num1 != 0 && num2 != 0) ? 1 : 0);
                            break;
                        case "||":
                            stack.push((num1 != 0 || num2 != 0) ? 1 : 0);
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
            if (Character.isDigit(ch)) {
                StringBuilder num = new StringBuilder();
                while (i < expression.length() && Character.isDigit(expression.charAt(i))) {
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
