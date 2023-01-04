package game;

import java.awt.Graphics;


abstract public class GameObject {
    protected boolean isVisible; 
    protected boolean isExpired;

    /**
     * Accessor method
     * 
     * @return Returns the visibility of the game object
     */
    public boolean isVisible() {
    	return isVisible;
    }
    
    /**
     * Accessor method
     * 
     * @return Returns whether the object is dead or not
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
     * Draws the object
     * 
     * @param g Graphics object used to draw the object
     */
    abstract public void draw (Graphics g);
}
