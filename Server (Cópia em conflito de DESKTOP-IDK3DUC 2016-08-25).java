import java.net.*;
import java.io.*;
import java.util.*;

public class Server {
    public static int players = 0;
    public static boolean start, ok = false;
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
    boolean start = false, ready, ok = false;
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
        String msg;

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

            if(!(Server.ok)){
                System.out.println("hey");
                bWall = new boolean[lin][col];
                bWall = breakableWall(Server.players,lin,col, 0.8);
                Server.ok = true;
            }

            for (m=0; m<lin; m++)
                for (n=0; n<col; n++)
                    o.writeBoolean(bWall[m][n]);
                o.flush();

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

    public boolean[][] breakableWall(int players, int lin, int col, double prob){
        boolean notHere = false;
        int i = 0, j = 0;
        boolean[][] bWall = new boolean[lin][col];

        for (i = 0; i<lin; i++){
            for (j = 0; j<col; j++){
                if (i%2!=0 && j%2!=0)
                    continue;
                else {
                    if (
(players >= 1 && (i>=0 && i<3 && j==0) || (j>=0 && j < 3 && i == 0) ) ||
(players >= 2 && (i>=0 && i<3 && j==(col-1)) || (j>=(col-3) && j < col && i == 0)) ||
(players >= 3 && (i>=(lin-3) && i < lin && j == 0) || (j>=0 && j < 3 && i == (lin-1))) ||
(players >= 2 && (i>=(lin-3) && i < lin && j == (col-1)) || (j>=(col-3) && j < col && i == (lin-1)))
                    )   notHere = true;
                    else
                        notHere = false;
                    
                    if (Math.random() <= prob && !notHere)
                        bWall[i][j] = true;
                    else
                        bWall[i][j] = false;
                }
            }
        }

        return bWall;
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