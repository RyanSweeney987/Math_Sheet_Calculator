package com.ryanairth.mathsheetcalculator.Math;

/**
 * Created by Ryan Airth (Sweeney) on 30/03/2016.
 * Copyright information found in accompanying License.txt file.
 */
public interface Block<T> {
    public T getValue();
    public Block getParent();
    public Block getChild();
    public boolean hasParent();
    public boolean hasChild();
    public void setChild(Block block);
}
