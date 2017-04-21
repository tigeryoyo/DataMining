package com.hust.datamining.simcal;

import java.util.List;

public class AcrossSimilarity extends Similarity {

    public AcrossSimilarity(List<double[]> vectors) {
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

}
