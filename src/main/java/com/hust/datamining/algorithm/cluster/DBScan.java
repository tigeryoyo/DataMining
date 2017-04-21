package com.hust.datamining.algorithm.cluster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.hust.datamining.simcal.CosSimilarity;

public class DBScan extends Cluster {

    private int minPts; // 密度
    private double eps; // 半径

    private List<Integer> kernelPts; // 核心点
    private List<Integer> noisePts; // 非核心点
    private Map<Integer, List<Integer>> map; // 每个点的邻居

    public int getMinPts() {
        return minPts;
    }

    public void setMinPts(int minPts) {
        this.minPts = minPts;
    }

    public double getEps() {
        return eps;
    }

    public void setEps(double eps) {
        this.eps = eps;
    }

    @Override
    public void clustering() throws Exception {
        // TODO Auto-generated method stub
        if (null == vectors || vectors.size() == 0) {
            throw new IllegalArgumentException("must init vectors before clustering");
        }
        if (null == simi) {
            simi = new CosSimilarity(vectors);
        }
        kernelPts = new ArrayList<Integer>();
        noisePts = new ArrayList<Integer>();
        map = new HashMap<Integer, List<Integer>>();
        resultIndex = new ArrayList<List<Integer>>();
        for (int i = 0; i < vectors.size(); i++) {
            List<Integer> neighbor = findNeighbors(i);
            if (neighbor.size() >= minPts) {
                kernelPts.add(i);
            } else {
                noisePts.add(i);
            }
            map.put(i, neighbor);
        }
        flush();
    }

    private List<Integer> findNeighbors(int index) {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < vectors.size(); i++) {
            double sim = simi.getResult(index, i);
            if (sim >= eps) {
                list.add(i);
            }
        }
        return list;
    }

    private void flush() {
        List<Integer> visited = new ArrayList<Integer>();
        while (kernelPts.size() != 0) {
            List<Integer> tmpSet = new ArrayList<Integer>();
            LinkedList<Integer> queue = new LinkedList<Integer>();
            queue.add(kernelPts.get(0));
            while (queue.size() > 0) {
                Integer ele = queue.poll();
                kernelPts.remove((Object) ele);
                tmpSet.add(ele);
                visited.add(ele);
                List<Integer> neighbors = map.get(ele);
                for (Integer nei : neighbors) {
                    if (queue.contains(nei) || visited.contains(nei)) {
                        continue;
                    }
                    queue.add(nei);
                }
            }
            resultIndex.add(tmpSet);
        }
    }

    public List<Integer> getNoisePts() {
        return noisePts;
    }
}
