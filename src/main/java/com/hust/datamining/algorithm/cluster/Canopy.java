package com.hust.datamining.algorithm.cluster;

import java.util.ArrayList;
import java.util.List;

import com.hust.datamining.simcal.CosSimilarity;

public class Canopy extends Cluster {

    private float threshold;

    public float getThreshold() {
        return threshold;
    }

    public void setThreshold(float threshold) {
        this.threshold = threshold;
    }

    @Override
    public void clustering() throws Exception {
        if (null == vectors || vectors.size() == 0) {
            throw new IllegalArgumentException("must init vectors before clustering");
        }
        if (null == simi) {
            simi = new CosSimilarity(vectors);
        }
        resultIndex = new ArrayList<List<Integer>>();
        for (int i = 0; i < vectors.size(); i++) {
            if (i == 0) {
                List<Integer> setIndex = new ArrayList<Integer>();
                setIndex.add(i);
                resultIndex.add(setIndex);
                continue;
            }
            boolean findSet = false;
            for (int j = 0; j < resultIndex.size(); j++) {
                List<Integer> tmpVector = resultIndex.get(j);
                double sim = simi.getAvgResult(i, tmpVector);
                if (sim >= threshold) {
                    List<Integer> tmpIndex = resultIndex.get(j);
                    tmpIndex.add(i);
                    findSet = true;
                    break;
                }
            }
            if (!findSet) {
                List<Integer> setIndex = new ArrayList<Integer>();
                setIndex.add(i);
                resultIndex.add(setIndex);
            }
        }
    }

}
