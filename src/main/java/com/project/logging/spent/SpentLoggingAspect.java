package com.project.logging.spent;

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
public class SpentLoggingAspect {
    private final LoggingService loggingService;

    public SpentLoggingAspect(LoggingService loggingService) {
        this.loggingService = loggingService;
    }

    @Pointcut("execution(* com.project.spent.controllers.*.*(..))")
    private void forSpentControllerPackage() {
    }

    @Pointcut("forSpentControllerPackage()")
    private void forSpentControllerFlow() {
    }

    @Before("forSpentControllerFlow()")
    public void beforeSpentController(JoinPoint joinPoint) {
        loggingService.beforeMethod(joinPoint);
    }

    @AfterReturning(
            pointcut = "forSpentControllerFlow()",
            returning = "obj"
    )
    public void afterSpentController(JoinPoint joinPoint, Object obj) {
        loggingService.afterMethod(joinPoint, obj);
    }

    @AfterThrowing(pointcut = "execution(* com.project.spent.controllers.*.*(..))", throwing = "ex")
    public void afterThrowingException(JoinPoint joinPoint, Throwable ex) {
        loggingService.doRecoveryActions(joinPoint, ex);
    }
}
