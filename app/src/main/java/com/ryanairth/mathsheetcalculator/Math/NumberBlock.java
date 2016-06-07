package com.ryanairth.mathsheetcalculator.Math;

/**
 * Created by Ryan Airth (Sweeney) on 01/04/2016.
 * Copyright information found in accompanying License.txt file.
 */
public class NumberBlock implements Block<Double> {
    private Double number;

    public NumberBlock(Double number) {
        this.number = number;
    }

    @Override
    public Double getValue() {
        return number;
    }

    @Override
    public String toString() {
        return "Type: Number, Value: " + String.valueOf(number);
    }
}
