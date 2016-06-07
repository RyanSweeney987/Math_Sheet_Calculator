/*
 * Created by Ryan Airth (Sweeney) on 2016.
 * Copyright information found in accompanying License.txt file.
 */

package com.ryanairth.mathsheetcalculator.GUI;

import android.widget.TextView;

/**
 * Created by Ryan Airth (Sweeney) on 04/06/2016.
 * Copyright information found in accompanying License.txt file.
 */
public interface MathPreview {
    TextView getPrimary();
    TextView getSecondary();
    void resetPreview();
    void scrollText(int direction);
}
