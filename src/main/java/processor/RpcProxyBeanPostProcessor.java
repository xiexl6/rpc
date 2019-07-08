package processor;

import annotation.Reference;
import client.RpcProxyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:bean的后处理器，用来处理rpc的动态代理对象
 * @Author: xiexl
 * @Date: 2019/6/13 14:40
 */
public class RpcProxyBeanPostProcessor implements BeanPostProcessor {

    private static final Logger logger = LoggerFactory.getLogger(RpcProxyBeanPostProcessor.class);

    private final Map<Class<?>, Object> cache = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        //遍历所有字段
        for(Field field : bean.getClass().getDeclaredFields()){
            //判断是否有Reference注解
            if (field.isAnnotationPresent(Reference.class)){
                field.setAccessible(true);
                //获取类型
                Class<?> clazz = field.getType();
                //保存缓存中的值
                Object proxy = null;
                //判断缓存中是否存在该字段类型
                if (cache.containsKey(clazz)){
                    proxy = cache.get(clazz);
                }else{
                    //动态代理生成对象
                    proxy = new RpcProxyFactory<>(clazz).getProxyObject();
                    cache.put(bean.getClass(), proxy);
                }
                try {
                    field.set(bean, proxy);
                    logger.info("为{}注入了{}", field, proxy);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    logger.error("属性" + field + "注入失败，" + e);
                }

            }
        }
        return bean;
    }
}
