
public class Char extends Interactible {
	final int BOMB_MAX = 5;
	int intensity = 10;
	int cont_bombs;
	Bomb[] bomb = new Bomb[BOMB_MAX];
	boolean top, bottom, right, left;

	public Char(){
		cont_bombs = posX = posY = 0;
		loadImage("bomber.png");
      	height = img.getHeight(this);
     	width = img.getHeight(this);
	}

	public void moveUp(){
		if (posY > GameBase.TOP_BOUND && !top)
		posY -= intensity;
	}
	public void moveDown(){
		if (posY < (GameBase.BOTTOM_BOUND - height) && !bottom)
		posY += intensity;
	}
	public void moveRight(){
		if (posX < (GameBase.RIGHT_BOUND - width) && !right)
		posX += intensity;
	}
	public void moveLeft(){
		if (posX > GameBase.LEFT_BOUND && !left)
		posX -= intensity;
	}

	public void dropBomb(){
		if (cont_bombs < BOMB_MAX){
			bomb[cont_bombs] = new Bomb();
			bomb[cont_bombs].drop(posX, posY);
			cont_bombs++;
		}
		//for(int i = 0; i < Bomb.qte && Bomb.qte != 0 ; i++)
		//System.out.println(i+"-"+bomb[i].posX+","+bomb[i].posY);
	}
	/*public boolean colided(int x, int y, int w, int h){
		/*int x = obj.posX;
		int y = obj.posY;
		int h = obj.height;
		int w = obj.width;
		
		top = bottom = right = left = false;		

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

		return (top||bottom||right||left);
	}*/
}