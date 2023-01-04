/**
 * Abstract class representing a dead enemy's body that extends the GameObject class.
 * Contains fields representing where and for how long the corpse should be drawn on the screen.
 * Implements the GameObject update() method, checking if the corpse has exceeded its lifespan and expiring the sprite.
 */
package game;

import java.awt.Point;

public abstract class Corpse extends GameObject {
	// Corpse fields - Implemented by subclasses
	protected int lifeSpan;				// How long should the corpse last
	protected double generationTime;	// When was the corpse generated (seconds)
	
	// Coordinate fields
	protected Point loc;

	/**
	 * Instantiates a new corpse object at the location that the instantiating enemy died.
	 * Sets the draw level to the ground level and flags the current time for tracking.
	 * 
	 * @param state The state object passed to all GameObjects.
	 * @param control The control object controlling game operations.
	 * @param x The x location of the dying enemy.
	 * @param y The y location of the dying enemy.
	 */
	public Corpse(State state, Control control, int x, int y) {
		super(state, control);

		loc = new Point(x,y);
		
		drawLevel = control.GROUND; // All corpses should be drawn just above the background
		
		// Flag construction time
		generationTime = System.currentTimeMillis()/1000d;
		
		isVisible = true;
		isExpired = false;
	}

	/**
	 * Checks if the time the corpse has been on the screen exceeds its timer.
	 * If so, it sets the isVisible flag to false and the isExpired flag to true.
	 */
	@Override
	public void update(double elapsedTime) {
		if (System.currentTimeMillis()/1000d - generationTime > lifeSpan) {
			isVisible = false;
			isExpired = true;
		}
	}
}
