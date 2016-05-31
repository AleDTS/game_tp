import java.awt.*;
import javax.imageio.*;
import java.io.*;
import java.awt.image.*;
import javax.swing.*;

public class Background extends JPanel{
	Image img, breakable;
	static int lin, col;
	int wall_width, wall_height;
	Wall[][] static_matrix, breakable_matrix;
	boolean[][] breakable_wall;

	public class Wall {
		int posX, posY;

		public Wall(int x, int y){
			posX = x;
			posY = y;
		}
	}

	public void breakableWall(int lin, int col, double prob){
		boolean notHere = false;
		int i = 0, j = 0;

		this.lin = lin;
		this.col = col;
		for (i = 0; i<lin; i++){
			for (j = 0; j<col; j++){
				if (static_matrix[i][j] == null){
					if ((i>=0 && i < 3 && j == 0) || (j>=0 && j < 4 && i == 0) )
						notHere = true;
					else
						notHere = false;
					if (Math.random() <= prob && !notHere)
						//System.out.format(" 1");
						breakable_wall[i][j] = true;
					else
						breakable_wall[i][j] = false;
						//System.out.format(" 0");
				}
					//System.out.format(" 0"); 
			}
			//System.out.format("\n");
		}
	}

	public Background(int lin, int col){
		this.lin = lin;
		this.col = col;
		wall_height = 50;
		wall_width = 50;
	    static_matrix = new Wall[lin][col];
	    breakable_matrix = new Wall[lin][col];
	    breakable_wall = new boolean[lin][col];

	    //System.out.println(lin+" "+col);

		try {
		    img = ImageIO.read(new File("background.png"));
		    breakable = ImageIO.read(new File("breakable.png"));
	    } catch (IOException e) {
		    System.exit(1);
	    }

	    for (int i=0; i<lin; i++){
			for (int j=0; j<col; j++){
				if (i%2!=0 && j%2!=0){
					static_matrix[i][j] = new Wall(j*wall_width, i*wall_height);
					//System.out.format( "(%d, %d)", j*wall_width, i*wall_height );
				}
				else 
					static_matrix[i][j] = null;
			}
			//System.out.format("\n");
		}

		breakableWall(lin,col,0.8);

		for (int i=0; i<lin; i++){
			for (int j=0; j<col; j++){
				if (breakable_wall[i][j] == true){

					breakable_matrix[i][j] = 
				new Wall(j*wall_width, i*wall_height);
				//System.out.format("\t%d ",breakable_matrix[i][j].posX);
				}
				else
					breakable_matrix[i][j] = new Wall(0,0);
			}
			//System.out.format("\n");
		}

	}

	void drawBreakable(Graphics g){
		for (int i=0; i<lin; i++)
			for (int j=0; j<col; j++)
				if (breakable_wall[i][j])
				g.drawImage 
				(breakable, 
				breakable_matrix[i][j].posX, 
				breakable_matrix[i][j].posY, this);
	}

	void hitWall(Interactible obj){
		for (int i=0; i<lin; i++){

			for (int j=0; j<col; j++){
				if (i%2!=0 && j%2!=0){
				//System.out.println(
					obj.colided(static_matrix[i][j].posX,
							static_matrix[i][j].posY,
							wall_width,
							wall_height);
				}
				if (breakable_wall[i][j])
					 obj.colided(breakable_matrix[i][j].posX,
							breakable_matrix[i][j].posY,
							wall_width,
							wall_height);
			}
		}
	}

	public Interactible breakableWall(int i, int j){
		Interactible obj = new Interactible();
		obj.posX = j*wall_width;
		obj.posY = i*wall_height;
		obj.height = wall_height;
		obj.width = wall_width;
		return obj;
	}	

	void hitBreakable(Bomb b){
		int i,j;
		for (i=0; i<lin; i++)
			for (j=0; j<col; j++)
				if (b.explode(breakableWall(i,j)))
					breakable_wall[i][j] = false;
	}

	public void draw(Graphics g){
		g.drawImage(img, 0, 0, this);
	}
}