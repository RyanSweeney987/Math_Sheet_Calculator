package com.ryanairth.mathsheetcalculator.MathObject_Deprecated;

import com.ryanairth.mathsheetcalculator.GUI.Container;

/**
 * Created by Ryan Airth (Sweeney) on 21/10/2015.
 * Copyright information found in License.txt file.
 */
public interface MathObject {
    int getOrigin();
    Container[] getContainers();
    void setOrigin(int origin);
}
