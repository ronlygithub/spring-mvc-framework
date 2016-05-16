package org.springframework.samples.app.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

public class MyExecutor {
	private final AtomicInteger	count	= new AtomicInteger(10);
	ExecutorService				pool	= Executors.newFixedThreadPool(5);
	CompletionService<String>	service	= new ExecutorCompletionService<String>(pool);

	static {
		System.out.println("static block");
	}

	private class Task implements Callable<String> {

		private String	name;

		public Task(String name) {
			this.name = name;
		}

		@Override
		public String call() throws Exception {
			Thread.sleep(count.get() * 100);
			String result = "run " + name;
			count.getAndDecrement();
			return result;
		}

	}

	@Test
	public void test() throws InterruptedException, ExecutionException {
		List<Future<String>> list = new LinkedList<Future<String>>();
		for (int i = 0; i < 10; i++) {
			Future<String> submit = pool.submit(new Task("task " + i));
			list.add(submit);
			// System.out.println(submit.get());
		}

		for (Future<String> future : list) {
			System.out.println(future.get());
		}

		pool.shutdownNow();
	}

	@Test
	public void complete() throws InterruptedException, ExecutionException {
		List<Future<String>> list = new LinkedList<Future<String>>();
		for (int i = 0; i < 10; i++) {
			service.submit(new Task("task " + i));
		}

		for (int i = 0; i < 10; i++) {
			String string = service.take().get();
			System.out.println(string);
		}
		service.submit(new Task("test"));
		pool.shutdownNow();
	}
}

