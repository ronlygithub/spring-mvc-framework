package org.springframework.samples.app.utils;

import java.util.Random;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class MemorySort {
	private static int length = 10024;

	private static int[] data = new int[length];
	private static int[] helper = new int[length];
	/**
	 * —°‘Ò≈≈–Ú	 * 
	 * @param data
	 */
	public void selectSort(int[] data){
		for (int i = 0; i < data.length; i++) {
			int min = i;
			for (int j = i; j < data.length; j++) {
				if (data[j] < data[min]) {
					min =j;
				}
			}
			swap(data, i, min);
		}
	}
	
	/**
	 * ≤Â»Î≈≈–Ú
	 * @param data
	 */
	public void insertSort(int[] data){
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < i; j++) {
				if (data[j]>data[i]) {
					int tmp =data[i];
					for (int k = j; k < i; k++) {
						data[k+1]=data[k];
					}
					data[j] = tmp;
				}
			}
		}
	}
	
	/**
	 * √∞≈›≈≈–Ú
	 * @param data
	 */
	public void bubleSort(int[] data){
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data.length-i-1; j++) {
				if (data[j]>data[j+1]) {
					swap(data, j, j+1);
				}
			}
		}
	}
	
	/**
	 * øÏÀŸ≈≈–Ú
	 * @param data
	 */
	public void quickSort(int[] data){
		int low =0;
		int high = data.length-1;
		quickSort(data, low, high);		
	}
	
	private void quickSort(int[] data, int low, int high){
		if (low >=high) {
			return;
		}
		int middle = partion(data, low, high);
		quickSort(data, low, middle);
		quickSort(data, middle+1, high);
	}
	
	private int partion(int[] data,int low,int high){
		int start = low;
		int end = high+1;
		while (true) {
			while (++start<end) {
				if (data[start] > data[low] ) {
					break;
				}
			}
			
			while (--end > start) {
				if (data[end] <data[low]) {
					break;
				}
			}
			
			if (start>end) {
				break;
			}
			swap(data, start, end);
		}
		swap(data, low, end);		
		return end;		
	}
	
	/**
	 * πÈ≤¢≈≈–Ú
	 */
	public void mergeSort(int[] data){
		int low =0;
		int high = data.length-1;
		mergeSort(data, low, high);
	}
	
	private void mergeSort(int[] data, int low,int high){
		if (low>=high) {
			return;
		}
		int middle = low+(high-low)/2;
		mergeSort(data,low,middle);
		mergeSort(data,middle+1,high);	
		merge(data, low, middle, high);
	}
	
	private void merge(int[] data,int low, int middle,int high){
		int lowA = low;
		int lowB = middle+1;
		for (int i = low; i <=high; i++) {
			helper[i] = data[i];
		}
		int index = low;
		while(lowA<=middle && lowB<=high){
			if (helper[lowA] < helper[lowB]) {
				data[index++] = helper[lowA++];
			}else{
				data[index++] = helper[lowB++];
			}
		}
		while (lowA<=middle) {
			data[index++]=helper[lowA++];
		}
		while (lowB <=high) {
			data[index++]= helper[lowB++];
		}		
	}
	
	public void dumpSort(int[] data){
		int len = data.length-1;		
		buildDumpSink(data);
		Assert.assertTrue(isDump(data));
		for (int i = len; i >1;i-- ) {
			swap(data, 1, i);			
			sink(data, 1, i-1);			
		}
		
	}
	
	private void buildDumpSink(int[] data){
		int length = data.length-1;
		for (int i = length/2; i >0; i--) {
			sink(data, i, length);
		}
	}	
	
	private void sink(int[] data,int k,int len){
		while (k*2<=len) {
			int index = k*2;
			if (index<len && data[index] <data[index+1]) {
				index++;
			}
			if (data[k]>data[index]) {
				break;
			}
			swap(data, k, index);
			k=index;
		}
	}
	
	@SuppressWarnings("unused")
	private void buildDumpSwim(int[] data){
		int length = data.length-1;
		for (int i = 1; i<=length; i++) {
			swim(data, i, length);
		}
	}
	
	private void swim(int[] data,int k,int len){
		while (k>1 && data[k/2]<data[k]) {
			int index = k/2;
//			if (data[k]>data[index]) {
				swap(data, k, index);
				k=index;
//			}
		}
	}
	
	@BeforeClass
	public static void  init(){
		for (int i = 0; i < data.length; i++) {
			data[i]=i;
		}
		
		for (int i = 0; i < data.length; i++) {
			Random random = new Random(System.nanoTime());
			int indexA = random.nextInt(length);
			Random randomB = new Random(System.nanoTime());
			int indexB = randomB.nextInt(length);
			swap(data, indexA, indexB);
			
		}
	}
	
	@Test
	public void test(){
		int[] clone = data.clone();
		dumpSort(clone);
		Assert.assertTrue(isSorted(clone));
	}
	public boolean isSorted(int[] data){
		for (int i = 1; i < data.length-1; i++) {
			if (data[i]>data[i+1]) {
				return false;
			}
		}
		return true;
		
	}
	
	public boolean isDump(int[] data){
		for (int i = 1; i < data.length/2; i++) {
			if (data[2*i]>data[i]) {
				return false;
			}
			
			if (2*i+1<data.length && data[2*i+1] >data[i]) {
				return false;
			}
		}
		return true;
		
	}
	
	private static void swap(int[] data,int indexA,int indexB){
		Assert.assertNotNull(data);
		Assert.assertTrue(indexA >=0&&indexA<data.length);
		Assert.assertTrue(indexB >=0&&indexB<data.length);
		int tmp = data[indexA];
		data[indexA] = data[indexB];
		data[indexB] = tmp;
	}
}
