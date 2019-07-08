package server;

import annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:服务注册
 * @Author: xiexl
 * @Date: 2019/6/12 14:32
 */
public class ServiceRegister implements ApplicationContextAware{

    private static final Logger logger = LoggerFactory.getLogger(ServiceRegister.class);

    //定义服务注册容器
    public static final Map<String, Object> registeredServices = new HashMap<>();

    /**
    * 根据接口获取接口实例服务
    */
    public static <T> T getService(String interfaceName){
       return (T) registeredServices.get(interfaceName);
    }

    /**
    * 注册服务
    */
    public static void registerService(Class<?> interfaceClass, Class<?> implClass){
        try{
            registeredServices.put(interfaceClass.getName(), implClass.newInstance());
            logger.info("服务注册成功, 接口:{},实现类:{}" + interfaceClass.getName(), implClass.getName());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("服务" + implClass + "注册失败", e);
        }
    }


    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        Map<String, Object> services = ctx.getBeansWithAnnotation(Service.class);
        if (services != null && services.size() > 0){
            for(Object service : services.values()){
                String interfaceName = service.getClass().getAnnotation(Service.class).value().getName();
                registeredServices.put(interfaceName, service);
                logger.info("加载服务：{}", interfaceName);
            }
        }

    }
}
