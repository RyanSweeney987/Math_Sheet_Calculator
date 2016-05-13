package com.ryanairth.mathsheetcalculator.Math_Deprecated;

import android.util.Log;

import com.ryanairth.mathsheetcalculator.Exceptions.ChainModificationException;
import com.ryanairth.mathsheetcalculator.MathObject_Deprecated.Direction;
import com.ryanairth.mathsheetcalculator.MathObject_Deprecated.MathObject;
import com.ryanairth.mathsheetcalculator.MathObject_Deprecated.NumberObject;
import com.ryanairth.mathsheetcalculator.MathObject_Deprecated.Symbol;
import com.ryanairth.mathsheetcalculator.MathObject_Deprecated.SymbolObjectFactory;
import com.ryanairth.mathsheetcalculator.MathObject_Deprecated.SymbolObject;

import java.util.Iterator;
import java.util.List;

import static com.ryanairth.mathsheetcalculator.MainActivity.TAG;

/**
 * Created by Ryan Airth (Sweeney) 31/12/2015.
 * Copyright information found in License.txt file.
 */
public class ChainManager {
    // TODO - chain management operations such as adding, removing, changing, layout management
    // TODO - align numbers on the right hand side rather than left
    // TODO - keep track of current values

    private MathChain mathChain;

    private boolean isMathChainEmpty = false;
    private NumberObject currentNumber;
    private SymbolObject currentSymbol;
    private Direction currentDirection;

    private SymbolObjectFactory symbolFactory;

    public ChainManager() {
        mathChain = new MathChain();
        symbolFactory = new SymbolObjectFactory();
    }

    public MathChain getMathChain() {
        return mathChain;
    }

    public boolean isMathChainEmpty() {
        return isMathChainEmpty;
    }

    public NumberObject getCurrentNumber() {
        return currentNumber;
    }

    public SymbolObject getCurrentSymbol() {
        return currentSymbol;
    }

    public Direction getCurrentDirection() {
        return currentDirection;
    }

    public int getStartPosition() {
        return mathChain.getStartPosition();
    }

    // TODO - test to see if it works, perhaps make it so that it would keep track of how far it
    // has calculated to reduce times needed to redo, potential optimization if needed
    public double getCurrentSum() {
        double sum = 0;
        double currentVal = 0;
        SymbolObject currentSymbol = null;
        boolean isPreviousObjectSymbol = false;
        List<MathObject> mathObjectList = mathChain.getMathObjectDequeAsArrayList();

        for (int i = 0; i < mathObjectList.size(); i++) {
            if(mathObjectList.get(i) instanceof NumberObject) {
                currentVal = ((NumberObject) mathObjectList.get(i)).getNumber();
                isPreviousObjectSymbol = false;
            } else if(mathObjectList.get(i) instanceof SymbolObject) {
                currentSymbol = (SymbolObject) mathObjectList.get(i);
                isPreviousObjectSymbol = true;
            }

            if(isPreviousObjectSymbol) {
                sum = currentSymbol.calculate(sum, currentVal);
            }
        }

        return sum;
    }

    private void setCurrentNumber(NumberObject currentNumber) {
        this.currentNumber = currentNumber;
    }

    private void setCurrentSymbol(SymbolObject currentSymbol) {
        this.currentSymbol = currentSymbol;
    }

    private void setCurrentDirection(Direction currentDirection) {
        this.currentDirection = currentDirection;
    }

    public void setStartPosition(int startPosition) {
        mathChain.setStartPosition(startPosition);
    }

