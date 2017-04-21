package com.hust.datamining.algorithm.cluster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.hust.datamining.simcal.CosSimilarity;

public class KMeans extends Cluster {

    private int K;
    private List<double[]> centerPoints;
    private List<double[]> oldCenterPoints;
    private int iterationTimes = 10;

    public int getK() {
        return K;
    }

    public void setK(int k) {
        K = k;
    }

    public List<double[]> getCenterPoints() {
        return centerPoints;
    }

    public int getIterationTimes() {
        return iterationTimes;
    }

    public void setIterationTimes(int iterationTimes) {
        this.iterationTimes = iterationTimes;
    }

    private void init() throws IllegalArgumentException {
        if (K == 0) {
            throw new IllegalArgumentException("must init K before clustering");
        }
        resultIndex = new ArrayList<List<Integer>>();
        for (int i = 0; i < K; i++) {
            List<Integer> setIndex = new ArrayList<Integer>();
            resultIndex.add(setIndex);
        }
    }

    private void clear() {
        for (int i = 0; i < K; i++) {
            resultIndex.get(i).clear();
        }
    }

    @Override
    public void clustering() throws Exception {
        if (null == vectors || vectors.size() == 0) {
            throw new IllegalArgumentException("must init vectors before clustering");
        }
        if (null == simi) {
            simi = new CosSimilarity(vectors);
        }
        init();
        selectRandomCenterPoints();
        int time = 0;
        while (isContinue(time)) {
            time++;
            clear();
            for (int i = 0; i < vectors.size(); i++) {
                addToClosestSet(i);
            }
            updateCenterPoints();
        }
    }

    /**
     * 判断是否继续
     * 
     * @param time 当天迭代次数
     * @return
     */
    private boolean isContinue(int time) {
        boolean centerChanged = false;
        boolean overtime = false;
        if (time != 0) {
            for (int i = 0; i < K; i++) {
                if (oldCenterPoints.get(i) != centerPoints.get(i)) {
                    centerChanged = true;
                }
            }
        } else {
            centerChanged = true;
        }
        if (time > iterationTimes) {
            overtime = true;
        }
        if (centerChanged == false || overtime == true) {
            return false;
        }
        return true;
    }

    /**
     * 选择随机中心点
     */
    private void selectRandomCenterPoints() throws Exception {
        if (K >= vectors.size()) {
            throw new IllegalArgumentException("K 的值必须小于元素的个数");
        }
        List<Integer> centerIndexs = new ArrayList<Integer>();
        centerPoints = new ArrayList<double[]>();
        Random randomMaker = new Random();
        for (int i = 0; i < K; i++) {
            int random = randomMaker.nextInt(vectors.size());
            if (!centerIndexs.contains(random) && !isContain(centerPoints, vectors.get(random))) {
                centerIndexs.add(random);
                centerPoints.add(vectors.get(random));
            } else {
                i--;
            }
        }
    }

    /**
     * 判断list中是否包含vector
     * 
     * @param list
     * @param vector
     * @return
     */
    private boolean isContain(List<double[]> list, double[] vector) {
        for (double[] elem : list) {
            if (Arrays.equals(elem, vector)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将目标vector加入距离最近的集合
     * 
     * @param index
     */
    private void addToClosestSet(int index) {
        double maxsim = -9999;
        int minset = -1;
        double[] vector = vectors.get(index);
        for (int i = 0; i < K; i++) {
            double[] centerPoint = centerPoints.get(i);
            double sim = simi.calculate(vector, centerPoint);
            if (sim > maxsim) {
                maxsim = sim;
                minset = i;
            }
        }
        if (minset == -1) {
            return;
        }
        List<Integer> setIndex = resultIndex.get(minset);
        setIndex.add(index);
    }

    /**
     * 更新簇中心点
     */
    private void updateCenterPoints() {
        oldCenterPoints = new ArrayList<double[]>(centerPoints);
        for (int i = 0; i < K; i++) {
            List<Integer> setIndex = resultIndex.get(i);
            double[] newCenterPoint = findCenter(setIndex);
            centerPoints.set(i, newCenterPoint);
        }
    }

    /**
     * 计算簇中心点
     * 
     * @param setIndex 簇包含的index
     * @return
     */
    private double[] findCenter(List<Integer> setIndex) {
        int length = vectors.get(0).length;
        double[] centerPoint = new double[length];
        for (int i = 0; i < setIndex.size(); i++) {
            for (int j = 0; j < length; j++) {
                centerPoint[j] += vectors.get(setIndex.get(i))[j];
            }
        }
        for (int i = 0; i < length; i++) {
            centerPoint[i] /= setIndex.size();
        }
        return centerPoint;
    }

    // private void print(List<double[]> vectors) {
    // for (double[] vector : vectors) {
    // for (double elem : vector) {
    // System.out.print(elem + "\t");
    // }
    // System.out.println();
    // }
    // }
}
