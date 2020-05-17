package com.company;

import java.util.ArrayList;
import java.util.List;

public class Sort {

    static List<SortState> selectionSort(int[] numbersArr) {
        int min, temp, previousMin = 0;
        List<SortState> sortStates = new ArrayList<>();
        //sortStates.add(new SortState(0, 0, 0,0, numbersArr, SortState.Type.State));
        sortStates.add(new SortState(0, 0, 0, 0, numbersArr, SortState.Type.StartedAndFinished));
        for (int i = 0; i < numbersArr.length - 1; i++) {
            min = i;
            sortStates.add(new SortState(i, i + 1, i, min, numbersArr, SortState.Type.State));
            for (int j = i + 1; j < numbersArr.length; j++) {
                sortStates.add(new SortState(i, j, i, min, numbersArr, SortState.Type.Compare));
                if (numbersArr[j] < numbersArr[min]) {
                    previousMin = min;
                    min = j;
                    sortStates.add(new SortState(i, j, previousMin, min, numbersArr, SortState.Type.FindNew));
                }
            }

            sortStates.add(new SortState(i, min, previousMin, min, numbersArr, SortState.Type.Change));
            temp = numbersArr[min];
            numbersArr[min] = numbersArr[i];
            numbersArr[i] = temp;
            sortStates.add(new SortState(i, min, previousMin, min, numbersArr, SortState.Type.Change));
            //sortStates.add(new SortState(i, i + 1, previousMin, min, numbersArr, SortState.Type.State));
        }
        sortStates.add(new SortState(numbersArr.length - 1, numbersArr.length - 1, numbersArr.length - 1, numbersArr.length - 1, numbersArr, SortState.Type.State));
        sortStates.add(new SortState(numbersArr.length, numbersArr.length, numbersArr.length, numbersArr.length, numbersArr, SortState.Type.State));
        sortStates.add(new SortState(0, 0, 0, 0, numbersArr, SortState.Type.StartedAndFinished));
        return sortStates;
    }
}
