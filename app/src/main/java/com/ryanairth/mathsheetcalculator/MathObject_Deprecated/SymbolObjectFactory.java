package com.ryanairth.mathsheetcalculator.MathObject_Deprecated;

/**
 * Created by ryan2 on 31/12/2015.
 * Copyright information found in License.txt file.
 */
public class SymbolObjectFactory {
    public SymbolObject createSymbolObject(Symbol symbol) {
        SymbolObject symbolObject = null;

        switch (symbol) {
            case PLUS: symbolObject = new PlusObject();
                break;
            case MINUS: symbolObject = new MinusObject();
                break;
            case DIVIDE: symbolObject = new DivideObject();
                break;
            case MULTIPLY: symbolObject = new MultiplyObject();
                break;
            case PERCENT: symbolObject = new PercentObject();
        }

        return symbolObject;
    }
}
