package com.hust.datamining.simcal;

import java.util.List;

import com.hust.datamining.util.VectorUtil;

public class CosSimilarity extends Similarity {

    public CosSimilarity(List<double[]> vectors) {
        super(vectors);
        // TODO Auto-generated constructor stub
    }

    @Override
    public double calculate(double[] vector1, double[] vector2) {
        // TODO Auto-generated method stub
        if (null == vector1 || null == vector2 || vector1.length == 0 || vector2.length == 0) {
            return 0;
        }
        return VectorUtil.multiply(vector1, vector2) / (VectorUtil.module(vector1) * VectorUtil.module(vector2));
    }

}
