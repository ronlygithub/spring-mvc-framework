package org.springframework.samples.app.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;

@Aspect
public class AopLog {

	@Pointcut("execution(* org.springframework.samples.app.service.impl.*.*(..))")
	public final void execute() throws Throwable{
	}
	
	@Before(value = "execute()")
	public void before(){
		System.out.println("before execute");
	}
	
	@After(value = "execute()")
	public void after(){
		System.out.println("after execute");
	}
	
	@Around(value = "execute()")
	public void around(ProceedingJoinPoint point) throws Throwable{
		System.out.println("begin around");
		point.proceed();
		System.out.println("end around");
	}
	
	@AfterReturning(value="execute()")
	public void afterReturning(){
		System.out.println("after returning");
	}
	
	@BeforeTransaction
	public void beforeTransaction(){
		System.out.println("before transaction");
	}
	
	@AfterTransaction
	public void afterTransaction(){
		System.out.println("after transaction");
	}

}
