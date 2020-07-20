package com.cdy.socket.client;

import lombok.SneakyThrows;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author chendeyou
 * @date 2020/7/21 0:02
 */
public class SocketClient implements Runnable{
    private String host;
    private int port;
    private Selector selector;
    private SocketChannel socketChannel;

    @SneakyThrows
    public SocketClient(String host, Integer port){
        this.host = host == null?"127.0.0.1":host;
        this.port = port == null?1234:port;
        selector = Selector.open();
        socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        assert host != null;
        assert port != null;
        socketChannel.connect(new InetSocketAddress(host,port));
        SelectionKey register = socketChannel.register(selector, SelectionKey.OP_CONNECT);
        register.attach(new ConnectHandler(selector,socketChannel));
    }

    @SneakyThrows
    public void run() {
        while(!Thread.interrupted()){
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> it = selectionKeys.iterator();
            while(it.hasNext()){
                dispatch(it.next());
                it.remove();
            }
        }
    }

    private static void dispatch(SelectionKey selectionKey ){
        Runnable attachment = (Runnable) selectionKey.attachment();
        attachment.run();
    }
}
