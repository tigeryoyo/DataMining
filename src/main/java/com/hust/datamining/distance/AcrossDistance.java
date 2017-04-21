package com.hust.datamining.distance;

import java.util.List;

public class AcrossDistance extends Distance {

    public AcrossDistance() {
    }

    public AcrossDistance(List<double[]> vectors) {
        super(vectors);
        // TODO Auto-generated constructor stub
    }

    @Override
    public double calculate(double[] vector1, double[] vector2) {
        if (null == vector1 || null == vector2 || vector1.length == 0 || vector2.length == 0) {
            return 0;
        }
        double same = 0;
        double count1 = 0;
        double count2 = 0;
        for (int i = 0; i < vector1.length; i++) {
            if (vector1[i] < 0.5 && vector2[i] < 0.5) {
                continue;
            } else if (vector1[i] == vector2[i]) {
                same++;
                count1++;
                count2++;
            } else if (vector1[i] > 0.5) {
                count1++;
            } else {
                count2++;
            }
        }
        return (same / count1 + same / count2) / 2;
    }

    @Override
    public double getDistance(int index, List<Integer> set) {
        if (index < 0 || set == null || set.size() == 0) {
            return 0;
        }
        double maxsim = 0;
        for (int i : set) {
            double sim = getDistance(index, i);
            if (maxsim < sim) {
                maxsim = sim;
            }
        }
        return maxsim;
    }

    @Override
    public double getDistance(double[] vector, List<double[]> list) {
        if (null == vector || null == list || vector.length == 0 || list.size() == 0) {
            return 0;
        }
        double sum = 0;
        for (double[] tmpVec : list) {
            sum += calculate(vector, tmpVec);
        }
        return sum / list.size();
    }

}
