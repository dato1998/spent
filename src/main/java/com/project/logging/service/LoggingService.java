package com.project.logging.service;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Service
public class LoggingService {

    @Value("${aspect.logger.method-name}")
    private String callingMethod;

    @Value("${aspect.logger.argument}")
    private String argument;

    @Value("${aspect.logger.result}")
    private String result;

    private Logger logger = Logger.getLogger(getClass().getName());

    public void beforeMethod(JoinPoint joinPoint) {
        try {
            String method = joinPoint.getSignature().toShortString();
            logger.info(callingMethod + method);
            Object[] args = joinPoint.getArgs();
            for (Object arg : args) {
                logger.info(argument + arg);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void afterMethod(JoinPoint joinPoint, Object obj) {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            String methodName = joinPoint.getSignature().getName();
            if (!request.getMethod().equals("GET") && !methodName.equals("search")) {
                logger.info(result + obj);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void doRecoveryActions(JoinPoint joinPoint, Throwable ex) {
        try {
            String method = joinPoint.getSignature().toShortString();
            logger.error(callingMethod + method);
            logger.error(ex.getMessage(), ex);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}
