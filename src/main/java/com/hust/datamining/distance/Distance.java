package com.hust.datamining.distance;

import java.util.List;

public abstract class Distance {

    private List<double[]> vectors;
    private double[][] matrix;

    public Distance() {
        
    }

    public Distance(List<double[]> vectors) {
        this.vectors = vectors;
        init();
    }

    private void init() {
        if (vectors == null || vectors.size() == 0) {
            throw new IllegalArgumentException("vectors can not be empty or null");
        }
        matrix = new double[vectors.size()][vectors.size()];
        for (int i = 0; i < vectors.size(); i++) {
            for (int j = i; j < vectors.size(); j++) {
                if (i == j) {
                    matrix[i][j] = 1;
                    continue;
                }
                matrix[i][j] = calculate(vectors.get(i), vectors.get(j));
                matrix[j][i] = matrix[i][j];
            }
        }
    }

    public double getDistance(int index1, int index2) {
        if (null == matrix) {
            throw new IllegalArgumentException("must init matrix first");
        }
        return matrix[index1][index2];
    }

    public abstract double calculate(double[] vector1, double[] vector2);

    public abstract double getDistance(int index, List<Integer> set);

    public abstract double getDistance(double[] vector, List<double[]> list);
}
