package com.company;

import java.util.Arrays;

public class SortState {
    public enum Type{State, Compare, Change, FindNew, Sorted, StartedAndFinished}

    private Type type;
    private int i;
    private int j;
    private int[] arr;
    private int min;
    private int previousMin;

    public SortState(int i, int j, int previousMin, int min, int[] arr, Type type) {
        this.i = i;
        this.j = j;
        this.min = min;
        this.arr = Arrays.copyOf(arr, arr.length);
        this.type = type;
        this.previousMin = previousMin;
    }

    public int getPreviousMin() {return previousMin;}

    public int getMin() {
        return min;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public int[] getArr() {
        return arr;
    }

    public Type getType() {
        return type;
    }
}
