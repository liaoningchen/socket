package com.cdy.socket.server;

import com.sun.corba.se.pept.broker.Broker;
import com.sun.corba.se.pept.encoding.InputObject;
import com.sun.corba.se.pept.encoding.OutputObject;
import com.sun.corba.se.pept.protocol.MessageMediator;
import com.sun.corba.se.pept.transport.Connection;
import com.sun.corba.se.pept.transport.EventHandler;
import com.sun.corba.se.pept.transport.InboundConnectionCache;
import lombok.SneakyThrows;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author chendeyou
 * @date 2020/7/18 16:27
 */
public class TCPReactor implements Runnable{

    private ServerSocketChannel serverSocketChannel ;
    private Selector selector;


    @SneakyThrows
    public TCPReactor(Integer port) {
        //服务端socket
        this.serverSocketChannel = ServerSocketChannel.open();
        //创建选择器对象
        this.selector=Selector.open();
        //端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress(port);
        //服务端socket绑定端口
        serverSocketChannel.socket().bind(inetSocketAddress);
        //设置为非阻塞(accept非阻塞)
        serverSocketChannel.configureBlocking(false);
        //selector 注册到 serverSocketChannel
        SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //附加对象
        selectionKey.attach(new Acceptor(serverSocketChannel,selector));

    }

    @SneakyThrows
    public void run() {

        while (!Thread.interrupted()){
            System.out.println("准备接收连接。。。。。。。。。。。。。。。。。");
            //selector.select() 是阻塞的    判断是否有感兴趣的事件发生，这里事件指的是上边注册的事件(accept)
            selector.select();
            System.out.println("有请求数据进来了");
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                dispatch(selectionKey);
                iterator.remove();
            }
        }

    }
    private void dispatch(SelectionKey selectionKey ){
        //取出附加对象
        Runnable runnable = (Runnable)selectionKey.attachment();
        if(runnable!=null){
            runnable.run();
        }
    }
}
