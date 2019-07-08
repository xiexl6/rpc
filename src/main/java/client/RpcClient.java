package client;


import protocol.RpcRequest;
import protocol.RpcResponse;

/**
* rpc客户端
*/
public interface RpcClient {

    /**
    * 发送请求
    */
    RpcResponse sendRequest(RpcRequest rpcRequest);

}
