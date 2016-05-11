
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.*;
import java.awt.image.*;

class Start extends JFrame {

	public Start(GameBase base, String name){
		super(name);
		add(base);
		base.init();
		pack();
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addKeyListener(base.key);
	}
}

public class GameBase extends Canvas{
	final int FPS = 30;
	int width = 600, height = 600;
	Keys key = null;

	Image offscreen = null;
	Graphics offgraphics = null;

	public GameBase(){
		setPreferredSize(new Dimension(width,height));
		new Frame(FPS);
	}

	public void paint(Graphics g) {}
	public void init() {
		
	}

	public void addKeys(Keys key){
		this.key = key;
		addKeyListener(key);
	}

	public void update(Graphics g) {
		if (height != getHeight() || width != getWidth() || offscreen == null) {
			height = getHeight();
			width = getWidth();
			offscreen = createImage(width, height);
			if (offgraphics != null) {
				offgraphics.dispose();
			}
			offgraphics = offscreen.getGraphics();
		}
		super.update(offgraphics);
		g.drawImage(offscreen, 0, 0, null);
	}

	class Keys extends KeyAdapter {
		int code;
	    public void keyPressed(KeyEvent e){	    	

	    switch (e.getKeyCode()){
	        case KeyEvent.VK_LEFT:
				left();
				break;
	        case KeyEvent.VK_RIGHT:
         		right();
				break;
	        case KeyEvent.VK_UP:
		        up();
				break;
	        case KeyEvent.VK_DOWN:
       			down();
				break;
      		case KeyEvent.VK_SPACE:
       			action();
				break;
      		}
      		tests();
		}

	    void up(){}
	    void down(){}
	    void right(){}
	    void left(){}
	    void action(){}
	    void tests(){}
	}
	
	class Frame implements ActionListener {
		public int frame;
		public Frame(int fps){
			Timer t = new Timer(1000/fps, this);
			t.setInitialDelay(0);
			t.setCoalesce(true);
			t.start();
		}
		
		public void actionPerformed(ActionEvent e){
			repaint();
			//System.out.println(frame);
			frame++;
			if (frame == (FPS+1)) frame = 0;
		}
	}

	public static void main(String[] args) {
		new GameBase();
	}
}