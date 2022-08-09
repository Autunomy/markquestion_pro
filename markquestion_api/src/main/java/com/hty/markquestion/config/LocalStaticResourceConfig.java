package com.hty.markquestion.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

//让存放在本地目录的静态资源也可以被访问到
//@Configuration  //未启用
public class LocalStaticResourceConfig extends WebMvcConfigurationSupport {
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        //本机 访问addResourceHandler的地址会自动映射到addResourceLocations中的地址并自动寻找资源
//        registry.addResourceHandler("/**")
//                .addResourceLocations("classpath:/static/");
//        registry.addResourceHandler("/local/**")
//                .addResourceLocations("file:E:\\");
        //服务器
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/markquestion/**")
                .addResourceLocations("file:/markquestion/");
    }
}
