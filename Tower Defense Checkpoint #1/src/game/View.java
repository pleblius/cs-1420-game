package game;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class View extends JPanel{
	private static final long serialVersionUID = 0;
	
	private Control control;
	private State state;
	
	/**
	 * View object constructor. Builds the JFrame and panel to display the game as it runs.
	 * 
	 * @param c The control object running the game
	 * @param s The state object that contains the list of game objects
	 */
	public View (Control c, State s) {
		control = c;
		state = s;
		
		JFrame f = new JFrame("Tower Defense");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setMinimumSize(new Dimension(600,600));
		this.setPreferredSize(getMinimumSize());
		this.setSize(getPreferredSize());
		
		f.setContentPane(this);
		f.pack();
		f.setVisible(true);
	}
	
	/**
	 * Paints the current list of game objects to the game field if they are alive and visible
	 */
	public void paint(Graphics g) {
        for (GameObject go : state.getFrameObjects())
            if (go.isVisible() && !go.isExpired())
                go.draw(g);
	}
}
