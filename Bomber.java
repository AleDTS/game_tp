
public class Bomber extends Interactible {
	public static final int MAX_BOMBS = 5;
	int intensity = 10;
	int cont_bombs;

	public Bomber(){
		cont_bombs = posX = posY = 0;
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

	public boolean dropBomb(Bomb bomb){
		if (cont_bombs < MAX_BOMBS){
			//bomb[cont_bombs] = new Bomb();
			bomb.drop(this.posX, this.posY);
			//cont_bombs++;
			//System.out.println(bomb.posX+" "+
	    	//					bomb.posY);
			return true;
		}
		else
			return false;
		//for(int i = 0; i < Bomb.qte && Bomb.qte != 0 ; i++)
		//System.out.println(i+"-"+bomb[i].posX+","+bomb[i].posY);
	}

	public void reset(){
		top = bottom = right = left = false;
	}
}