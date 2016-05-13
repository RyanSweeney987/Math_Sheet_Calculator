package com.ryanairth.mathsheetcalculator.Math;

/**
 * Created by Ryan Airth (Sweeney) on 01/04/2016.
 * Copyright information found in accompanying License.txt file.
 */
public class SymbolBlock implements Block<MathOperator> {
    private Block parent;
    private Block child;
    private MathOperator symbol;

    public SymbolBlock(MathOperator symbol) {
        this.symbol = symbol;
    }

    public SymbolBlock(MathOperator symbol, Block parent) {
        this.symbol = symbol;
        this.parent = parent;

        parent.setChild(this);
    }

    @Override
    public MathOperator getValue() {
        return symbol;
    }

    @Override
    public Block getParent() {
        return parent;
    }

    @Override
    public Block getChild() {
        return child;
    }

    @Override
    public boolean hasParent() {
        return parent != null;
    }

    @Override
    public boolean hasChild() {
        return child != null;
    }

    @Override
    public void setChild(Block block) {
        this.child = child;
    }

    @Override
    public String toString() {
        return "Type: Character, Value: " + String.valueOf(symbol);
    }
}
