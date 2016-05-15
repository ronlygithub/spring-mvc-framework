package org.springframework.samples.app.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.springframework.util.StringUtils;

/**
 * 大文件内容去重
 * 1.通过hash方法将该文件拆分为N个小文件，单个小文件可在内存处理，相同的内容落在同一个小文件中
 * 
 * @author Administrator
 *
 */
public class FileMerge {
	private static final String fileName = ".\\testFileMerge.txt";
	public void removeDuplicate(String fileName) throws IOException{
		int n = 5;
		fileSplite(fileName, n);
		for (int i = 1; i <= n; i++) {
			preProcess(fileName+"_"+i, fileName+"processed");
		}		
	}	
	
	public void preProcess(String fileName, String toFileName) throws IOException{
		String reader = MFileUtils.reader(fileName);
		String[] split = reader.split("\n");
		Set<String> set = new HashSet<String>();
		for (String item : split) {
			set.add(item);
		}
		
		StringBuffer buffer = new StringBuffer();
		for (String item : set) {
			if (StringUtils.isEmpty(item)) {
				continue;
			}			
			buffer.append(item).append("\n");
		}
		MFileUtils.writer(buffer.toString(), toFileName);
	}
	
	public void run() throws IOException{
		prepare();
		removeDuplicate(fileName);
	}
	
	
	
	public void prepare(){
		for (int i = 0; i < 10; i++) {
			StringBuffer result = new StringBuffer();
			for (int j = 0; j < 100; j++) {
				Random r = new Random(System.nanoTime());
				int value = r.nextInt(1000);
				result.append(value).append("\n");
			}
			MFileUtils.writer(result.toString(), fileName);
		}
		
	}
	
	public void fileSplite(String fileName, int n) throws IOException{
		if (StringUtils.isEmpty(fileName)) {
			throw new IllegalArgumentException("fileName can not be null");
		}		
		FileInputStream in =null;
		BufferedReader bufferedReader = null;
		try {
			in = new FileInputStream(fileName);
			InputStreamReader reader = new InputStreamReader(in);
			bufferedReader = new BufferedReader(reader);
			String line = null;
			while ((line = bufferedReader.readLine())!= null) {
				int index = (line.hashCode()%n)+1;
				MFileUtils.writer(line+"\n", fileName+"_"+index);
			}
		} catch (Exception e) {			
			throw new IOException("文件拆分错误");
		}finally{
			in.close();
			bufferedReader.close();
		}
		
	}
}
