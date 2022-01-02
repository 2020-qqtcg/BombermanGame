package network;

import network.doubleGame.Packge;
import progress.State;
import screen.EndScreen;
import thing.Player;
import thing.Thing;
import thing.World;

import java.awt.event.KeyEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
    private int number = 0;
    private World world;
    boolean pre[] = new boolean[3];  // 看玩家是否都点了开始,第三个用来保证只创建一次游戏主体

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
                    socketChannel.register(selector, SelectionKey.OP_READ|SelectionKey.OP_WRITE);

                    socketChannel.write(CHARSET.encode("ID"+(number++)));



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
                    if (pre[0] && pre[1] && !pre[2]){  //就绪但没有创建国游戏
                        world = new World(2);
                        pre[2] = true;
                        System.out.println("Game start");
                    }
                    else if (pre[0] && pre[1]){   //游戏进行中状态
                        examOrder(msg);
                    }
                    else {
                        ifPrepare(msg);
                    }
                    System.out.println(msg);
                    readBuffer.clear();


                } else if (key.isWritable()) {      // 向客户端传数据包
                     //a channel is ready for writing
                    if (world != null){
                        SocketChannel channel = (SocketChannel) key.channel();
                        // 序列化传输
                        Thing[][] things = world.getThings();
                        int live0 = world.getPlayer1().getLive();
                        int live1 = world.getPlayer2().getLive();
                        Packge pack = new Packge(things, live0, live1);
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(bos);
                        oos.writeObject(pack);
                        oos.flush();
                        byte[] temp = bos.toByteArray();
                        channel.write(ByteBuffer.wrap(temp));
                    }

                }

                keyIterator.remove();
            }
        }
    }


    public boolean examOrder(String msg){
        if (msg.startsWith("0")){
            Player player0 = world.getPlayer1();
            int x = player0.getX();
            int y = player0.getY();
            String order = msg.substring(1);


            switch (order){
                case "W" -> world.canGoUp(x, y);
                case "S" -> world.canGoDown(x, y);
                case "A" -> world.canGoLeft(x, y);
                case "D" -> world.canGoRight(x, y);
                case "J" -> new Thread(player0::attack).start();
            }

        }
        else {
            Player player1 = world.getPlayer2();
            int x = player1.getX();
            int y = player1.getY();
            String order = msg.substring(1);


            switch (order){
                case "W" -> world.canGoUp(x, y);
                case "S" -> world.canGoDown(x, y);
                case "A" -> world.canGoLeft(x, y);
                case "D" -> world.canGoRight(x, y);
                case "J" -> new Thread(player1::attack).start();
            }
        }

        return world.getPlayer1().isLive() && world.getPlayer2().isLive();
    }

    public void ifPrepare(String msg){
        if (msg.equals("1A")){
            pre[1] = true;
        }
        else if (msg.equals("0A")){
            pre[0] = true;
        }
    }


    public static void main(String[] args) throws IOException {
        GameServer chatServer = new GameServer(9000);
        chatServer.listen();
    }
}
