import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

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
	//StaticWall walls = new StaticWall(width, height);

	public void init(){
		bg = new Background(BOTTOM_BOUND/50, RIGHT_BOUND/50);
		for (int i=0; i<bomb.length; i++)
			bomb[i] = new Bomb();
		addKeys(key);
	}

	public void keys(){
		boolean up, down, right, left, space;

		right = key.isPressed(keys[0]);
		left = key.isPressed(keys[1]);
		up = key.isPressed(keys[2]);
		down = key.isPressed(keys[3]);
		space = key.isPressed(keys[4]);
		
		if(right && !bomber.oneDirection())
			bomber.moveRight(RIGHT_BOUND);
			else bomber.isMovingRight = false;
		if(left && !bomber.oneDirection())
			bomber.moveLeft(LEFT_BOUND);
			else bomber.isMovingLeft = false;
		if(up && !bomber.oneDirection())
			bomber.moveUp(TOP_BOUND);
			else bomber.isMovingUp = false;
		if(down && !bomber.oneDirection())
			bomber.moveDown(BOTTOM_BOUND);
			else bomber.isMovingDown = false;

		if(space){
			if (cont_bombs < MAX_BOMBS )
    			bomber.dropBomb(bomb[cont_bombs++]);
			key.button(keys[4]);
		}
	}

	public void paint(Graphics g){
		keys();
		
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
}