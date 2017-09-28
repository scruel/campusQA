package neuralNetWok;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;

/**
 * Created by Scruel on 2016/12/14.
 * Github: http://www.github.com/scruel
 */
public class Sent2Vec {
    LinkedList<Character> list = new LinkedList<Character>();

    public void initialCharVec() throws Exception {
        File inFile = new File("src/main/resources/singleCharVec.txt");
        FileReader fReader = new FileReader(inFile);
        BufferedReader bfr = new BufferedReader(fReader);
        String str;
        while ((str = bfr.readLine()) != null) {
            char[] s = str.toCharArray();
            list.add(s[0]);
        }
        bfr.close();
    }

    public LinkedList<String> getAswList() throws Exception {
        LinkedList aswList = new LinkedList();
        File inFile = new File("src/main/resources/answer.txt");
        FileReader fReader = new FileReader(inFile);
        BufferedReader bfr = new BufferedReader(fReader);
        String str;
        while ((str = bfr.readLine()) != null) {
            aswList.add(str);
        }
        bfr.close();
        return aswList;
    }

    public String getMatrixString(String text) {
        StringBuilder str = new StringBuilder();
        //将句子文字映射到字向量中
        int[] vector = new int[list.size()];
        char[] s = text.toCharArray();
        for (char c : s) {
            int index = list.indexOf(c);
            if (index != -1) vector[index] = 1;
        }
        for (int n : vector) {
            str.append(n).append(",");
        }
        str = new StringBuilder(str.substring(0, str.length() - 1));
        return str.toString();
    }
}
