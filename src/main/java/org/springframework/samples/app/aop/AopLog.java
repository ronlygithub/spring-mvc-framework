package org.springframework.samples.app.aop;

import org.aspectj.lang.ProceedingJoinPoint;

public class AopLog {

	public void execute(ProceedingJoinPoint point) throws Throwable{
		System.out.println("begin around");
		point.proceed();
		System.out.println("end around");
	}

}
