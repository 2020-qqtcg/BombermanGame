package network;

import asciiPanel.AsciiFont;
import asciiPanel.AsciiPanel;
import network.doubleGame.Packge;
import progress.Game;
import screen.ClientScreen;
import screen.ClientStartScreen;
import screen.Screen;
import thing.Thing;
import thing.World;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;

public class GameClient extends JFrame implements KeyListener {
    private AsciiPanel terminal;
    private Screen screen;
    private SocketChannel socketChannel;
    private Selector selector;
    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private ByteBuffer readBuffer = ByteBuffer.allocate(1024);
    private int id = -1;
    private Packge packge;

    public GameClient(String host, int port){
        super("GameClient");
        // 真实大小为30 X size
        terminal = new AsciiPanel(World.WIDTH+10, World.HEIGHT, AsciiFont.TALRYTH_15_15);
        add(terminal);
        pack();
        addKeyListener(this);

        screen = new ClientStartScreen(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        repaint();

        try {
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress(host, port));

            while (!socketChannel.finishConnect()) {
                System.out.println("正在等待连接");
            }

            System.out.println("连接成功");

            selector = Selector.open();
            socketChannel.register(selector, SelectionKey.OP_READ);

            new Thread(new Handler(selector)).start();

        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    /**
     * 给服务器发消息
     */
    public void send(String op) throws IOException {

        socketChannel.write(CHARSET.encode(id + op));

    }

    private void setId(int n){
        id = n;
    }

    /**
     * 接收来自服务器的消息
     */
    class Handler implements Runnable {

        private Selector selector;

        public Handler(Selector selector) {
            this.selector = selector;
        }

        @Override
        public void run() {
            try {
                while (true) {

                    int readyChannels = selector.selectNow();

                    if (readyChannels == 0) continue;

                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

                    while (keyIterator.hasNext()) {

                        SelectionKey key = keyIterator.next();

                        if (key.isReadable()) {
                            SocketChannel socketChannel = (SocketChannel) key.channel();
                            if (id != -1){             // 把序列化的package对象反序列化出来
                                ArrayList<byte[]> pack = new ArrayList<>();
                                int bufferLen = 0;
                                while ((socketChannel.read(readBuffer)) > 0){
                                    readBuffer.flip();
                                    pack.add(readBuffer.array());
                                    bufferLen += readBuffer.array().length;
                                }
                                byte[] packStream = new byte[bufferLen];
                                int j = 0;
                                for (byte[] tempBuffer : pack){
                                    System.arraycopy(tempBuffer, 0, packStream, j, tempBuffer.length);
                                    j += tempBuffer.length;
                                }
                                ByteArrayInputStream bln = new ByteArrayInputStream(packStream);
                                ObjectInputStream ois = new ObjectInputStream(bln);
                                try {
                                    packge = (Packge) ois.readObject();
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                            else {
                                StringBuilder msg = new StringBuilder();
                                while (socketChannel.read(readBuffer) > 0) {
                                    //  从写模式切换为读模式
                                    readBuffer.flip();
                                    msg.append(CHARSET.decode(readBuffer));
                                }

                                if (msg.toString().startsWith("ID")){
                                    setId(Integer.parseInt(msg.substring(2)));  //获取玩家编号
                                    System.out.println("ID:" + id);
                                }

                            }
                            readBuffer.clear();
                        }

                        keyIterator.remove();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Packge getPackge(){
        return packge;
    }

    public static void main(String[] args) throws IOException {
        GameClient chatClient = new GameClient("127.0.0.1", 9000);
        while (true){
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            chatClient.repaint();
        }
    }

    @Override
    public void repaint(){
        terminal.clear();
        screen.displayOutput(terminal);
        super.repaint();
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        screen = screen.respondToUserInput(e);

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
