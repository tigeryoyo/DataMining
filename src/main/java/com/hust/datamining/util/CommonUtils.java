package com.hust.datamining.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CommonUtils {

    public static boolean hasEmpty(String...strs) {
        if (null == strs || strs.length == 0) {
            return true;
        }
        for (String str : strs) {
            if (str == null || str.length() == 0) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("rawtypes")
    public static boolean hasEmpty(List...list) {
        if (null == list || list.length == 0) {
            return true;
        }
        for (List ele : list) {
            if (ele == null) {
                return true;
            }
        }
        return false;
    }

    public static boolean lowerThan0(int...is) {
        if (is == null || is.length == 0) {
            throw new IllegalArgumentException();
        }
        for (int i : is) {
            if (i < 0) {
                return true;
            }
        }
        return false;
    }

    public static List<Integer> generateRandomIntList(int bound, int number) {
        Random ran = new Random();
        if (number == 0) {
            number = ran.nextInt(bound);
        }
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < number; i++) {
            int tmp = ran.nextInt(bound);
            if (list.contains(tmp)) {
                i--;
                continue;
            }
            list.add(tmp);
        }
        return list;
    }

    public static List<Float> generateRandomFloatList(int number) {
        Random ran = new Random();
        List<Float> list = new ArrayList<Float>();
        for (int i = 0; i < number; i++) {
            float tmp = ran.nextFloat();
            if (list.contains(tmp)) {
                i--;
                continue;
            }
            list.add(tmp);
        }
        return list;
    }
}
