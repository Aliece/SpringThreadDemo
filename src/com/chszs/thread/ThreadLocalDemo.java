package com.chszs.thread;

import java.util.Random;

/**
 * @title: ThreadLocalDemo.java
 * @description: 
 * @copyright:
 * @company: 
 * @author saizhongzhang
 * @date 2014年4月4日
 * @version 1.0
 */

public class ThreadLocalDemo implements Runnable{
	
	private static final ThreadLocal<Object> teacher = new ThreadLocal<>();
	
	
	public static void main(String[] args) {
		ThreadLocalDemo td = new ThreadLocalDemo();
		
		Thread a = new Thread(td, "张三");
		Thread b = new Thread(td, "李四");
		a.start();
		b.start();
	}
	
	public void accessTeacher() {
		String teacherName = Thread.currentThread().getName();
		Random random = new Random();
        int age = random.nextInt(100);
        System.out.println("thread " + teacherName + " set age to:" + age);
        Teacher teachers = getTeacher();
        teachers.setAge(age);
        System.out.println("thread " + teacherName + " first read age is:" + teachers.getAge());
        try {
            Thread.sleep(500);
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        System.out.println("thread " + teacherName + " second read age is:" + teachers.getAge());
		
	}
	
	@Override
	public void run() {
		accessTeacher();
	}
	
	
	protected Teacher getTeacher() {
		Teacher tea = (Teacher) teacher.get();
		
		if(tea == null) {
			tea = new Teacher();
			teacher.set(tea);
		}
		
		return tea;
			
	}
	class Teacher{
		
		private String name;
		private Integer age;
		
		public Teacher() {}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Integer getAge() {
			return age;
		}

		public void setAge(Integer age) {
			this.age = age;
		}
		
		
	}
}
