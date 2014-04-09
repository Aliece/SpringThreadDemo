package com.chszs;

import java.util.Hashtable;

/**
 * @title: Node.java
 * @description:
 * @copyright:
 * @company:
 * @author saizhongzhang
 * @date 2014年4月4日
 * @version 1.0
 */

class Node {
	Node next = null;
	int data;

	public Node(int d) {
		data = d;
	}

	void appendToTail(int d) {/* put data to tail */
		Node end = new Node(d);
		Node n = this;
		while (n.next != null) {
			n = n.next;
		}
		n.next = end;
	}

	Node deleteNode(Node head, int d) {
		Node n = head;
		if (n.data == d) {
			return head.next;/* moved head */
		}
		while (n.next != null) {
			if (n.next.data == d) {
				n.next = n.next.next;
				return head; /* head didn’t change */
			}
			n = n.next;
		}
		return head;
	}
	
	static void deleteDups(Node node) {
		Node previous = node;
		Hashtable<Integer, Boolean> table = new Hashtable<>();
		while(node!=null) {
			if(table.containsKey(node.data)) {
				previous.next = node.next;
			} else {
				table.put(node.data, true);
				previous = node;
		    }
			node = node.next;
		}
		
	}
	
	public static void deleteDups2(Node head) {
	    if (head == null) return;
	    Node previous = head;
	    Node current = previous.next;
	    while (current != null) {
	      Node runner = head;
	      while (runner != current) { // Check for earlier dups
	        if (runner.data == current.data) {
	          Node tmp = current.next; // remove current
	          previous.next = tmp; 
	          current = tmp; // update current to next node
	          break; // all other dups have already been removed
	        }
	        runner = runner.next;
	      }
	      if (runner == current) { // current not updated - update now
	        previous = current;
	        current = current.next;
	      }
	    }
	 }
	
	static Node nthToLast(Node head, int n) {
	    if (head == null || n < 1) {
	      return null;
	    }
	    Node p1 = head;
	    Node p2 = head;
	    for (int j = 0; j < n - 1; ++j) { // skip n-1 steps ahead
	      if (p2 == null) {
	        return null; // not found since list size < n
	      }
	      p2 = p2.next;
	    }
	    while (p2.next != null) {
	      p1 = p1.next;
	      p2 = p2.next;
	      }
	      return p1;
	  }
	
	public static void main(String[] args) {
		
		long sta = System.nanoTime();
		
		Node head = new Node(1);
		
		head.appendToTail(2);
		head.appendToTail(3);
		head.appendToTail(4);
		head.appendToTail(3);
		head.appendToTail(3);
		head.appendToTail(5);
		head.appendToTail(3);
//		deleteDups(head);//403403 ns
//		deleteDups2(head);//8661 ns
		System.out.println(nthToLast(head,2).data);
		long end = System.nanoTime();
		System.out.println(end-sta);
		while(head!=null) {
			System.out.println(head.data);
			head = head.next;
		}
		
		
	}
}
