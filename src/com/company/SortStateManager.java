package com.company;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SortStateManager {
    private List<SortState> states;
    private int current = 0;


    private boolean cyclic = false;

    public void setCyclic(boolean cyclic) {
        this.cyclic = cyclic;
    }

    public boolean isCyclic() {
        return cyclic;
    }

    public SortStateManager() {
        this(null);
    }

    public SortStateManager(List<SortState> states) {
        setStates(states);
    }

    public void setStates(List<SortState> states) {
        this.states = new ArrayList<>();
        if (states != null) {
            this.states.addAll(states);
        }
        reset();
    }

    public void reset() {
        current = 0;
        onSortStateChanged();
    }


    public interface SortStateChangedListener {
        void stateChanged(@Nullable SortState state, int index, int total);

        void finished();
    }

    private SortStateChangedListener listener = null;

    public void setListener(SortStateChangedListener listener) {
        this.listener = listener;
    }

    protected void onSortStateChanged() {
        if (listener == null) return;
        if (states.size() == 0)
            listener.stateChanged(null, -1, 0);
        else
            listener.stateChanged(states.get(current), current, states.size());
    }

    protected void onFinished() {
        if (listener != null)
            listener.finished();
    }


    public boolean setCurrentIndex(int index) {
        if (index < 0 || index >= states.size()) return false;
        current = index;
        onSortStateChanged();
        return true;
    }

    public int getCurrentIndex() {
        return current;
    }

    public int getTotalStatesAmount() {
        return states.size();
    }

    public @Nullable SortState getCurrentState() {
        if (states.size() == 0)
            return null;
        else
            return states.get(current);
    }


    public boolean next() {
        if (current < states.size() - 1)
            current++;
        else if (cyclic)
            current = 0;
        else
            return false;
        onSortStateChanged();
        if (current == states.size() - 1)
            onFinished();
        return true;
    }

    public boolean prev() {
        if (current > 0)
            current--;
        else if (cyclic)
            current = states.size() - 1;
        else return false;
        onSortStateChanged();
        return true;
    }
}
