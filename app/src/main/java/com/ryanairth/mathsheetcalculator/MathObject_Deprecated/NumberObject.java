package com.ryanairth.mathsheetcalculator.MathObject_Deprecated;

import com.ryanairth.mathsheetcalculator.GUI.Container;

/**
 * Created by Ryan Airth (Sweeney) on 21/10/2015.
 * Copyright information found in License.txt file.
 */
public class NumberObject implements MathObject {
    private Double number;
    private int origin;
    private Container[] containers;

    public NumberObject(Double number){
        this.number = number;

        containers = new Container[number.toString().length()];
    }

    public NumberObject(Double number, int origin) {
        this(number);
        this.origin = origin;
    }

    @Override
    public int getOrigin(){
        return origin;
    }

    /*
        Containers required for the grid view
     */
    @Override
    public Container[] getContainers() {
        return containers;
    }

    public double getNumber() {
        return number;
    }

    @Override
    public void setOrigin(int origin) {
        this.origin = origin;
    }

    @Override
    public String toString() {
        return String.valueOf(number);
    }
}
