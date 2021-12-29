package network;

import asciiPanel.AsciiFont;
import asciiPanel.AsciiPanel;
import screen.ClientScreen;
import screen.Screen;
import thing.World;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.Socket;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class GameClient extends JFrame implements KeyListener {
    private AsciiPanel terminal;
    private Screen screen;
    private SocketChannel socketChannel;
    private Selector selector;

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
