/**
 * Abstract super-class for all game objects in the Tower Defense game.
 * Contains fields for the objects' visibility and expiration status.
 * Also contains fields for the control and state objects.
 * Contains an update and draw method, as well as accessors for the fields and a superconstructor.
 * Implements the Comparable interface to allow GameObjects to be drawn in the correct order.
 * 
 * @author Tyler C. Wilcox
 * @version 19 November 2022
 */
package game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;


abstract public class GameObject implements Comparable<GameObject>{
	
    protected boolean isVisible; 
    protected boolean isExpired;
    protected Control control;
    protected State state;
    public int drawLevel;
    BufferedImage image;
    
    /** 
     * Abstract superconstructor
     */
    public GameObject(State state, Control control) {
    	this.control = control;
    	this.state = state;
    }
    
    /**
     * Gets the visibility status of the game object.
     * 
     * @return true if the object is visible.
     */
    public boolean isVisible() { return isVisible; }
    
    /**
     * Gets the expiration status of the game object.
     * 
     * @return true if the object should be removed from the game list.
     */
    public boolean isExpired() { return isExpired; }
    
    /**
     * What the object does each time the game calls a frame update, based on the total time elapsed between frames.
     * 
     * @param elapsedTime Total time elapsed in the game.
     */
    abstract public void update (double elapsedTime);
    
    /**
     * Draws the object to the JPanel using its relevant sprite(s).
     * 
     * @param g Graphics object used to draw the object
     */
    abstract public void draw (Graphics g);
    
    /**
     * Compares two objects based on their draw level.
     */
    @Override
    public int compareTo(GameObject go) {
    	return Integer.valueOf(drawLevel).compareTo(Integer.valueOf(go.drawLevel));
    }
}
