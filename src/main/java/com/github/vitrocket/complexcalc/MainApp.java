package com.github.vitrocket.complexcalc;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.complex.ComplexFormat;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Simple console Calculator of complex numbers
 * - (1+3i)+(5-5i)
 * - (1+3i)+(5-5i)/(1+3i)*(5-5i)
 * - (1+3i)+(5-5i)/(1+3i)*(5-5i)+(5-5i)
 * You can check here
 * http://www.wolframalpha.com/input/?i=(1%2B3i)%2B(5-5i)
 *
 * @author Vit Rocket on 22.10.2017.
 * @version 1.0
 * @since on 22.10.2017
 */
public class MainApp {

    static Map<Integer, Complex> complexes = new HashMap<>();
    static Map<Integer, Character> firstOperator = new HashMap<>();
    static Map<Integer, Character> secondOperator = new HashMap<>();

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Examples: (1+3i)+(5-5i) or (1+3i)+(5-5i)/(1+3i)*(5-5i) or (1+3i)+(5-5i)/(1+3i)*(5-5i)+(5-5i)");
        System.out.println("Input please:");
        String formula = scanner.nextLine();
        System.out.println("You inputted: " + formula);

        parser(formula);
        String result = calculation(complexes, firstOperator, secondOperator);
        System.out.println("result: " + result);
    }

    private static String calculation(Map<Integer, Complex> complexes, Map<Integer, Character> firstOperator, Map<Integer, Character> secondOperator) {
        while (firstOperator.size() > 0) {
            Map.Entry<Integer, Character> entry = firstOperator.entrySet().iterator().next();
            Integer key = entry.getKey();
            Character operator = entry.getValue();
            complexes.put(key - 1, getResult(complexes.get(key - 1), complexes.get(key), operator));
            firstOperator.remove(key);
            for (Map.Entry<Integer, Character> entry1 : firstOperator.entrySet()) {
                Integer key1 = entry1.getKey();
                Character value = entry1.getValue();
                firstOperator.put(key1 - 1, value);
                firstOperator.remove(key1);
            }
            for (Map.Entry<Integer, Character> entry1 : secondOperator.entrySet()) {
                Integer key1 = entry1.getKey();
                Character value = entry1.getValue();
                if (key1 > key) {
                    secondOperator.put(key1 - 1, value);
                    secondOperator.remove(key1);
                }
            }
            complexes.remove(key);
            Map<Integer, Complex> newComplex = new HashMap<>();
            for (Map.Entry<Integer, Complex> entry1 : complexes.entrySet()) {
                Integer key1 = entry1.getKey();
                if (key1 > key) {
                    newComplex.put(key1 - 1, entry1.getValue());
                } else {
                    newComplex.put(key1, entry1.getValue());
                }
            }
            complexes = newComplex;
        }
        Complex result = null;
        if (complexes.size() == 1) {
            result = complexes.get(0);
        } else {
            for (Map.Entry<Integer, Complex> entry1 : complexes.entrySet()) {
                Integer key1 = entry1.getKey();
                if (key1 == 0) {
                    result = complexes.get(key1);
                } else {
                    result = getResult(result, complexes.get(key1), secondOperator.get(key1));
                }

            }
        }
        ComplexFormat cf = new ComplexFormat();
        return cf.format(result);
    }

    private static void parser(String formula) {
        Integer count = 0;
        while (formula.length() > 0) {
            int startC = formula.indexOf('(');
            int endC = formula.indexOf(')', startC);
            if (startC == 0) {
                String complexString = formula.substring(startC + 1, endC);
                ComplexFormat cf = new ComplexFormat();
                Complex complex = cf.parse(complexString);
                secondOperator.put(count, '+');
                complexes.put(count, complex);
                count++;
            }
            if (startC == 1) {
                Character oper = formula.charAt(0);
                if (oper.equals('*') | oper.equals('/')) {
                    String complexString = formula.substring(startC + 1, endC);
                    ComplexFormat cf = new ComplexFormat();
                    Complex complex = cf.parse(complexString);
                    firstOperator.put(count, oper);
                    complexes.put(count, complex);
                    count++;
                } else {
                    String complexString = formula.substring(startC + 1, endC);
                    ComplexFormat cf = new ComplexFormat();
                    Complex complex = cf.parse(complexString);
                    secondOperator.put(count, oper);
                    complexes.put(count, complex);
                    count++;
                }
            }
            formula = formula.substring(endC + 1);
        }
    }

    private static Complex getResult(Complex complex1, Complex complex2, Character character) {
        Complex result = null;
        if (character.equals('+')) {
            result = complex1.add(complex2);
        } else if (character.equals('-')) {
            result = complex1.subtract(complex2);
        } else if (character.equals('*')) {
            result = complex1.multiply(complex2);
        } else if (character.equals('/')) {
            result = complex1.divide(complex2);
        }
        return result;
    }
}