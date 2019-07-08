package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import server.RpcServer;
import server.ServiceRegister;
import server.bio.BioRpcServer;

/**
 * @Description:rpc服务配置
 * @Author: xiexl
 * @Date: 2019/6/13 12:31
 */
@Configuration
@ComponentScan(basePackages="service")
public class RpcServerConfig {

    @Bean
    public RpcServer rpcServer(){
        return new BioRpcServer();
    }

    @Bean
    public ServiceRegister serviceRegister(){
        return new ServiceRegister();
    }

}

