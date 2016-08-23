import java.awt.*;
import javax.imageio.*;
import java.io.*;
import javax.swing.*;

public class Interactible extends JPanel {
	Image img;
	int height, width;
	int posX, posY;
	boolean top, bottom, right, left;
	
	void loadImage(String name){
		try {
        	img = ImageIO.read(new File(name));
      	} catch (IOException e) {
	      	System.out.println("Nao foi possivel carregar " + name);
	        System.exit(1);
      	}

      	height = img.getHeight(this);
     	width = img.getHeight(this);
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

	public boolean colided(int x, int y, int h, int w){

		if (posX == (x+w)){
			if  ((y <= posY && posY < (y+h)) ||
				(y < (posY+height) && (posY+height) <= (y+h))){
				left = true;
			}
		}

		if ((posX + width) == x){
			if  ((y <= posY && posY < (y+h)) ||
				(y < (posY+height) && (posY+height) <= (y+h)))
				right = true;
		}

		if (posY == (y+h)){
			if  ((x <= posX && posX < (x+w)) ||
				(x < (posX+width) && (posX+width) <= (x+w)))
				top = true;
		}

		if ((posY+height) == y){
			if  ((x <= posX && posX < (x+w)) ||
				(x < (posX+width) && (posX+width) <= (x+w)))
				bottom = true;
		}

		return (top||bottom||right||left);
	}

	public boolean colided(Interactible obj){
		int x, y, w, h;
		//int i;

		//for (i=0; i<obj.length; i++){
			x = obj.posX;
			y = obj.posY;
			h = obj.height;
			w = obj.width;
		
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
			//System.out.format("%b %b %b %b\n", top, bottom, right, left);
		//}
		return (top||bottom||right||left);
	}

		public boolean inside(Interactible obj){
			boolean up, down, left, right;
			int x,y,h,w;
			up = down = left = right = false;

			x = obj.posX;
			y = obj.posY;
			h = obj.height;
			w = obj.width;

			if (posX < (x+w) && posX >= x){
				if  ((y <= posY && posY < (y+h)) &&
					(y < (posY+height) && (posY+height) <= (y+h))){
					
					left = true;
				}
			}

			if ((posX + width) > x && (posX + width) <= (x+w) ){
				if  ((y <= posY && posY < (y+h)) &&
					(y < (posY+height) && (posY+height) <= (y+h)))
					right = true;
			}

			if (posY < (y+h) && posY >= y){
				if  ((x <= posX && posX < (x+w)) &&
					(x < (posX+width) && (posX+width) <= (x+w)))
					up = true;
			}

			if ((posY+height) > y && (posY+height) <= (y+h)){
				if  ((x <= posX && posX < (x+w)) &&
					(x < (posX+width) && (posX+width) <= (x+w)))
					down = true;
			}

			return (up||down||left||right);
		}

		public boolean inside(int x, int y, int w, int h){
			boolean up, down, left, right;
			up = down = left = right = false;

			if (posX < (x+w) && posX >= x){
				if  ((y <= posY && posY < (y+h)) &&
					(y < (posY+height) && (posY+height) <= (y+h))){
					
					left = true;
				}
			}

			if ((posX + width) > x && (posX + width) <= (x+w) ){
				if  ((y <= posY && posY < (y+h)) &&
					(y < (posY+height) && (posY+height) <= (y+h)))
					right = true;
			}

			if (posY < (y+h) && posY >= y){
				if  ((x <= posX && posX < (x+w)) &&
					(x < (posX+width) && (posX+width) <= (x+w)))
					up = true;
			}

			if ((posY+height) > y && (posY+height) <= (y+h)){
				if  ((x <= posX && posX < (x+w)) &&
					(x < (posX+width) && (posX+width) <= (x+w)))
					down = true;
			}

			return (up||down||left||right);
		}
}