package com.ryanairth.mathsheetcalculator;

import android.app.Application;
import android.content.Context;

/**
 * Created by Ryan Airth (Sweeney) on 21/10/2015.
 * Copyright information found in License.txt file.
 */
public class MathSheetApp extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
    }

    public static Context getAppContext() {
        return context;
    }
}
