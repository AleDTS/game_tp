
public class Bomb extends Interactible{
	public static int qte = 0;
	int tempo;

	public Bomb(){
		loadImage("bomb.png");
      	height = img.getHeight(this);
     	width = img.getHeight(this);
	}

	public void drop(int posX, int posY){
		this.posY = posY;
		this.posX = posX;
		qte++;
	}

}