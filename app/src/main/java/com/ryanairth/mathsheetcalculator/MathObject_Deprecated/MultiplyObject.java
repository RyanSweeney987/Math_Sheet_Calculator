package com.ryanairth.mathsheetcalculator.MathObject_Deprecated;

/**
 * Created by Ryan Airth (Sweeney) on 26/12/2015.
 * Copyright information found in License.txt file.
 */
public class MultiplyObject extends SymbolObject {
    public MultiplyObject() {
        super(Symbol.MULTIPLY);
    }

    public MultiplyObject(int origin) {
        super(Symbol.MULTIPLY, origin);
    }

    @Override
    public double calculate(double v1, double v2) {
        return v1 * v2;
    }
}
