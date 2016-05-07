import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameRun extends GameBase {
	public static int TOP_BOUND = 0;
	public static int BOTTOM_BOUND = 600;
	public static int LEFT_BOUND = 0;
	public static int RIGHT_BOUND = 600;
	Bomber bomber = new Bomber();
	Background bg = new Background();

	public void init(){
		addKeys(key);
	}

	public void paint(Graphics g){
		bg.draw(g);
		bomber.draw(g,bomber.bomb,Bomb.qte);
		bomber.draw(g);
	}
	
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
	    	bomber.dropBomb();
	    }
	    void tests(){
	    	System.out.println(bomber.posX + " "+ bomber.posY);
	    	bomber.reset();
	    	bomber.colided(bomber.bomb, Bomb.qte);
	    }
	};

	public static void main(String[] args) {
		GameBase run = new GameRun();
		new Start(run, "Teste");
	}
}