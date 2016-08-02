import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import javax.swing.Timer;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GameRun extends GameBase {
	public static int TIME_BOMBS = 50;
	public static int TOP_BOUND;
	public static int BOTTOM_BOUND;
	public static int LEFT_BOUND;
	public static int RIGHT_BOUND;
	public static int PLAYERS = 4;
	public int PLAYER;
	public int mSec;
	
	KeyEvent k;
	int[] keys = {k.VK_RIGHT, k.VK_LEFT, k.VK_UP, k.VK_DOWN, k.VK_SPACE};
	Keys key = new Keys(keys);
	Queue<Bomb> bombs = new ConcurrentLinkedQueue<Bomb>();
	Map<Integer, Bomber> bombers = new HashMap<Integer, Bomber>();
	Background bg;
		
	public void init(){
		setSize(850,650);
		TOP_BOUND = 0;
		BOTTOM_BOUND = height;
		LEFT_BOUND = 0;
		RIGHT_BOUND = width;
		PLAYER = 1;
		int lin = BOTTOM_BOUND/50;
		int col = RIGHT_BOUND/50;
		System.out.println(lin+" "+col);
		for (int i = 1; i <= PLAYERS; i++)
			bombers.put(i,new Bomber(i, lin, col));
		bg = new Background(PLAYERS, lin, col);
		addKeys(key);
		new Counter();
		System.out.println(PLAYER);
	}

	public void keys(Bomber bomber){
		Bomb bomb_aux = null;
		boolean up, down, right, left, space;
		
		right = key.isPressed(keys[0]);
		left = key.isPressed(keys[1]);
		up = key.isPressed(keys[2]);
		down = key.isPressed(keys[3]);
		space = key.isPressed(keys[4]);
		
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
				}
				else if (!bomber.inside(bomb_aux))
					bombs.add(bomber.dropBomb());
			}
			key.button(keys[4]);
		}

		bomber.reset();
	}

	public void paint(Graphics g){
		Bomber bomber;
		keys(bombers.get(PLAYER));
		
		for (Map.Entry<Integer, Bomber> b : bombers.entrySet()){
			bomber = b.getValue();
	    	bg.hitWall(bomber);				//If bomber hits static wall
		}

		//Tests before painting
		for(Bomb i : bombs){
			if (i.counter(mSec)){ 		//If bomb counter ends
				i.remove = true; 
				for (Map.Entry<Integer, Bomber> b : bombers.entrySet()){
					bomber = b.getValue();
					if (i.explode(bomber)) 	//If bomber hitted
						bomber.hitted();
				}
				for(Bomb b : bombs) 		//If another bomb hitted
					if (i.explode(b))
						b.remove = true;
				bg.hitBreakable(i);		//If explosion hits breakable wall
			}
			for (Map.Entry<Integer, Bomber> b : bombers.entrySet()){
				bomber = b.getValue();
				i.hitBomb(bomber);		//If bomber colides bomb
			}
			bg.hitWall(i);				//If bomb colides static wall
		}

		//Painting
		bg.draw(g);
		bg.drawBreakable(g);
		for(Bomb i : bombs)
			i.draw(g);

		
		for(Bomb i : bombs){					//Explosion
			if (i.remove == true){
				i.drawExplosion(g);
				if (i.counter(mSec, 10))	//Explosion time!
					bombs.remove(i);
				for (Map.Entry<Integer, Bomber> b : bombers.entrySet()){
					bomber = b.getValue();
					if (i.explode(bomber))
						bomber.hitted();
				}
			}
		}

		for (Map.Entry<Integer, Bomber> b : bombers.entrySet()){
			bomber = b.getValue();
			bomber.draw(g);
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

	public static void main(String[] args) {
		GameBase run = new GameRun();
		new Start(run, "Teste");
	}
}