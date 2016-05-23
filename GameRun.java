import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import javax.swing.Timer;

public class GameRun extends GameBase {
	public static int MAX_BOMBS = 5;
	public static int TOP_BOUND = 0;
	public static int BOTTOM_BOUND = 650;
	public static int LEFT_BOUND = 0;
	public static int RIGHT_BOUND = 850;
	int cont_bombs = 0;
	
	KeyEvent k;
	int[] keys = {k.VK_RIGHT, k.VK_LEFT, k.VK_UP, k.VK_DOWN, k.VK_SPACE};
	Keys key = new Keys(keys);

	Bomber bomber = new Bomber();
	Background bg;
	Bomb[] bomb = new Bomb[MAX_BOMBS];

	public void init(){
		bg = new Background(BOTTOM_BOUND/50, RIGHT_BOUND/50);
		for (int i=0; i<bomb.length; i++){
			bomb[i] = new Bomb();
			bomb[i].posY = BOTTOM_BOUND;
			bomb[i].posX = RIGHT_BOUND;
		}
		addKeys(key);
		//new Counter();
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
			if (cont_bombs < MAX_BOMBS){
				//bomb[cont_bombs].start(GameBase.sec, RIGHT_BOUND, BOTTOM_BOUND);
    			bomber.dropBomb(bomb[cont_bombs++]);
    			//System.out.println(cont_bombs);
			}
			key.button(keys[4]);
		}
	}

	public void bombs(){

		for (int i=0; i<cont_bombs; i++){
			if (bomb[i].dropped)
				if (bomb[i].counter(GameBase.sec)){
					bomb[i].explode(RIGHT_BOUND, BOTTOM_BOUND);
					cont_bombs--;
				}
		}
	}


	public void paint(Graphics g){
		keys();
		//bombs();
		
		bomber.reset();
	    bomber.colided(bomb);
	    bg.hitWall(bomber);

		bg.draw(g);
		for (int i=0; i<cont_bombs; i++)
			bomb[i].draw(g);
		bomber.draw(g);
	}

	public static void main(String[] args) {
		GameBase run = new GameRun();
		new Start(run, "Teste");
	}

	class Counter implements ActionListener {
		public int sec;
		public Counter(){
			Timer t = new Timer(1000, this);
			t.setInitialDelay(0);
			t.setCoalesce(true);
			t.start();
		}
		
		public void actionPerformed(ActionEvent e){
			sec++;
			System.out.println(sec);
		}
	}
}