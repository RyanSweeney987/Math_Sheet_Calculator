package com.ryanairth.mathsheetcalculator.GUI;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.FrameLayout;

import static android.view.View.MeasureSpec.*;

import static com.ryanairth.mathsheetcalculator.MainActivity.*;

import com.ryanairth.mathsheetcalculator.MathSheetApp;
import com.ryanairth.mathsheetcalculator.R;
import com.ryanairth.mathsheetcalculator.Util.GridUtil;
import com.ryanairth.mathsheetcalculator.Util.ScreenUtil;
import com.ryanairth.mathsheetcalculator.Util.Util;

/**
 * Created by Ryan Airth (Sweeney) on 16-Sep-15.
 * Copyright information found in License.txt file.
 */
public class MultiScrollView extends FrameLayout {
    private GestureDetector gestureDetector;
    private float currentX, currentY, deltaX, deltaY;
    private int actionBarHeight, notificationBarHeight, navigationBarHeight, heightOffset;
    private int screenWidth, screenHeight;
    private int maxX, maxY;
    private Rect viewRect;


    // TODO - if it has a child, enable scrolling, else disable it
    // TODO - if scrolling enabled, scroll when user generates a "move" input event
    // TODO - keep track of current position
    // TODO - get current available screen space
    // TODO - implement flinging using velocity tracker
    // TODO - implement edge effects and over edge
    // TODO -

    public MultiScrollView(Context context) {
        this(context, null, 0, 0);
    }

    public MultiScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0, 0);
    }

    public MultiScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context) {
        //gestureDetector = new GestureDetector(context, new MSVGestureDetector());

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
        TypedArray styledAttributes = context.getTheme()
                .obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
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
//        navigationBarHeight = context.getResources().getDimensionPixelSize(resourceId);

        heightOffset = actionBarHeight + notificationBarHeight;

        viewRect = new Rect();

//        maxX = getChildAt(0).getWidth() - (getWidth() - getPaddingLeft() - getPaddingRight());
 //       maxY = getChildAt(0).getHeight() - (getHeight() - getPaddingTop() - getPaddingBottom());
    }

    @Override
    public void addView(View child) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        }

        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        }

        super.addView(child, index);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        }

        super.addView(child, params);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        }

        super.addView(child, index, params);
    }

    /*@Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == MeasureSpec.UNSPECIFIED) {
            return;
        }

        if (getChildCount() > 0) {
            final View child = getChildAt(0);
            final int height = getMeasuredHeight();
            if (child.getMeasuredHeight() < height) {
                final int widthPadding;
                final int heightPadding;
                final FrameLayout.LayoutParams lp = (LayoutParams) child.getLayoutParams();
                final int targetSdkVersion = getContext().getApplicationInfo().targetSdkVersion;
                if (targetSdkVersion >= Build.VERSION_CODES.M) {
                    widthPadding = getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin;
                    heightPadding = getPaddingTop() + getPaddingBottom() + lp.topMargin + lp.bottomMargin;
                } else {
                    widthPadding = getPaddingLeft() + getPaddingRight();
                    heightPadding = getPaddingTop() + getPaddingBottom();
                }

                final int childWidthMeasureSpec = getChildMeasureSpec(
                        widthMeasureSpec, widthPadding, lp.width);
                final int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                        height - heightPadding, MeasureSpec.EXACTLY);
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
        }
    }*/

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        getGlobalVisibleRect(viewRect);
    }

    /*@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = 0;
        int height = 0;

        //Measure Width
        *//*
            Exactly -- Specific value or match_parent
            At_most -- match_parent or wrap_content
            Uspecified -- wrap_content
         *//*
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
           // width = Math.min(boxWidth * columns, widthSize);
        } else {
            //Be whatever you want
           // width = boxWidth * columns;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
          //  height = Math.min(boxHeight * rows, heightSize);
        } else {
            //Be whatever you want
          //  height = boxHeight * rows;
        }

        setMeasuredDimension(width, height);
    }*/
