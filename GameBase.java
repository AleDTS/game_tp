import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.Timer;

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
	//DEFINE SIZE AND FPS
	public static int FPS = 60;
	public static int width;
	public static int height;

	Keys key = null;
	Image offscreen = null;
	Graphics offgraphics = null;

	public GameBase(){
		
		new Frame(FPS);
	}

	public void setSize(int w, int h){
		setPreferredSize(new Dimension(w,h));
		width = w;
		height = h;
	}

	public int height(){
		return getHeight();
	}

	public int width(){
		return getWidth();
	}

	public void paint(Graphics g) {}
	public void init() {}

	public void addKeys(Keys key){
		this.key = key;
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
		Map<Integer, Boolean> key = new HashMap<Integer,Boolean>();
    	Iterator<Map.Entry<Integer, Boolean>> i;
    	Map.Entry<Integer, Boolean> entry;

		public Keys(int[] code){
			for (int i=0; i<code.length; i++)
				key.put(code[i], false);
		}

	    public void  keyPressed(KeyEvent e){
	    	i = key.entrySet().iterator();
        	while(i.hasNext()){
            	entry = i.next();
            	if(entry.getKey() == e.getKeyCode())
		        	entry.setValue(true);
	        }
		}

		public void keyReleased(KeyEvent e){	 
			i = key.entrySet().iterator();
        	while(i.hasNext()){
            	entry = i.next();
            	if(entry.getKey() == e.getKeyCode())
		        	entry.setValue(false);
	        }
		}

		public Boolean isPressed(int code){
			for (Map.Entry<Integer, Boolean> k : key.entrySet())
	    		if (k.getKey() == code)
        			if (k.getValue() == true)
		    			return k.getValue();
	    	return false;
		}

		public void button(int code){
			i = key.entrySet().iterator();
        	while(i.hasNext()){
            	entry = i.next();
            	if(entry.getKey() == code)
		        	entry.setValue(false);
	        }
		}
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
			frame++;
			if (frame == (FPS+1))
				frame = 0;
		}
	}
}