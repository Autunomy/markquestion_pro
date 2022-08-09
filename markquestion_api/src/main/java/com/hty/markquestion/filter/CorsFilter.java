package com.hty.markquestion.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//解决跨域问题
@WebFilter("/*")
@Component
public class CorsFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        //允许的ip地址
        resp.addHeader("Access-Control-Allow-Origin","*");
        resp.addHeader("Access-Control-Allow-Credentials","true");
        resp.addHeader("Access-Control-Allow-Methods","*");
        resp.addHeader("Access-Control-Max-Age","true");
        //允许的跨域请求头
        resp.addHeader("Access-Control-Allow-Headers","*");
        //允许发送自定义头部
        resp.addHeader("Access-Control-Expose-Headers","*");
        filterChain.doFilter(servletRequest,resp);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
