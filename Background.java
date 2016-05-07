import java.awt.*;
import javax.imageio.*;
import java.io.*;
import java.awt.image.*;
import javax.swing.*;

public class Background extends JPanel{
	Image img;
	int width, height;

	public Background(){
		//TOP_BOUND = 0;
		//BOTTOM_BOUND = GameBase.HEIGHT;
		//LEFT_BOUND = 0;
		//RIGHT_BOUND = GameBase.WIDTH;
		try {
        img = ImageIO.read(new File("background.png"));
      } catch (IOException e) {
        System.exit(1);
      }
	}

	public void draw(Graphics g){
		g.drawImage(img, 0, 0, this);
	}
}