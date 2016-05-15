
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
		for (int i=0; i<base.keys; i++){
			addKeyListener(base.key[i]);
			System.out.println(base.key[i].keyCode);
		}
	}
}

public class GameBase extends Canvas{
	final int FPS = 30;
	static int keys = 0;
	int width = 600, height = 600;
	Keys[] key = new Keys[5];

	Image offscreen = null;
	Graphics offgraphics = null;

	public GameBase(){
		setPreferredSize(new Dimension(width,height));
		new Frame(FPS);
	}

	public void paint(Graphics g) {}
	public void init() {}

	public void addKeys(Keys key){
		this.key[keys++] = key;
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

	class Keys extends KeyAdapter{
		int keyCode ;

		public Keys(int code){
			keyCode = code;
			//System.out.println(keyCode);
		}

	    public synchronized void keyPressed(KeyEvent e){	 
	    	//System.out.println(e.KEY_PRESSED);
	    	if (e.getKeyCode() == keyCode){
	    		action();
	    	}
		}

	    void action(){}
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