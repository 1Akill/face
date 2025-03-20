package cn.cy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebAppConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String path = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\img\\";
        // /upload/**是对应resource下工程目录
        registry.addResourceHandler("/img/**").addResourceLocations("file:" + path);
    }
}
