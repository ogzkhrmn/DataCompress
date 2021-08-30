package com.ytu.dc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainClass {

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        String in = null;
        try {
            in = new String(Files.readAllBytes(Paths.get("mids.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Compress compress = new Compress();
        compress.initialize(in);
        compress.setWeights();
        Huffman huffman = new Huffman();
        huffman.buildHuffmanTree(compress.getCharList());
        String decoded = huffman.getEncoded(in);
        long endTime   = System.nanoTime();
        int last = decoded.length();
        int first = in.length() * 8;
        System.out.println("First : " + first + " Last: " + last + "\nCompression Ratio = " + ((float) (first - last) / first) * 100.0);
        huffman.getDecoded(decoded);
        long totalTime = endTime - startTime;
        double elapsedTimeInSecond = (double) totalTime / 1_000_000_000;
        System.out.println(elapsedTimeInSecond);
    }

}
