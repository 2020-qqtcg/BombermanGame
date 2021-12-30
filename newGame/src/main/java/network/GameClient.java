package network;

import asciiPanel.AsciiFont;
import asciiPanel.AsciiPanel;
import network.doubleGame.Packge;
import progress.Game;
import screen.ClientScreen;
import screen.Screen;
import thing.World;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class GameClient extends JFrame implements KeyListener {
    private AsciiPanel terminal;
    private Screen screen;
    private SocketChannel socketChannel;
    private Selector selector;
    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private ByteBuffer readBuffer = ByteBuffer.allocate(1024);
    private int id;
    private Packge packge;

    public GameClient(String host, int port){
        super("GameClient");
        // 真实大小为30 X size
        terminal = new AsciiPanel(World.WIDTH+10, World.HEIGHT, AsciiFont.TALRYTH_15_15);
        add(terminal);
        pack();
        addKeyListener(this);

        screen = new ClientScreen(this);

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
                            StringBuilder msg = new StringBuilder();
                            while (socketChannel.read(readBuffer) > 0) {
                                //  从写模式切换为读模式
                                readBuffer.flip();
                                msg.append(readBuffer);
                            }
                            System.out.println(msg);
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
