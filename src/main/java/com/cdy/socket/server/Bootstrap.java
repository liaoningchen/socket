package com.cdy.socket.server;

/**
 * @author chendeyou
 * @date 2020/7/18 18:57
 */
public class Bootstrap {
    public static void main(String[] args) {
        SocketServer socketServer = new SocketServer(1234);
        socketServer.run();
    }
}
