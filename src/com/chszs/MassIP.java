package com.chszs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.chszs.trie.Node;

/**
 * @title: MassIP.java
 * @description: 
 * @copyright:
 * @company: 
 * @author saizhongzhang
 * @date 2014年4月3日
 * @version 1.0
 */

/**
 * 提取出某日访问百度次数最多的那K个IP。<br>
 * 注意，此处简化起见，没有处理重复的情况，即有多个ip出现次数一样多
 * 
 * @author Administrator
 * 
 */
public class MassIP {
	public static int K10 = 1024 * 10;
	public static int M1 = 1024 * 1024;
	public static int M4 = 4 * M1;
	public static int M50 = 50 * M1;
	private static int partationCount = 100;
	private static String FILE_NAME = "E:/test/test.txt";
	private static String FOLDER = "E:/test/test/";

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		
		long st = System.currentTimeMillis();
//		generateMassIp("ip", "ips.txt", 100000000);
//		generatePartitionTrie(FOLDER, "test.txt", 1000);
//		generatePartitionFile(FOLDER, "test.txt", 100);
//		searchTopN(10);
		searchsTopN(10);
		// searchTopN2("ip", "ips.txt", 10);
		long mi = System.currentTimeMillis();
		System.out.println((mi-st)/1000+"s");
		
	}
	/**
	 * 如果是1亿条记录，直接放到内存排序会如何呢？
	 * 
	 * @param srcDirName
	 * @param srcFileName
	 * @param count
	 * @throws IOException
	 */
	public static void searchTopN2(String srcDirName, String srcFileName,
			int count) throws IOException {
		Map<String, Integer> ipCountMap = new HashMap<String, Integer>();
		File file = FileUtils.getFile(srcDirName, srcFileName);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file), M50);
			String s;
			int i = 0, j = 0;
			while ((s = br.readLine()) != null) {
				Integer cnt = ipCountMap.get(s);
				if (cnt == null) {
					i++;
					if (i % 10000000 == 0) {
						System.out.println(i);
					}
				}
				j++;
				if (j % 10000000 == 0) {
					System.out.println(j);
				}
				ipCountMap.put(s, cnt == null ? 1 : cnt + 1);
			}
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		TopNHeap<StrInfo> heap = new TopNHeap<StrInfo>(count);
		searchMostCountIps(ipCountMap, heap);
		printResult(heap);
	}

	/**
	 * 查找出现次数最多的K个ip地址
	 * 
	 * @param count
	 * @throws IOException
	 */
	public static void searchTopN(int count) throws IOException {
		File[] smallFiles = getPartitionFile(FOLDER, partationCount);
		DataInputStream dis = null;
		Map<String, Integer> ipCountMap = new HashMap<String, Integer>();
		TopNHeap<StrInfo> heap = new TopNHeap<StrInfo>(count);
		for (int i = 0; i < partationCount; i++) {
			ipCountMap.clear();
			try {
				dis = new DataInputStream(new BufferedInputStream(
						new FileInputStream(smallFiles[i]), M50));
				while (dis.available() > 0) {
					String ip = dis.readLine();
					Integer cnt = ipCountMap.get(ip);
					ipCountMap.put(ip, cnt == null ? 1 : cnt + 1);
				}
				searchMostCountIps(ipCountMap, heap);
			} finally {
				if (dis != null) {
					try {
						dis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		printResult(heap);
	}

	/**
	 * 打印结果集
	 * 
	 * @param heap
	 */
	private static void printResult(TopNHeap<StrInfo> heap) {
		while (heap.hasNext()) {
			System.out.println(heap.removeTop().toString());
		}
	}

	/**
	 * 查找出现次数最多的ip
	 * 
	 * @param map
	 * @param heap
	 */
	private static void searchMostCountIps(Map<String, Integer> map,
			TopNHeap<StrInfo> heap) {
		Iterator<String> iter = map.keySet().iterator();
		String key = null;
		while (iter.hasNext()) {
			key = iter.next();
			int count = map.get(key);
			if (!heap.isFull() || count > heap.getHeapTop().getCount()) {
				heap.addToHeap(new StrInfo(count, key));
			}
		}
	}

	/**
	 * 把32位int值转换成ip字符串
	 * 
	 * @param value
	 * @return
	 */
	public static String parseInt2Ip(int value) {
		StringBuilder sb = new StringBuilder(15);
		String[] segs = new String[4];
		for (int i = 0; i < 4; i++) {
			segs[3 - i] = String.valueOf((0xFF & value));
			value >>= 8;
		}
		for (int i = 0; i < 4; i++) {
			sb.append(segs[i]).append(".");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	/**
	 * 把ip字符串转换成int值
	 * 
	 * @param ip
	 * @return
	 */
	public static int parseIp2Int(String ip) {
		String[] segs = ip.split("\\.");
		int rst = 0;
		for (int i = 0; i < segs.length; i++) {
			rst = (rst << 8) | Integer.valueOf(segs[i]);
		}
		return rst;
	}

	/**
	 * 随机生成ip
	 * 
	 * @param srcDirName
	 * @param srcFileName
	 * @param count
	 * @throws IOException
	 */
	public static void generateMassIp(String srcDirName, String srcFileName,
			int count) throws IOException {}

	public static File[] getPartitionFile(String srcDirName, int count)
			throws IOException {
		File[] files = new File[count];
		for (int i = 0; i < count; i++) {
			files[i] = FileUtils.getFile(srcDirName, i+"_"+i + ".txt");
		}
		return files;
	}

	/**
	 * 分割大文件到小文件中
	 * 
	 * @param srcDirName
	 * @param srcFileName
	 * @param count
	 * @return
	 * @throws IOException
	 */
	public static File[] generatePartitionFile(String srcDirName,
			String srcFileName, int count) throws IOException {
		File[] files = new File[count];
		DataOutputStream[] dops = new DataOutputStream[count];
		for (int i = 0; i < count; i++) {
			files[i] = new File(srcDirName+i + ".txt");
		}
		File file = FileUtils.getFile(srcDirName, srcFileName);
		BufferedReader br = null;
		// Buffered
		try {
			for (int i = 0; i < count; i++) {
				dops[i] = new DataOutputStream(new BufferedOutputStream(
						new FileOutputStream(files[i]), K10));
			}
			br = new BufferedReader(new FileReader(file), M50);
			String s;
			while ((s = br.readLine()) != null) {
				int ip = s.hashCode();
				dops[Math.abs(ip % count)].writeBytes(s+"\n");
			}
		} finally {
			for (int i = 0; i < count; i++) {
				if (dops[i] != null) {
					try {
						dops[i].close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return files;
	}
	
	public static void searchsTopN(int count) throws IOException {
		int partationCount = 1000;
		File[] smallFiles = getPartitionFile(FOLDER, partationCount);
		DataInputStream dis = null;
//		Map<String, Integer> ipCountMap = new HashMap<String, Integer>();
		TopNHeap<StrInfo> heap = new TopNHeap<StrInfo>(count);
		for (int i = 0; i < partationCount; i++) {
			Node root = new Node();
			Trie trie = new Trie(heap,root);
			try {
				dis = new DataInputStream(new BufferedInputStream(
						new FileInputStream(smallFiles[i]), M50));
				while (dis.available() > 0) {
					String ip = dis.readLine();
					trie.insert(ip.toCharArray());
//					Integer cnt = ipCountMap.get(ip);
//					ipCountMap.put(ip, cnt == null ? 1 : cnt + 1);
				}
				trie.dfs(root);
//				searchMostCountIps(ipCountMap, heap);
			} finally {
				if (dis != null) {
					try {
						dis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		printResult(heap);
	}
	
static class Trie {
	    private Node root;
	    String res;
	    TopNHeap<StrInfo> heap;
	    public Trie(TopNHeap<StrInfo> heap,Node root){  
	        this.heap = heap;
	        this.root = root;
	    }  
	    void solve() {

	    	long st = System.currentTimeMillis();
//			try {
//	    	FileReader read = new FileReader(file);
//			BufferedReader br = new BufferedReader(read, 500*1024);
//			DataInputStream dis = new DataInputStream(new BufferedInputStream(
//					new FileInputStream(new File("E:/test/0.txt")), 50*1024));
//			while (dis.available() > 0) {
//				int ip = dis.readInt();
//			}
//	        String input;  
//			while ((input = br.readLine()) != null) {
//	        insert(input.toCharArray());  
//	        }br.close();
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//	        res = "";
//	        long mi = System.currentTimeMillis();
//	        System.out.println((mi-st)/1000);
//	        dfs(root);//深度优先搜索字典树  
	    
//	        Scanner  cin = new Scanner(System.in);  
//	        
//	        String input;  
//	        while(cin.hasNext()){//用所有字符串,构造字典树  
//	            input = cin.nextLine();  
//	           // if(input.equals("exit")) break;  
//	            insert(input.toCharArray());  
//	        }  
//	        res = "";  
//	        dfs(root);//深度优先搜索字典树  
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
//	        System.out.println(res + " " + p.cnt);  
	        for(int i=0;i< 128;i++){//遍历p的所有儿子节点(邻接点)  
	            if(p.next[i] != null){  
	                res+=(char)i;  
	                dfs(p.next[i]);
	                res = res.substring(0, res.length()-1);//恢复现场  
	            }  
	        }
	        del(p);
	    } 
	}  
}
