package com.hust.datamining.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.hust.datamining.algorithm.neutral.BP;
import com.hust.datamining.convertor.Convertor;
import com.hust.datamining.convertor.TFIDFConvertor;

public class BPTest extends TestBase {

    private List<double[]> inputs;
    private List<double[]> outputs;

    @Before
    public void init() {
        List<String> trainContent = readFromFile("BPTrainContent.txt");
        List<String[]> segList = getSegresult(trainContent, 0);
        Convertor convertor = new TFIDFConvertor();
        convertor.setList(segList);
        inputs = convertor.getVector();
        List<String> trainResult = readFromFile("BPTrainResult.txt");
        outputs = new ArrayList<double[]>();
        for (String line : trainResult) {
            String[] array = line.split(" ");
            double[] v = new double[array.length];
            for (int i = 0; i < array.length; i++) {
                v[i] = Double.parseDouble(array[i]);
            }
            outputs.add(v);
        }
    }

    @Test
    public void bp() {
        List<double[]> trainInput = inputs.subList(0, 181);
        List<double[]> trainOutput = outputs.subList(0, 181);

        List<double[]> testInput = inputs.subList(181, 208);
        List<double[]> testOutput = outputs.subList(181, 208);

        BP bp = new BP();
        bp.setInputs(trainInput);
        bp.setOutputs(trainOutput);
        bp.setMAX_ITER(5000);
        bp.setMIN_ERROR(0.0005);
        bp.setHIDE_LENGTH(5);
        bp.train();

        float sum = 0;
        for (int i = 0; i < testInput.size(); i++) {
            double[] input = testInput.get(i);
            double[] output = bp.calOutput(input);
            System.out.println("standard output：" + Arrays.toString(testOutput.get(i)));
            System.out.println("real output：" + Arrays.toString(output));
            if (isEqual(output, testOutput.get(i))) {
                sum++;
            }
        }
        System.out.println("accuracy: " + (sum / testInput.size()) * 100 + "%");
    }

    private boolean isEqual(double[] v1, double[] v2) {
        if (v1.length != v2.length) {
            return false;
        }
        for (int i = 0; i < v1.length; i++) {
            if (Math.abs(v1[i] - v2[i]) > 0.5) {
                return false;
            }
        }
        return true;
    }
}
