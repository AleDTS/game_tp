
public class Bomber extends Interactible {
	public static final int MAX_BOMBS = 7;
	int intensity = 5;
	boolean isMovingRight, isMovingLeft, isMovingUp, isMovingDown;

	public Bomber(){posX = posY = 0;
		loadImage("bomber.png");
      	height = img.getHeight(this);
     	width = img.getHeight(this);
	}

	public synchronized void moveUp(int bound){
		if (posY > bound && !top){
			isMovingUp = true;
			posY -= intensity;
		}
	}
	public synchronized void moveDown(int bound){
		if (posY < (bound - height) && !bottom){
			isMovingDown = true;
			posY += intensity;
		}
	}
	public synchronized void moveRight(int bound){
		if (posX < (bound - width) && !right){
			isMovingRight = true;
			posX += intensity;
		}
	}
	public synchronized void moveLeft(int bound){
		if (posX > bound && !left){
			isMovingLeft = true;
			posX -= intensity;
		}
	}

	public boolean oneDirection(){
		return (isMovingRight^isMovingLeft^isMovingUp^isMovingDown);
	}

	public synchronized void dropBomb(Bomb bomb){
		bomb.drop((int)((posX+width/2)/width)*width, (int)((posY+height/2)/height)*height);
	}

	public synchronized void reset(){
		top = bottom = right = left = false;
	}
}