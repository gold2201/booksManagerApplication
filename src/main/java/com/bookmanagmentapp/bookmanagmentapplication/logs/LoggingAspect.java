package com.bookmanagmentapp.bookmanagmentapplication.logs;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    @Around("execution(* com.bookmanagmentapp.bookmanagmentapplication.service.*.*(..))")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        log.info("▶️ Вызов метода: {}", methodName);
        Object result;
        try {
            result = joinPoint.proceed();
            log.info("✅ Метод выполнен успешно: {}", methodName);
        } catch (Exception ex) {
            log.error("❌ Ошибка в методе {}: {}", methodName, ex.getMessage(), ex);
            throw ex;
        }
        return result;
    }
}

