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
    /*
        Columns and rows for the grid
     */
    private int columns = 10, rows = 10;
    /*
        The width and height for each "box"
     */
    private int boxWidth = 50, boxHeight = 50;
    /*
        The paint for the line, can change anything that you adjust in the paint object
     */
    private Paint linePaint;
    /*
        The box that is selected, useful for highlighting it by changing its color
     */
    private Box selectedBox;
    /*
        2D array for each horizontal lines. Horizontal lines length are equal to width.
        Number of lines along the Y axis is rows + 1 to account for the extra line at the very end
     */
    private Line[][] horizontalLinesArray;
    /*
        2D array for each vertical lines. Vertical lines length are equal to height.
        Number of lines along the X axis is columns + 1 to account for the extra line at the very end
     */
    private Line[][] verticalLinesArray;
    /*
        List containing every single line, makes it easier to draw and iterate through
     */
    private List<CustomDrawable> lines;

    public LineGrid(Context context) {
        super(context, null);

        init();
    }

    /**
     * LineGrid draw a line with a specified number of rows and columns at a specified width and height
     * per {@link Box}
     *
     * @see Line
     * @see Box
     *
     * @param context context required to do stuffs
     * @param attrs attributes from the XML file
     */
    public LineGrid(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Get the attributes passed by the via XML layout
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LineGrid, 0, 0);

        // Set the attributes
        try {
            columns = a.getInteger(R.styleable.LineGrid_l_g_rows, 10);
            rows = a.getInteger(R.styleable.LineGrid_l_g_columns, 10);

            boxWidth = a.getDimensionPixelSize(R.styleable.LineGrid_l_g_width, 50);
            boxHeight = a.getDimensionPixelSize(R.styleable.LineGrid_l_g_height, 50);

            Log.i(TAG, "BoxWidth: " + boxWidth + ", BoxHeight: " + boxHeight);

            linePaint = new Paint();
            linePaint.setColor(a.getColor(R.styleable.LineGrid_l_g_color, 0));
            linePaint.setStrokeWidth(a.getFloat(R.styleable.LineGrid_l_g_stroke, 0));

            // Initiate object
            init();
        } finally {
            a.recycle();
        }
    }

    /**
     * Alternative constructor if one needs to create a LineGrid outside of XML
     *
     * @param context context required to create stuff as well as draw
     * @param boxWidth width of a single box
     * @param boxHeight height of a single box
     * @param columns the number of columns the grid contains
     * @param rows the number of rows the grid contains
     * @param linePaint the paint required to draw the lines
     */
    public LineGrid(Context context, int boxWidth, int boxHeight, int columns, int rows, Paint linePaint) {
        super(context);

        this.columns = columns;
        this.rows = rows;

        this.boxWidth = boxWidth;
        this.boxHeight = boxHeight;

        this.linePaint = linePaint;

        init();
    }

    /**
     * Get the 2D horizontal line array that contains all of the horizontal lines
     *
     * @return 2D array containing all of the horizontal lines
     */
    public Line[][] getHorizontalLinesArray() {
        return horizontalLinesArray;
    }

    /**
     * Get the 2D vertical line array that contains all of the vertical lines
     *
     * @return 2D array containing all of the horizontal lines
     */
    public Line[][] getVerticalLinesArray() {
        return verticalLinesArray;
    }

    /**
     * Get the {@link Box} that is currently selected
     *
     * @see Box
     *
     * @return selected box
     */
    public Box getSelectedBox() {
        return selectedBox;
    }

    /**
     * Set the number of columns for the grid on a per box basis
     *
     * @param columns number of columns
     */
    public void setColumns(int columns) {
        this.columns = columns;
    }

    /**
     * Set the number of rows for the grid on a per box basis
     *
     * @param rows number of rows
     */
    public void setRows(int rows) {
        this.rows = rows;
    }

    /**
     * Set the width and height of boxes in the grid
     *
     * @param boxWidth width of box
     * @param boxHeight height of box
     */
    public void setBoxDimensions(int boxWidth, int boxHeight) {
        this.boxWidth = boxWidth;
        this.boxHeight = boxHeight;
    }

    /**
     * Set the {@link Paint} for the lines, this dictates how the lines are drawn
     *
     * @see Paint
     *
     * @param linePaint paint for drawing the lines
     */
    public void setLinePaint(Paint linePaint) {
        this.linePaint = linePaint;
    }

    /**
     * Sets the current selected {@link Box}
     *
     * @see Box
     *
     * @param selectedBox reference of box that is to be selected
     */
    public void setSelectedBox(Box selectedBox) {
        this.selectedBox = selectedBox;
    }

    /**
     * Initialize objects
     */
    protected void init() {
        // TODO see if I have this backwards!?!?
        Log.i(TAG, "Setting up grid UI!");
        // Set up 2D horizontal/vertical arrays
        // Add 1 to the columns for the horizontal lines to account for the lines at the bottom
        // Add 1 to the rows for the vertical lines to account for the lines at the right
        // Rows = Y axis, Columns = X axis
        horizontalLinesArray = new Line[rows][columns + 1];
        verticalLinesArray = new Line[rows + 1][columns];

        // Initialize the lines array list
        lines = new ArrayList<>();

        int x1, y1, x2, y2;

        // Initialize the lines for the horizontal lines array
        for(int i = 0; i < horizontalLinesArray.length; i++) {
            for(int j = 0; j < horizontalLinesArray[i].length; j++) {
                // Set the start of the horizontal lines with X1 and Y1
                // Set the end of with X2 and Y2
                // X position is current X iteration (j) mult by boxWidth
                x1 = j * boxWidth;
                x2 = x1;
                // Y position is current Y iteration (i) mult by boxHeight
                y1 = i * boxHeight;
                y2 = y1 + boxHeight;

                // Create a new line set it to the current iteration and add it to the lines array
                horizontalLinesArray[i][j] = new Line(x1, y1, x2, y2, linePaint);
                lines.add(horizontalLinesArray[i][j]);
            }
        }

        // Follow similar steps as with the horizontalLinesArray
        for(int i = 0; i < verticalLinesArray.length; i++) {
            for(int j = 0; j < verticalLinesArray[i].length; j++) {
                x1 = j * boxWidth;
                x2 = x1 + boxWidth;
                y1 = i * boxHeight;
                y2 = y1;

                verticalLinesArray[i][j] = new Line(x1, y1, x2, y2, linePaint);
                lines.add(verticalLinesArray[i][j]);
            }
        }
    }

    /**
     * Update the {@link Paint} of all the lines with the provided paint
     *
     * @see Paint
     * @see Line
     *
     * @param paint the paint object that dictates how the lines should be drawn
     */
    public void updateLinePaint(Paint paint) {
        // Set the linePaint to the new paint
        linePaint = paint;

        // Iterate through all of the lines and set the paint
        for(CustomDrawable elem : lines) {
            if(elem instanceof Line) {
                ((Line) elem).setPaint(paint);
            }
        }

        // Iterate through the horizontalLinesArray and set the paint
        /*for(Line[] elem : horizontalLinesArray) {
            for(Line elem2 : elem) {
                elem2.setPaint(paint);
            }
        }

        for (Line[] elem : verticalLinesArray) {
            for (Line elem2 : elem) {
                elem2.setPaint(paint);
            }
        }*/
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

    /**
     * Get the {@link Box} according to the given X and Y coordinates.
     * Coordinates are assumed to be from 0 to Width/Height of grid
     *
     * @see Box
     *
     * @param x location on X axis
     * @param y location on Y axis
     * @return box that surrounds the point indicated by the parameters passed
     */
    public Box getBox(float x, float y) {
        // TODO - remake to make use of get grid position
        // First with calculate roughly what integer we would be at by dividing the X/Y coordinates
        // by the box width/height
        int ay = (int)(x / boxWidth);
        int ax = (int)(y / boxHeight);

        final int gridPosition = getGridPosition(x, y);

        // Then create an array of 4, representing each side
        // 0 - Top, 1 - Right, 2 - Bottom, 3 - Left
        Line[] box = new Line[4];
        // Set the elements of the array
        box[0] = horizontalLinesArray[ax][ay];
        box[1] = verticalLinesArray[ax][ay + 1];
        box[2] = horizontalLinesArray[ax + 1][ay];
        box[3] = verticalLinesArray[ax][ay];

        return new Box(box);
    }

    /**
     * Change the paint of a given {@link Box}
     *
     * @see Box
     * @see Paint
     *
     * @param box box with the paint to be changed
     * @param paint new paint to be applied to the box
     */
    public void changeBoxPaint(Box box, Paint paint) {
        box.setPaint(paint);
    }

    /**
     * Gets the grid position of where the given coordinates are located
     * Position is calculated as gridYPos * columns + gridXPos
     *
     * @param x location along X axis
     * @param y location along Y axis
     * @return grid position
     */
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
    protected void onDraw(Canvas canvas) {
        // Override onDraw to draw the lines of the grid
        for(CustomDrawable elem : lines) {
            elem.draw(canvas);
        }
    }

    @Override
    public String toString() {
        return "Columns: " + columns + ", Rows: " + rows + ", Total Size: " + boxWidth * columns + "/"
                + boxHeight * rows + ", Box Width: " + boxWidth + ", Box Height: " + boxHeight;
    }
}
