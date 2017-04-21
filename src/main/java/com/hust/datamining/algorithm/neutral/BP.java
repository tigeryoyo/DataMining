package com.hust.datamining.algorithm.neutral;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BP {

    private static int OUTPUT_LENGTH;
    private static int HIDE_LENGTH;
    private static int INPUT_LENGTH;

    private static double[] INPUT2OUTPUTLEVEL;
    private static double[] OUTPUT2OUTPUTLEVEL;
    private static double[] THRESHOLD2OUTPUTLEVEL;

    private static double[] INPUT2HIDELEVEL;
    private static double[] OUTPUT2HIDELEVEL;
    private static double[] THRESHOLD2HIDELEVEL;

    private static double[] REAL_INPUT;
    private static double[] REAL_OUTPUT;
    private List<double[]> inputs;
    private List<double[]> outputs;

    private static double[] GRADIENT2OUTPUTLEVEL;
    private static double[] GRADIENT2HIDELEVEL;

    private static double[][] WEIGHT_HIDE_OUTPUT;
    private static double[][] WEIGHT_HIDE_INPUT;

    private static final double SP = 0.09;
    private static double ERROR = Double.MAX_VALUE;
    private static long ITER = 0;

    private long MAX_ITER;
    private double MIN_ERROR;

    private void init() {
        INPUT_LENGTH = inputs.get(0).length;
        OUTPUT_LENGTH = outputs.get(0).length;

        INPUT2OUTPUTLEVEL = new double[OUTPUT_LENGTH];
        OUTPUT2OUTPUTLEVEL = new double[OUTPUT_LENGTH];
        THRESHOLD2OUTPUTLEVEL = new double[OUTPUT_LENGTH];

        INPUT2HIDELEVEL = new double[HIDE_LENGTH];
        OUTPUT2HIDELEVEL = new double[HIDE_LENGTH];
        THRESHOLD2HIDELEVEL = new double[HIDE_LENGTH];

        GRADIENT2HIDELEVEL = new double[HIDE_LENGTH];
        GRADIENT2OUTPUTLEVEL = new double[OUTPUT_LENGTH];

        WEIGHT_HIDE_INPUT = new double[INPUT_LENGTH][HIDE_LENGTH];
        WEIGHT_HIDE_OUTPUT = new double[HIDE_LENGTH][OUTPUT_LENGTH];

        Random ran = new Random();
        for (int i = 0; i < INPUT_LENGTH; i++) {
            for (int j = 0; j < HIDE_LENGTH; j++) {
                WEIGHT_HIDE_INPUT[i][j] = ran.nextDouble();
            }
        }
        for (int j = 0; j < HIDE_LENGTH; j++) {
            THRESHOLD2HIDELEVEL[j] = ran.nextDouble();
        }
        for (int i = 0; i < HIDE_LENGTH; i++) {
            for (int j = 0; j < OUTPUT_LENGTH; j++) {
                WEIGHT_HIDE_OUTPUT[i][j] = ran.nextDouble();
            }
        }
        for (int j = 0; j < OUTPUT_LENGTH; j++) {
            THRESHOLD2OUTPUTLEVEL[j] = ran.nextDouble();
        }
    }

    /**
     * 计算神经网络输出
     * 
     * @return
     */
    private void calOutput4OutputLevel() {
        for (int i = 0; i < OUTPUT_LENGTH; i++) {
            OUTPUT2OUTPUTLEVEL[i] = sigmod(INPUT2OUTPUTLEVEL[i] - THRESHOLD2OUTPUTLEVEL[i]);
        }
    }

    /**
     * 计算隐含层输出
     */
    private void calOutput4HideLevel() {
        for (int i = 0; i < HIDE_LENGTH; i++) {
            OUTPUT2HIDELEVEL[i] = sigmod(INPUT2HIDELEVEL[i] - THRESHOLD2HIDELEVEL[i]);
        }
    }

    private void calInput4OutputLevel() {
        for (int i = 0; i < OUTPUT_LENGTH; i++) {
            double tmpSum = 0;
            for (int j = 0; j < HIDE_LENGTH; j++) {
                tmpSum += WEIGHT_HIDE_OUTPUT[j][i] * OUTPUT2HIDELEVEL[j];
            }
            INPUT2OUTPUTLEVEL[i] = tmpSum;
        }
    }

    private void calInput4HideLevel() {
        for (int i = 0; i < HIDE_LENGTH; i++) {
            double tmpSum = 0;
            for (int j = 0; j < INPUT_LENGTH; j++) {
                tmpSum += WEIGHT_HIDE_INPUT[j][i] * REAL_INPUT[j];
            }
            INPUT2HIDELEVEL[i] = tmpSum;
        }
    }

    /**
     * 计算激活函数值
     * 
     * @param x
     * @return
     */
    private double sigmod(double x) {
        return 1 / (1 + Math.pow(Math.E, -x));
    }

    /**
     * 计算输出层梯度项
     */
    private void calGradient4OutputLevel() {
        for (int i = 0; i < OUTPUT_LENGTH; i++) {
            GRADIENT2OUTPUTLEVEL[i] =
                    OUTPUT2OUTPUTLEVEL[i] * (1 - OUTPUT2OUTPUTLEVEL[i]) * (REAL_OUTPUT[i] - OUTPUT2OUTPUTLEVEL[i]);
            // GRADIENT2OUTPUTLEVEL[i] = SP * (REAL_OUTPUT[i] - OUTPUT2OUTPUTLEVEL[i]);
        }
    }

    /**
     * 计算隐含层梯度项
     */
    private void calGradient2HideLevel() {
        for (int i = 0; i < HIDE_LENGTH; i++) {
            double tmpsum = 0;
            for (int j = 0; j < OUTPUT_LENGTH; j++) {
                tmpsum += WEIGHT_HIDE_OUTPUT[i][j] * GRADIENT2OUTPUTLEVEL[j];
            }
            // GRADIENT2HIDELEVEL[i] = SP * tmpsum;
            GRADIENT2HIDELEVEL[i] = OUTPUT2HIDELEVEL[i] * (1 - OUTPUT2HIDELEVEL[i]) * tmpsum;
        }
    }

    /**
     * 更新权重和阈值
     */
    private void update() {
        for (int i = 0; i < OUTPUT_LENGTH; i++) {
            for (int j = 0; j < HIDE_LENGTH; j++) {
                WEIGHT_HIDE_OUTPUT[j][i] += SP * GRADIENT2OUTPUTLEVEL[i] * OUTPUT2HIDELEVEL[j];
            }
        }

        for (int i = 0; i < OUTPUT_LENGTH; i++) {
            THRESHOLD2OUTPUTLEVEL[i] += -SP * GRADIENT2OUTPUTLEVEL[i];
        }

        for (int i = 0; i < INPUT_LENGTH; i++) {
            for (int j = 0; j < HIDE_LENGTH; j++) {
                WEIGHT_HIDE_INPUT[i][j] += SP * GRADIENT2HIDELEVEL[j] * REAL_INPUT[i];
            }
        }

        for (int i = 0; i < HIDE_LENGTH; i++) {
            THRESHOLD2HIDELEVEL[i] += -SP * GRADIENT2HIDELEVEL[i];
        }
    }

    /**
     * 判断是否继续训练
     * 
     * @return
     */
    private boolean isContinue() {
        if (ITER > MAX_ITER) {
            return false;
        }
        if (ERROR < MIN_ERROR) {
            return false;
        }
        return true;
    }

    private void calError() {
        double[] error = new double[inputs.size()];
        for (int n = 0; n < inputs.size(); n++) {
            double[] tmpOutput2Output = calOutput(inputs.get(n));
            double[] tmpRealOutput = outputs.get(n);
            double tmpError = 0;
            for (int i = 0; i < OUTPUT_LENGTH; i++) {
                tmpError += (tmpRealOutput[i] - tmpOutput2Output[i]) * (tmpRealOutput[i] - tmpOutput2Output[i]);
            }
            error[n] = tmpError / 2;
        }
        double result = 0;
        for (int n = 0; n < inputs.size(); n++) {
            result += error[n];
        }
        ERROR = result / inputs.size();
    }

    public double[] calOutput(double[] input) {
        REAL_INPUT = input;
        calInput4HideLevel();
        calOutput4HideLevel();
        calInput4OutputLevel();
        calOutput4OutputLevel();
        return OUTPUT2OUTPUTLEVEL;
    }

//    private void clear() {
//        INPUT2OUTPUTLEVEL = new double[OUTPUT_LENGTH];
//        OUTPUT2OUTPUTLEVEL = new double[OUTPUT_LENGTH];
//
//        INPUT2HIDELEVEL = new double[HIDE_LENGTH];
//        OUTPUT2HIDELEVEL = new double[HIDE_LENGTH];
//    }

    public void train() {
        init();
        int NUM = inputs.size();
        while (isContinue()) {
            System.out.println("正在进行第 " + ITER + " 次迭代");
            for (int n = 0; n < NUM; n++) {
                REAL_INPUT = inputs.get(n);
                REAL_OUTPUT = outputs.get(n);
                calInput4HideLevel();
                calOutput4HideLevel();
                calInput4OutputLevel();
                calOutput4OutputLevel();
                calGradient4OutputLevel();
                calGradient2HideLevel();
                // System.out.println("隐层-输出层权重：");
                // for (double[] w : WEIGHT_HIDE_OUTPUT) {
                // System.out.println(Arrays.toString(w));
                // }
                // System.out.println("标准输出: " + Arrays.toString(REAL_OUTPUT));
                // System.out.println("实际输出: " + Arrays.toString(OUTPUT2OUTPUTLEVEL));
                //
                // System.out.println("隐层梯度：" + Arrays.toString(GRADIENT2HIDELEVEL));
                // System.out.println("输出层梯度：" + Arrays.toString(GRADIENT2OUTPUTLEVEL));
                update();
                // System.out.println("更新后隐层-输出层权重：");
                // for (double[] w : WEIGHT_HIDE_OUTPUT) {
                // System.out.println(Arrays.toString(w));
                // }
                // System.out.println("******************************************************");
            }
            calError();
            ITER++;
        }
    }

    public int getHIDE_LENGTH() {
        return HIDE_LENGTH;
    }

    public void setHIDE_LENGTH(int hIDE_LENGTH) {
        HIDE_LENGTH = hIDE_LENGTH;
    }

    public List<double[]> getInputs() {
        return inputs;
    }

    public void setInputs(List<double[]> inputs) {
        this.inputs = inputs;
    }

    public List<double[]> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<double[]> outputs) {
        this.outputs = outputs;
    }

    public static double getSp() {
        return SP;
    }

    public long getMAX_ITER() {
        return MAX_ITER;
    }

    public void setMAX_ITER(long mAX_ITER) {
        MAX_ITER = mAX_ITER;
    }

    public double getMIN_ERROR() {
        return MIN_ERROR;
    }

    public void setMIN_ERROR(double mIN_ERROR) {
        MIN_ERROR = mIN_ERROR;
    }
}
