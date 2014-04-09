package com.chszs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @title: NioMemoryMapper.java
 * @description: 
 * @copyright:
 * @company: 
 * @author saizhongzhang
 * @date 2014年3月28日
 * @version 1.0
 */

public class NioMemoryMapper {
	  
    public static void main(String[] args) throws IOException {
        int defaultbuffer=1024;
        File file=new File("E:/test/a.txt");
        FileInputStream input=new FileInputStream(file);
        FileChannel channel=input.getChannel();
        MappedByteBuffer buffer=channel.map(FileChannel.MapMode.READ_ONLY,0,channel.size());
        //准备缓冲区
        buffer.clear();
        byte []bytes=new byte[defaultbuffer];
        long begin = System.currentTimeMillis();
        int len=(int)file.length();
        for(int i=0;i<len;i=i+defaultbuffer){
            if(len-i>defaultbuffer){
                buffer.get(bytes);
            }else{
                buffer.get(new byte[len-i]);
            }
        }
         
        //写文件
        for(int i=0;i<buffer.limit();i++){
            byte b=buffer.get(i);
        }
        channel.close();
        input.close();
        System.out.println();
        long end = System.currentTimeMillis(); 
        System.out.println("time is:" + (end - begin)/1000);
    }
}
