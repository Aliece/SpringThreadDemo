package com.chszs.test;

import java.util.ArrayList;

/**
 * @title: N.java
 * @description: 
 * @copyright:
 * @company: 
 * @author saizhongzhang
 * @date 2014年4月8日
 * @version 1.0
 */

public class N {
	
	public static int countN(int[] array) {
		int n = array.length;
		int[] p = new int[n];
		int[] s = new int[n];
		int[] t = new int[n];
		s[0] = 1;
		t[n-1] = 1;
		for(int i = 1 ; i < array.length; i++){
			s[i] = s[i-1]*array[i-1];
		}
		
		for(int i = n-2 ; i >=0; i--){
			t[i] = t[i+1]*array[i+1];
		}
		
		int max = -1000000;
		
		for(int i = 0; i<n; ++i) {
			p[i] = s[i]*t[i];
	        if(p[i] > max){
	        	max = p[i];
	        }
	    }
		
		return max;
	}
	
	public static void main(String[] args) {
//		
//		int a[]={1,2,2,3,4,-5};
//		System.out.println(a[2]+a[1]);
//		System.out.println(countN(a));
		int i = 1;
		System.out.println(i--);
		System.out.println(i);
		System.out.println(--i);
		System.out.println(i);
	}

}
