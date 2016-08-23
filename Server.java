import java.net.*;
import java.io.*;
import java.util.*;

public class Server {
    public static int players = 0;
    public static boolean start;
    public boolean [][] bWall;

    public Server (int port) throws IOException {
        ServerSocket server = new ServerSocket (port);
        
        boolean start = false;

        while(true){
            Socket client = server.accept ();
            System.out.println ("Accepted from " + client.getInetAddress ());
            Handler c = new Handler (client);
            ++players;
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

    String com;
    boolean start = false, ready = false;
    public static boolean[][] bWall = null;
    
    public Handler (Socket s) throws IOException {
        this.s = s;
        o = new ObjectOutputStream (s.getOutputStream ());
        o.flush();
        i = new ObjectInputStream (s.getInputStream ());
    }

    protected static Vector handlers = new Vector ();
    
    public void run () {
        String msg = null;
        try {
            handlers.addElement (this);

            do{
                o.writeInt(Server.players);
                o.flush();
                com = i.readUTF();
                if (com.equalsIgnoreCase("READY"))
                    ready = true;
                start = allReady();
                o.writeBoolean(start);
                o.flush();
            }while(!start);

            if (bWall == null)
                bWall = (boolean[][])i.readObject();
            else{
                o.writeObject(bWall);
                o.flush();
            }
            
            while (readMsg(msg = i.readUTF ())) {
                broadcast (msg);
            }
        } catch (IOException ex) {
            ex.printStackTrace ();
        } catch (ClassNotFoundException e) {}   
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

    protected static void broadcastObj (Object obj) {
    
        synchronized (handlers) {
            Enumeration e = handlers.elements ();
            
            while (e.hasMoreElements ()) {
                Handler c = (Handler) e.nextElement ();
                try {
                    synchronized (c.o) {
                        c.o.writeObject (obj);
                    }
                    c.o.flush ();
                } catch (IOException ex) {
                    c.stop ();
                }
            }
        }
    }

}