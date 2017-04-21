package com.hust.datamining.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.ansj.domain.Result;
import org.ansj.recognition.impl.FilterRecognition;
import org.ansj.splitWord.analysis.NlpAnalysis;

public class TestBase {

    private static FilterRecognition filter = new FilterRecognition();
    static {
        filter.insertStopNatures("w", "u", "mq", "p", "e", "y", "o");
    }

    private String[] getSegresult(String str) {
        Result res;
        try {
            res = NlpAnalysis.parse(str).recognition(filter);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println(e.toString() + "\t" + str);
            return new String[] { "失败" };
        }
        String[] words = new String[res.size()];
        for (int i = 0; i < res.size(); i++) {
            words[i] = res.get(i).getName();
        }
        return words;
    }

    protected List<String[]> getSegresult(List<String> list, int start) {
        if (null == list) {
            throw new IllegalArgumentException();
        }
        if (start + 1 > list.size()) {
            throw new IllegalArgumentException();
        }
        List<String[]> result = new ArrayList<String[]>();
        for (int i = start; i < list.size(); i++) {
            String[] array = getSegresult(list.get(i));
            result.add(array);
        }
        return result;
    }

    protected List<String> readFromFile(String filepath) {
        List<String> content = new ArrayList<String>();
        BufferedReader reader = null;
        try {
            filepath = this.getClass().getResource("").getPath() + "/" + filepath;
            reader = new BufferedReader(new FileReader(new File(filepath)));
            String tmpStr = null;
            while ((tmpStr = reader.readLine()) != null) {
                content.add(tmpStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                reader.close();
            } catch (Exception e2) {
                // TODO: handle exception
            }
        }
        return content;
    }
}
