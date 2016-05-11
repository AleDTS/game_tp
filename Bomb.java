import java.awt.*;
import java.io.*;
import javax.imageio.*;

public class Bomb extends Interactible{
	public static Image img;
	public int tempo;

	public Bomb(){
		if (img == null){
		try {
        	img = ImageIO.read(new File("bomb.png"));
      	} catch (IOException e) {
	      	System.out.println("Nao foi possivel carregar ");
	        System.exit(1);
      	}
      	}
      	
      	height = img.getHeight(this);
     	width = img.getHeight(this);
     	//System.out.println(height+" "+width);
	}

	/*
	public void draw(Graphics g){
		g.drawImage(img, posX, posY, this);
	}
	*/

	public void drop(int posX, int posY){
		this.posY = posY;
		this.posX = posX;
		//qte++;
	}

}