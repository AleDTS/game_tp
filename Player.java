import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Player extends JFrame implements Runnable, KeyListener{

	  protected ObjectInputStream i;
    protected DataOutputStream o;
    protected int player;
    protected Thread listener;
    protected GameRun client = null;
    protected boolean[] buttons = new boolean[5];
    boolean close = false;

    public Player (int player, InputStream i, OutputStream o) {
      	super("Player " + player);
      	this.player = player;
        try {
            this.i = new ObjectInputStream (new BufferedInputStream (i));
          	this.o = new DataOutputStream (new BufferedOutputStream (o));
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to host");
        }
        client = new GameRun(player,4);
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
      	// setDefaultCloseOperation(EXIT_ON_CLOSE);
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


    public static void main (String args[]) throws IOException {
      	Socket s = new Socket (args[1], Integer.parseInt (args[2]));
      	new Player (Integer.parseInt(args[0]), 
      		s.getInputStream (), s.getOutputStream ());
    }
}

// import java.io.*;
// import java.net.*;
// import java.util.*;

// public class Player implements Runnable {
//   	//static PrintStream os = null;
//   	static DataOutputStream os = null;
//   	static boolean stopThread = false;
//   	static boolean[] buttons = new boolean[5];
//   	GameRun client = null;
  	
//   	static int porta = 5974;
  	

//   	public static void main(String[] args) {
// 	    Socket socket = null;
// 	    boolean b = true;
// 	    ObjectInputStream is = null;
// 	    Thread t = new Thread(new Player());

// 	    try {
// 	      	socket = new Socket("127.0.0.1", porta);
// 	      	os = new DataOutputStream(socket.getOutputStream());
// 	      	is = new ObjectInputStream(socket.getInputStream());
// 	    } catch (UnknownHostException e) {
// 	      	System.err.println("Don't know about host.");
// 	    } catch (IOException e) {
// 	      	System.err.println("Couldn't get I/O for the connection to host");
// 	    }


// 	    t.start();
	    

// 	    try {
// 	      	int posX, posY;

// 		    do {
// 		    	for(int i=0; i<buttons.length; i++)
// 		    		buttons[i] = is.readBoolean();
// 		    	//posX = is.readInt();
// 		    	//posY = is.readInt();
// 		    	//System.out.println("hey");
// 		    	//System.out.println(posX+" "+posY);
// 		    } while (b);

// 		    stopThread = true;
// 		    os.close();
// 		    is.close();
// 		    socket.close();
// 	    } catch (UnknownHostException e) {
// 	      	System.err.println("Trying to connect to unknown host: " + e);
// 	    } catch (IOException e) {
// 	     	System.err.println("IOException:  " + e);
// 	    }	
//   	}

//   	public void run() {
//   		Scanner tecl = new Scanner(System.in);
//   		int player = tecl.nextInt();
//   		System.out.println(player);
//   		client = new GameRun(player,4);
//   		new Start(client, "Teste");

//     	int posX, posY;

//     	do {
//     		client.buttons(buttons);
//     		//posX = client.posX();
//     		//posY = client.posY();

// 	      	//System.out.print("hey");

// 	      	try {
// 		      	//os.writeInt(posX);
// 	            //os.writeInt(posY);
// 	            for(int i=0; i<buttons.length; i++){
// 	            	os.writeBoolean(buttons[i]);
// 	            	System.out.printf("%b ",buttons[i]);
// 	            }
// 	            System.out.printf("\n");
// 	            //System.out.println("hey");
	        
// 	      	} catch (IOException e) { 
// 	      		System.out.println("deu ruim");
// 	      	}
//    	 	} while (!stopThread);
//   	}
// }
