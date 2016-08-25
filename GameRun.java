import java.awt.*;
import java.net.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import javax.swing.Timer;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GameRun extends GameBase implements Runnable {
	// public static int RIGHT=0, LEFT=1, UP=2, DOWN=3, SPACE=4;
	// public static String[] msg_others = {"1fffff","2fffff","3fffff","4fffff"};
	protected DataInputStream i;
    protected DataOutputStream o;
    protected Thread listener;
	public static int TIME_BOMBS = 50;
	public static int TOP_BOUND;
	public static int BOTTOM_BOUND;
	public static int LEFT_BOUND;
	public static int RIGHT_BOUND;
	public static int PLAYERS;
	public int PLAYER;
	public int mSec;
	public String msg;
	
	int posX, posY;
	boolean dropped;
	boolean[][] bWall;
	boolean up, down, right, left, space;
	int cont=0;
	
	KeyEvent k;
	int[] keys = {k.VK_RIGHT, k.VK_LEFT, k.VK_UP, k.VK_DOWN, k.VK_SPACE};
	Keys key = new Keys(keys);
	Queue<Bomb> bombs = new ConcurrentLinkedQueue<Bomb>();
	Map<Integer, Bomber> bombers = new HashMap<Integer, Bomber>();
	Background bg;

	public GameRun(Socket socket) throws IOException{
		this.o = new DataOutputStream (socket.getOutputStream ());
        this.i = new DataInputStream (socket.getInputStream ());
		setSize(width = 850,height = 650);
		//addListener(key);
		key.add();
		TOP_BOUND = 0;
		BOTTOM_BOUND = height;
		LEFT_BOUND = 0;
		RIGHT_BOUND = width;

		PLAYER = i.readInt();

		boolean start = false;
		do{
			PLAYERS = i.readInt();
			start = i.readBoolean();
        }while(!start);

		int lin = height/50, col = width/50, m,n;
		o.writeInt(lin);
		o.writeInt(col);
		o.flush();
		//bWall = new boolean[lin][col];

		if (PLAYER == 1){
			bWall = breakableWall(PLAYERS,lin,col,0.8);
	        for (m=0; m<lin; m++)
	        	for (n=0; n<col; n++)
	        		o.writeBoolean(bWall[m][n]);
			o.flush();
		}

		else{
			bWall = new boolean[lin][col];
			for (m=0; m<lin; m++)
	        	for (n=0; n<col; n++)
	        		bWall[m][n] = i.readBoolean();
		}	

        System.out.println(PLAYERS);

        bg = new Background(PLAYERS, width, height, bWall);


		for (int i = 1; i <= PLAYERS; i++)
			bombers.put(i,new Bomber(i, width, height));
		new Counter();
		// listener = new Thread (this);
  //     	listener.start ();
	}


	// public void receive(String msg){
	// 	int i = Character.getNumericValue(msg.charAt(0));
	// 	//System.out.println(msg);
	// 	msg_others[i-1] = msg;
	// }

	// public void msg(){
	// 	msg = buttons2str();
	// }

	public void run() {
		int p;
		int x, y;
		boolean drop;
		Bomber bomber;
        try {
      		while (true) {
      		  	p = i.readInt();
      		  	x = i.readInt();
      		  	y = i.readInt();
      		  	drop = i.readBoolean();

      		  	bomber = bombers.get(p);
	  		  	bomber.setPosX(x);
	  		  	bomber.setPosY(y);
      		  	if(drop)
	      		  	bomber.dropBomb();
      		  	//System.out.println(p+" x: "+x+" y: "+y+" "+drop);
        	}
        } catch (IOException ex) {
        		ex.printStackTrace ();
      	} finally {
        		listener = null;
        		// validate ();
	      try {
	        o.close ();
	      } catch (IOException ex) {
	        ex.printStackTrace ();
	    	}
        }
    }

    public void others(){
    	Bomber bomber;
    		int p;
		int x, y;
		boolean drop;
		try {
    	//for(Map.Entry<Integer, Bomber> b : bombers.entrySet()){
    	for (int n=0; n<PLAYERS; n++){
			//bomber = b.getValue();
			p = i.readInt();
  		  	x = i.readInt();
  		  	y = i.readInt();
  		  	drop = i.readBoolean();

  		  	// if (p!= PLAYER){
	  		  // 	bomber = bombers.get(p);
	  		  // 	bomber.setPosX(x);
	  		  // 	bomber.setPosY(y);
	  		  // 	if(drop)
	  		  // 		bombs.add(bomber.dropBomb());
  		  		
  		  	// }
  		  	//System.out.println(p+" x: "+x+" y: "+y+" "+drop);
		}	
		} catch (IOException ex) {
        		ex.printStackTrace ();
        }
    }

	public void action(){
		Bomber bomber;
		Bomb bomb_aux = null;
		dropped = false;

		right = key.isPressed(keys[0]);
		left = key.isPressed(keys[1]);
		up = key.isPressed(keys[2]);
		down = key.isPressed(keys[3]);
		space = key.button(keys[4]);

		//for(Map.Entry<Integer, Bomber> b : bombers.entrySet()){
			// bomber = b.getValue();
			// str2buttons(msg_others[b.getKey()-1]);			
			bomber = bombers.get(PLAYER);
			
			if(right && !bomber.oneDirection() && !left)
				bomber.moveRight(RIGHT_BOUND);
				else bomber.isMovingRight = false;
			if(left && !bomber.oneDirection() && !right)
				bomber.moveLeft(LEFT_BOUND);
				else bomber.isMovingLeft = false;
			if(up && !bomber.oneDirection() && !down)
				bomber.moveUp(TOP_BOUND);
				else bomber.isMovingUp = false;
			if(down && !bomber.oneDirection() && !up)
				bomber.moveDown(BOTTOM_BOUND);
				else bomber.isMovingDown = false;

			if(space){
				if (bomber.bombs < bomber.max){
					if (bomb_aux == null || bombs.size() == 0){
						bomb_aux = bomber.dropBomb();
						bombs.add(bomb_aux);
						dropped = true;
					}
					else if (!bomber.inside(bomb_aux)){
						bombs.add(bomber.dropBomb());
						dropped = true;
					}
				}
			}
			bomber.reset();

			posX = bomber.posX;
			posY = bomber.posY;
		//}
	}

	public void sendMsg(int x, int y, boolean drop){
		try {
            //System.out.println(client.msg);
            o.writeInt(PLAYER);
			o.writeInt(x);
			o.writeInt(y);
			o.writeBoolean(drop);
            o.flush ();
        } catch (IOException ex) {
            ex.printStackTrace();
            //listener.stop ();
        }
	}

	public void updatePlayers(){

	}

	public void paint(Graphics g){
		System.out.println(++cont);
		Bomber bomber;

		action();
		sendMsg(posX, posY, dropped);
		//receiveMsg(posX, posY, dropped);
		others();
		
		for (Map.Entry<Integer, Bomber> b : bombers.entrySet()){
			bomber = b.getValue();
	    	bg.hitWall(bomber);				//If bomber hits static wall
	    	for (Map.Entry<Integer, Bomber> b2 : bombers.entrySet())
	    		bomber.colided(b2.getValue());
		}

		//Tests before painting
		for(Bomb i : bombs){
			if (i.counter(mSec)){ 			//If bomb counter ends
				i.remove = true; 
				for (Map.Entry<Integer, Bomber> b : bombers.entrySet()){
					bomber = b.getValue();
					if (i.explode(bomber)) 	//If bomber hitted
						bomber.hitted();
				}
				for(Bomb b : bombs) 		//If another bomb hitted
					if (i.explode(b))
						b.remove = true;
				bg.hitBreakable(i);			//If explosion hits breakable wall
			}
			for (Map.Entry<Integer, Bomber> b : bombers.entrySet()){
				bomber = b.getValue();
				i.hitBomb(bomber);			//If bombers colide bomb
			}
			bg.hitWall(i);					//If bomb colides static wall
		}

		//Painting
		bg.draw(g);
		bg.drawBreakable(g);
		for(Bomb i : bombs)
			i.draw(g);

		
		for(Bomb i : bombs){					//Explosion
			if (i.remove == true){
				i.drawExplosion(g);
				if (i.counter(mSec, 10))		//Explosion time!
					bombs.remove(i);
				for (Map.Entry<Integer, Bomber> b : bombers.entrySet()){
					bomber = b.getValue();
					if (i.explode(bomber))		//If bombers walk into explosion
						bomber.hitted();
				}
			}
		}

		for (Map.Entry<Integer, Bomber> b : bombers.entrySet()){
			bomber = b.getValue();
			bomber.draw(g);
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
(players >= 1 && (i>=0 && i<3 && j==0) || (j>=0 && j < 3 && i == 0) ) ||
(players >= 2 && (i>=0 && i<3 && j==(col-1)) || (j>=(col-3) && j < col && i == 0)) ||
(players >= 3 && (i>=(lin-3) && i < lin && j == 0) || (j>=0 && j < 3 && i == (lin-1))) ||
(players >= 2 && (i>=(lin-3) && i < lin && j == (col-1)) || (j>=(col-3) && j < col && i == (lin-1)))
					)	notHere = true;
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

	//This counter controls game time
	class Counter implements ActionListener { 	
		
		public Counter(){
			Timer t = new Timer(100, this);
			t.setInitialDelay(0);
			t.setCoalesce(true);
			t.start();
		}
		
		public void actionPerformed(ActionEvent e){
			mSec++;
		}
	}

	public static void main (String args[]) throws IOException {
      	Socket s = new Socket (args[0], Integer.parseInt(args[1]));
      	GameBase game = new GameRun(s);
      	new Start(game, width, height);
    }
}
	// public String buttons2str(){
	// 	String msg;
	// 	msg = Integer.toString(PLAYER);
	// 	if (right)
	// 		msg += "r";
	// 		else
	// 			msg += "f";
	// 	if (left)
	// 		msg += "l";
	// 		else
	// 			msg += "f";
	// 	if (up)
	// 		msg += "u";
	// 		else
	// 			msg += "f";
	// 	if (down)
	// 		msg += "d";
	// 		else
	// 			msg += "f";
	// 	if (space)
	// 		msg += "s";
	// 		else
	// 			msg += "f";
		
	// 	return msg;
	// }

	// public void str2buttons(String msg){
	// 	if (msg.charAt(1) == 'r')
	// 		right = true;
	// 		else
	// 			right = false;
	// 	if (msg.charAt(2) == 'l')
	// 		left = true;
	// 		else
	// 			left = false;
	// 	if (msg.charAt(3) == 'u')
	// 		up = true;
	// 		else
	// 			up = false;
	// 	if (msg.charAt(4) == 'd')
	// 		down = true;
	// 		else
	// 			down = false;
	// 	if (msg.charAt(5) == 's')
	// 		space = true;
	// 		else
	// 			space = false;
	// }