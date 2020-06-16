package com.project.security.session.aop.ecommerce;

import com.project.security.session.aop.service.AopService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class SpentSessionAspect {
    private final AopService aopService;

    public SpentSessionAspect(AopService aopService) {
        this.aopService = aopService;
    }

    @Pointcut("execution(* com.project.spent.controllers.*.*(..))")
    private void forSpentControllerPackage() {
    }

    @Pointcut("forSpentControllerPackage()")
    private void forSpentControllerFlow() {
    }

    @Before("forSpentControllerFlow()")
    public void beforeSpentController() {
        aopService.beforeMethod();
    }
}
