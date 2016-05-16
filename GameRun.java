import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class GameRun extends GameBase {
	public static int MAX_BOMBS = 5;
	public static int TOP_BOUND = 0;
	public static int BOTTOM_BOUND = 600;
	public static int LEFT_BOUND = 0;
	public static int RIGHT_BOUND = 600;
	int cont_bombs = 0;
	KeyEvent k;
	int[] keys = {k.VK_RIGHT, k.VK_LEFT, k.VK_UP, k.VK_DOWN, k.VK_SPACE};
	Keys key = new Keys(keys);

	Bomber bomber = new Bomber();
	Background bg = new Background();
	Bomb[] bomb = new Bomb[MAX_BOMBS];

	public void init(){
		for (int i=0; i<bomb.length; i++)
			bomb[i] = new Bomb();
		addKeys(new Keys(keys));
	}

	public void paint(Graphics g){


		if(key.isPressed(keys[0]))
			bomber.moveRight(RIGHT_BOUND);
		if(key.isPressed(keys[1]))
			bomber.moveLeft(LEFT_BOUND);
		if(key.isPressed(keys[2]))
			bomber.moveUp(TOP_BOUND);
		if(key.isPressed(keys[3]))
			bomber.moveDown(BOTTOM_BOUND);
		if(key.isPressed(keys[4]))
			if (cont_bombs < MAX_BOMBS)
    			bomber.dropBomb(bomb[cont_bombs++]);
		
		bomber.reset();
	    bomber.colided(bomb);

		bg.draw(g);
		for (int i=0; i<cont_bombs; i++)
			bomb[i].draw(g);
		bomber.draw(g);
	}

	/*
	Keys key = new Keys(){
		void up(){
			bomber.moveUp(TOP_BOUND);
		}
	    void down(){
	    	bomber.moveDown(BOTTOM_BOUND);
	    }
	    void right(){
	    	bomber.moveRight(RIGHT_BOUND);
	    }
	    void left(){
	    	bomber.moveLeft(LEFT_BOUND);
	    }
	    void action(){
	    	if (cont_bombs < MAX_BOMBS)
	    		bomber.dropBomb(bomb[cont_bombs++]);
	    }
	    void tests(){
	    	//System.out.println(bomber.posX + " "+ bomber.posY);
	    	bomber.reset();
	    	bomber.colided(bomb);
	    }
	};
	*/

	public static void main(String[] args) {
		GameBase run = new GameRun();
		new Start(run, "Teste");
	}
}