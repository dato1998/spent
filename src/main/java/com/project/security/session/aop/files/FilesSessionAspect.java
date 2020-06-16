package com.project.security.session.aop.files;

import com.project.security.session.aop.service.AopService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class FilesSessionAspect {
    private final AopService aopService;

    public FilesSessionAspect(AopService aopService) {
        this.aopService = aopService;
    }

    @Pointcut("execution(* com.project.files.controllers.*.*(..))")
    private void forFilesControllerPackage() {
    }

    @Pointcut("forFilesControllerPackage()")
    private void forFilesControllerFlow() {
    }

    @Before("forFilesControllerFlow()")
    public void beforeFilesController() {
        aopService.beforeMethod();
    }
}
