package com.chszs;

/**
 * @title: TestNio.java
 * @description: 
 * @copyright:
 * @company: 
 * @author saizhongzhang
 * @date 2014年3月27日
 * @version 1.0
 */

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Hashtable;
import java.util.Map;

	public class TestNio { 

		static Map<String, Integer> words =  new Hashtable<String, Integer>();
		public static void main(String args[]) throws Exception{ 

		long start = System.currentTimeMillis();
		int bufSize = 1024*1024*5;
		File fin = new File("E:/test/test.txt"); 

		@SuppressWarnings("resource")
		FileChannel fcin = new RandomAccessFile(fin, "r").getChannel(); 
		
//		MappedByteBuffer rBuffer = fcin.map(FileChannel.MapMode.READ_ONLY, 0, fin.length());
		ByteBuffer rBuffer = ByteBuffer.allocate(bufSize);

		readFileByLine(bufSize, fcin, rBuffer); 
		System.out.println("total:" + (System.currentTimeMillis()-start)/ 1000 );
		System.out.print("OK!!!"); 
		} 

		public static void readFileByLine(int bufSize, FileChannel fcin, ByteBuffer rBuffer){ 
			String enterStr = "\n"; 
			int bit = 0;
			try{ 
			byte[] bs = new byte[bufSize]; 

			StringBuffer strBuf = new StringBuffer(); 
			BloomFilter bf = new BloomFilter();
			while(fcin.read(rBuffer) != -1){
			      int rSize = rBuffer.position(); 
			      rBuffer.rewind(); 
			      rBuffer.get(bs); 
			      rBuffer.clear();
			      String tempString = new String(bs, 0, rSize);
//
//			      int fromIndex = 0; 
//			      int endIndex = 0; 
//			      while((endIndex = tempString.indexOf(enterStr, fromIndex)) != -1){ 
//			       String line = tempString.substring(fromIndex, endIndex); 
//			       line = new String(strBuf.toString() + line);
//			       if (!words.containsKey(line)) {
//		                words.put(line, 1);
//		            } else {
//		                words.put(line, words.get(line) + 1);
//		            }
//
//			       strBuf.delete(0, strBuf.length()); 
//			       fromIndex = endIndex + 1; 
//			      } 
//			      if(rSize > tempString.length()){ 
//			      strBuf.append(tempString.substring(fromIndex, tempString.length())); 
//			      }else{ 
//			      strBuf.append(tempString.substring(fromIndex, rSize)); 
//			      } 
			} 
			} catch (IOException e) { 
			e.printStackTrace(); 
			} 
		} 
		
}
