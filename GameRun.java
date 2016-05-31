import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import javax.swing.Timer;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GameRun extends GameBase {
	public static int TIME_BOMBS = 50;
	public static int TOP_BOUND = 0;
	public static int BOTTOM_BOUND = 650;
	public static int LEFT_BOUND = 0;
	public static int RIGHT_BOUND = 850;
	public int mSec;
	
	KeyEvent k;
	int[] keys = {k.VK_RIGHT, k.VK_LEFT, k.VK_UP, k.VK_DOWN, k.VK_SPACE};
	Keys key = new Keys(keys);
	Queue<Bomb> bomb = new ConcurrentLinkedQueue<Bomb>();
	Bomb bomb_aux = null;

	Bomber bomber = new Bomber();
	Background bg;
	
	public void init(){
		bg = new Background(BOTTOM_BOUND/50, RIGHT_BOUND/50);
		addKeys(key);
		new Counter();
	}

	public void keys(){
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
				if (bomb_aux == null || bomb.size() == 0){
					bomb_aux = bomber.dropBomb();
					bomb.add(bomb_aux);
				}
				else if (!bomber.inside(bomb_aux))
					bomb.add(bomber.dropBomb());
			}
			key.button(keys[4]);
		}
	}

	public void paint(Graphics g){
		keys();
		
		bomber.reset();
	    bg.hitWall(bomber);				//If bomber hits static wall

		//Tests before painting
		for(Bomb i : bomb){
			if (i.counter(mSec)){ 		//If bomb counter ends
				i.remove = true; 		
				if (i.explode(bomber)) 	//If bomber hitted
					bomber.hitted();
				for(Bomb b : bomb) 		//If another bomb hitted
					if (i.explode(b))
						b.remove = true;
				bg.hitBreakable(i);		//If explosion hits breakable wall
			}
			i.hitBomb(bomber);			//If bomber colides bomb
			bg.hitWall(i);				//If bomb colides static wall
		}

		//Painting
		bg.draw(g);
		bg.drawBreakable(g);
		for(Bomb i : bomb)
			i.draw(g);

		
		for(Bomb i : bomb){					//Explosion
			if (i.remove == true){
				i.drawExplosion(g);
				if (i.counter(mSec, 10))	//Explosion time!
					bomb.remove(i);
				if (i.explode(bomber))
					bomber.hitted();
			}
		}
		bomber.draw(g);
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