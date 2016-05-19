import java.awt.*;

public class StaticWall extends Interactible {
	static int[][] matrix;
	int lin, col;

	public StaticWall(int w, int h){
		loadImage("StaticWall.png");
			col = (w/width);
			lin = (h/height);
		matrix = new int[lin][col];

		for (int i=0; i<lin; i++){
			for (int j=0; j<col; j++){
				if (i%2!=0 && j%2!=0){
					matrix[i][j] = 1;
					//System.out.format(" 1");	
				}
				else{
					matrix[i][j] = 0;
					//System.out.format(" 0");	
				}
			}
			//System.out.format("\n");
		}
	}

	public void draw(Graphics g){
		for (int i=0; i<lin; i++)
			for (int j=0; j<col; j++)
				g.drawImage(img, i*width*matrix[i][j], j*height*matrix[i][j], this);
	}
}