package com.hust.datamining.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Test;

import com.hust.datamining.algorithm.cluster.KMeans;
import com.hust.datamining.convertor.Convertor;
import com.hust.datamining.convertor.TFIDFConvertor;
import com.hust.datamining.simcal.CosSimilarity;

public class KMeansTest extends ClusterTest {
    @Test
    public void kmeans() {
        Convertor convertor = new TFIDFConvertor();
        convertor.setList(segList);
        List<double[]> vectors = convertor.getVector();
        KMeans kmeans = new KMeans();
        kmeans.setVectors(vectors);
        kmeans.setIterationTimes(20);
        kmeans.setSimi(new CosSimilarity(vectors));
        kmeans.setK(4);
        ExecutorService exec = Executors.newSingleThreadExecutor();
        Future<List<List<Integer>>> future = exec.submit(kmeans);
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        try {
            result = future.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (List<Integer> set : result) {
            for (int index : set) {
                String[] array = segList.get(index);
                for (String str : array) {
                    System.out.print(str);
                }
                System.out.println();
            }
            System.out.println();
        }
    }
}
