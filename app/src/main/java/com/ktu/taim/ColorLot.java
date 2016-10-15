package com.ktu.taim;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by A on 14/10/2016.
 */
public class ColorLot {

    public static String WHITE_COLOR_CODE = "#FFFFFF";

    private static ColorLot instance;

    synchronized public static ColorLot getInstance()
    {
        if (instance == null) {
            instance = new ColorLot();
        }

        return instance;
    }

    private List<String> usedColorCodeList;

    private ColorLot() {
        usedColorCodeList = new ArrayList<>();
    }

    public int getColorToUse() {
        Random r = new Random(System.currentTimeMillis());
        String nextColorCode = WHITE_COLOR_CODE;
        do {
            int i = r.nextInt(MyColors.ALL_COLORS.length);
            nextColorCode = MyColors.ALL_COLORS[i];
        } while ((usedColorCodeList.size() < MyColors.ALL_COLORS.length) && usedColorCodeList.contains(nextColorCode));
        if (!nextColorCode.equals(WHITE_COLOR_CODE) && !usedColorCodeList.contains(nextColorCode)) {
            usedColorCodeList.add(nextColorCode);
        }

        return Color.parseColor(nextColorCode);
    }

    public void reset() {
        usedColorCodeList.clear();
    }
}
