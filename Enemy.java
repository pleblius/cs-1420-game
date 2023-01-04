/**
 * Abstract class representing an enemy unit that extends the GameObject class. 
 * Contains fields and accessors to get the unit's location (center of mass), height, and width.
 * Contains a method that checks if a given coordinate is within the unit's hitbox, based on its location and size.
 * Contains abstract methods for damaging and killing the enemy in question.
 * 
 * @author Tyler C. Wilcox
 * @version 11/28/2022
 */
package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public abstract class Enemy extends GameObject {
	// Coordinate fields
	protected double percentage;
	protected Point loc;
	
	// Size fields - Implemented by subclasses
	protected int width;
	protected int height;
	
	// Enemy parameters - Implemented by subclasses
	protected double speed;			// Speed it travels at
	protected int maxHealth;		// Max health
	protected int health;			// Current health
	protected int monetaryValue;	// Monetary worth when killed
	protected int scoreValue;		// Score worth when killed
	protected int attackDamage;		// Damage it inflicts
	
	// AI logic fields
	protected boolean isTracked;	// Whether or not a projectile is currently attacking this enemy

	public Enemy(State state, Control control) {
		super(state, control);
		
		loc = control.getPath().convertToCoordinates(percentage);
		
		isVisible = true;
		isExpired = false;
	}
	
	// Accessors
	
	/**
	 * Gets the x-coordinate of the center of the enemy's sprite.
	 * 
	 * @return x The x-coordinate of the enemy's center.
	 */
	public int getX() { return loc.x; }
	
	/**
	 * Gets the y-coordinate of the center of the enemy's sprite.
	 * 
	 * @return y The y-coordinate of the enemy's center.
	 */
	public int getY() { return loc.y; }
	
	/**
	 * Gets the width of the enemy's hitbox.
	 * 
	 * @return width The width of the enemy's hitbox, in pixels.
	 */
	public int getWidth() { return width; }
	
	/**
	 * Gets the height of the enemy's hitbox.
	 * 
	 * @return height The height of the enemy's hitbox, in pixels.
	 */
	public int getHeight() { return height; }
	
	/**
	 * Gets the percentage along the path that this object is.
	 * 
	 * @return a double representing the fractional percentage of the path the object has traversed.
	 */
	public double getPercentage() { return percentage; }
	
	/**
	 * Gets the enemy's speed, in percentage per second.
	 * 
	 * @return a double representing the enemy's speed.
	 */
	public double getSpeed() { return speed; }
	
	/**
	 * Gets this object's current health.
	 * 
	 * @return the object's current health.
	 */
	public int getHealth() { return health; }
	
	/**
	 * Checks if the given x and y coordinates fall within this object's hitbox, defined by the size of its image sprite.
	 * 
	 * @param x The x coordinate being checked.
	 * @param y The y coordinate being checked.
	 * @return true if both x and y are within the given bounds and false otherwise.
	 */
	public boolean isWithinHitbox(int x, int y) {
		// Check if x-coordinate is within bounds
		boolean xCheck;
		xCheck = x <= this.loc.x + this.width/2 && x >= this.loc.x - this.width/2;
		
		// Check if y-coordinate is within bounds
		boolean yCheck;
		yCheck = y <= this.loc.y + this.height/2 && y >= this.loc.y - this.height/2;
		
		return xCheck && yCheck;
	}
	
	/**
	 * Checks if the enemy is currently being tracked by any salt projectiles.
	 * 
	 * @return true if any salt projectile has this enemy as a target. False otherwise.
	 */
	public boolean isTracked() {
		for (GameObject go : state.getFrameObjects())
			if (go instanceof SaltCrystal) {
				SaltCrystal sc = (SaltCrystal) go;
				
				if (sc.getTarget() == this)
					return true;
			}
		
		// If we get here nothing has flagged as true
		return false;
	}
	
	/**
	 * Gets all of the projectile objects that are currently tracking this enemy.
	 * Will return null if no enemies are tracking this object, so either checking if there are any trackers using
	 * this.isTracked() or being prepared to handle null values is necessary.
	 * 
	 * @return a list of SaltCrystal objects with this as their target, or null if no such objects exist.
	 */
	public List<SaltCrystal> getTrackers() {
		List<SaltCrystal> trackers = new ArrayList<SaltCrystal>();
		
		for (GameObject go : state.getFrameObjects())
			if (go instanceof SaltCrystal) {
				SaltCrystal sc = (SaltCrystal) go;
				
				if (sc.getTarget() == this)
					trackers.add(sc);
			}
		
		if (trackers.isEmpty()) return null;
		else return trackers;
	}
	
	// Damage methods
	
	/**
	 * Causes damage to the given object, reducing its health by the given amount.
	 * @param damage Damage dealt to the receiving object.
	 */
	public void damage(int damage) {
		health -= damage;
	}
	
	/**
	 * Awards the user a kill for this enemy.
	 * Gives the user an increase in money and score, generates a corpse, and removes the object from the game list.
	 */
	public void kill() {
		isVisible = false;
		isExpired = true;
		
		state.creditUser(monetaryValue);
		state.increaseScore(scoreValue);
		
		addCorpse();
	}
	
	/**
	 * Abstract method to generate a corpse based on the type of enemy that died.
	 */
	abstract public void addCorpse();
	
	/**
	 * Game Object Methods
	 */
	
	/**
	 * The update override function for the enemy class.
	 * Checks if the enemy is dead--killing it if so--and otherwise has the enemy travel further along the path based on its speed.
	 */
	@Override
	public void update(double elapsedTime) {
		// Check if enemy is dead
		if (health <= 0) {
			// If so, kill the enemy and end the update
			this.kill();
			return;
		}
		
		// Update enemy travel distance and location
		percentage += speed*elapsedTime;
		loc = control.getPath().convertToCoordinates(percentage);

		// Check if enemy has reached the end of the path
		if (percentage >= 1.0) {
			// If enemy reaches end of path, have it damage the user and despawn
			state.damageUser(attackDamage);
			isExpired = true;
			isVisible = false;
		}
	}
	
	
	/**
	 * Draws the enemy at the given location, adjusting for the height and the width of the image to keep the sprite center-justified.
	 * Also draws a healthbar above the enemy that adjusts to the enemy's current health value.
	 */
	@Override
	public void draw(Graphics g) {
		// Draws the image at the given point about its center
		g.drawImage(image, loc.x - width/2, loc.y - height/2, null);
		
		// Draw the enemy's healthbar
		// Background
		g.setColor(Color.BLACK);
		g.fillRect(loc.x - width/2, loc.y - height/2 - 8, width, 8);
		
		// Health fill
		g.setColor(Color.RED);
		g.fillRect(loc.x - width/2 + 1, loc.y - height/2 - 7, ((width-2)*health)/maxHealth, 6);
		
		// Dividers
		g.setColor(Color.BLACK);
		for (int i = 1; i < maxHealth; i++) {
			g.fillRect(loc.x - width/2 + width*i/maxHealth, loc.y - height/2 - 8, 2, 8);
		}
	}
}
