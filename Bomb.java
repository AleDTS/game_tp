import java.awt.*;
import java.io.*;
import javax.imageio.*;

public class Bomb extends Interactible {
	public static Image img, explosion;
	public static int MAX_TIME = 40;
	public int ini = -1, ini_expl = -1;
	public int time;
	public int range = 1;
	public boolean remove = false;
	Bomber bomber;

	public Bomb(int posX, int posY, Bomber b){

		if (img == null && explosion == null){
			//loadImage("bomb.png", img);
		try {
        	img = ImageIO.read(new File("bomb.png"));
        	explosion = ImageIO.read(new File("explosion.png"));
      	} catch (IOException e) {
	      	System.out.println("Nao foi possivel carregar ");
	        System.exit(1);
      	}
      	
      	}
      	
      	height = img.getHeight(this);
     	width = img.getHeight(this);
      	this.bomber = b;
      	this.posY = (int)((posY+height/2)/height)*height;
		this.posX = (int)((posX+width/2)/width)*width;
      	
	}

	public void hitBomb(Interactible obj){
		obj.colided(posX, posY,(width), (height));
	}

	public void draw(Graphics g){
		g.drawImage(img, posX, posY, this);
	}

	public void drawExplosion(Graphics g){
		/*
		//VERTICAL
		if (!(top||bottom))
		g.fillRect(posX, posY-height*range , width, (height*(1+range*2)));
		//HORIZONTAL
		if (!(right||left))
		g.fillRect(posX-width*range, posY, (width*(1+range*2)), height);
		*/
		//System.out.println("hey");

		if (!top)
			g.fillRect(posX, posY-height*range,(width), (height*(1+range)));
		if (!bottom)
			g.fillRect(posX, posY,(width), (height*(1+range)));
		if (!right)
			g.fillRect(posX, posY,(width*(1+range)), (height));
		if (!left)
			g.fillRect(posX-width*range, posY,(width*(1+range)), (height));

	}

	public boolean explode(Interactible obj){
		//System.out.println("Boom!");
		boolean hit;
		ini = -1;
		bomber.bombs--;
		//System.out.println
		
		hit = 
		(
			obj.inside
			(posX, posY-height*range,(width), (height*(1+range))) ||	// UP
			obj.inside
			(posX, posY,(width), (height*(1+range))) || 				// DOWN
			obj.inside
			(posX-width*range, posY,(width*(1+range)), (height)) || 	// LEFT
			obj.inside
			(posX, posY,(width*(1+range)), (height)) 					// RIGHT
		);
		return hit;
	}

	public boolean counter(int t){
		if (ini<0) 
			ini = t;
		time = t - ini;

		return (time==MAX_TIME);
	}

	public boolean counter(int t, int max){
		if (ini_expl<0) 
			ini_expl = t;
		time = t - ini_expl;

		return (time==max);
	}
}