package com.cdy.socket.server;

import lombok.SneakyThrows;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author chendeyou
 * @date 2020/7/18 17:17
 */
public class Acceptor implements Runnable{

    private ServerSocketChannel serverSocketChannel ;
    private Selector selector;

    public Acceptor(ServerSocketChannel serverSocketChannel, Selector selector) {
        this.serverSocketChannel = serverSocketChannel;
        this.selector = selector;
    }

    @SneakyThrows
    public void run() {
        SocketChannel socketChannel = serverSocketChannel.accept();
        if(socketChannel!=null){
            //设置非阻塞 (read 方法)
            socketChannel.configureBlocking(false);
            SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_READ);
            selectionKey.attach(new TCPHandler(selectionKey,socketChannel));
        }
    }
}
