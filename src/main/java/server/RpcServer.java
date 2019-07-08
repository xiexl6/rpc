package server;
/**
* rpc服务端，实现方式可以是bio、nio、aio，定义接口，方便后期扩展
*/
public interface RpcServer {

    void start();

    void stop();

}
