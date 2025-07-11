package com.platform.common.config;

import cn.hutool.core.io.file.FileNameUtil;
import com.platform.common.constant.AppConstants;
import com.platform.common.web.handler.VersionHandlerMapping;
import com.platform.common.web.interceptor.DeviceInterceptor;
import com.platform.common.web.interceptor.SignInterceptor;
import com.platform.common.web.interceptor.VersionInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import java.util.List;

/**
 * 通用配置
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Resource
    private VersionInterceptor versionInterceptor;

    @Resource
    private DeviceInterceptor deviceInterceptor;

    @Resource
    private SignInterceptor signInterceptor;

    @Override
    public RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
        return new VersionHandlerMapping();
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(ApplicationConfig.objectMapper());
    }

    @Value("${platform.rootPath}")
    private String rootPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // favicon.ico
        registry.addResourceHandler(AppConstants.FAVICON).addResourceLocations("classpath:/static/");
        // file
        registry.addResourceHandler(AppConstants.PREVIEW).addResourceLocations("file:" + rootPath + FileNameUtil.UNIX_SEPARATOR);
    }

    /**
     * 自定义拦截规则
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(versionInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(AppConstants.PREVIEW);
        registry.addInterceptor(deviceInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(AppConstants.PREVIEW);
        registry.addInterceptor(signInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(AppConstants.PREVIEW);
    }

}