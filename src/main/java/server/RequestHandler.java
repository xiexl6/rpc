package server;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.RpcRequest;
import protocol.RpcResponse;

import java.lang.reflect.Method;

/**
 * @Description:请求处理器
 * @Author: xiexl
 * @Date: 2019/6/12 14:28
 */
public class RequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    /**
    * 处理请求
    */
    public static RpcResponse handlerRequest(RpcRequest request){
        try{
           //获取服务
            Object service = ServiceRegister.registeredServices.get(request.getClassName());
            if (service != null){
                Class<?> clazz = service.getClass();
                //获取方法
                Method method = clazz.getMethod(request.getMethodName(), request.getParameterTypes());
                //执行方法
                Object data = method.invoke(service, request.getParameters());
                //返回数据
                return RpcResponse.ok(data);
            }else{
                logger.info("请求的服务未找到, {}.{}({})", request.getClassName(), request.getMethodName()
                        , StringUtils.join(request.getParameters(), ","));
                return RpcResponse.error("未知服务");
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("处理请求失败", e);
            return RpcResponse.error(e.getMessage());
        }
    }

}
