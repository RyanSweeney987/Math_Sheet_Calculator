package com.ryanairth.mathsheetcalculator.MathObject_Deprecated;

/**
 * Created by Ryan Airth (Sweeney) on 26/12/2015.
 * Copyright information found in License.txt file.
 */
public class DivideObject extends SymbolObject {
    public DivideObject() {
        super(Symbol.DIVIDE);
    }

    public DivideObject(int origin) {
        super(Symbol.MINUS, origin);
    }

    @Override
    public double calculate(double v1, double v2) {
        return v1 / v2;
    }
}
