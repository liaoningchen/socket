package com.cdy.socket.client;

import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * @author chendeyou
 * @date 2020/7/20 23:44
 */
public class ConnectHandler implements Runnable {
    private Selector selector;
    private SocketChannel socketChannel;

    public ConnectHandler(Selector selector, SocketChannel socketChannel) {
        this.selector = selector;
        this.socketChannel = socketChannel;
    }

    @SneakyThrows
    public void run() {
        if(socketChannel.finishConnect()){
            SelectionKey register = socketChannel.register(selector, SelectionKey.OP_READ);
            register.attach(new TCPHandler(socketChannel,register));
            doWrite(socketChannel);
        }else{
            //连接失败，进程退出
            System.exit(1);
        }
    }
    private void doWrite(SocketChannel sc) throws IOException {
        byte[] bytes = "hello world".getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        writeBuffer.put(bytes);
        writeBuffer.flip();
        sc.write(writeBuffer);
        if (!writeBuffer.hasRemaining()){
            System.out.println("客户端数据发送成功");
        }
    }
}
