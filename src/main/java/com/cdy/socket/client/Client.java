package com.cdy.socket.client;

import java.io.IOException;

/**
 * @author chendeyou
 * @date 2020/7/18 19:13
 */
public class Client {
    public static void main(String[] args) throws IOException {
        TimeClientHandle timeClientHandle = new TimeClientHandle("127.0.0.1", 1234);
        timeClientHandle.run();

    }
}
