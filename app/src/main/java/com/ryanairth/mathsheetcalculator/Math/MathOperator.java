package com.ryanairth.mathsheetcalculator.Math;

/**
 * Created by Ryan Airth (Sweeney) on 03/05/2016.
 * Copyright information found in accompanying License.txt file.
 */
public enum MathOperator {
    PLUS('+'),
    MINUS('-'),
    MULTIPLY('×'),
    DIVIDE('÷'),
    PERCENTAGE('%'),
    NONE('\u0000');

    private char symbol;

    MathOperator(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    public static MathOperator getEnumFromCharacter(char symbol) {
        MathOperator operator = null;

        switch (symbol) {
            case '+': return PLUS;
            case '-': return MINUS;
            case '×': return MULTIPLY;
            case '÷': return DIVIDE;
            case '%': return PERCENTAGE;
            default: return NONE;
        }
    }
}
