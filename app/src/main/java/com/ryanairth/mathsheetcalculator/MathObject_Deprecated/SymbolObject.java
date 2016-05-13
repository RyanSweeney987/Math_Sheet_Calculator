package com.ryanairth.mathsheetcalculator.MathObject_Deprecated;

import com.ryanairth.mathsheetcalculator.GUI.Container;
import com.ryanairth.mathsheetcalculator.Math_Deprecated.Calculate;

/**
 * Created by Ryan Airth (Sweeney) on 21/10/2015.
 * Copyright information found in License.txt file.
 */
public abstract class SymbolObject implements MathObject, Calculate {
    private Symbol symbol;
    private int origin;
    private Container[] containers;

    public SymbolObject(Symbol symbol) {
        this.symbol = symbol;
    }

    public SymbolObject(Symbol symbol, int origin) {
        this(symbol);
        this.origin = origin;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    @Override
    public int getOrigin() {
        return origin;
    }

    @Override
    public Container[] getContainers() {
        return containers;
    }

    @Override
    public void setOrigin(int origin) {
        this.origin = origin;
    }

    @Override
    public String toString() {
        return Character.toString(symbol.toChar());
    }
}
