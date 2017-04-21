package com.hust.datamining.algorithm.optimize;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Genetic {

    private int maxGeneration;
    private int initialNumber;
    private LinkedList<float[]> generations;
    private LinkedList<Float> distances;
    private float[] data;
    private float targetValue;
    private float[] OptimalVector;
    private List<Integer> sortedList;
    private int vectorlength;

    public int getMaxGeneration() {
        return maxGeneration;
    }

    public void setMaxGeneration(int maxGeneration) {
        this.maxGeneration = maxGeneration;
    }

    public float getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(float targetValue) {
        this.targetValue = targetValue;
    }

    public int getInitialNumber() {
        return initialNumber;
    }

    public void setInitialNumber(int initialNumber) {
        this.initialNumber = initialNumber;
    }

    // private void variation() {
    // Random ran = new Random();
    // if (generations.size() < 3) {
    // return;
    // }
    // int varnum = ran.nextInt(generations.size());
    // List<Integer> varlist = new ArrayList<Integer>();
    // for (int i = 0; i < varnum; i++) {
    // int num = ran.nextInt(generations.size());
    // if (varlist.contains(num)) {
    // i--;
    // continue;
    // }
    // varlist.add(num);
    // float[] vector = generations.get(num);
    // List<Integer> varpoints = CommonUtils.generateRandomIntList(vector.size(), ran.nextInt(vector.size()));
    // for (int index : varpoints) {
    // float data = vector[index];
    // data.setValue(ran.nextFloat());
    // vector.set(index, data);
    // }
    // }
    //
    // }

    private void mates() {
        int mateGroupLength = generations.size() / 2;
        if (mateGroupLength == 0) {
            return;
        }
        Random ran = new Random();
        for (int i = 0; i < mateGroupLength; i++) {
            int maleIndex = -1, femaleIndex = -1;
            // 随机选两条染色体（数据）
            while (maleIndex == femaleIndex || maleIndex == -1 || femaleIndex == -1) {
                maleIndex = ran.nextInt(generations.size());
                femaleIndex = ran.nextInt(generations.size());
            }
            float[] maleVector = generations.get(maleIndex);
            float[] femaleVector = generations.get(femaleIndex);
            // 随机产生两个基因交换位置
            int firstPosition = -1, secondPosition = -1;
            while (firstPosition == secondPosition || firstPosition == -1 || secondPosition == -1) {
                firstPosition = ran.nextInt(vectorlength);
                secondPosition = ran.nextInt(vectorlength);
            }
            // 如果第一个位置大于第二个位置，交换他们
            if (firstPosition > secondPosition) {
                int trans = firstPosition;
                firstPosition = secondPosition;
                secondPosition = trans;
            }
            float[] child1 = maleVector.clone();
            float[] child2 = femaleVector.clone();
            // 交换基因片段
            for (int pos = firstPosition; pos <= secondPosition; pos++) {
                child1[pos] = femaleVector[pos];
                child2[pos] = maleVector[pos];
            }
            generations.add(child1);
            generations.add(child2);
        }
    }

    private void sort() {
        sortedList.clear();
        LinkedList<Float> tmpvalues = new LinkedList<Float>(distances);
        int num = 0;
        while (num < tmpvalues.size()) {
            float min = Float.MAX_VALUE;
            int index = -1;
            int loop = 0;
            for (float value : tmpvalues) {
                if (value < min) {
                    min = value;
                    index = loop;
                }
                loop++;
            }
            tmpvalues.set(index, Float.MAX_VALUE);
            sortedList.add(index);
            num++;
        }
        System.out.println("sortedlist:" + sortedList);
    }

    // private int getMateNum() {
    // int size = generations.size();
    // int num = 0;
    // if (size > 45) {
    // num = 10;
    // } else if (size >= 72) {
    // num = 9;
    // } else if (size >= 56) {
    // num = 8;
    // } else if (size >= 42) {
    // num = 7;
    // } else if (size >= 30) {
    // num = 6;
    // } else if (size >= 20) {
    // num = 5;
    // } else if (size >= 12) {
    // num = 4;
    // } else if (size >= 6) {
    // num = 3;
    // } else if (size >= 2) {
    // num = 2;
    // }
    // return generations.size() / 2;
    // }

    private void flush() {
        LinkedList<float[]> tmpgene = new LinkedList<float[]>();
        int num = 0;
        for (float[] vector : generations) {
            boolean find = false;
            for (float[] ele : tmpgene) {
                if (Arrays.equals(ele, vector)) {
                    find = true;
                    break;
                }
            }
            if (!find) {
                tmpgene.add(vector);
            } else {
                sortedList.remove((Object) num);
            }
        }
        generations = tmpgene;
    }

    private void evaluate() {
        distances.clear();
        Iterator<float[]> iterator = generations.iterator();
        while (iterator.hasNext()) {
            float[] vector = iterator.next();
            if (vector == null || vector.length != data.length || vector.length == 0) {
                throw new RuntimeException();
            }
            float sum = 0;
            for (int i = 0; i < vector.length; i++) {
                sum += vector[i] * data[i];
            }
            distances.add(Math.abs(sum - targetValue));
        }
    }

    private void select() {
        int deletenums[] = new int[generations.size() / 2];

        for (int i = generations.size() - 1, j = 0; i >= generations.size() / 2 && j < deletenums.length; i--, j++) {
            deletenums[j] = sortedList.get(i);
        }
        Arrays.sort(deletenums);
        for (int i = deletenums.length - 1; i >= 0; i--) {
            generations.remove(deletenums[i]);
            distances.remove(deletenums[i]);
        }
    }

    private void init() {
        vectorlength = data.length;
        generations = new LinkedList<float[]>();
        distances = new LinkedList<Float>();
        sortedList = new ArrayList<Integer>();
        for (int i = 0; i < initialNumber; i++) {
            float[] vector = new float[vectorlength];
            for (int j = 0; j < vectorlength; j++) {
                vector[j] = getRandomWeight(data[j], targetValue);
            }
            generations.add(vector);
        }
    }

    private float getRandomWeight(float input, float target) {
        Random ran = new Random();
        float quot = target / input;
        float random = ran.nextFloat();
        return quot * random;
    }

    private void printInit() {
        System.out.println("inputs:" + Arrays.toString(data));
        System.out.println("maxGeneration:" + maxGeneration);
        System.out.println("initial generations:");
        Iterator<float[]> vectorIte = generations.iterator();
        Iterator<Float> valueIte = distances.iterator();
        while (vectorIte.hasNext() && valueIte.hasNext()) {
            System.out.println("vector:" + Arrays.toString(vectorIte.next()) + "\t value:" + valueIte.next());
        }
    }

    private void printVector(float[] vector) {
        float sum = 0;
        for (int i = 0; i < data.length; i++) {
            sum += vector[i] * data[i];
        }
        System.out.println("vector:" + Arrays.toString(vector) + "\t output:" + sum);
    }

    public void processing() {
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("input data cannot be empty and null");
        }
        init();
        flush();
        if (generations == null || generations.size() == 0) {
            throw new RuntimeException("initGroup cannot be empty and null");
        }
        printInit();
        int loop = 0;
        evaluate();
        sort();
        while (loop != maxGeneration && generations.size() != 1) {
            mates();
            flush();
            evaluate();
            sort();
            select();
            System.out.println("generation index : " + loop + "\t" + "generation count:" + generations.size());
            Iterator<float[]> vectorIte = generations.iterator();
            Iterator<Float> valueIte = distances.iterator();
            while (vectorIte.hasNext() && valueIte.hasNext()) {
                System.out.println("vector:" + Arrays.toString(vectorIte.next()) + "\t distance:" + valueIte.next());
            }
            loop++;
        }
        evaluate();
        sort();
        OptimalVector = getOptimal();
        System.out.println("OptimalVector:");
        printVector(OptimalVector);
    }

    private float[] getOptimal() {
        int index = sortedList.get(0);
        return generations.get(index);
    }

    public void setInput(int[] input) {
        data = new float[input.length];
        for (int i = 0; i < input.length; i++) {
            data[i] = input[i];
        }
    }

    public void setInput(float[] input) {
        data = input;
    }

    public void setInput(double[] input) {
        data = new float[input.length];
        for (int i = 0; i < input.length; i++) {
            data[i] = (float) input[i];
        }
    }

    public void setInput(long[] input) {
        data = new float[input.length];
        for (int i = 0; i < input.length; i++) {
            data[i] = input[i];
        }
    }

}
