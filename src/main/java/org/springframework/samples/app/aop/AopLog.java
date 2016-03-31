package org.springframework.samples.app.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class AopLog {

	@Pointcut("execution(* org.springframework.samples.app.service.impl.*.*(..))")
	public final void execute() throws Throwable{
		System.out.println("executing");		
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

}
