package protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description:rpc请求参数封装
 * @Author: xiexl
 * @Date: 2019/6/12 14:27
 */
@Data
public class RpcRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String className;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;


}
