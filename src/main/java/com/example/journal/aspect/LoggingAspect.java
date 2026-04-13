package com.example.journal.aspect;

import com.example.journal.annotations.LogRequest;
import com.example.journal.annotations.LogResponse;
import com.example.journal.annotations.LogTime;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;


@Component
@Aspect
@Slf4j
public class LoggingAspect {
    private final ObjectMapper mapper = new ObjectMapper();

    @Before("@annotation(logRequest)")
    public void logRequest(JoinPoint joinPoint, LogRequest logRequest) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getName();

        Object[] args = joinPoint.getArgs();
        String request = mapper.writeValueAsString(args);

        log.info("Request | {}.{} Body: {}", className, methodName, request);
    }

    @Around("@annotation(logResponse)")
    public Object logRequest(ProceedingJoinPoint joinPoint, LogResponse logResponse) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getName();

        Object result = joinPoint.proceed();
        String responseBody = mapper.writeValueAsString(result);

        log.info("Response | {}.{} Body: {}", className, methodName, responseBody);

        return result;
    }

    @Around("@annotation(logTime)")
    public Object logTime(ProceedingJoinPoint joinPoint, LogTime logTime) throws Throwable {
        long startTime = System.currentTimeMillis();

        // Execute method
        String methodName = joinPoint.getSignature().toShortString();
        Object result = joinPoint.proceed();

        long timeTaken = System.currentTimeMillis() - startTime;

        log.info("Method {} executed in {} ms", methodName, timeTaken);
        return result;
    }
}
