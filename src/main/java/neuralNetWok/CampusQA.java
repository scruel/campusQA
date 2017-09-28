package neuralNetWok;

import org.datavec.api.writable.Text;
import org.datavec.api.writable.Writable;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Scruel on 2016/12/14.
 * Github: http://www.github.com/scruel
 */
public class CampusQA implements QA {
    private MultiLayerNetwork model;
    private Sent2Vec sent2Vec;
    private LinkedList<String> aswList;
    private int numInputs;

    public CampusQA() throws Exception {

        File charFile = new File("src/main/resources/singleCharVec.txt");
        FileReader cfIn = new FileReader(charFile);
        BufferedReader bcfr = new BufferedReader(cfIn);
        LineNumberReader lnr = new LineNumberReader(bcfr);
        // 定位到最后一行
        lnr.skip(charFile.length());
        // 字典向量大小
        this.numInputs = lnr.getLineNumber();
        bcfr.close();
        OneHotNN oneHotNN = new OneHotNN(this.numInputs);
        this.model = oneHotNN.getModel();
        this.sent2Vec = new Sent2Vec();
        this.sent2Vec.initialCharVec();
        this.aswList = this.sent2Vec.getAswList();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("请稍后，正在初始化(生成深度学习神经网络)...");

        CampusQA campusQA = new CampusQA();
        //初始化完毕
        System.out.println("你好(请继续输入，和我对话)");
        Scanner input = new Scanner(System.in);
        String text;
        while (true) {
            text = input.nextLine();
            if (text.contains("再见")) return;
            System.out.println(campusQA.getAnswer(text));
        }
    }

    public String getAnswer(String text) {

        String val = sent2Vec.getMatrixString(text);
        String[] split = val.split(",", -1);
        List<Writable> ret = new ArrayList<Writable>();
        for (String s : split) {
            ret.add(new Text(s));
        }// 获取结果集
        INDArray featureVector = Nd4j.create(numInputs);
        int featureCount = 0;
        for (int j = 0; j < ret.size(); j++) {
            Writable current = ret.get(j);
            double value = current.toDouble();
            featureVector.putScalar(featureCount++, value);
        }
//                System.out.println(featureVector);
        INDArray predicted = model.output(featureVector, false);
        INDArray binaryGuesses = predicted.gt(0.5);
        System.out.println(predicted);
        if (binaryGuesses.maxNumber().doubleValue() == 1) {
            for (int i = 0; i < aswList.size(); i++) {
                if (binaryGuesses.getDouble(i) == 1) return aswList.get(i);
            }
            return "Index Error";
        } else if (featureVector.maxNumber().doubleValue() != 0) {
            return "\"" + text + "\"描述的不是很具体，请具体描述一下~";
        } else return "抱歉，暂未收录哦~";
    }

}
