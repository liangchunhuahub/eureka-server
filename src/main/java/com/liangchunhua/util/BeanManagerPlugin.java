package com.liangchunhua.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BeanManagerPlugin implements ApplicationContextAware {
    private static ApplicationContext applicationContext = null;

    @SuppressWarnings("NullableProblems")
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(BeanManagerPlugin.applicationContext == null) {
            BeanManagerPlugin.applicationContext = applicationContext;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name){
        try {
            if (applicationContext != null) {
                return (T) applicationContext.getBean(name);
            }
        }catch (Exception e){
            log.debug(e.getMessage());
        }
        return null;
    }
}
