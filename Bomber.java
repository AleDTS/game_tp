
public class Bomber extends Interactible {
	public static final int MAX_BOMBS = 5;
	int intensity = 10;

	public Bomber(){posX = posY = 0;
		loadImage("bomber.png");
      	height = img.getHeight(this);
     	width = img.getHeight(this);
	}

	public synchronized void moveUp(int bound){
		if (posY > bound && !top)
		posY -= intensity;
	}
	public synchronized void moveDown(int bound){
		if (posY < (bound - height) && !bottom)
		posY += intensity;
	}
	public synchronized void moveRight(int bound){
		if (posX < (bound - width) && !right)
		posX += intensity;
	}
	public synchronized void moveLeft(int bound){
		if (posX > bound && !left)
		posX -= intensity;
	}

	public synchronized void dropBomb(Bomb bomb){
			bomb.drop(this.posX, this.posY);
	}

	public synchronized void reset(){
		top = bottom = right = left = false;
	}
}