package protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description:rpc相应数据封装
 * @Author: xiexl
 * @Date: 2019/6/12 14:27
 */
@Data
public class RpcResponse implements Serializable{
    private static final long serialVersionUID = 2L;

    private Integer status;//相应状态 0：成功 1：失败
    private String error;//错误信息
    private Object data;//相应数据

    /**
    * 表示相应成功，用来接收返回数据
    */
    public static RpcResponse ok(Object data){
        return build(0, null, data);
    }

    /**
    * 表示相应失败，用来接收错误信息
    */
    public static RpcResponse error(String error){
        return build(1, error, null);
    }

    /**
    * 用来自定义相应状态和信息
    */
    public static RpcResponse build(int status, String error, Object data) {
        RpcResponse result = new RpcResponse();
        result.setData(data);
        result.setError(error);
        result.setStatus(status);
        return result;
    }

}
