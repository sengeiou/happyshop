package com.imooc.ecommerce.conf;

import com.alibaba.cloud.seata.web.SeataHandlerInterceptor;
import com.imooc.ecommerce.filter.LoginUserInfoInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * <h1>Web Mvc 配置</h1>
 * */
@Configuration
public class ImoocWebMvcConfig extends WebMvcConfigurationSupport {

    /**
     * <h2>添加拦截器配置</h2>
     * 重写addInterceptor()方法
     * */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        // 添加用户身份统一登录拦截的拦截器
        registry.addInterceptor(new LoginUserInfoInterceptor())
                .addPathPatterns("/**").order(0);
        // Seata 传递 xid 事务 id 给其他的微服务
        // 只有这样, 其他的服务才会写 undo_log, 才能够实现回滚
        registry.addInterceptor(new SeataHandlerInterceptor()).addPathPatterns("/**");
    }

    /**
     * <h2>让 MVC 加载 Swagger 的静态资源</h2>
     * */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/**").
                addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

        super.addResourceHandlers(registry);
    }
}
