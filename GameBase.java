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
	final int FPS = 60;
	int width = 850, height = 650;
	Keys key = null;

	Image offscreen = null;
	Graphics offgraphics = null;

	public GameBase(){
		setPreferredSize(new Dimension(width,height));
		new Frame(FPS);
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
            	if(entry.getKey() == e.getKeyCode()){
		        	entry.setValue(true);
            		//key.put(e.getKeyCode(), true);
            	}
	        }

		}

		public void keyReleased(KeyEvent e){	 
			i = key.entrySet().iterator();
        	while(i.hasNext()){
            	entry = i.next();
            	if(entry.getKey() == e.getKeyCode()){
		        	entry.setValue(false);
            		//key.put(e.getKeyCode(), false);
            	}
	        }
		}

		public Boolean isPressed(int code){
			for (Map.Entry<Integer, Boolean> k : key.entrySet()){
	    		if (k.getKey() == code){
        				//System.out.println(k.getKey() + " " + k.getValue());
        			if (k.getValue() == true){
		    			return k.getValue();
        			}
	    		}
			}
	    	return false;
		}

		public void button(int code){
			i = key.entrySet().iterator();
        	while(i.hasNext()){
            	entry = i.next();
            	if(entry.getKey() == code){
		        	entry.setValue(false);
            		//key.put(e.getKeyCode(), true);
            	}
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
			//System.out.println(frame);
			frame++;
			if (frame == (FPS+1)) frame = 0;
		}
	}

	public static void main(String[] args) {
		new GameBase();
	}
}