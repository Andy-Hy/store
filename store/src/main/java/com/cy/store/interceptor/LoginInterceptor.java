package com.cy.store.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**定义一个拦截器*/
public class LoginInterceptor implements HandlerInterceptor {

    /**
     * 检测全局Session对象中是否有uid数据，若有则放行，若没有则重定向到登录界面
     * @param request 请求对象
     * @param response 响应对象
     * @param handler 处理器（url+Controller:映射）
     * @return 若返回值为true表示放行当前的请求，若返回值为false则表示拦截当前请求
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        //HttpServletRequest对象来获取Session对象
        Object obj = request.getSession().getAttribute("uid");
        if (obj == null) {
            //说明用户没有登录过系统，则重定向到login.html页面
            response.sendRedirect("web/login.html");
            //结束后续的调用
            return false;
        }
        //请求放行
        return true;
    }
}
