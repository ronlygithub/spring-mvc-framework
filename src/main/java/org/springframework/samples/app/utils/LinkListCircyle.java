package org.springframework.samples.app.utils;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 链表中是否存在环
 * @author Administrator
 *
 */
public class LinkListCircyle {
	
	private Node head;
	private int limit = 10;
	
	private Node createList(){
		Random random = new Random(System.nanoTime());
		int length = random.nextInt(limit);
		int i=0;
		Node p = head;
		do {
			i++;
			if (head==null) {
				head=new Node();
				head.setValue(i);
				p=head;
				continue;
			}
			
			p.next = new Node();
			p.next.setValue(i);
			p = p.next;			
			
		} while (i<length);
		return head;		
	}
	
	private boolean addCircyle(){
		if (head==null) {
			return false;
		}		
		Random random = new Random(System.nanoTime());
		int position = random.nextInt(limit/2);
		if (position >= limit) {
			return false;
		}
		Node end = head;
		Node posi = head;
		int index= 0;
		do {
			end= end.next;
			if (index <position) {
				posi= posi.next;
			}
			index++;
		} while (end!=null &&end.next!=null);
		if (position <index && end!=null) {			
			end.next= posi;
			return true;
		}
		return false;
		
	}
	
	private boolean isCircyleExist(){		
		Node fast=  head;;
		Node slow = head;
		while (fast!=null && slow !=null) {
			if (fast.next==null) {
				return false;
			}
			slow=slow.next;
			fast= fast.next.next;
			if (slow== null || fast == null) {
				return false;
			}
			if (slow == fast) {
				return true;
			}			
		}
		
		return false;
	}
	
	
	
	@Test
	public void test(){
		for (int i = 0; i < 100; i++) {
			createList();
			boolean addCircyle = addCircyle();
			boolean circyleExist = isCircyleExist();
			System.out.println(addCircyle +"   " +circyleExist);
			assertEquals(addCircyle, circyleExist);
			print();
			head=null;
			
		}
		
	}
	
	private void print(){
		Node p = head;
		int index=0;
		while(p!=null && index <=limit){
			System.out.println(p.value);
			p=p.next;
			index++;
		}
	}

	private class Node{
		private int value;
		private Node next;
		public void setValue(int value) {
			this.value = value;
		}
		
	}
}
