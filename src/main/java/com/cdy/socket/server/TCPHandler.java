package com.cdy.socket.server;

import lombok.SneakyThrows;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * @author chendeyou
 * @date 2020/7/18 17:58
 */
public class TCPHandler implements Runnable{
    private SelectionKey selectionKey ;
    private SocketChannel socketChannel ;

    public TCPHandler(SelectionKey selectionKey, SocketChannel socketChannel) {
        this.selectionKey = selectionKey;
        this.socketChannel = socketChannel;
    }

    public void run() {
        read();
    }
    @SneakyThrows
    private synchronized void read(){
        byte[] bytes = new byte[1024];
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        int read = socketChannel.read(byteBuffer);
        if(read==-1){
            closeChannel();
            return;
        }
        String str = new String(bytes);
        if(str!=null &&!"".equals(str)){
            ByteBuffer result = ByteBuffer.wrap(hanlder(str).getBytes());
            //会写数据
            if(result.hasRemaining()){
                socketChannel.write(result);
            }
            //阻塞操作 立即返回
            selectionKey.selector().wakeup();

        }
    }
    @SneakyThrows
    private void closeChannel(){
        socketChannel.close();
    }
    @SneakyThrows
    private String hanlder(String str){
        System.out.println("请求数据:"+str);
        Thread.sleep(500L);
        return "数据处理完成:"+str+System.currentTimeMillis();
    }
}
