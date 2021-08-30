package com.ytu.dc;

import java.io.*;
import java.util.*;

class Huffman {

    private Map<Character, String> huffmanCode = new HashMap<>();
    private Node root;
    private StringBuilder sb1 = new StringBuilder("");

    public void buildHuffmanTree(List<CharWeight> charWeights) {
        Collections.sort(charWeights);
        PriorityQueue<Node> pq = new PriorityQueue<>((l, r) -> l.freq - r.freq);
        for (CharWeight entry : charWeights) {
            pq.add(new Node(entry.getCharacter(), entry.getWeight()));
        }

        while (pq.size() != 1) {
            Node left = pq.poll();
            Node right = pq.poll();

            int sum = left.freq + right.freq;
            pq.add(new Node(null, sum, left, right));
        }

        root = pq.peek();
        encode(root, "", huffmanCode);

    }

    public void encode(Node root, String str, Map<Character, String> huffmanCode) {
        if (root == null)
            return;

        if (root.left == null && root.right == null) {
            huffmanCode.put(root.ch, str);
        }
        encode(root.left, str + "0", huffmanCode);
        encode(root.right, str + "1", huffmanCode);
    }

    public int decode(Node root, int index, String sb) {
        if (root == null)
            return index;

        if (root.left == null && root.right == null) {
            sb1.append(root.ch);
//            System.out.print(root.ch);
            return index;
        }

        index++;

        if (sb.charAt(index) == '0')
            index = decode(root.left, index, sb);
        else
            index = decode(root.right, index, sb);

        return index;
    }

    public String getEncoded(String text) {
        if (huffmanCode.size() == 0) {
            System.out.println("Please initialize tree.");
            return "ERROR";
        }
        String str = "";
        for (Character character : text.toCharArray()) {
            str += huffmanCode.get(character);
        }
        try {
            OutputStream os = new FileOutputStream(new File("texts/encoded.txt"));
            os.write(str.getBytes(), 0, str.length());
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    public String getDecoded(String encoded) {
        if (huffmanCode.size() == 0) {
            System.out.println("Please initialize tree.");
            return "ERROR";
        }
        int index = -1;
        while (index < encoded.length() - 2) {
            index = decode(root, index, encoded);
        }
        try {
            OutputStream os = new FileOutputStream(new File("texts/decoded.txt"));
            os.write(sb1.toString().getBytes(), 0, sb1.toString().length());
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "SUCCESS";
    }
}