    public void addDataToMathChain(String number, Symbol symbol, Direction direction) throws ChainModificationException {
        // If number has a decimal, then it is floating point, otherwise it's a normal digit
        NumberObject numberObject;

        try {
            Log.i(TAG, "Adding number!");

            addNumberToChain(numberObject = new NumberObject(Double.parseDouble(number)));

            Log.i(TAG, "Is math chain empty? - " + isMathChainEmpty);
            if(isMathChainEmpty) {
                Log.i(TAG, "Math chain is empyt, setting to true!");
                isMathChainEmpty = false;
            }
        } catch (NumberFormatException e) {
            throw new ChainModificationException("Failed to format number and add it to chain!");
        }
        addDirectiontoChain(direction);

        addSymbolToChain(symbol);

        addDirectiontoChain(direction);

        setCurrentNumber(numberObject);
        setCurrentSymbol(symbolFactory.createSymbolObject(symbol));
        setCurrentDirection(direction);
    }

    private void addNumberToChain(NumberObject numberObject) {
        mathChain.getMathObjectDeque().add(numberObject);
    }

    private void addSymbolToChain(Symbol symbol) {
        mathChain.getMathObjectDeque().add(symbolFactory.createSymbolObject(symbol));
    }

    private void addDirectiontoChain(Direction direction) {
        mathChain.getDirectionDeque().add(direction);
    }

    public int getElementSize() {
        return mathChain.getMathObjectDeque().size();
    }

    public Direction getLastDirection() {
        return mathChain.getDirectionDeque().peekLast();
    }

    public MathObject getLastMathObject() {
        return mathChain.getMathObjectDeque().peekLast();
    }

    /**Attempt to remove the top of the chain, returning true if succeeded, false if attempt failed.
     * If attempt failed that means the chain is empty.
     *
     * @return if removing the top of chain succeeded
     */
    public boolean removeTopOfChain() {
        if(mathChain.getMathObjectDeque().size() == 0 && mathChain.getDirectionDeque().size() == 0) {
            isMathChainEmpty = true;
            return false;
        } else {
            Log.i(TAG, "MathObject size: " + mathChain.getMathObjectDeque().size() + ", Direction size: "
                + mathChain.getDirectionDeque().size());

            for(int i = 0; i < 2; i++) {
                Log.i(TAG, "Removed " + mathChain.getMathObjectDeque().removeLast());
                Log.i(TAG, "Direction " + mathChain.getDirectionDeque().removeLast());
            }

            Iterator<MathObject> iterator = mathChain.getMathObjectDeque().descendingIterator();
            NumberObject newNumberObject = null;
            SymbolObject newSymbolObject = null;

            if(iterator.hasNext()) {
                while (iterator.hasNext()) {
                    MathObject mathObject = iterator.next();

                    if(newNumberObject != null && newSymbolObject != null) {
                        break;
                    } else {
                        if(mathObject instanceof NumberObject) {
                            newNumberObject = (NumberObject)mathObject;
                        } else if(mathObject instanceof SymbolObject) {
                            newSymbolObject = (SymbolObject)mathObject;
                        }
                    }
                }
            } else {
                isMathChainEmpty = true;
            }

            setCurrentNumber(newNumberObject);
            setCurrentSymbol(newSymbolObject);

            setCurrentDirection(mathChain.getDirectionDeque().peekLast());

            Log.i(TAG, "MathObject size: " + mathChain.getMathObjectDeque().size() + ", Direction size: "
                    + mathChain.getDirectionDeque().size());

            Log.i(TAG, "Current number set to: " + newNumberObject);
            Log.i(TAG, "Current symbol set to: " + newSymbolObject);
            Log.i(TAG, "Current direction set to: " + mathChain.getDirectionDeque().peekLast());

            return true;
        }
    }

    public void removeTrailingSymbol() {
        if(mathChain.getMathObjectDeque().peekLast() instanceof SymbolObject) {
            mathChain.getMathObjectDeque().pollLast();
        }
    }

    public void formatMathChain() {
        // TODO - set up chain formatting
        List<MathObject> mathObjectList = mathChain.getMathObjectDequeAsArrayList();
        List<Direction> directionList = mathChain.getDirectionDequeAsArrayList();

        boolean hasDirectionChanged = false;
    }

    public void test() {
        mathChain.test();
    }
}
