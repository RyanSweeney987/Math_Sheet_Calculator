package com.ryanairth.mathsheetcalculator.Math_Deprecated;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.ryanairth.mathsheetcalculator.Exceptions.UnknownChainObjectException;
import com.ryanairth.mathsheetcalculator.MathObject_Deprecated.Direction;
import com.ryanairth.mathsheetcalculator.MathObject_Deprecated.DivideObject;
import com.ryanairth.mathsheetcalculator.MathObject_Deprecated.MathObject;
import com.ryanairth.mathsheetcalculator.MathObject_Deprecated.MinusObject;
import com.ryanairth.mathsheetcalculator.MathObject_Deprecated.MultiplyObject;
import com.ryanairth.mathsheetcalculator.MathObject_Deprecated.NumberObject;
import com.ryanairth.mathsheetcalculator.MathObject_Deprecated.PlusObject;

import static com.ryanairth.mathsheetcalculator.MainActivity.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Ryan Airth (Sweeney) on 21/10/2015.
 * Copyright information found in License.txt file.
 */
public class MathChain implements Parcelable, Iterable<MathObject>{
    private int startPosition;
    private Deque<MathObject> mathObjects;
    private Deque<Direction> directions;

    private final String DELIM = "DELIM";

    // TODO - remake in accordance to new design, maybe

    public MathChain() {
        mathObjects = new ArrayDeque<>();
        directions = new ArrayDeque<>();
    }

    /*
        Start position on grid view
     */
    public int getStartPosition() {
        return startPosition;
    }

    public Deque<MathObject> getMathObjectDeque() {
        return mathObjects;
    }

    public List<MathObject> getMathObjectDequeAsArrayList() {
        return Arrays.asList(mathObjects.toArray(new MathObject[mathObjects.size()]));
    }

    public Deque<Direction> getDirectionDeque() {
        return directions;
    }

    public List<Direction> getDirectionDequeAsArrayList() {
        return Arrays.asList(directions.toArray(new Direction[directions.size()]));
    }

    public Iterator<MathObject> getMathObjects() {
        return mathObjects.iterator();
    }

    @Override
    public Iterator<MathObject> iterator() {
        return getMathObjects();
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    //TODO update scrollable_number_grid_layout and parceling to use the new enum setup
    public void test() {
        Log.i(TAG, "Starting to scrollable_number_grid_layout!");
        List<String> parcelArray = new ArrayList<>();

        for(Direction elem : directions) {
            parcelArray.add(elem.toString());
        }

        parcelArray.add(DELIM);

        Iterator<MathObject> iterator = mathObjects.iterator();

        while(iterator.hasNext()) {
            MathObject object = iterator.next();

            if(object instanceof  NumberObject) {
                parcelArray.add("NumberObject," + String.valueOf(((NumberObject) object).getNumber()) + ",origin:"
                        + object.getOrigin());
            } else {
                parcelArray.add(object.getClass().getSimpleName() + ",origin:" + object.getOrigin());
            }
        }

        Log.i(TAG, "Listing array elements in scrollable_number_grid_layout!");
        for (String elem : parcelArray) {
            Log.i(TAG, elem);
        }
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MathChain createFromParcel(Parcel in) {
            return new MathChain(in);
        }

        public MathChain[] newArray(int size) {
            return new MathChain[size];
        }
    };

    public MathChain(Parcel in) {
        //TODO create classes of objects based on "Type" giving their value and location
        // Add all directions until "DELIM" then create actual math objects
        String[] data = in.createStringArray();

        int counter = 0;

        while(!data[counter].equals(DELIM)) {
            switch(data[counter]) {
                case "RIGHT":
                    directions.add(Direction.RIGHT);
                    break;
                case "DOWN":
                    directions.add(Direction.DOWN);
                    break;
                default:
                    break;
            }
            counter++;
        }

        for(int i = counter; i < data.length; i++) {
            List<String> objData = new ArrayList<String>() ;
            objData = Arrays.asList(data[i].split("/,"));

            Integer origin = Integer.parseInt(objData.get(1).split(":")[1]);

            // Check to see if first value is an number regardless of whether it has a decimal or not
            if(objData.get(0).matches("\\d*\\.?\\d*")) {
                // Check to see if first value, which should be a number, contains a number
                mathObjects.add(new NumberObject(Double.parseDouble(objData.get(0)), origin));
            } else {
                // If the first value isn't a number, then it's (should be) a symbol object
                try {
                    switch(objData.get(0)) {
                        case "PlusObject":
                            mathObjects.add(new PlusObject(origin));
                            break;
                        case "MinusObject":
                            mathObjects.add(new MinusObject(origin));
                            break;
                        case "MultiplyObject":
                            mathObjects.add(new MultiplyObject(origin));
                            break;
                        case "DivideObject":
                            mathObjects.add(new DivideObject(origin));
                            break;
                        default:
                            throw new UnknownChainObjectException("Unknow object found when trying to "
                            + "recreate chain object - " + data[i]);
                    }
                } catch (UnknownChainObjectException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // Write stuff here
        String[] directionsArray = directions.toArray(new String[directions.size()]);

        List<String> parcelArray = new ArrayList<>();

        for(String elem : directionsArray) {
            parcelArray.add(elem);
        }

        parcelArray.add(DELIM);

        parcelArray.add("Start:" + startPosition);

        Iterator<MathObject> iterator = mathObjects.iterator();

        while(iterator.hasNext()) {
            MathObject object = iterator.next();

            if(object instanceof  NumberObject) {
                parcelArray.add("NumberObject," + String.valueOf(((NumberObject) object).getNumber()) + ",origin:"
                        + object.getOrigin());
            } else {
                parcelArray.add(object.getClass().getSimpleName() + ",origin:" + object.getOrigin());
            }
        }

        dest.writeStringArray(parcelArray.toArray(new String[parcelArray.size()]));
    }
}
