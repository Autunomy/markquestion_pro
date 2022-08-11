package com.hty.markquestion.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/manage/index").permitAll()//登陆成功后跳转的页面
                .and()
                .authorizeRequests()//配置权限 哪些地址需要认证，哪些不需要
                //  /local/**是本地地址  "/markquestion/**是服务器地址
                .antMatchers("/images/**",
                        "/markquestion/**",
                        "/login",
                        "/login.html",
                        "/author/*",
                        "/contest/*",
                        "/friendLink/*",
                        "/",
                        "/log/*",
                        "/question/*",
                        "/js/**",
                        "/css/**",
                        "/images/**",
                        "/advice/**",
                        "/practice/**",
                        "/verification.html",
                        "/messageBoard/**",
                        "/comment/**").permitAll()//设置哪些路径可以直接访问 不需要认证
                .anyRequest().authenticated()
                .and().csrf().disable();//关闭csrf防护
    }
}
