package com.cdy.socket.client;

import lombok.SneakyThrows;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * @author chendeyou
 * @date 2020/7/20 23:52
 */
public class TCPHandler implements Runnable {
    private SocketChannel socketChannel;
    private SelectionKey selectionKey ;

    public TCPHandler(SocketChannel socketChannel, SelectionKey selectionKey) {
        this.socketChannel = socketChannel;
        this.selectionKey = selectionKey;
    }

    @SneakyThrows
    public void run() {
        //判断此键通道是否已准备好进行读取操作
        if(selectionKey.isReadable()) {
            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
            int readBytes = socketChannel.read(readBuffer);
            if (readBytes > 0) {
                readBuffer.flip();//将缓冲区当前的limit设置为position,position设置为0
                byte[] bytes = new byte[readBuffer.remaining()];
                readBuffer.get(bytes);
                String body = new String(bytes, "UTF-8");
                System.out.println("服务端返回数据： :" + body);
                //this.stop = true;
            } else if (readBytes < 0) {
                //对端链路关闭
                selectionKey.cancel();
                socketChannel.close();
            } else {
                //读到0字节，忽略
            }
        }
    }
}