/*
    @Override
    public void measureChild(View child, int parentWidthMeasureSpec,
                             int parentHeightMeasureSpec) {
        ViewGroup.LayoutParams lp = child.getLayoutParams();

        int childWidthMeasureSpec;
        int childHeightMeasureSpec;

        childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec, getPaddingLeft()
                + getPaddingRight(), lp.width);

        childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec, getPaddingTop()
                + getPaddingBottom(), lp.height);

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @Override
    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed,
                                           int parentHeightMeasureSpec, int heightUsed) {
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

        final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
                getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin
                        + widthUsed, lp.width);
        final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
                getPaddingTop() + getPaddingBottom() + lp.topMargin + lp.bottomMargin
                        + heightUsed, lp.height);

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }*/

    private void moveChild(float x, float y) {
        getChildAt(0).scrollTo(-Math.round(x),-Math.round(y));

        Log.i(TAG, "Scrolling child to: " + -Math.round(x) + "/" + -Math.round(y));
    }

    /**
     * Check to see if the touch event is a scroll, if it is, handle it here, otherwise the touch event
     * belongs to the child of this view.
     *
     * @param e The motion event triggered by the user pressing on the screen.
     * @return Return true if it's a scrolling action, otherwise false.
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        Log.i(TAG, "MSV intercepted touch event!");

        if(getChildCount() < 1) {
            return false;
        }

        if(e.getAction() == MotionEvent.ACTION_UP || e.getAction() == MotionEvent.ACTION_CANCEL){
            return false;
        } else {
            //return gestureDetector.onTouchEvent(e);
            if(e.getAction() == MotionEvent.ACTION_DOWN ) {
                //return false;
            }
            return true;
        }
    }

    /**
     * Handle the scroll that was intercepted by onInterceptTouchEvent.
     *
     * @param e The touch event triggered byt the user pressing on the screen.
     * @return False or true, doesn't really matter here.
     */
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        Log.i(TAG, "MSV recieved touch event!");

        if(getChildCount() < 1) {
            return true;
        }

        if(e.getAction() == MotionEvent.ACTION_CANCEL){
            return false;
        } else {
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    deltaX = e.getX() - currentX;
                    deltaY = e.getY() - currentY;

                    //Log.i(TAG, "DeltaX: " + deltaX + ", DeltaY: " + deltaY);

                    Log.i(TAG, "MSV down!");
                    break;
                case MotionEvent.ACTION_MOVE:
                    currentX = e.getX() - deltaX;
                    currentY = e.getY() - deltaY;

                    if(currentX > 0) {
                        currentX = 0;
                    } else if(currentX < viewRect.width() - getChildAt(0).getWidth() - 50) {
                        currentX = viewRect.width() - getChildAt(0).getWidth() - 50;
                    }

                    //Log.i(TAG, "ViewRect Width: " + viewRect.width() + ", Height: " + viewRect.height());

                    if(currentY > 0) {
                        currentY = 0;
                    } else if(currentY < viewRect.height() - getChildAt(0).getHeight() - 50) {
                        currentY = viewRect.height() - getChildAt(0).getHeight() - 50;
                    }

                    //TODO fix MSV width so it's different to child width, might need to override onMeasure()
                    //TODO make it so you can't scroll beyond the boundaries
                    //TODO implement some kind of "fling" effect

                    Log.i(TAG, "MSV moving!");

                    /*Log.i(TAG, "CurrentX: " + currentX + "\t\t\t CurrentY: " + currentY);
                    Log.i(TAG, "Width: " + getWidth() + "\t\t\t ChildWidth: " + getChildAt(0).getWidth());
                    Log.i(TAG, "Height: " + getHeight() + "\t\t\t ChildHeight: " + getChildAt(0).getHeight());
                    Log.i(TAG, "ScreenWidth: " + screenWidth + "\t\t ScreenHeight: " + screenHeight);
                    Log.i(TAG, "PTop: " + getPaddingTop() + "\t PRight: " + getPaddingRight() +
                        "\t PBottom: " + getPaddingBottom() + "\t PLeft: " + getPaddingLeft());*/

                    break;
                case MotionEvent.ACTION_UP:
                    Log.i(TAG, "MSV up!");
                    break;
            }
        }

        moveChild(currentX, currentY);
        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    /*private class MSVGestureDetector implements android.view.GestureDetector.OnGestureListener {
        private float deltaX, deltaY, currentX, currentY, clampX, clampY;
        private float offSetX, offSetY;
        private int screenWidth, screenHeight;
        private int actionBarHeight;
        private int notificationBarHeight;
        private int navigationBarHeight;
        private Display display;
        private Point size = new Point();
       // private int heightOffset = screenUtil.getActionBarHeight() + screenUtil.getNotificationBarHeight();
        private int heightOffset;

        public MSVGestureDetector() {
            // Set the move X/Y values taking into account the total height/width of the screen and
            // the height of the navbar/notificationbar and actionbar
        //    clampX = -gridUtil.getTotalWidth() + screenUtil.getScreenWidth() - 150;
        //    clampY = -gridUtil.getTotalHeight() + screenUtil.getScreenHeight() - heightOffset - 150;

            // Go through hoops to get display and such so I can get screen width and screen height
            DisplayMetrics metrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) MathSheetApp.getAppContext().getSystemService(Context.WINDOW_SERVICE);

            Display display = windowManager.getDefaultDisplay();
            Point screenSize = new Point();
            display.getSize(screenSize);

            // Set screen width and screen height
            screenWidth = screenSize.x;
            screenHeight = screenSize.y;

            // Go through hoops to get notification bar height
            TypedArray styledAttributes = MathSheetApp.getAppContext().getTheme()
                    .obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
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
            int resourceId = MathSheetApp.getAppContext().getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            navigationBarHeight = MathSheetApp.getAppContext().getResources().getDimensionPixelSize(resourceId);

            heightOffset = actionBarHeight + notificationBarHeight;

            Log.i(TAG, "Child count: " + getChildCount());

            if(getChildAt(0) != null) {
                Log.i(TAG, "Child at 0: " + getChildAt(0).toString());
            } else {
                Log.i(TAG, "Child at 0 is null!");
            }
        }

        @Override
        public boolean onDown(MotionEvent e) {
            Log.i(TAG, "Pressed");

            offSetX = e.getX() - currentX;
            offSetY = e.getY() - currentY;

            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.i(TAG, "Tapped");

            /*//*eventListener.update(gridUI.getGridPosition(e.getX(), e.getY()));*//**//**//**//*

            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.i(TAG, "Scrolled");

            if(getChildCount() > 0) {
                //Set current x and y (input location + delta)
                x = e2.getRawX() + deltaX;
                y = e2.getRawY() + deltaY;

                // If beyond the bounds limit to bounds
                if(x >= 0) {
                    x = 0;
                }else if(x < clampX) {
                    x = clampX;
                }
                if(y >= 0) {
                    y = 0;
                } else if(y < clampY) {
                    y = clampY;
                }

                Log.i(TAG, "X: " + x + ", Y: " + y + "\n" + "DeltaX: " + deltaX + ", DeltaY: " + deltaY
                        + "\n" + "ClampX: " + clampX + ", ClampY: " + clampY);*//*

                currentX = e2.getX() - offSetX;
                currentY = e2.getY() - offSetY;

                final View child = getChildAt(0);

                if(currentX > getPaddingLeft()) {
                    currentX = getPaddingLeft();
                } else if(currentX < -(child.getHeight() + getPaddingRight())) {
                    currentX = -(child.getHeight() + getPaddingRight());
                }
                if(currentY > getPaddingTop()) {
                    currentY = getPaddingTop();
                } else if(currentY < -(child.getWidth() + getPaddingBottom() - heightOffset + 700)) {
                    currentY = -(child.getWidth() + getPaddingBottom() - heightOffset + 700);
                }

                Log.i(TAG, "CurrentX: " + currentX + ", CurrentY: " + currentY);
                Log.i(TAG, "OffsetX: " + offSetX + ", OffsetY: " + offSetY);

                // Animate and update screen util
                moveChild(currentX, currentY);
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.i(TAG, "Flung");

            return true;
        }
    }*/
}
