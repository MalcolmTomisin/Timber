package com.skillslevel.joules.timely.models.numbers;

import com.skillslevel.joules.timely.models.core.Figure;

public class Null extends Figure {
    private static final float[][] POINTS = {
            {0.5f, 0.5f}, {0.5f, 0.5f}, {0.5f, 0.5f},
            {0.5f, 0.5f}, {0.5f, 0.5f}, {0.5f, 0.5f},
            {0.5f, 0.5f}, {0.5f, 0.5f}, {0.5f, 0.5f},
            {0.5f, 0.5f}, {0.5f, 0.5f}, {0.5f, 0.5f},
            {0.5f, 0.5f}
    };

    private static final Null INSTANCE = new Null();

    protected Null() {
        super(POINTS);
    }

    public static Null getInstance() {
        return INSTANCE;
    }
}
