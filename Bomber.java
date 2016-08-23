
public class Bomber extends Interactible {
	int player, lin, col;
	int max = 3;
	static int intensity = 5;
	boolean isMovingRight, isMovingLeft, isMovingUp, isMovingDown;
	boolean hitted = false;
	int bombs;

	public Bomber(int player, int width, int height){
		this.player = player;
		lin = height/50;
		col = width/50;
		//System.out.println(lin+" "+col);
		switch (player){
			case 1: posX = posY = 0; break;
			case 2: posY = 0; 			posX = (col-1)*50; break;
			case 3: posY = (lin-1)*50; 	posX = 0; break;
			case 4: posY = (lin-1)*50; 	posX = (col-1)*50; break;
		}
		
		loadImage("bomber"+player+".png");
	    height = img.getHeight(this);
	    width = img.getHeight(this);
	    //System.out.println(player +" "+ posX +" "+ posY);
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
		System.out.println("Player "+player+" dead!");

		switch (player){
			case 1: posX = posY = 0; break;
			case 2: posY = 0; 			posX = (col-1)*50; break;
			case 3: posY = (lin-1)*50; 	posX = 0; break;
			case 4: posY = (lin-1)*50; 	posX = (col-1)*50; break;
		}
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