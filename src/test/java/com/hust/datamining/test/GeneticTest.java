package com.hust.datamining.test;

import org.junit.Before;
import org.junit.Test;

import com.hust.datamining.algorithm.optimize.Genetic;

public class GeneticTest {
    private int[] inputs;

    @Before
    public void init() {
        inputs = new int[] {42, 5, 3, 71, 5, 39, -11000, 8 };
    }

    @Test
    public void genetic() {
        Genetic gene = new Genetic();
        gene.setInput(inputs);
        gene.setInitialNumber(300);
        gene.setMaxGeneration(-1);
        gene.setTargetValue(1);
        gene.processing();
    }
}
