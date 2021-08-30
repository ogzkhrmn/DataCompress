package com.ytu.dc;

class Node {
    Character ch;
    int freq;
    Node left = null, right = null;

    Node(Character ch, int freq) {
        this.ch = ch;
        this.freq = freq;
    }

    public Node(Character ch, int freq, Node left, Node right) {
        this.ch = ch;
        this.freq = freq;
        this.left = left;
        this.right = right;
    }
}