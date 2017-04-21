package com.hust.datamining.simcal;

import java.util.List;

public abstract class Similarity {
    private List<double[]> vectors;
    private double[][] matrix;

    protected Similarity(List<double[]> vectors) {
        this.vectors = vectors;
        matrix = new double[vectors.size()][vectors.size()];
    }

    public abstract double calculate(double[] vector1, double[] vector2);

    public double getAvgResult(int index, List<Integer> set) {
        if (index < 0 || set == null || set.size() == 0) {
            return 0;
        }
        double sum = 0;
        for (int ele : set) {
            sum += getResult(index, ele);
        }
        return sum / set.size();
    }

    public double getTop1Result(int index, List<Integer> set) {
        if (index < 0 || set == null || set.size() == 0) {
            return 0;
        }
        return getResult(index, set.get(0));
    }

    public double getClosestResult(int index, List<Integer> set) {
        if (index < 0 || set == null || set.size() == 0) {
            return 0;
        }
        double maxsim = 0;
        for (int ele : set) {
            double sim = getResult(index, ele);
            if (maxsim < sim) {
                maxsim = sim;
            }
        }
        return maxsim;
    }

    public double getResult(int index1, int index2) {
        if (index1 == index2) {
            return 1;
        }
        if (matrix[index1][index2] != 0) {
            return matrix[index1][index2];
        }
        double[] vector1 = vectors.get(index1);
        double[] vector2 = vectors.get(index2);
        double sim = calculate(vector1, vector2);
        matrix[index1][index2] = sim;
        matrix[index2][index1] = sim;
        return sim;
    }
}
