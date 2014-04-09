package com.chszs.trie;

import javax.swing.plaf.SliderUI;

/**
 * @title: Singleton.java
 * @description: 
 * @copyright:
 * @company: 
 * @author saizhongzhang
 * @date 2014年4月2日
 * @version 1.0
 */

public class Singleton {
    private static class SingletonHolder {
	private static final Singleton INSTANCE = new Singleton();
    }
    private Singleton (){}
    public static final Singleton getInstance() {
	return SingletonHolder.INSTANCE;
    }
    
    
    public void count() {
    	for(;;){
    		System.out.println(1);
    	}
    }
    
    
    public static void main(String[] args) {
		Singleton s = getInstance();
		s.count();
	}
}
