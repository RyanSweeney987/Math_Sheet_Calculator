package com.ryanairth.mathsheetcalculator.Util;

/**
 * Created by ryan2 on 03/06/2016.
 */
public interface PreviewUpdateListener {
    void updatePreview(char value);
    void resetPreview();
    void deleteLastInput();
}
