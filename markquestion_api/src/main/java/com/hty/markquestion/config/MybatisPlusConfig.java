package com.hty.markquestion.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/***
 * 定义方法，返回的返回值是java 对象，这个对象是放入到spring容器中
 * 使用@Bean修饰方法
 * @Bean等同于<bean></bean>
 */
@Configuration
public class MybatisPlusConfig {
    /***
     * 定义方法，返回的返回值是java 对象，这个对象是放入到spring容器中
     * 使用@Bean修饰方法
     * @Bean等同于<bean></bean>
     */
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        return new PaginationInterceptor();
    }
}