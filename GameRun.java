import java.awt.*;
import java.net.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import javax.swing.Timer;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GameRun extends GameBase {
	protected DataInputStream i;
    protected DataOutputStream o;
	public static int TIME_BOMBS = 50;
	public static int TOP_BOUND;
	public static int BOTTOM_BOUND;
	public static int LEFT_BOUND;
	public static int RIGHT_BOUND;
	public static int PLAYERS;
	public boolean open = true;
	public int PLAYER;
	public int mSec;
	public String msg;
	
	public Boolean win = false;
	int posX, posY, winner;
	boolean dropped;
	
	
	int cont=0;

	KeyEvent k;
	int[] keys = {k.VK_RIGHT, k.VK_LEFT, k.VK_UP, k.VK_DOWN, k.VK_SPACE, k.VK_Q};
	Keys key = new Keys(keys);
	Queue<Bomb> bombs = new ConcurrentLinkedQueue<Bomb>();
	Map<Integer, Bomber> bombers = new HashMap<Integer, Bomber>();
	Background bg;

	public GameRun(Socket socket) throws IOException{
		this.o = new DataOutputStream (socket.getOutputStream ());
        this.i = new DataInputStream (socket.getInputStream ());
		setSize(650,650);
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

		for (int i = 1; i <= PLAYERS; i++)
			bombers.put(i,new Bomber(i, width, height));

		int lin = height/50, col = width/50, m,n;
		o.writeInt(lin);
		o.writeInt(col);
		o.flush();

		boolean[][] bWall = new boolean[lin][col];
		for (m=0; m<lin; m++)
        	for (n=0; n<col; n++)
        		bWall[m][n] = i.readBoolean();

        bg = new Background(PLAYERS, width, height, bWall);
		new Counter();
	}

	public int player(){
		return PLAYER;
	}

	public void close(){
		try{
			o.writeUTF("close");
			o.flush();
		} catch (IOException ex) {

        }
	}

	public void finish(Graphics g){
		finish = true;
		System.out.println("Player " +winner+ " wins!");
	}

    public void others(){
    	Bomber bomber;
    		int p;
		int x, y;
		boolean drop, w;
		try {

    	for (int n=0; n<PLAYERS; n++){

    		String str = i.readUTF();
    		Scanner msg = new Scanner(str).useDelimiter("#");

    		w = msg.nextBoolean();
    		p = msg.nextInt();
    		x = msg.nextInt();
    		y = msg.nextInt();
    		drop = msg.nextBoolean();

  		  	if (p!= PLAYER){
	  		  	bomber = bombers.get(p);
	  		  	bomber.setPosX(x);
	  		  	bomber.setPosY(y);
	  		  	if(drop)
	  		  		bombs.add(bomber.dropBomb());
	  		  	if(w){
	  		  		win = w;
	  		  		winner = p;
	  		  	}
  		  	}
		}	
		} catch (IOException ex) {
    		ex.printStackTrace ();
        }
    }

	public void sendMsg(Integer posX, Integer posY, Boolean drop){
		try {
			String b = drop.toString();
			String x = posX.toString();
			String y = posY.toString();
			String w = win.toString();
			Integer pl = PLAYER;
			String p = pl.toString();
			String msg = w+"#"+p+"#"+x+"#"+y+"#"+b;
			o.writeUTF(msg);
            o.flush ();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
	}
	
	public void action(){
		Bomber bomber;
		Bomb bomb_aux = null;
		boolean up, down, right, left, space, q;
		dropped = false;

		right = key.isPressed(keys[0]);
		left = key.isPressed(keys[1]);
		up = key.isPressed(keys[2]);
		down = key.isPressed(keys[3]);
		space = key.button(keys[4]);
		q = key.isPressed(keys[5]);


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

		if (q || bomber.win()){
			win = true;
			winner = PLAYER;
		}
		bomber.reset();

		posX = bomber.posX;
		posY = bomber.posY;
}


	public void paint(Graphics g){
		Bomber bomber;
		int p = 0;

		action();
		sendMsg(posX, posY, dropped);
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
		//System.out.println(win);
		if (win == true){
			g.setColor(Color.RED);
			g.setFont(new Font("TimesRoman", Font.PLAIN, 30)); 
			g.drawString("Player " +winner+ " wins!",50,100);
			System.out.println("Player " +winner+ " wins!");
			finish = true;
		}
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
		if (args.length != 2)
            throw new RuntimeException ("Syntax: GameRun <adress> <port>");
      	Socket s = new Socket (args[0], Integer.parseInt(args[1]));
      	GameRun game = new GameRun(s);
      	new Start(game,game.player(), width, height);
    }
}