package client.bio;

import client.RpcClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.RpcRequest;
import protocol.RpcResponse;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @Description:rpc客户端的bio实现
 * @Author: xiexl
 * @Date: 2019/6/12 14:25
 */
public class BioRpcClient implements RpcClient {

    private static final Logger logger = LoggerFactory.getLogger(BioRpcClient.class);

    private String host;
    private Integer port;

    public BioRpcClient(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public RpcResponse sendRequest(RpcRequest request) {
        try(
           Socket client = new Socket(this.host, this.port);
           //jdk默认序列化方式
           ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
           ObjectInputStream in = new ObjectInputStream(client.getInputStream());
        ){
           logger.info("建立连接成功，{}:{}", this.host, this.port);
           //发送请求
           out.writeObject(request);
           logger.info("发送请求，目标地址：{}:{}, 服务：{}.{}({})", this.host, this.port,
                        request.getClassName(), request.getMethodName(), StringUtils.join(request.getParameters(), ","));

           //获取结果
            RpcResponse response = (RpcResponse) in.readObject();
            return response;

        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }
}
