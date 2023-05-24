package cn.jmzzz.javaweb.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Objects;

public class Login implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if (request.getServletPath().equals("/login") || request.getServletPath().equals("/register")) {
            return true;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return map.map.containsKey(cookie.getValue());
            }else {
                return false;
            }
        }
        return false;
    }
}

