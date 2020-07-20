package com.cdy.socket.client;

/**
 * @author chendeyou
 * @date 2020/7/20 23:44
 * 客户端启动类
 */
public class Bootstrap{
    public static void main(String[] args) {
        SocketClient scoketClient = new SocketClient("127.0.0.1",1234);
        scoketClient.run();
    }
}