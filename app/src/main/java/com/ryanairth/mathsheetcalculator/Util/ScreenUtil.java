package com.ryanairth.mathsheetcalculator.Util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.ryanairth.mathsheetcalculator.MainActivity;
import com.ryanairth.mathsheetcalculator.MathSheetApp;
import com.ryanairth.mathsheetcalculator.R;

/**
 * Created by ryan2 on 12/10/2015.
 */
public class ScreenUtil {
    private int screenWidth, screenHeight;
    private int actionBarHeight;
    private int notificationBarHeight;
    private int navigationBarHeight;

    public ScreenUtil() {
        // TODO - relocate or make it easier to set values?
        // TODO - make certain variables static?

        this(MathSheetApp.getAppContext());
    }

    public ScreenUtil(Context context) {
        // Go through hoops to get display and such so I can get screen width and screen height
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        Display display = windowManager.getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize);

        // Set screen width and screen height
        screenWidth = screenSize.x;
        screenHeight = screenSize.y;

        // Go through hoops to get notification bar height
        TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(new int[] {android.R.attr.actionBarSize});
        actionBarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        windowManager.getDefaultDisplay().getMetrics(metrics);


        switch(metrics.densityDpi) {
            case DisplayMetrics.DENSITY_HIGH:
                notificationBarHeight = 48;
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                notificationBarHeight = 32;
                break;
            case DisplayMetrics.DENSITY_LOW:
                notificationBarHeight = 24;
        }

        // And eventually set navigation bar height
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        navigationBarHeight = context.getResources().getDimensionPixelSize(resourceId);
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getActionBarHeight() {
        return actionBarHeight;
    }

    public int getNotificationBarHeight() {
        return notificationBarHeight;
    }

    public int getNavigationBarHeight() {
        return navigationBarHeight;
    }
}
