package server.bio;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.RpcRequest;
import server.RequestHandler;
import server.RpcServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description:rpc服务端的bio实现
 * @Author: xiexl
 * @Date: 2019/6/12 14:32
 */
public class BioRpcServer implements RpcServer {

    private static final Logger logger = LoggerFactory.getLogger(BioRpcServer.class);

    //处理请求的线程池
    private static final ExecutorService es = Executors.newCachedThreadPool();

    private Integer port = 9000; //默认端口

    private volatile boolean shutdown = false; //是否停止

    /**
    * 使用默认端口初始化一个rpc服务端
    */
    public BioRpcServer() {}

    /**
    * 使用指定端口初始化服务
    */
    public BioRpcServer(Integer port) {
        this.port = port;
    }

    @Override
    public void start() {
        try{
            //启动服务
            ServerSocket server = new ServerSocket(this.port);
            logger.info("服务启动成功，端口：{}", this.port);
            while (!this.shutdown){
                //接收客户端请求
                Socket client = server.accept();
                es.execute(() -> {
                    try(
                        // 使用jdk默认的序列化流
                        ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
                        ObjectInputStream in = new ObjectInputStream(client.getInputStream());
                    ){
                        //读取请求参数
                        RpcRequest request = (RpcRequest) in.readObject();
                        logger.info("接收请求，{}.{}({})", request.getClassName(), request.getMethodName(),
                                        StringUtils.join(request.getParameters(), ","));

                        //处理请求
                        out.writeObject(RequestHandler.handlerRequest(request));

                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("客户端连接异常，客户端{}:{}", client.getInetAddress().toString());
                        throw new RuntimeException(e);
                    }

                });
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("服务端启动异常", e);
        }
    }

    @Override
    @PreDestroy
    public void stop() {
        this.shutdown = true;
        logger.info("服务器即将停止");
    }

    @PostConstruct
    public void init(){
        es.submit(this::start);
    }

}
