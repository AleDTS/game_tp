import java.net.*;
import java.io.*;
import java.util.*;

public class Server {
    public static int players = 0;
    public static int currentPlayers = 0;
    public static boolean start, ok = false;
    public boolean [][] bWall;

    public Server (int port, int players) throws IOException, ClassNotFoundException {
        ServerSocket server = new ServerSocket (port);
        this.players = players;
        boolean start = false;

        while(true){
            Socket client = server.accept ();
            System.out.println ("Accepted from " + client.getInetAddress ());
            ++currentPlayers;
            Handler c = new Handler (client);
            c.start ();
        }
    }

    public static void main (String args[]) throws IOException, ClassNotFoundException {
        if (args.length != 2)
            throw new RuntimeException ("Syntax: Server <port> <players>");
        if (Integer.parseInt(args[1]) > 4)
            throw new RuntimeException ("1-4 players!");
        new Server (Integer.parseInt(args[0]), Integer.parseInt(args[1]) );
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

            o.writeInt(player = Server.currentPlayers);
            o.flush();
           
            do{           
                if (Server.currentPlayers == Server.players)
                    start = true;
                o.writeInt(Server.currentPlayers);
                o.writeBoolean(start);
                o.flush();
            }while(!start);

            int lin, col, m, n;
            lin = i.readInt();
            col = i.readInt();

            if(!(Server.ok)){
                bWall = new boolean[lin][col];
                bWall = breakableWall(Server.currentPlayers,lin,col, 0.8);
                Server.ok = true;
            }

            for (m=0; m<lin; m++)
                for (n=0; n<col; n++)
                    o.writeBoolean(bWall[m][n]);
                o.flush();

            boolean stop = false;
            do{
                msg = i.readUTF();
                if (msg.equals("close"))
                    stop = true;
                broadcast(msg);
            }while(!stop);
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
(players >= 1 && (i>=0 && i<3 && j==0) || 
    (j>=0 && j < 3 && i == 0)   ||   (i == (int)lin/2 && j == col-1)) ||
(players >= 2 && (i>=0 && i<3 && j==(col-1)) || 
    (j>=(col-3) && j < col && i == 0) || (i == lin-1 && j == (int)col/2) ||
(players >= 3 && (i>=(lin-3) && i < lin && j == 0) || 
    (j>=0 && j < 3 && i == (lin-1)) || (i == (int)lin/2 && j == 0)) ||
(players >= 2 && (i>=(lin-3) && i < lin && j == (col-1)) || 
    (j>=(col-3) && j < col && i == (lin-1)) || (i == 0 && j == (int)col/2))))
                        notHere = true;
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

    protected static void broadcast (String msg) {
    
        synchronized (handlers) {
            Enumeration e = handlers.elements ();
            
            while (e.hasMoreElements ()) {
                Handler c = (Handler) e.nextElement ();
                try {
                    synchronized (c.o) {
                        c.o.writeUTF(msg);
                    }
                    c.o.flush ();
                } catch (IOException ex) { }
            }
        }
    }
}