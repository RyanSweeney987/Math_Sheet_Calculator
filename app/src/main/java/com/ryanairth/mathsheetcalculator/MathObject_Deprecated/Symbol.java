package com.ryanairth.mathsheetcalculator.MathObject_Deprecated;

/**
 * Created by ryan2 on 31/12/2015.
 * Copyright information found in License.txt file.
 */
public enum Symbol {
    PLUS('+'),
    MINUS('-'),
    MULTIPLY('*'),
    DIVIDE('/'),
    PERCENT('%');

    private char symbol;

    private Symbol(char symbol) {
        this.symbol = symbol;
    }

    public char toChar() {
        return symbol;
    }

    public static Symbol getEnumFromChar(char symbol) {
        switch (symbol) {
            case '+': return PLUS;
            case '-': return MINUS;
            case '×': return MULTIPLY;
            case '÷': return DIVIDE;
            case '%': return PERCENT;
            default: throw new RuntimeException("Unknown character used as symbol! : " + "\'" + symbol + "\'");
        }
    }
}
