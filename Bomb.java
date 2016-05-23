import java.awt.*;
import java.io.*;
import javax.imageio.*;

public class Bomb extends Interactible{
	public static Image img;
	public static int MAX_TIME = 5;
	public boolean dropped = false;
	public int ini = -1;
	public int time;

	public Bomb(){

		if (img == null){
			//loadImage("bomb.png", img);
		try {
        	img = ImageIO.read(new File("bomb.png"));
      	} catch (IOException e) {
	      	System.out.println("Nao foi possivel carregar ");
	        System.exit(1);
      	}
      	
      	}
      	
      	height = img.getHeight(this);
     	width = img.getHeight(this);
	}

	public void draw(Graphics g){
		g.drawImage(img, posX, posY, this);
	}
	

	public void drop(int posX, int posY){
		this.posY = posY;
		this.posX = posX;
		dropped = true;
	}

	public void explode(int x, int y){
		System.out.println("Boom!");
		posX = x;
		posY = y;
		ini = -1;
	}

	public boolean counter(int t){
		if (ini<0) 
			ini = t;
		time = t - ini;

		return (time==MAX_TIME);
	}
}