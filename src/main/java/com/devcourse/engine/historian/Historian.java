package com.devcourse.engine.historian;

import java.util.HashMap;
import java.util.List;

public class Historian {

    private HashMap<Integer, String> history = new HashMap<>();
    private static int lastIndex = 0;

    public void saveHistory(List<String> expression, double result) {
        history.put(++ lastIndex, String.join(" ", expression) + " = " + result);
    }

    public String getHistory(int index) {
        return "#" + index + ". " + history.get(index);
    }

    public int getLastIndex() {
        return lastIndex;
    }
}
