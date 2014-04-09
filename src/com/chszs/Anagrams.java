package com.chszs;

/**
 * @title: Anagrams.java
 * @description: 
 * @copyright:
 * @company: 
 * @author saizhongzhang
 * @date 2014年4月4日
 * @version 1.0
 */

public class Anagrams {
	
	public static boolean isAnagrams(String str1, String str2) {
		boolean bool = true ;
		
		if(str1.length() == str2.length()) {
			int[] strs1 = new int [256];
			int[] strs2 = new int [256];
			
			for(int i =0,len=str1.length(); i<len; i++ ) {
				strs1[(int)str1.charAt(i)]++;
				strs2[(int)str2.charAt(i)]++;
			}
			
			for(int i=0;i<256;++i){
				if(strs1[i]!=strs2[i]){
					bool = false;
				}
			}
		} else {
			bool = false;
		}
		
		return bool;
	}
	
	static String remove_duplicate(String str){
		int len = str.length();
		StringBuilder sb = new StringBuilder();
		int flags[] = new int[256];
		int i;
		for(i=0;i<len;i++){
			if(flags[(int)str.charAt(i)]!=1){
				sb.append(str.charAt(i));
			  flags[(int)str.charAt(i)]=1;
			}
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		String s = "";
		System.out.println(remove_duplicate(s));
		
//		for(char ss : remove_duplicate(s)) {
//			System.out.println(ss);
//			if (ss == '\0') {
//				break;
//			}
//		}
//		System.out.println(isAnagrams("aabbcc", "babcca"));
	}

}
