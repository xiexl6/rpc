package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import processor.RpcProxyBeanPostProcessor;

/**
 * @Description:bean后置处理器配置
 * @Author: xiexl
 * @Date: 2019/6/13 14:59
 */
@Configuration
public class RpcClientConfig {

    @Bean
    public RpcProxyBeanPostProcessor rpcProxyBeanPostProcessor(){
        return new RpcProxyBeanPostProcessor();
    }

}
