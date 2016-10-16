package com.ktu.taim;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class provides utilities for get a unique color code.
 *
 * Created by A on 14/10/2016.
 */
public class ColorPicker {

    public static String WHITE_COLOR_CODE = "#FFFFFF";

    private Random random;
    private List<String> usedColorCodeList;

    public ColorPicker() {
        random = new Random(System.currentTimeMillis());
        usedColorCodeList = new ArrayList<>();
    }

    public int getNextColor() {
        String nextColorCode = WHITE_COLOR_CODE;
        if (usedColorCodeList.size() < MyColors.ALL_COLORS.length) {
            do {
                int i = random.nextInt(MyColors.ALL_COLORS.length);
                nextColorCode = MyColors.ALL_COLORS[i];
            } while (usedColorCodeList.contains(nextColorCode));
            usedColorCodeList.add(nextColorCode);
        }

        return Color.parseColor(nextColorCode);
    }

    public void reset() {
        usedColorCodeList.clear();
    }
}
