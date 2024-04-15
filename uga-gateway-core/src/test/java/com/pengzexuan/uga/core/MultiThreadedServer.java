package com.pengzexuan.uga.core;

import com.pengzexuan.uga.common.concurrent.queue.flusher.UgaFlusher;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class MultiThreadedServer {
    private static final int PORT = 8888;
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(PORT));
            serverSocketChannel.configureBlocking(false);

            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                selector.select();

                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove();

                    if (key.isAcceptable()) {
                        ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                        SocketChannel socketChannel = channel.accept();
                        socketChannel.configureBlocking(false);

                        Thread thread = new Thread(new ClientHandler(socketChannel));
                        thread.start();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private final SocketChannel socketChannel;
        private final ByteBuffer buffer;

        public ClientHandler(SocketChannel socketChannel) {
            this.socketChannel = socketChannel;
            this.buffer = ByteBuffer.allocate(BUFFER_SIZE);
        }

        @Override
        public void run() {
            try {
                while (true) {
                    int bytesRead = socketChannel.read(buffer);
                    if (bytesRead == -1) {
                        socketChannel.close();
                        break;
                    }
                    if (bytesRead > 0) {
                        buffer.flip();
                        byte[] bytes = new byte[buffer.remaining()];
                        buffer.get(bytes);
                        System.out.println("Received: " + new String(bytes));
                        buffer.clear();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
