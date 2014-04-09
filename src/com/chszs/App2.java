package com.chszs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.access.intercept.RunAsManager;

import com.chszs.config.AppConfig;
import com.chszs.thread.PrintTask2;

public class App2 {
	public static void mai(String[] args) {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
		ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor)ctx.getBean("taskExecutor");
		
		PrintTask2 printTask1 = (PrintTask2)ctx.getBean("printTask2");
		printTask1.setName("Thread 1");
		taskExecutor.execute(printTask1);
		
		PrintTask2 printTask2 = (PrintTask2)ctx.getBean("printTask2");
		printTask2.setName("Thread 2");
		taskExecutor.execute(printTask2);
		
		PrintTask2 printTask3 = (PrintTask2)ctx.getBean("printTask2");
		printTask3.setName("Thread 3");
		taskExecutor.execute(printTask3);
		
		while(true) {
			int count = taskExecutor.getActiveCount();
			System.out.println("Active Threads : " + count);
			try{
				Thread.sleep(1000);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			if(count==0){
				taskExecutor.shutdown();
				break;
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		

		  final int BUFFER_SIZE = 0x500000;

		  File f = new File("E:/test/test.txt");
		  long start = System.currentTimeMillis();
		  
		  
		  for( int j = 0; j <100; j++ ) {

			  MappedByteBuffer inputBuffer = new RandomAccessFile(f, "r").getChannel().map(FileChannel.MapMode.READ_ONLY, (f.length())*j/100 , f.length() / 100);

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

		}
	
}
