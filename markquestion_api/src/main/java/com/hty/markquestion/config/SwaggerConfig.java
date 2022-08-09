package com.hty.markquestion.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

//swagger的配置类
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    public Docket docket(Environment environment){
        //如果是test测试环境，则启用swagger
        boolean flag = environment.acceptsProfiles("test");

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(flag)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.hty.markquestion.controller"))
                .build();
    }

    public ApiInfo apiInfo(){
        Contact contact = new Contact("autuonmy","http://localhost:8080","1156388927@qq.com");
        return new ApiInfo(
                "markquestion的API文档",
                "该项目主要为了记录自己的算法进阶之旅",
                "1.0.0",
                "http://localhost:8080",
                contact,
                "Apache 2.0",
                "https://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList<>()
        );
    }
}
