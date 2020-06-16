package com.project.security.session.aop.sso;

import com.project.security.session.aop.service.AopService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class SecuritySessionAspect {
    private final AopService aopService;

    public SecuritySessionAspect(AopService aopService) {
        this.aopService = aopService;
    }

    @Pointcut("execution(* com.project.security.controller.*.*(..))")
    private void forSecurityControllerPackage() {
    }

    @Pointcut("forSecurityControllerPackage()")
    private void forSecurityControllerFlow() {
    }

    @Before("forSecurityControllerFlow()")
    public void beforeSecurityController() {
        aopService.beforeMethod();
    }
}
