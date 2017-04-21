package com.hust.datamining.convertor;

import java.util.ArrayList;
import java.util.List;

public abstract class Convertor {
    protected List<String[]> seglist;
    protected List<String> vectorBase;

    public void setList(List<String[]> list) {
        this.seglist = list;
        initVectorBase();
    }

    public Convertor() {
        super();
    }

    public Convertor(List<String[]> list) {
        super();
        this.seglist = list;
        initVectorBase();
    }

    /**
     * 初始化vectorBase
     */
    private void initVectorBase() {
        if (null == seglist) {
            return;
        }
        vectorBase = new ArrayList<String>();
        for (String[] array : seglist) {
            for (String word : array) {
                if (!vectorBase.contains(word)) {
                    vectorBase.add(word);
                }
            }
        }
    }

    public abstract List<double[]> getVector();
}
