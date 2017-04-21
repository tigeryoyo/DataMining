package com.hust.datamining.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Test;

import com.hust.datamining.algorithm.cluster.DBScan;
import com.hust.datamining.convertor.Convertor;
import com.hust.datamining.convertor.TFIDFConvertor;
import com.hust.datamining.simcal.CosSimilarity;

public class DBScanTest extends ClusterTest {

    @Test
    public void dbscan() {
        Convertor convertor = new TFIDFConvertor();
        convertor.setList(segList);
        List<double[]> vectors = convertor.getVector();
        DBScan dbscan = new DBScan();
        dbscan.setVectors(vectors);
        dbscan.setSimi(new CosSimilarity(vectors));
        dbscan.setMinPts(2);
        dbscan.setEps(0.6);
        ExecutorService exec = Executors.newSingleThreadExecutor();
        Future<List<List<Integer>>> future = exec.submit(dbscan);
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
        List<Integer> noise = dbscan.getNoisePts();
        System.out.println("noise");
        for (int index : noise) {
            String[] array = segList.get(index);
            for (String str : array) {
                System.out.print(str);
            }
            System.out.println();
        }
        System.out.println();
    }
}
