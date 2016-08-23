import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Scanner;

public class Player extends JFrame implements Runnable, KeyListener{

	  protected ObjectInputStream i;
    protected ObjectOutputStream o;
    protected Thread listener;
    protected GameRun client = null;
    
    public int player, players;
    boolean close = false, start = false, ready = false;
    boolean[][] bWall = null;

    public Player (int player, Socket s) throws IOException,ClassNotFoundException{
      	super("Player " + player);
      	this.player = player;
        this.o = new ObjectOutputStream (s.getOutputStream ());
        this.i = new ObjectInputStream (s.getInputStream ());

        do{
            players = i.readInt();
            System.out.println("Players: " + players);
            System.out.println
            ("Digite 'ready' e aguarde os outros players para começar");
            Scanner tecl = new Scanner(System.in);
            String com = tecl.nextLine();
            write(com);
            if (com.equalsIgnoreCase("ready"))
                ready = true;
            start = i.readBoolean();
        } while(!start);
        
        Background bg;

        if (player == 1){
          System.out.println("caso 1 " + players);
            bg = new Background(players,850,650,bWall);
            bWall = bg.breakable_wall;
            o.writeObject(bWall);
        }

        else{
          System.out.println("caso 2");
            bWall = (boolean[][])i.readObject();
            bg = new Background(players,850,650,bWall);
        }

        client = new GameRun(players, players, bg, 850, 650);

      	add(client);
        addKeyListener(client.key);
        addKeyListener(this);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                  write("close");
                  close = true;
                  if (listener != null)
                      listener.stop ();
                  hide ();
                  System.exit(0);
            }
        });
      	pack();
      	setResizable(false);
      	setFocusable(true);
      	setVisible(true);
      	listener = new Thread (this);
      	listener.start ();
    }

    public void run () {
        try {
      		  while (!close) {
          		  String msg = i.readUTF ();
                client.receive(msg);
        		}
      	} catch (IOException ex) {
        		ex.printStackTrace ();
      	} finally {
        		listener = null;
        		validate ();
          try {
            o.close ();
        	} catch (IOException ex) {
            ex.printStackTrace ();
        	}
        }
    }

    public void keyPressed(KeyEvent e){
        write(client.msg);
    }

    public void keyReleased(KeyEvent e){
        write(client.msg);
    }

    public void write(String msg){
      //System.out.println("hey");
        try {
            // System.out.println(client.msg);
            o.writeUTF(msg);
            o.flush ();
        } catch (IOException ex) {
            ex.printStackTrace();
            listener.stop ();
        }
    }

    public void keyTyped(KeyEvent e){}


    public static void main (String args[]) throws IOException, ClassNotFoundException {
      	Socket s = new Socket (args[1], Integer.parseInt (args[2]));
      	new Player (Integer.parseInt(args[0]), s);
    }
}