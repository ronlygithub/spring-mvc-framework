package org.springframework.samples.app.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.springframework.util.StringUtils;

public class MFileUtils {

	public static String reader(String path) throws IOException{
		if (StringUtils.isEmpty(path)) {
			return null;
		}
		StringBuffer result = new StringBuffer();
		char[] buffer = new char[4096];
		
			FileInputStream in = new FileInputStream(path);
			InputStreamReader reader = new InputStreamReader(in, "utf-8");
			while(reader.read(buffer)!=-1){
				result.append(buffer);
				buffer = new char[4096];
			}
			reader.close();		
		return result.toString();
		
	}
	
	public static void writer(String resultSet ,String path){
		try {
			FileOutputStream out = new FileOutputStream(path,true);
			OutputStreamWriter writer = new OutputStreamWriter(out,"utf-8");			
			writer.write(resultSet.toString());
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static  void preProcess(String fileName, String toFileName) throws IOException{
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
			String[] property = item.split(",");
			if (property == null || property.length<=1) {
				continue;
			}
			buffer.append(item).append("\n");
		}
		MFileUtils.writer(buffer.toString(), toFileName);
	}
	
	public static void prepare(String fileName){
		for (int i = 0; i < 10; i++) {
			StringBuffer result = new StringBuffer();
			for (int j = 0; j < 100; j++) {
				Random r = new Random(System.nanoTime());
				int value = r.nextInt(1000000);
				result.append(value).append("\n");
			}
			MFileUtils.writer(result.toString(), fileName);
		}
		
	}
	
}
