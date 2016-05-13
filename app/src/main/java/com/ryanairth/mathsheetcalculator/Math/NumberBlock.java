package com.ryanairth.mathsheetcalculator.Math;

/**
 * Created by Ryan Airth (Sweeney) on 01/04/2016.
 * Copyright information found in accompanying License.txt file.
 */
public class NumberBlock implements Block<Double> {
    private Block parent;
    private Block child;
    private Double number;

    public NumberBlock(Double number) {
        this.number = number;
    }

    public NumberBlock(Double number, Block parent) {
        this.number = number;
        this.parent = parent;

        parent.setChild(this);
    }

    @Override
    public Double getValue() {
        return number;
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
        return "Type: Number, Value: " + String.valueOf(number);
    }
}
