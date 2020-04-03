package com.qty.aspect;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;


/**
 * 控制台打印日志
 */
@Slf4j
@Aspect
@Configuration
@AllArgsConstructor
public class RequestLogAspect {

    private ObjectMapper objectMapper;

    @Around("execution(* com.qty.controller.*Controller.*(..))||(execution(com.qty.response.BaseResponse  *(..)))&&(@within(org.springframework.web.bind.annotation.RestController)||@within(org.springframework.stereotype.Controller))")
    public Object aroundapi(ProceedingJoinPoint pjp)throws Throwable{
//        MethodSignature ms= (MethodSignature) point.getSignature();
//        Method method= ms.getMethod();
//        Object[] args= point.getArgs();
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();

        String method = request.getMethod();
        String uri = request.getRequestURI();
        String paraString = JSON.toJSONString(request.getParameterMap());
        log.warn("***************************************************");
        log.warn("请求开始, URI: {}, method: {}, params: {}", uri, method, paraString);

        // result的值就是被拦截方法的返回值
        Object result = pjp.proceed();
        log.warn("请求结束，controller的返回值是 " + JSON.toJSONString(result));
        log.warn("***************************************************");
        return result;
    }
}
