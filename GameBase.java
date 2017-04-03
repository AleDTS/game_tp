import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.Timer;

public class GameBase extends Canvas {
	//DEFAULT
	public static int FPS = 60;
	public static int width;
	public static int height;
	public static boolean finish = false;

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

	public void paint(Graphics g) {}

	public void close() {}

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

	    public void keyPressed(KeyEvent e){
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

		public Boolean button(int code){
			for (Map.Entry<Integer, Boolean> k : key.entrySet())
	    		if (k.getKey() == code)
        			if (k.getValue() == true){
        				boolean b = true;
        				k.setValue(false);
		    			return true;
        			}
	    	return false;
		}

		public void add(){
			addKeyListener(this);
		}
	}
	
	class Frame implements ActionListener {
		public int frame;
		Timer t;
		public Frame(int fps){
			t = new Timer(1000/fps, this);
			t.setInitialDelay(0);
			t.setCoalesce(true);
			t.start();
		}
		
		public void actionPerformed(ActionEvent e){
			if (finish)
				t.stop();
			else
				repaint();
			frame++;
			if (frame == (FPS+1))
				frame = 0;
			//System.out.println(frame);
		}


	}

}

class Start extends JFrame {
	GameBase canvas;

	public Start(GameBase c,int player, int width, int height) {
		super("Player "+player);
		canvas = c;
		add(canvas);
		canvas.setSize(width, height);
		pack();
		setVisible(true);
		setResizable(false);
      	setFocusable(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                canvas.close();
                System.exit(0);
            }
        } );
	}
}