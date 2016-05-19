import java.awt.*;
import javax.imageio.*;
import java.io.*;
import java.awt.image.*;
import javax.swing.*;

public class Background extends JPanel{
	Image img;
	int lin, col;
	int wall_width, wall_height;
	StaticWall[][] matrix;

	public class StaticWall {
		int posX, posY;

		public StaticWall(int x, int y){
			posX = x;
			posY = y;
		}
	}

	public Background(int lin, int col){
		this.lin = lin;
		this.col = col;
		wall_height = 50;
		wall_width = 50;
	    matrix = new StaticWall[lin][col];

	    System.out.println(lin+" "+col);

		try {
		    img = ImageIO.read(new File("background.png"));
	    } catch (IOException e) {
		    System.exit(1);
	    }

	    for (int i=0; i<lin; i++){
			for (int j=0; j<col; j++){
				if (i%2!=0 && j%2!=0){
					matrix[i][j] = new StaticWall(j*wall_width, i*wall_height);
					System.out.format( "(%d, %d)", j*wall_width, i*wall_height );
				}
			}
			System.out.format("\n");
		}	   
	}

	void hitWall(Interactible obj){
		for (int i=0; i<lin; i++)
			for (int j=0; j<col; j++)
				if (i%2!=0 && j%2!=0)
				//System.out.println(
					obj.colided(matrix[i][j].posX,
							matrix[i][j].posY,
							wall_width,
							wall_height);
	}

	public void draw(Graphics g){
		g.drawImage(img, 0, 0, this);
	}
}