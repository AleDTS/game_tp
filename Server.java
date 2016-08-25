import java.net.*;
import java.io.*;
import java.util.*;

public class Server {
    public static int players = 0;
    public static boolean start;
    public boolean [][] bWall;

    public Server (int port) throws IOException, ClassNotFoundException {
        ServerSocket server = new ServerSocket (port);
        
        boolean start = false;

        while(true){
            Socket client = server.accept ();
            System.out.println ("Accepted from " + client.getInetAddress ());
            ++players;
            Handler c = new Handler (client);
            c.start ();
            // System.out.println (Handler.bWall);
        }
    }

    public static void main (String args[]) throws IOException, ClassNotFoundException {
        if (args.length != 1)
            throw new RuntimeException ("Syntax: Server <port>");
        new Server (Integer.parseInt (args[0]));
    }
}

class Handler extends Thread {
    protected Socket s;
    protected DataInputStream i;
    protected DataOutputStream o;

    String com ;
    boolean start = false, ready;
    public static boolean[][] bWall = null;
    int player;
    
    public Handler (Socket s) throws IOException, ClassNotFoundException {
        this.s = s;
        o = new DataOutputStream (s.getOutputStream ());
        i = new DataInputStream (s.getInputStream ());
        

    }

    protected static Vector handlers = new Vector ();
    
    public void run () {
        int x, y, p;
        boolean drop, ready;

        try {
            handlers.addElement (this);

            o.writeInt(player = Server.players);
            o.flush();
           
            do{           
                if (Server.players == 2)
                    start = true;
                o.writeInt(Server.players);
                o.writeBoolean(start);
                o.flush();
            }while(!start);

            int lin, col, m, n;
            lin = i.readInt();
            col = i.readInt();
            bWall = new boolean[lin][col];

            if (player == 1){
                for (m=0; m<lin; m++)
                    for (n=0; n<col; n++)
                        bWall[m][n] = i.readBoolean();

            }
            else{
                for (m=0; m<lin; m++)
                    for (n=0; n<col; n++)
                        o.writeBoolean(bWall[m][n]);
                o.flush();
            }

            while (true) {
                p = i.readInt();
                x = i.readInt();
                y = i.readInt();
                drop = i.readBoolean();
                broadcast (p,x,y,drop);
            }
        } catch (IOException ex) {
            ex.printStackTrace ();
        }  
        finally {
            handlers.removeElement (this);
            Server.players--;
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

    protected static boolean allReady(){
        synchronized (handlers) {
            Enumeration e = handlers.elements ();

            while (e.hasMoreElements ()) {
                Handler c = (Handler) e.nextElement ();
                if (!c.ready)
                    return false;
            }
            return true;
        }
    }

    protected static void broadcast (int p, int x, int y, boolean drop) {
    
        synchronized (handlers) {
            Enumeration e = handlers.elements ();
            
            while (e.hasMoreElements ()) {
                Handler c = (Handler) e.nextElement ();
                try {
                    synchronized (c.o) {
                        c.o.writeInt(p);
                        c.o.writeInt(x);
                        c.o.writeInt(y);
                        c.o.writeBoolean(drop);
                    }
                    c.o.flush ();
                } catch (IOException ex) { }
            }
        }
    }
}