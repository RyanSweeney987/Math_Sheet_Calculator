package com.ryanairth.mathsheetcalculator.MathObject_Deprecated;

/**
 * Created by Ryan Airth (Sweeney) on 26/12/2015.
 * Copyright information found in License.txt file.
 */
public class PlusObject extends SymbolObject {
    public PlusObject () {
        super(Symbol.PLUS);
    }

    public PlusObject(int origin) {
        super(Symbol.PLUS, origin);
    }

    @Override
    public double calculate(double v1, double v2) {
        return v1 + v2;
    }
}
