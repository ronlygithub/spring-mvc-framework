package org.springframework.samples.app.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.AndFileFilter;
import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class FileSort {

	private static final String fileSort = ".\\fileSort.txt";

	

	@BeforeClass
	public static void init() {
		MFileUtils.prepare(fileSort);
	}

	@Test
	public void run() throws IOException {
		spliteAndSort(250);
	}

	private void mergeSort() {

	}	

	private void spliteAndSort(int n) throws IOException {
		FileInputStream in = new FileInputStream(fileSort);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		try {
			in = new FileInputStream(fileSort);
			reader = new BufferedReader(new InputStreamReader(in));
			String line = reader.readLine();
			List<Integer> buffer = new ArrayList<Integer>();
			int count = 0;
			while ((line) != null) {
				buffer.add(Integer.valueOf(line));
				count++;
				line = reader.readLine();
				if (count % n == 0 || line == null) {
					Collections.sort(buffer);
					File createTempFile = File.createTempFile("sort", ".part");
					org.apache.commons.io.FileUtils.writeLines(createTempFile,
							buffer);
					buffer.clear();
				}
			}
		} catch (Exception e) {
			throw new IOException();
		} finally {
			in.close();
			reader.close();
		}
	}

}
