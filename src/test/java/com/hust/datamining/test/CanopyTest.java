package com.hust.datamining.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Test;

import com.hust.datamining.algorithm.cluster.Canopy;
import com.hust.datamining.convertor.Convertor;
import com.hust.datamining.convertor.TFIDFConvertor;

public class CanopyTest extends ClusterTest {
    @Test
    public void capony() {
        Convertor convertor = new TFIDFConvertor();
        convertor.setList(segList);
        List<double[]> vectors = convertor.getVector();
        Canopy canopy = new Canopy();
        canopy.setVectors(vectors);
        canopy.setThreshold(0.68f);
        ExecutorService exec = Executors.newSingleThreadExecutor();
        Future<List<List<Integer>>> future = exec.submit(canopy);
        List<List<Integer>> result =  new ArrayList<List<Integer>>();
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
