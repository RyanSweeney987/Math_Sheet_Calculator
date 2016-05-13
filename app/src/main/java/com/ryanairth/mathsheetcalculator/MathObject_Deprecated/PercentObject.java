package com.ryanairth.mathsheetcalculator.MathObject_Deprecated;

/**
 * Created by Ryan Airth (Sweeney) on 30/03/2016.
 * Copyright information found in accompanying License.txt file.
 */
public class PercentObject extends SymbolObject {

    public PercentObject () {
        super(Symbol.PLUS);
    }

    public PercentObject(int origin) {
        super(Symbol.PLUS, origin);
    }


    @Override
    public double calculate(double v1, double v2) {
        return v2/v1 * 100.0d;
    }
}
