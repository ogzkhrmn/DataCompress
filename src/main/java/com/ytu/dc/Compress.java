package com.ytu.dc;

import java.security.SecureRandom;
import java.util.*;


public class Compress {

    private static double[][] weightMatrixL1;
    private static double[][] weightMatrixL2;
    private static double[] ymatrix;
    private static Double[] probArray;
    private static int length = 0;
    private double learningRate = 0.1;
    private double epsilon = 0.0001;
    private static Map<Character, Double> probs = new LinkedHashMap<>();

    public void initialize(String text) {
        probs.clear();
        int size = text.length();
        double prob = 1.0 / size;
        for (Character selected : text.toCharArray()) {
            if (probs.containsKey(selected)) {
                Double times = probs.get(selected);
                probs.put(selected, times + prob);
            } else {
                probs.put(selected, prob);
            }
        }
        int keysetSize = probs.keySet().size();
        probArray = probs.values().toArray(new Double[keysetSize]);
        weightMatrixL1 = new double[keysetSize][keysetSize];
        weightMatrixL2 = new double[keysetSize][keysetSize];
        length = probArray.length;
        initializeWeights();
    }

    private void initializeWeights() {
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                weightMatrixL1[i][j] = random.nextInt(1000) * 0.001;
                weightMatrixL2[i][j] = random.nextInt(1000) * 0.001;
            }
        }
    }

    public Compress setWeights() {
        double total = 0.0;
        double[] valueArray;
        double totalerror = Double.MAX_VALUE;
        double last = 0.0;
        int times = 0;
        while (totalerror > 0.005 && times < 50 && Math.abs(totalerror - last) > epsilon) {
            last = totalerror;
            totalerror = 0;
            int index = 0;
            for (Double value : probArray) {
                double[] valueArrayL1 = new double[length];
                ymatrix = new double[length];
                valueArray = new double[length];
                valueArray[index] = value;
                for (int i = 0; i < length; i++) {
                    total = 0.0;
                    for (int j = 0; j < length; j++) {
                        total = valueArray[j] * weightMatrixL1[j][i] + total;
                    }
                    valueArrayL1[i] = total + valueArrayL1[i];
                }
                for (int i = 0; i < length; i++) {
                    for (int j = 0; j < length; j++) {
                        ymatrix[j] = valueArrayL1[j] * weightMatrixL2[j][i] + ymatrix[j];
                    }
                }
                total = 1.0 / (1.0 + Math.exp(-ymatrix[index]));
                totalerror += Math.abs(total - value);
                updateWeights(value, total, index);
                index++;
            }
            times++;
        }
        return this;
    }

    private void updateWeights(double value, double total, int location) {
        double result = (total - value) * value;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                weightMatrixL1[i][j] = weightMatrixL1[i][j] + learningRate * result;
            }
            weightMatrixL2[i][location] = weightMatrixL2[i][location] + learningRate * result;
        }
    }

    public double[] getYMatrix() {
        double total;
        double[] valueArray;
        double[] lastMatrix = new double[length];
        int index = 0;
        for (Double value : probArray) {
            valueArray = new double[length];
            valueArray[index] = value;
            double[] valueArrayL1 = new double[length];
            ymatrix = new double[length];
            total = 0.0;
            for (int i = 0; i < length; i++) {
                for (int j = 0; j < length; j++) {
                    total = valueArray[j] * weightMatrixL1[j][i] + total;
                }
                valueArrayL1[i] = total + valueArrayL1[i];
            }
            for (int i = 0; i < length; i++) {
                for (int j = 0; j < length; j++) {
                    ymatrix[j] = valueArrayL1[j] * weightMatrixL2[j][i] + ymatrix[j];
                }
            }
            lastMatrix[index] = ymatrix[index] + 1;
            index++;
        }

        return lastMatrix;
    }

    public List<CharWeight> getCharList() {
        List<CharWeight> charWeights = new LinkedList<>();
        double[] ymatrix = getYMatrix();
        Character[] characters = probs.keySet().toArray(new Character[ymatrix.length]);
        for (int i = 0; i < ymatrix.length; i++) {
            CharWeight charWeight = new CharWeight(characters[i], (int) ymatrix[i]);
            charWeights.add(charWeight);
        }
        return charWeights;
    }


}
