package com.cdy.socket.server;

/**
 * @author chendeyou
 * @date 2020/7/18 18:57
 */
public class Bootstrap {
    public static void main(String[] args) {
        TCPReactor tcpReactor = new TCPReactor(1234);
        tcpReactor.run();
    }
}
