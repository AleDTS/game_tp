import java.awt.*;
import java.io.*;
import javax.imageio.*;

public class Bomb extends Interactible {
	public static Image img;
	public static int MAX_TIME = 30;
	static int height, width;
	public int ini = -1;
	public int time;
	Bomber bomber;

	public Bomb(int posX, int posY, Bomber b){

		if (img == null){
			//loadImage("bomb.png", img);
		try {
        	img = ImageIO.read(new File("bomb.png"));
        	System.out.println("hey");
      	} catch (IOException e) {
	      	System.out.println("Nao foi possivel carregar ");
	        System.exit(1);
      	}
      	
      	height = img.getHeight(this);
     	width = img.getHeight(this);
      	}
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

	public void explode(){
		System.out.println("Boom!");
		ini = -1;
		bomber.bombs--;
	}

	public boolean counter(int t){
		if (ini<0) 
			ini = t;
		time = t - ini;

		return (time==MAX_TIME);
	}
}