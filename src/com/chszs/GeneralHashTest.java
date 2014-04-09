package com.chszs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class GeneralHashTest
{

   public static void main(String args[]) throws IOException
   {
	   Point pnt1 = new Point(0,0); 
	   Point pnt2 = new Point(0,0); 
	   System.out.println("X: " + pnt1.x + " Y: " +pnt1.y); 
	   System.out.println("X: " + pnt2.x + " Y: " +pnt2.y); 
	   System.out.println(" "); 
	   tricky(pnt1,pnt2); 
	   System.out.println("X: " + pnt1.x + " Y:" + pnt1.y); 
	   System.out.println("X: " + pnt2.x + " Y: " +pnt2.y);
	   

      GeneralHashFunctionLibrary ghl = new GeneralHashFunctionLibrary();

      String key = "abcdefghijklmnopqrstuvw";

      System.out.println("General Purpose Hash Function Algorithms Test");
      System.out.println("By Arash Partow - 2002\n");
      System.out.println("Key: " + key);
      System.out.println(" 5. BKDR-Hash Function Value: " + ghl.BKDRHash(key));
      System.out.println("Press 'ENTER' to exit...");

      BufferedReader stdin  = new BufferedReader(new InputStreamReader(System.in));
      stdin.readLine();

   }
   
   public static void swap(int a, int b) {
	   
	   int temp = a;
	   
	   a = b;
	   
	   b= temp;
	   System.out.println(a);
	   System.out.println(b);
   }
   public static void tricky(Point arg1, Point arg2) { 
	   arg1.x = 100; 
	   arg1.y = 100; 
	   
	   Point temp = arg1; 
	   arg1 = arg2; 
	   arg2 = temp; 
	   } 
	static class Point{
		int x ;
		int y;
		
		public Point(int x, int y) {
			this.x = x; 
			this.y = y; 
		}
	}
}
