package com.ryanairth.mathsheetcalculator.GUI;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.ryanairth.mathsheetcalculator.R;
import static com.ryanairth.mathsheetcalculator.MainActivity.TAG;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryan Airth (Sweeney) on 13-Sep-15.
 * Copyright information found in License.txt file.
 */
public class NumberGrid extends LineGrid {
    private List<Container> containers;
    private Paint numberColor;

    // TODO - remake so adds text components when needed
    // TODO - remake in general!
    // TODO - DOCUMENT

    public NumberGrid(Context context) {
        this(context, null);
    }

    public NumberGrid(Context context, AttributeSet attrs) {
        this(context, attrs, 0, 0);
    }

    public NumberGrid(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public NumberGrid(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.NumberGrid, 0, 0);

        if(a == null) {
            Log.i(TAG, "TypedArray is null!");
        }
        
        try {
            final int columns = a.getInteger(R.styleable.NumberGrid_n_g_columns, 10);
            final int rows = a.getInteger(R.styleable.NumberGrid_n_g_rows, 10);
            setColumns(columns);
            setRows(rows);

            final int width = a.getDimensionPixelSize(R.styleable.NumberGrid_n_g_width, 50);
            final int height = a.getDimensionPixelSize(R.styleable.NumberGrid_n_g_height, 50);
            setBoxDimensions(width, height);

            Paint temp = new Paint();
            temp.setColor(a.getColor(R.styleable.NumberGrid_n_g_line_color, 0));
            temp.setStrokeWidth(a.getFloat(R.styleable.NumberGrid_n_g_line_stroke, 0));
            setLinePaint(temp);

            temp = new Paint();
            temp.setColor(a.getColor(R.styleable.NumberGrid_n_g_number_color, 0));
            setNumberColor(temp);

            init();
        } finally {
            a.recycle();
        }
    }

    public void setNumberColor(Paint numberColor) {
        this.numberColor = numberColor;
    }

    protected void init() {
        super.init();

        // Create array containing all the number containers
        containers = new ArrayList<>();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        Log.i(TAG, "NumberBlock grid recieved touch event!");

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "NumberBlock grid down!");

                // TODO - ensure that things are taken care of in terms of moving view
                // When pressed down, set the box to be drawn lines
                setSelectedBox(getBox(e.getX(), e.getY()));

                Log.i(TAG, "Action down X: " + e.getX() + ", Y: " + e.getY());

                // Set the states of the lines so the color changes

                getSelectedBox().setPaint(LineStates.PRESSED.getPaint());

                // Redraw screen with updates
                invalidate();

                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "NumberBlock grid move!");

                return true;
            case MotionEvent.ACTION_UP:
                getSelectedBox().setPaint(LineStates.RELEASED.getPaint());

                invalidate();

                Log.i(TAG, "NumberBlock grid up!");
                break;
        }
        return true;
    }

    public void addNewContainer(int x, int y, Container container) {

    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
