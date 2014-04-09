package com.chszs.trie;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @title: Node.java
 * @description: 
 * @copyright:
 * @company: 
 * @author saizhongzhang
 * @date 2014年4月1日
 * @version 1.0
 */

public class Node {
	public Node next[] = new Node[128];//所有儿子节点  
    public int cnt;/* 用于记录单词出现的次数，若cnt大于0，说明   
                 从根节点到此节点的父节点构成了一个单词，这个   
                 单词的次数就是cnt */    
    public Node(){  
        cnt = 0;  
    } 
}
