package com.project.logging.files;

import com.project.logging.service.LoggingService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class FilesLoggingAspect {
    private final LoggingService loggingService;

    public FilesLoggingAspect(LoggingService loggingService) {
        this.loggingService = loggingService;
    }

    @Pointcut("execution(* com.project.files.controllers.*.*(..))")
    private void forFilesControllerPackage() {
    }

    @Pointcut("forFilesControllerPackage()")
    private void forFilesControllerFlow() {
    }

    @Before("forFilesControllerFlow()")
    public void beforeFilesController(JoinPoint joinPoint) {
        loggingService.beforeMethod(joinPoint);
    }

    @AfterReturning(
            pointcut = "forFilesControllerFlow()",
            returning = "obj"
    )
    public void afterFilesController(JoinPoint joinPoint, Object obj) {
        loggingService.afterMethod(joinPoint, obj);
    }

    @AfterThrowing(pointcut = "execution(* com.project.files.controllers.*.*(..))", throwing = "ex")
    public void afterThrowingException(JoinPoint joinPoint, Throwable ex) {
        loggingService.doRecoveryActions(joinPoint, ex);
    }
}
