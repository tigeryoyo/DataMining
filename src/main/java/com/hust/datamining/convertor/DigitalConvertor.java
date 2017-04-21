package com.hust.datamining.convertor;

import java.util.ArrayList;
import java.util.List;

public class DigitalConvertor extends Convertor {

    @Override
    public List<double[]> getVector() {
        if (null == vectorBase || vectorBase.size() == 0 || null == seglist || seglist.size() == 0) {
            return null;
        }
        List<double[]> vectors = new ArrayList<double[]>();
        for (String[] segArray : seglist) {
            double[] vector = new double[vectorBase.size()];
            for (String word : segArray) {
                int index = vectorBase.indexOf(word);
                vector[index] = 1;
            }
            vectors.add(vector);
        }
        return vectors;
    }

}
