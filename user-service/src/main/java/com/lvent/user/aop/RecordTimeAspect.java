package com.lvent.user.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Aspect
@Component
public class RecordTimeAspect {
    // 记录方法执行耗时
    // 表示对这个包下的   所有类的     所有    具有任意形参的方法   生效
    @Around("execution(* com.lvent.user.service.impl.*.*(..))")
    public Object recordTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        // 执行业务方法
        // 执行业务方法需要引入ProProceedingJoinPoint
        // 通过调用proceed方法执行
        Object proceed = proceedingJoinPoint.proceed();
        // 获取方法的签名，用于知道当前执行的方法是哪个方法
        Signature signature = proceedingJoinPoint.getSignature();

        long end = System.currentTimeMillis();

        log.info("{} 方法耗时 {} ms", signature, end - start);
        return proceed;
    }
}
