package com.wclp.springserver.cfg;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMVCconfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/toLoginPage").setViewName("login");
        registry.addViewController("/index.html").setViewName("login");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        //添加默认的静态资源访问路径
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:static/","classpath:resources/","classpath:public/","classpath:/","classpath:images/")
                .addResourceLocations("classpath:templates/view/")
                .addResourceLocations("classpath:templates/view/**");
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/images/");
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/templates/view/**")
                .addResourceLocations("classpath:/templates/view/*")
                .addResourceLocations("classpath:/static/","classpath:/resources/","classpath:/public/","classpath:/");
    }


}
