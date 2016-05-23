import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import javax.swing.Timer;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GameRun extends GameBase {
	public static int MAX_BOMBS = 5;
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
			if (bomber.bombs < Bomber.MAX_BOMBS && bomb.size() < MAX_BOMBS)
				bomb.add(bomber.dropBomb());
			//System.out.println(bomber.bombs);
			key.button(keys[4]);
		}
	}

	public void paint(Graphics g){
		keys();
		
		bomber.reset();
		for(Bomb i : bomb){
			i.hitBomb(bomber);
		}
	    bg.hitWall(bomber);

	   	for(Bomb i : bomb){
			if (i.counter(mSec)){
				i.explode();
				bomb.remove(i);
			}
		}

		bg.draw(g);
		for(Bomb i : bomb)
			i.draw(g);
		bomber.draw(g);
	}

	public static void main(String[] args) {
		GameBase run = new GameRun();
		new Start(run, "Teste");
	}

	class Counter implements ActionListener {
		
		public Counter(){
			Timer t = new Timer(100, this);
			t.setInitialDelay(0);
			t.setCoalesce(true);
			t.start();
		}
		
		public void actionPerformed(ActionEvent e){
			mSec++;
			//System.out.println(mSec);
		}
	}
}