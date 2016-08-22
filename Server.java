import java.net.*;
import java.io.*;
import java.util.*;

public class Server {
    public Server (int port) throws IOException {
        ServerSocket server = new ServerSocket (port);

        while (true) {
            Socket client = server.accept ();
            System.out.println ("Accepted from " + client.getInetAddress ());
            Handler c = new Handler (client);
            c.start ();
        }
    }

    public static void main (String args[]) throws IOException {
        if (args.length != 1)
            throw new RuntimeException ("Syntax: Server <port>");
        new Server (Integer.parseInt (args[0]));
    }
}

class Handler extends Thread {
    protected Socket s;
    protected ObjectInputStream i;
    protected ObjectOutputStream o;

    int players;
    boolean[][] breakable_wall;
    
    public Handler (Socket s) throws IOException {
        this.s = s;
        i = new ObjectInputStream (new BufferedInputStream (s.getInputStream ()));
        o = new ObjectOutputStream (new BufferedOutputStream (s.getOutputStream ()));
    }

    protected static Vector handlers = new Vector ();
    
    public void run () {
        String msg = null;
        try {
            handlers.addElement (this);
            while (readMsg(msg = i.readUTF ())) 
                broadcast (msg);
        } catch (IOException ex) {
            ex.printStackTrace ();
        } finally {
            handlers.removeElement (this);
            try {
                s.close ();
            } catch (IOException ex) {
            ex.printStackTrace();
        }
        }   
    }

    public boolean readMsg(String msg){
        if (msg.equals("close"))
            return false;
        else
            return true;
    }

    protected static void broadcast (String message) {
    
        synchronized (handlers) {
            Enumeration e = handlers.elements ();
            
            while (e.hasMoreElements ()) {
                Handler c = (Handler) e.nextElement ();
                try {
                    synchronized (c.o) {
                        c.o.writeUTF (message);
                    }
                    c.o.flush ();
                } catch (IOException ex) {
                    c.stop ();
                }
            }
        }
    }
}

// import java.net.*;
// import java.io.*;
// import java.util.*;

// class Server {
//     public static void main(String[] args) {
//         ServerSocket serverSocket=null;
//         int porta = 5974;

//         try {
//             serverSocket = new ServerSocket(porta);
//         } catch (IOException e) {
//             System.out.println("Could not listen on port: " + porta + ", " + e);
//             System.exit(1);
//         }

//         for (int i=0; i<4; i++) {
//             Socket playerSocket = null;
//             try {
//                 playerSocket = serverSocket.accept();
//             } catch (IOException e) {
//                 System.out.println("Accept failed: " + porta + ", " + e);
//                 System.exit(1);
//             }

//             System.out.println("Accept Funcionou!");

//             new Serving(playerSocket).start();
//         }

//         try {
//             serverSocket.close();
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }
// }


// class Serving extends Thread {
//     Socket playerSocket;
//     static ObjectOutputStreamos[] = new DataOutputStream[4];
//     static int players=0;
//     boolean[] buttons = new boolean[5];
//     boolean b = true;

//     Serving(Socket playerSocket) {
//         this.playerSocket = playerSocket;
//     }

//     public void run() {
//         try {
//             ObjectInputStream is = new ObjectInputStream(playerSocket.getInputStream());
//             os[players++] = new DataOutputStream(playerSocket.getOutputStream());

//             int posX, posY;

//             do {
//                 //for(int k=0; k<players; k++)
//                 //posX = is.readInt();
//                 //posY = is.readInt();
//                 for(int k=0; k<players; k++) 
//                     for(int i=0; i<buttons.length; i++)
//                         buttons[i] = is.readBoolean();

//                 for(int k=0; k<players; k++) {
                    
//                 System.out.printf("P%d : ", (k+1));
//                     for(int i=0; i<buttons.length; i++){
//                         os[k].writeBoolean(buttons[i]);
//                         System.out.printf("%b ",buttons[i]);
//                     }
//                 System.out.printf("\n");
//                     //os[i].writeInt(posX);
//                     //os[i].writeInt(posY);
//                     os[k].flush();
//                 }
//             } while (b);


//             for (int i=0; i<players; i++)
//                 os[i].close();
//             is.close();
//             playerSocket.close();

//         } catch (IOException e) {
//             e.printStackTrace();
//         } catch (NoSuchElementException e) {
//             System.out.println("Conexacao terminada pelo cliente");
//         }  
//     }
// };