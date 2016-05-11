import java.awt.*;
import javax.imageio.*;
import java.io.*;
import java.awt.image.*;
import javax.swing.*;

public class Interactible extends JPanel {
	Image img;
	int height, width;
	int posX = 0, posY = 0;
	boolean top, bottom, right, left;
	
	void loadImage(String name){
		try {
        	img = ImageIO.read(new File(name));
      	} catch (IOException e) {
	      	System.out.println("Nao foi possivel carregar " + name);
	        System.exit(1);
      	}
	}

	void loadImage(String name, Image img){
		try {
        	img = ImageIO.read(new File(name));
      	} catch (IOException e) {
	      	System.out.println("Nao foi possivel carregar " + name);
	        System.exit(1);
      	}
	}

	public void draw(Graphics g){
		g.drawImage(img, posX, posY, this);
	}

	public void draw(Graphics g, Image i){
		g.drawImage(i, posX, posY, this);
	}

	public void colided(Interactible obj){
		int x = obj.posX;
		int y = obj.posY;
		int h = obj.height;
		int w = obj.width;
		
		//top = bottom = right = left = false;

		if (posX == (x+w)){
			if  ((y <= posY && posY < (y+h)) ||
				(y < (posY+height) && (posY+height) <= (y+h))){
				left = true;
			}
		}

		else if ((posX + width) == x){
			if  ((y <= posY && posY < (y+h)) ||
				(y < (posY+height) && (posY+height) <= (y+h)))
				right = true;
		}

		else if (posY == (y+h)){
			if  ((x <= posX && posX < (x+w)) ||
				(x < (posX+width) && (posX+width) <= (x+w)))
				top = true;
		}

		else if ((posY+h) == y){
			if  ((x <= posX && posX < (x+w)) ||
				(x < (posX+width) && (posX+width) <= (x+w)))
				bottom = true;
		}

		//return (obj.colided = (top||bottom||right||left));
	}

	public void colided(Interactible[] obj){
		int x, y, w, h;
		int i;
		
		//top = bottom = right = left = false;

		for (i=0; i<obj.length; i++){
			x = obj[i].posX;
			y = obj[i].posY;
			h = obj[i].height;
			w = obj[i].width;
		
			if (posX == (x+w)){
				if  ((y <= posY && posY < (y+h)) ||
					(y < (posY+height) && (posY+height) <= (y+h))){
					left = true;
				}
			}

			else if ((posX + width) == x){
				if  ((y <= posY && posY < (y+h)) ||
					(y < (posY+height) && (posY+height) <= (y+h)))
					right = true;
			}

			else if (posY == (y+h)){
				if  ((x <= posX && posX < (x+w)) ||
					(x < (posX+width) && (posX+width) <= (x+w)))
					top = true;
			}

			else if ((posY+h) == y){
				if  ((x <= posX && posX < (x+w)) ||
					(x < (posX+width) && (posX+width) <= (x+w)))
					bottom = true;
			}
			//obj[i].colided = (top||bottom||right||left)
		}
	}

}