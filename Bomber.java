
public class Bomber extends Interactible {
	int player, lin, col, posWX, posWY;
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
			case 1: 
				posX = posY = 0; 
				posWX = (col-1)*50; posWY = ((int)lin/2)*50;
				// posWX = (height-50)/2;posWY = width-50;
				break;
			case 2: 
				posY = 0; posX = (col-1)*50; 
				posWX = ((int)col/2)*50; posWY = (lin-1)*50;
				// posWX = height-50; posWY = (width-50)/2;
				break;
			case 3: 
				posY = (lin-1)*50; 	posX = 0; 
				posWX = 0; posWY = ((int)lin/2)*50;
				// posWX = (height-50)/2; posWY = 0;
				break;
			case 4: 
				posY = (lin-1)*50; 	posX = (col-1)*50; 
				posWX = ((int)col/2+1)*50; posWY = 0;
				// posWX = 0; posWY = (width-50)/2;
				break;
		}
		// System.out.println(posWX +" "+ posWY);
		
		loadImage("pictures/bomber"+player+".png");
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

	public void setPosX(int x){
		posX = x;
	}

	public void setPosY(int y){
		posY = y;
	}

	public boolean win(){
		return (posX == posWX && posY == posWY);
	}

	public void hitted(){
		// System.out.println("Player "+player+" dead!");

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