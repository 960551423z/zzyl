package com.zzyl.config;

import com.zzyl.properties.SecurityConfigProperties;
import com.zzyl.security.JwtAuthorizationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

/**
 *  权限核心配置类
 */
@Configuration
@EnableConfigurationProperties(SecurityConfigProperties.class)
public class SecurityConfig  {

    @Autowired
    SecurityConfigProperties securityConfigProperties;

    @Autowired
    JwtAuthorizationManager jwtAuthorizationManager;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        //忽略地址
        List<String> ignoreUrl = securityConfigProperties.getIgnoreUrl();
        http.authorizeHttpRequests()
                .antMatchers( ignoreUrl.toArray( new String[ignoreUrl.size() ] ) )
                .permitAll()
                .anyRequest().access(jwtAuthorizationManager);

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS );//关闭session
        http.headers().cacheControl().disable();//关闭缓存

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return  authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * BCrypt密码编码
     * @return
     */
    @Bean
    public BCryptPasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}