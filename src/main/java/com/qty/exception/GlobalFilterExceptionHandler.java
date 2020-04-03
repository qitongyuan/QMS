package com.qty.exception;

import com.qty.response.BaseResponse;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 目前系统中所有的接口不限于于Controller，均采取外抛
 * 异常的方式来处理，所有的异常会优先被该类截取，
 * 作处理后再抛给GlobalExceptionHandler来统一处理
 */
@RestController
public class GlobalFilterExceptionHandler extends BasicErrorController {

    public GlobalFilterExceptionHandler() {
        super(new DefaultErrorAttributes(true), new ErrorProperties());
    }

    @RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Override
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        boolean falg=isIncludeStackTrace(request, MediaType.ALL);
        Map<String, Object> body = getErrorAttributes(request, true);

        HttpStatus status = getStatus(request);
        String message = body.get("message").toString();
        String exception = body.get("exception").toString();
        //单独处理用户realm中抛出的权限不足和认证失败异常，抛出交给GlobalExceptionHandler处理
        if (exception.equals(AuthenticationException.class.getName())) {
            throw new AuthenticationException(message);
        } else if (exception.equals(AuthorizationException.class.getName())) {
            throw new AuthorizationException(message);
        }
        //其他500异常直接返回给前端
        return new ResponseEntity(new BaseResponse(status.value(),message), HttpStatus.OK);
    }

    @Override
    public String getErrorPath() {
        return "error/error";
    }
}
