package network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class GameServer {
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private ByteBuffer readBuffer = ByteBuffer.allocate(1024);
    private ByteBuffer writeBuffer = ByteBuffer.allocate(1024);

    public GameServer(int port) {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);

            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 监听客户端连接
     */
    public void listen() throws IOException {
        System.out.println("服务器启动成功");
        while(true) {

            int readyChannels = selector.selectNow();

            if(readyChannels == 0) {
                continue;
            }

            Set<SelectionKey> selectedKeys = selector.selectedKeys();

            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

            while(keyIterator.hasNext()) {

                SelectionKey key = keyIterator.next();

                if(key.isAcceptable()) {
                    // a connection was accepted by a ServerSocketChannel.
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    socketChannel.write(CHARSET.encode("来自服务器的问候: 你好!"));
                } else if (key.isConnectable()) {
                    // a connection was established with a remote server.
                } else if (key.isReadable()) {
                    // a channel is ready for reading
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    String msg = "";
                    while (socketChannel.read(readBuffer) > 0) {
                        readBuffer.flip();
                        msg += CHARSET.decode(readBuffer).toString();
                    }
                    System.out.println(msg);
                    readBuffer.clear();

                    //  给客户端回复消息
                    writeBuffer.put("服务器对你说: 收到".getBytes());
                    writeBuffer.flip();
                    socketChannel.write(writeBuffer);
                    writeBuffer.clear();

                } else if (key.isWritable()) {
                    // a channel is ready for writing
                }

                keyIterator.remove();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        GameServer chatServer = new GameServer(9000);
        chatServer.listen();
    }
}
