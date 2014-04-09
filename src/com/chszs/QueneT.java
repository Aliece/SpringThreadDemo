package com.chszs;

import java.util.Queue;
import java.util.Stack;


/**
 * @title: QueneT.java
 * @description: 
 * @copyright:
 * @company: 
 * @author saizhongzhang
 * @date 2014年4月4日
 * @version 1.0
 */

public class QueneT{
	private static Stack<Object> stackpush = new Stack<Object>();
	private static Stack<Object> stackpop = new Stack<Object>();
	
	public void QueueT(){}
	
	public Object deQueue() {
		Object o ;
		if(stackpop.isEmpty()) {
			for(int i = 1,size=stackpush.size();i<size;i++) {
				stackpop.add(stackpush.pop());
			}
			o = stackpush.pop();
		} else {
			o = stackpop.pop();
		}
		
		return o;
	}
	
	public boolean isEmpty() {
		boolean bool = (stackpop.isEmpty() && stackpush.isEmpty());
		return bool;
	}
	
	public int size() {
		int size = stackpop.size()+ stackpush.size();
		return size;
	}
	
	public void addQueue(Object o) {
		stackpush.add(o);
	}
	
	public static void main(String[] args) {
		QueneT queue = new QueneT();
		
		queue.addQueue("1");
		queue.addQueue("2");
		queue.addQueue("3");
		
		System.out.println(queue.deQueue());
		queue.addQueue("4");
		System.out.println(queue.deQueue());
		System.out.println(queue.deQueue());
		System.out.println(queue.deQueue());
		System.out.println(queue.size());
	}

}
