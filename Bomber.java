
public class Bomber extends Interactible {
	public static final int MAX_BOMBS = 5;
	int intensity = 10;

	public Bomber(){posX = posY = 0;
		loadImage("bomber.png");
      	height = img.getHeight(this);
     	width = img.getHeight(this);
	}

	public void moveUp(int bound){
		if (posY > bound && !top)
		posY -= intensity;
	}
	public void moveDown(int bound){
		if (posY < (bound - height) && !bottom)
		posY += intensity;
	}
	public void moveRight(int bound){
		if (posX < (bound - width) && !right)
		posX += intensity;
	}
	public void moveLeft(int bound){
		if (posX > bound && !left)
		posX -= intensity;
	}

	public void dropBomb(Bomb bomb){
			bomb.drop(this.posX, this.posY);
	}

	public void reset(){
		top = bottom = right = left = false;
	}
}