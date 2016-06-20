package com.ryanairth.mathsheetcalculator.Math;

/**
 * Created by Ryan Airth (Sweeney) on 01/04/2016.
 * Copyright information found in accompanying License.txt file.
 */
public class SymbolBlock implements Block<MathOperator> {
    private MathOperator symbol;

    public SymbolBlock(MathOperator symbol) {
        this.symbol = symbol;
    }

    @Override
    public MathOperator getValue() {
        return symbol;
    }

    @Override
    public String toString() {
        return "Type: Character, Value: " + String.valueOf(symbol);
    }
}
