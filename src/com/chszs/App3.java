package com.chszs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;
import java.util.UUID;

/**
 * @title: App3.java
 * @description: 


 * @author saizhongzhang
 * @date 2014年3月21日
 * @version 1.0
 */

public class App3 {
	
	static String FILE_NAME = "E:/test/test.txt";
	static String FOLDER = "E:/test/test/";
	static final String FILE_EXTENSION = ".txt";
	static int HASH_UNIT = 1000;
	
	static Map<String, Integer> map  = new Hashtable<String,Integer>();
	static Map<String, Integer> finalMap = new Hashtable<String,Integer>();
	static Map<String, Integer> words =  new Hashtable<String, Integer>();
	
	static TreeMap<String, Integer> wordFreqs = new TreeMap<String, Integer>();
	
	
	static BloomFilter bf = new BloomFilter();
	
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		System.out.println("Start File:" + start);
//		generateFile("E:/test/a.txt", 200000000);
//		splitTxt(1000);
		divideIpsFile();
		
		long middle = System.currentTimeMillis();
		System.out.println("Middle File:" + (middle-start)/1000);
		
//		caculateFiles();
//		long thrd = System.currentTimeMillis();
//		System.out.println("Middle File:" + (thrd-middle)/1000);
		
//		display();
//		NioCopyFile();
//		readfile();
//		System.out.println("End:" + System.currentTimeMillis());
//		System.out.println("total:" + (System.currentTimeMillis()-thrd)/ 1000 );
//		long starte = System.currentTimeMillis();
//		System.out.println("Start Divide Ips File:" + starte);
//		calculate();
//		System.out.println("End:" + System.currentTimeMillis());
//		System.out.println("total:" + (System.currentTimeMillis()-starte));
	}
	
	static class ByValueComparator implements Comparator<String> {

		TreeMap<String, Integer> base_map;
		public ByValueComparator(TreeMap<String, Integer> base_map) {

		this.base_map = base_map;

		}

		public int compare(String arg0, String arg1) {

		if (!base_map.containsKey(arg0) || !base_map.containsKey(arg1)) {

		return 0;

		}

		if (base_map.get(arg0) < base_map.get(arg1)) {

		return 1;

		} else if (base_map.get(arg0) == base_map.get(arg1)) {

		return 0;

		} else {
		return -1;
		}
		}
		}
	
	public static void display() {
		TopNHeap<Integer> heap = new TopNHeap<Integer>(10);
		
		List<String> newList=new ArrayList<String>(words.keySet());
		for(String str:newList){
			heap.addToHeap(words.get(str));
//			if(wordFreqs.get(str)>=2){
//				System.out.println(str+"="+wordFreqs.get(str));
//			}
		}
		
        while (heap.hasNext()) { 
            System.out.print(heap.removeTop()); 
            System.out.print("  "); 
        } 
		
		
	}

	
	public static void dispalys() {
		TreeMap<Integer, List<String>> wordFreqs = 
                new TreeMap<Integer, List<String>>(
            new Comparator<Integer>() {
                // Descending order
                @Override
                public int compare(Integer a, Integer b) {
                    return a == b ? 0 : (a < b ? 1 : -1);
                }
            }
        );
 
        for (Map.Entry<String, Integer> e : words.entrySet()) {
            if (!wordFreqs.containsKey(e.getValue())) {
                ArrayList<String> wordList = new ArrayList<String>();
                wordList.add(e.getKey());
                wordFreqs.put(e.getValue(), wordList);
            } else {
                wordFreqs.get(e.getValue()).add(e.getKey());
            }
        }
 
        int highestFreq = wordFreqs.firstKey();
        System.out.println(wordFreqs.get(highestFreq) + 
                " => " + highestFreq);
	}
	
	
	public static String getRandomString(int length)
	{
	String str="abcdefghigklmnopkrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ0123456789";
	Random random=new Random();
	StringBuffer sf=new StringBuffer();
	for(int i=0;i<length;i++)
	{
	 int number=random.nextInt(62);//0~61
	 sf.append(str.charAt(number));
	 

	}
	return sf.toString();
	}
	
	private static String generateIp() {
		
		return new Random().nextInt(100)+"." + (int) (Math.random() * 255) + "."  
        + (int) (Math.random() * 255) + "\n";  
	}
	private static void generateFile(String FILE_NAME, int MAX_NUM) {
		File file = new File(FILE_NAME);
		try {
			
			BufferedWriter writer  = new BufferedWriter(new FileWriter(file)); 
			for (int i = 0; i < MAX_NUM; i++) {
				writer.write(getRandomString((int)(Math.random()*10+5)));
				writer.newLine();
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private static String hash(long hashCode) {
		return String.valueOf(Math.abs(hashCode%HASH_UNIT));
	}
	
    public File getFileByHashCode(int hashCode){  
        int i = hashCode%HASH_UNIT; 
        return new File(FOLDER+i+"_"+ i + 1 + FILE_EXTENSION);  
    } 
    
    public static long BKDRHash(String str)
    {
       long seed = 131;
       long hash = 0;

       for(int i = 0; i < str.length(); i++)
       {
          hash = (hash * seed) + str.charAt(i);
       }

       return hash;
    }


	public static void readfile() {
		
		try {
			File files = new File(FILE_NAME);
			FileChannel channel = new RandomAccessFile(files, "r").getChannel();	
			MappedByteBuffer buff = channel.map(FileChannel.MapMode.READ_ONLY, 0 , channel.size());

			byte[] b = new byte[1024];
			int len = (int) buff.limit();

			long begin = System.currentTimeMillis();
			

			for (int offset = 0; offset < len; offset += 1024) {

				if (len - offset > 1024) {
					buff.get(b);
				} else {
					buff.get(new byte[len - offset]);
				}
			}

			long end = System.currentTimeMillis();
			System.out.println("time is:" + (end - begin));

		} catch (Exception e) {
			System.out.println(e);
		}

	}
	
	@SuppressWarnings("resource")
	public static void NioCopyFile() {
		long before = System.currentTimeMillis();
		
		try {
			File files = new File(FILE_NAME);	//源文件

			long size = files.length(); 		// 文件总大小
			long copycount = size * 2 / Integer.MAX_VALUE; //获取读、写之和所占用虚拟内存 倍数
			int copynum = copycount >= 1 ? (int) copycount + 3 : (int) copycount + 2; // 根据倍数确认分割份数

			long countSize = Integer.MAX_VALUE  / copynum; 	//每块分割大小<每次读写的大小>															
			long lontemp = countSize;		//初始读、写大小
			FileChannel channels = new RandomAccessFile(files, "r").getChannel();		//得到映射读文件的通道
			long j = 0; // 每次循环累加字节的起始点
			MappedByteBuffer mbbs = null; // 声明读源文件对象
			while (j < size) {
				mbbs = channels.map(FileChannel.MapMode.READ_ONLY, j, lontemp);		//每次读源文件都重新构造对象
				for (int i = 0; i < lontemp; i++) {
					
					
					
				}
				System.gc();				//手动调用 GC		<必须的，否则出现异常>
				System.runFinalization();	//运行处于挂起终止状态的所有对象的终止方法。<必须的，否则出现异常>
				j += lontemp;				//累加每次读写的字节
				lontemp = size - j;			//获取剩余字节
				lontemp = lontemp > countSize ? countSize : lontemp;	//如果剩余字节 大于 每次分割字节 则 读取 每次分割字节 ，否则 读取剩余字节
			}
			System.out.println("MillTime : "
					+ (double) (System.currentTimeMillis() - before) / 1000 + "s");
		} catch (Exception e) {
			System.out.println(e);
		}
		
		
	}
	
	private static void splitFile() {
		  final int BUFFER_SIZE = 0x500000;

		  File f = new File("E:/test/test.txt");
		  long start = System.currentTimeMillis();
		  
		  try {
			  for( int j = 0; j <100; j++ ) {

				  MappedByteBuffer inputBuffer = new RandomAccessFile(f, "r").getChannel().map(FileChannel.MapMode.READ_ONLY, (f.length())*j/1000 , f.length() / 1000);

				  byte[] dst = new byte[BUFFER_SIZE];


				  for (int offset = 0; offset < inputBuffer.capacity(); offset += BUFFER_SIZE) {

				   if (inputBuffer.capacity() - offset >= BUFFER_SIZE) {

				    for (int i = 0; i < BUFFER_SIZE; i++)

				     dst[i] = inputBuffer.get(offset + i);

				   } else {

				    for (int i = 0; i < inputBuffer.capacity() - offset; i++)

				     dst[i] = inputBuffer.get(offset + i);

				   }
				   
				   File file = new File("E:/test/test/"+j+"_"+ j + ".txt");
				   FileWriter fileWriter = new FileWriter(file, true);
				   int length = (inputBuffer.capacity() % BUFFER_SIZE == 0) ? BUFFER_SIZE : inputBuffer.capacity() % BUFFER_SIZE;
				   fileWriter.write(new String(dst, 0, length));
				   fileWriter.close();

				   // String(dst,0,length)这样可以取出缓存保存的字符串，可以对其进行操作

				  }
			  }

			  long end = System.currentTimeMillis();

			  System.out.println("读取文件内容花费：" + (end - start)/1000 + "秒");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private static void caculateFiles() {

		File folder = new File(FOLDER);
		File[] files = folder.listFiles();
		FileReader fileReader = null;
		BufferedReader br;
		for (File file : files) {
			try {
				fileReader = new FileReader(file);
				br = new BufferedReader(fileReader, 512*1024);
				String line;
				while ((line = br.readLine()) != null) {
					if (!words.containsKey(line)) {
						words.put(line, 1);
		            } else {
		            	words.put(line, words.get(line) + 1);
		            }
				}
				br.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
					fileReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void splitTxt(int count) {
		try {
//			File file = new File("E:/test/a.txt");
			File file = new File(FILE_NAME);
			FileReader read = new FileReader(file);
			BufferedReader br = new BufferedReader(read, 1024);
			String row;
			List<FileWriter> flist = new ArrayList<FileWriter>();
			for (int i = 0; i < count; i++) {
				File ipFile = new File(FOLDER+i+"_"+ i + FILE_EXTENSION);
				flist.add(new FileWriter(ipFile));
			}
			Map<Integer, StringBuilder> map  = new Hashtable<Integer,StringBuilder>();
			StringBuilder sb = new StringBuilder();
			
			int rownum = 1;
			while ((row = br.readLine()) != null) {
				Integer key = rownum % count;
				if(map.containsKey(rownum % count)) {
					sb = map.get(rownum % count);
					sb.append(row).append("\n");
					map.put(key, sb);
				}else {
					sb = new StringBuilder(row);
					sb.append("\n");
					map.put(key, sb);
				}
				
				rownum ++;
				if(rownum == 2000000){
					Iterator<Integer> it = map.keySet().iterator();					
					while(it.hasNext()){
						Integer keys = it.next();
						sb = map.get(keys);
						FileWriter fileWriter = flist.get(keys);
						fileWriter.write(sb.toString());
//					}
					rownum = 1;
					map.clear();
				}
				}
				
//				flist.get(rownum % count).append(row + "\n");
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
	private static void divideIpsFile() {
		File file = new File("E:/test/test/test.txt");
//		File file = new File("E:/test/a.txt");
		Map<String, StringBuilder> map  = new Hashtable<String,StringBuilder>();
		int count = 0;
		try {
			FileReader fileReader = new FileReader(file);
			BufferedReader br = new BufferedReader(fileReader, 512*1024);
			String line;
			
			while ((line = br.readLine()) != null) {
				String hashIp = hash(line.hashCode());
				if(map.containsKey(hashIp)){
					map.put(hashIp, map.get(hashIp).append(line).append("\n"));
				}else{
					map.put(hashIp, new StringBuilder(line).append("\n"));
				}
				count++;
				if(count == 4000000){
					Iterator<String> it = map.keySet().iterator();					
					while(it.hasNext()){
						String fileName = it.next();
						File ipFile = new File(FOLDER+fileName+"_"+ fileName + FILE_EXTENSION);
						FileWriter fileWriter = new FileWriter(ipFile, true);
						StringBuilder sb = map.get(fileName);				
						fileWriter.write(sb.toString());
						fileWriter.close();
					}
					count = 0;
					map.clear();
					System.gc();
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void calculate() {
		File folder = new File(FOLDER);
		File[] files = folder.listFiles();
		FileReader fileReader;
		BufferedReader br;
		for (File file : files) {
			try {
				fileReader = new FileReader(file);
				br = new BufferedReader(fileReader);
				String ip;
				Map<String, Integer> tmpMap = new Hashtable<String, Integer>();
				while ((ip = br.readLine()) != null) {
					if (tmpMap.containsKey(ip)) {
						int count = tmpMap.get(ip);
						tmpMap.put(ip, count + 1);
					} else {
						tmpMap.put(ip, 0);
					}
				}	
				fileReader.close();
				br.close();
				count(tmpMap,map);
				tmpMap.clear();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		count(map,finalMap);		
		Iterator<String> it = finalMap.keySet().iterator();
		while(it.hasNext()){
			String ip = it.next();
			System.out.println("result IP : " + ip + " | count = " + finalMap.get(ip));
		}
		
	}		

	private static void count(Map<String, Integer> pMap, Map<String, Integer> resultMap) {
		Iterator<Entry<String, Integer>> it = pMap.entrySet().iterator();
		int max = 0;
		String resultIp = "";
		while (it.hasNext()) {
			Entry<String, Integer> entry = (Entry<String, Integer>) it.next();
			if (entry.getValue() > max) {
				max = entry.getValue();
				resultIp = entry.getKey();
			}
		}
		resultMap.put(resultIp,max);	
	}
	

}
