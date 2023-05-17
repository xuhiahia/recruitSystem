package com.fzy.project.common;

import com.fzy.project.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception  {
        // 1.判断是否需要拦截（ThreadLocal中是否有用户）
        String tokenKey = UserHolder.get();
        if (StringUtils.isBlank(tokenKey)) {
            // 没有，需要拦截，抛出异常
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
            // 拦截
        }
        // 有用户，则放行
        return true;
    }

}
