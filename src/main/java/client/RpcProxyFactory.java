package client;

import client.bio.BioRpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.RpcRequest;
import protocol.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 动态代理工厂，为接口生成实现了rpc远程调用的实现类
 * @Author: xiexl
 * @Date: 2019/6/12 14:25
 */
public class RpcProxyFactory<T> implements InvocationHandler {

    private static final Logger logger = LoggerFactory.getLogger(RpcProxyFactory.class);

    private Class<T> clazz;

    public RpcProxyFactory(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T getProxyObject(){
        return (T) Proxy.newProxyInstance(this.clazz.getClassLoader(), new Class[]{this.clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //处理Object中的方法
        if (Object.class == method.getDeclaringClass()){
            String name = method.getName();
            if ("equals".equals(name)){
                return proxy == args[0];
            }else if ("hashCode".equals(name)){
                return System.identityHashCode(proxy);
            }else if ("toString".equals(name)){
                return proxy.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(proxy)) +
                        " , with invocationHandler " + this;
            }else {
                throw new IllegalStateException(String.valueOf(method));
            }
        }
        //封装请求参数
        RpcRequest request = new RpcRequest();
        request.setClassName(clazz.getName());
        request.setMethodName(method.getName());
        request.setParameters(args);
        request.setParameterTypes(method.getParameterTypes());
        try{
            //发起网络请求，并接收相应
            RpcClient client = new BioRpcClient("127.0.0.1", 9000);
            RpcResponse response = client.sendRequest(request);

            //解析并返回
            if (response.getStatus() == 0){
                logger.info("服务远程调用成功");
                return response.getData();
            }
            logger.error("调用远程服务失败, {}", response.getError());
            return null;

        }catch (Exception e){
            e.printStackTrace();
            logger.error("调用远程服务异常", e);
            throw new RuntimeException(e);
        }

    }
}
