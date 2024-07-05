package com.zzyl.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * IOC工厂处理工具
 */
@Slf4j
@Component
public class RegisterBeanHandler {

    ConfigurableApplicationContext configurableApplicationContext;

    @Autowired
    public RegisterBeanHandler(ConfigurableApplicationContext configurableApplicationContext) {
        this.configurableApplicationContext = configurableApplicationContext;
    }

    /**
     * 注册bean
     * @param beanName bean的id
     * @param bean 实现类
     */
    public <T> void registerBean(String beanName, T bean) {
        // 将bean对象注册到bean工厂
        configurableApplicationContext.getBeanFactory().registerSingleton(beanName, bean);
    }

    /**
     * 移除bean
     * @param beanName bean的id
     */
    public void unregisterBean(String beanName){
        ((DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory()).removeBeanDefinition(beanName);
    }

    /**
     * 获得bean
     * @param beanName bean的id
     */
    public <T> T getBean(String beanName, Class<T> t) {
        return configurableApplicationContext.getBean(beanName,t);
    }

    /**
     * bean是否存在
     * @param beanName bean的id
     */
    public boolean containsBean(String beanName) {
        return configurableApplicationContext.containsBean(beanName);
    }
}
