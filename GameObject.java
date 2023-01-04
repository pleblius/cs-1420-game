/**
 * Abstract super-class for all game objects in the Tower Defense game.
 * Contains fields for the objects' visibility and expiration status.
 * Also contains fields for the control and state objects.
 * Contains an update and draw method, as well as accessors for the fields and a superconstructor.
 * 
 * @author Tyler C. Wilcox
 * @version 19 November 2022
 */
package game;

import java.awt.Graphics;


abstract public class GameObject implements Comparable<GameObject>{
    protected boolean isVisible; 
    protected boolean isExpired;
    protected Control control;
    protected State state;
    public int drawLevel;
    
    /** 
     * Abstract superconstructor
     */
    public GameObject(State state, Control control) {
    	this.control = control;
    	this.state = state;
    }
    
    /**
     * Accessor method
     * 
     * @return Returns the visibility of the game object.
     */
    public boolean isVisible() {
    	return isVisible;
    }
    
    /**
     * Accessor method
     * 
     * @return Returns whether the object is dead or not.
     */
    public boolean isExpired() {
    	return isExpired;
    }
    
    /**
     * Updates the object based on the elapsed game time.
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
