package com.zzyl.config;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.zzyl.intercept.UserInterceptor;
import com.zzyl.intercept.UserTokenIntercept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 *  webMvc高级配置
 */
@Configuration
@ComponentScan("springfox.documentation.swagger.web")
public class WebMvcConfig implements WebMvcConfigurer {

   @Autowired
   UserTokenIntercept userTokenIntercept;

    @Autowired
   UserInterceptor userInterceptor;

    //拦截的时候过滤掉swagger相关路径和登录相关接口
    private static final String[] EXCLUDE_PATH_PATTERNS = new String[]{"/swagger-ui.html",
            "/webjars/**",
            "/swagger-resources",
            "/v2/api-docs",
            // 登录接口
            "/customer/user/login",
            // 房型列表接口
            "/customer/roomTypes",
            "/customer/orders/project/**",
            "/user/refresh/**"};

    /**
     *  拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //userToken拦截
        registry.addInterceptor(userTokenIntercept).excludePathPatterns(ADMIN_EXCLUDE_PATH_PATTERNS);
        // 小程序端接口鉴权拦截器
        registry.addInterceptor(userInterceptor).excludePathPatterns(EXCLUDE_PATH_PATTERNS).addPathPatterns("/customer/**");
    }

    //拦截的时候过滤掉swagger相关路径和登录相关接口
    private static final String[] ADMIN_EXCLUDE_PATH_PATTERNS = new String[]{"/swagger-ui.html",
            "/webjars/**",
            "/swagger-resources",
            "/v2/api-docs",
            "/customer/**",
            "/security/**",
            "/common/**",
            "/user/refresh/**"};

    /**
     * 资源路径 映射
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //支持webjars
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        //支持swagger
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        //支持小刀
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
    }

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            // 序列化
            builder.serializerByType(Long.class, ToStringSerializer.instance);
            builder.serializerByType(BigInteger.class, ToStringSerializer.instance);
            builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
            builder.serializerByType(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
            builder.serializerByType(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));

            // 反序列化
            builder.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
            builder.deserializerByType(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
            builder.deserializerByType(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));

        };
    }
}
