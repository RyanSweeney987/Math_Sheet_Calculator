package com.ryanairth.mathsheetcalculator.GUI;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.ryanairth.mathsheetcalculator.R;

import java.util.ArrayList;
import java.util.List;

import static com.ryanairth.mathsheetcalculator.MainActivity.TAG;

/**
 * Created by Ryan Airth (Sweeney) on 06/01/2016.
 * Copyright information found in accompanying License.txt file.
 */
public class LineGrid extends View {
    private int columns = 10, rows = 10;
    private int boxWidth = 50, boxHeight = 50;
    private Line[][] horizontalLinesArray;
    private Line[][] verticalLinesArray;
    private List<CustomDrawable> lines;
    private Paint lineColor;

    public LineGrid(Context context) {
        super(context, null);

        init();
    }

    public LineGrid(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LineGrid, 0, 0);

        try {
            columns = a.getInteger(R.styleable.LineGrid_l_g_rows, 10);
            rows = a.getInteger(R.styleable.LineGrid_l_g_columns, 10);

            boxWidth = a.getDimensionPixelSize(R.styleable.LineGrid_l_g_width, 50);
            boxHeight = a.getDimensionPixelSize(R.styleable.LineGrid_l_g_height, 50);

            Log.i(TAG, "BoxWidth: " + boxWidth + ", BoxHeight: " + boxHeight);

            lineColor = new Paint();
            lineColor.setColor(a.getColor(R.styleable.LineGrid_l_g_color, 0));
            lineColor.setStrokeWidth(a.getFloat(R.styleable.LineGrid_l_g_stroke, 0));

            init();
        } finally {
            a.recycle();
        }
    }

    public LineGrid(Context context, int boxWidth, int boxHeight, int columns, int rows, Paint lineColor) {
        super(context);

        this.columns = columns;
        this.rows = rows;

        this.boxWidth = boxWidth;
        this.boxHeight = boxHeight;

        this.lineColor = lineColor;

        init();
    }

    public Line[][] getHorizontalLinesArray() {
        return horizontalLinesArray;
    }

    public Line[][] getVerticalLinesArray() {
        return verticalLinesArray;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setBoxDimensions(int boxWidth, int boxHeight) {
        this.boxWidth = boxWidth;
        this.boxHeight = boxHeight;
    }

    public void setLineColor(Paint lineColor) {
        this.lineColor = lineColor;
    }

    protected void init() {
        Log.i(TAG, "Setting up grid UI!");
        horizontalLinesArray = new Line[rows][columns + 1];
        verticalLinesArray = new Line[rows + 1][columns];

        lines = new ArrayList<>();

        int x1, y1, x2, y2;

        for(int i = 0; i < horizontalLinesArray.length; i++) {
            for(int j = 0; j < horizontalLinesArray[i].length; j++) {
                x1 = j * boxWidth;
                x2 = x1;
                y1 = i * boxHeight;
                y2 = y1 + boxHeight;

                horizontalLinesArray[i][j] = new Line(x1, y1, x2, y2, lineColor);
                lines.add(horizontalLinesArray[i][j]);
            }
        }

        for(int i = 0; i < verticalLinesArray.length; i++) {
            for(int j = 0; j < verticalLinesArray[i].length; j++) {
                x1 = j * boxWidth;
                x2 = x1 + boxWidth;
                y1 = i * boxHeight;
                y2 = y1;

                verticalLinesArray[i][j] = new Line(x1, y1, x2, y2, lineColor);
                lines.add(verticalLinesArray[i][j]);
            }
        }
    }

    public void updateLineColor(Paint paint) {
        lineColor = paint;

        for(Line[] elem : horizontalLinesArray) {
            for(Line elem2 : elem) {
                elem2.setPaint(paint);
            }
        }

        for (Line[] elem : verticalLinesArray) {
            for (Line elem2 : elem) {
                elem2.setPaint(paint);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        // Exactly -- Specific value or match_parent
        // At_most -- match_parent or wrap_content
        // Uspecified -- wrap_content
        // Measure Width
        /*if (widthMode == MeasureSpec.EXACTLY) {
            // Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            // Can't be bigger than...
            width = Math.max(boxWidth * columns, widthSize);
        } else {
            // Be whatever you want
            width = boxWidth * columns;
        }

        // Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            // Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            // Can't be bigger than...
            height = Math.max(boxHeight * rows, heightSize);
        } else {
            // Be whatever you want
            height = boxHeight * rows;
        }*/

        width = boxWidth * columns;
        height = boxHeight * rows;

        Log.i(TAG, "MeasuredWidth: " + width + ", MeasuredHeight: " + height);
        Log.i(TAG, "NumberBlock of Rows: " + rows + ", NumberBlock of Columns: " + columns);
        Log.i(TAG, "WidthSize: " + widthSize + ", HeightSize: " + heightSize);
        Log.i(TAG, "ExpectedWidth: " + boxWidth * columns + ", ExpectedHeight: " + boxHeight * rows);

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for(CustomDrawable elem : lines) {
            elem.draw(canvas);
        }
    }

    public Box getBox(float x, float y) {
        int ay = (int)(x / boxWidth);
        int ax = (int)(y / boxHeight);

        Line[] box = new Line[4];

        box[0] = horizontalLinesArray[ax][ay];
        box[1] = verticalLinesArray[ax][ay + 1];
        box[2] = horizontalLinesArray[ax + 1][ay];
        box[3] = verticalLinesArray[ax][ay];

        return new Box(box);
    }

    public void changeBoxColor(Box box, Paint paint) {
        box.setPaint(paint);
    }

    public int getGridPosition(float x, float y) {
        // Grid position is (x / box size)(row) * (y / box size)(column)
        int gridX = Math.round(x) / boxWidth;
        int gridY = (Math.round(y) - 1) / boxHeight;
        Log.i(TAG, "GridX: " + gridX + ", GridY: " + gridY);

        int finalPos = gridY * columns + gridX;

        Log.i(TAG, "Grid position: " + finalPos);
        return finalPos;
    }

    @Override
    public String toString() {
        return "Columns: " + columns + ", Rows: " + rows + ", Total Size: " + boxWidth * columns + "/"
                + boxHeight * rows + ", Box Width: " + boxWidth + ", Box Height: " + boxHeight;
    }
}
