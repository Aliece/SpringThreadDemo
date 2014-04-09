package com.chszs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * @title: BloomFilter.java
 * @description: 
 * @copyright:
 * @company: 
 * @author saizhongzhang
 * @date 2014年3月26日
 * @version 1.0
 */

public class BloomFilter 
{
	static String FILE_NAME = "E:/test/test.txt";
	static String FOLDER = "E:/test/test/";
	static final String FILE_EXTENSION = ".txt";
	static int HASH_UNIT = 1000*1000*1000;
	
    private static final int DEFAULT_SIZE = 1 << 25; 
    /* 不同哈希函数的种子，一般应取质数 */
    private static final int[] seeds = new int[] { 5, 7, 11, 13, 31, 37, 61 };
    private BitSet bits = new BitSet(DEFAULT_SIZE);
    /* 哈希函数对象 */ 
    private BKDRHash[] func = new BKDRHash[seeds.length];

    public BloomFilter() 
    {
        for (int i = 0; i < seeds.length; i++)
        {
            func[i] = new BKDRHash(DEFAULT_SIZE, seeds[i]);
        }
    }

    public void add(String value) 
    {
        for (BKDRHash f : func) 
        {
            bits.set(f.hash(value), true);
        }
    }

    public boolean contains(String value) 
    {
        if (value == null) 
        {
            return false;
        }
        boolean ret = true;
        for (BKDRHash f : func) 
        {
            ret = ret && bits.get(f.hash(value));
        }
        return ret;
    }

    public static class BKDRHash 
    {
        private int cap;
        private int seed;

        public BKDRHash(int cap, int seed) 
        {
            this.cap = cap;
            this.seed = seed;
        }

        public int hash(String value) 
        {
            int result = 0;
            int len = value.length();
            for (int i = 0; i < len; i++) 
            {
                result = seed * result + value.charAt(i);
            }
            return (cap - 1) & result;
        }
    }
    
    
    public static void main(String [] args){
    	BloomFilter bf =new BloomFilter();
    	bf.add("msdakasdashshdgba");
    	boolean bool = bf.contains("msdakasdashshdgba");
    	System.out.println(bool);
//    	readFile();
//    	readLines();
//    	changeFile(FILE_NAME,0);
    }
    
    public static void readFile() {
    	Path logFile=Paths.get(FILE_NAME);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        FileChannel channel = null;
		try {
			channel = FileChannel.open(logFile, StandardOpenOption.READ);
			
			long length = channel.size();
			
			channel.read(buffer, length-10000);// 读取文件最后的100个字符
			buffer.flip();
			while (buffer.hasRemaining()) {
			System.out.print((char)buffer.get());  
			}
			buffer.clear();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}finally {
			try {
				channel.close();
			} catch (IOException e) {
			}
		}
    }
    
    public static void readLines() {
    	
    	try (BufferedReader reader = Files.newBufferedReader(Paths.get("E:/test/test.txt"), StandardCharsets.UTF_8)) {  
            for (;;) { 
                String line = reader.readLine();  
                if (line == null)  
                    break;  
                System.out.println(line);
                }  
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  finally {
			}
    }
    
    public static void changeFile(String fName,int start){
        RandomAccessFile raf;
		try {long begin = System.currentTimeMillis();
			raf = new RandomAccessFile(fName,"r");
			long totalLen = raf.length();    
			long length = totalLen/1024;
			System.out.println("文件总长字节是: "+totalLen/1024);    
			FileChannel channel = raf.getChannel();  
			MappedByteBuffer buffer =  channel.map(FileChannel.MapMode.READ_ONLY, start, length);
			
			for (int i = 0; i <= length; i++) {
				System.out.println(i);
//	            System.out.print((char) buffer.get(i));
	        }
			long end = System.currentTimeMillis();
			
			System.out.println(end-begin);
			try {
				buffer.force();//强制输出,在buffer中的改动生效到文件    
				buffer.clear();    
				channel.close();    
				raf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}    
		} catch (Exception e) {
			e.printStackTrace();
		}    
      }    
    
    public static void test()
    {
     Path logFile=Paths.get(FILE_NAME);
     ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
     try {
    	FileChannel channel = FileChannel.open(logFile, StandardOpenOption.READ);
		channel.read(byteBuffer);
		byteBuffer.flip();
		int limit = byteBuffer.limit();
		while(limit>0)
		{
			limit--;
		}
		
		channel.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    }
    
}