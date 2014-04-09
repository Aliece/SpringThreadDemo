package com.chszs.trie;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import com.chszs.StrInfo;
import com.chszs.TopNHeap;

/**
 * @title: Trie.java
 * @description: 
 * @copyright:
 * @company: 
 * @author saizhongzhang
 * @date 2014年4月1日
 * @version 1.0
 */

public class Trie {
    private Node root = new Node();
    private File file;
    String res;
    static TopNHeap<StrInfo> heap = new TopNHeap<StrInfo>(10);
    public Trie(File file){  
        this.file = file;
    }  
    void solve() {

    	long st = System.currentTimeMillis();
		try {
    	FileReader read = new FileReader(file);
		BufferedReader br = new BufferedReader(read, 500*1024);
        String input;  
		while ((input = br.readLine()) != null) {
            insert(input.toCharArray());  
        }br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        res = "";
        long mi = System.currentTimeMillis();
        System.out.println((mi-st)/1000);
        dfs(root);//深度优先搜索字典树  
    
//        Scanner  cin = new Scanner(System.in);  
//        
//        String input;  
//        while(cin.hasNext()){//用所有字符串,构造字典树  
//            input = cin.nextLine();  
//           // if(input.equals("exit")) break;  
//            insert(input.toCharArray());  
//        }  
//        res = "";  
//        dfs(root);//深度优先搜索字典树  
    }  
    void insert(char[] str){//将一个字符串插入字典树  
        int len = str.length;  
        int k = 0, t;  
        Node p = root;  
        while(k!=len){  
            t = str[k++];  
            if(p.next[t] == null) p.next[t] = new Node();  
            p = p.next[t];  
        }  
        p.cnt++;//字符串的最后一个节点,根节点到此节点构成了一个单词,此单词的个数加1  
    }
    
    void del(Node p)
    {
    int i;
    if(p.cnt!=0) //p不为空
    {
    for(i = 0; i < 128; i++)
    if(p.next[i] != null)
    del(p.next[i]); //递归删除每一个p->next[]
    }
    p = null; 
    }
    
    void dfs(Node p){
        if(p.cnt!=0) heap.addToHeap(new StrInfo(p.cnt, res));
//        System.out.println(res + " " + p.cnt);  
        for(int i=0;i< 128;i++){//遍历p的所有儿子节点(邻接点)  
            if(p.next[i] != null){  
                res+=(char)i;  
                dfs(p.next[i]);
                res = res.substring(0, res.length()-1);//恢复现场  
            }  
        }
        del(p);
    } 
      
    public static void main(String[] args) {  
        Trie trie = new Trie(new File("E:/test/test/0.txt"));
        trie.solve();
        while (heap.hasNext()) { 
            System.out.print(heap.removeTop().toString()); 
            System.out.print("  "); 
        } 
    }  
  
}  