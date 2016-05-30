
public class Bomber extends Interactible {
	public static final int MAX_BOMBS = 7;
	int intensity = 5;
	boolean isMovingRight, isMovingLeft, isMovingUp, isMovingDown;
	boolean hitted = false;
	int bombs;

	public Bomber(){
		posX = posY = 0;
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

	public void hitted(){
		System.out.println("SHIT!");
		posX = 0;
		posY = 0;
	}

	public boolean oneDirection(){
		return (isMovingRight^isMovingLeft^isMovingUp^isMovingDown);
	}

	public Bomb dropBomb(){
		bombs++;
		return (new Bomb(posX, posY,this));
	}

	public synchronized void reset(){
		top = bottom = right = left = false;
	}
}