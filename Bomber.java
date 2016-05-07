
public class Bomber extends Interactible {
	final int BOMB_MAX = 5;
	int intensity = 10;
	int cont_bombs;
	Bomb[] bomb = new Bomb[BOMB_MAX];

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

	public void dropBomb(){
		if (cont_bombs < BOMB_MAX){
			bomb[cont_bombs] = new Bomb();
			bomb[cont_bombs].drop(posX, posY);
			cont_bombs++;
		}
		//for(int i = 0; i < Bomb.qte && Bomb.qte != 0 ; i++)
		//System.out.println(i+"-"+bomb[i].posX+","+bomb[i].posY);
	}

	public void reset(){
		top = bottom = right = left = false;
	}
}