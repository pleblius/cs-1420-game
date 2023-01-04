/**
 * View class for the Tower Defense game.
 * Extends JPanel and operates the game's visuals.
 * Creates a JPanel and menu and then constantly draws visible game objects to the frame.
 * 
 * @author Tyler C. Wilcox
 * @version 19 November 2022
 */
package game;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class View extends JPanel{
	private static final long serialVersionUID = 0;
	
	private Control control;
	private State state;
	
	private int gameWidth = 600;
	private int gameHeight = 600;
	private int uiWidth = 200;
	
	/**
	 * View object constructor. Builds the JFrame and panel to display the game as it runs.
	 * 
	 * @param c The control object running the game
	 * @param s The state object that contains the list of game objects
	 */
	public View (State state, Control control) {
		this.state = state;
		this.control = control;
		
		JFrame f = new JFrame("Tower Defense");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setMinimumSize(new Dimension(gameWidth + uiWidth,gameHeight));
		this.setPreferredSize(getMinimumSize());
		this.setSize(getPreferredSize());
		
		f.setContentPane(this);
		f.pack();
		f.setVisible(true);
	}
	
	/**
	 * Paints the current list of game objects to the game field if they are alive and visible.
	 * First sorts the array according to draw-level to ensure everything is drawn at the appropriate time.
	 */
	public void paint(Graphics g) {
		// Sort the game object list
		Collections.sort(state.getFrameObjects());
		
		// Draw the visible game objects
        for (GameObject go : state.getFrameObjects())
            if (go.isVisible() && !go.isExpired())
                go.draw(g);
	}
}
