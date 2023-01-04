/**
 * SCargo class, a subclass of GameObject that implements the Killable interface.
 * Implements GameObject update() and draw() methods.
 * Implements Killable damage() and kill() methods.
 * Represents a van of snail enemies that moves quickly along the path to attack the user when it reaches the end.
 * 
 * @author Tyler C. Wilcox
 * @version 25 November 2022
 */
package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class SCargo extends GameObject implements Killable {
	private double percentage;	// Distance traversed
	
	// Parameter fields
	private double speed;		// Speed it travels at
	private int maxHealth;		// Max health
	private int health;			// Current health
	private int monetaryValue;	// Monetary worth when killed
	private int scoreValue;		// Score worth when killed
	private int attackDamage;	// Damage it inflicts

	public SCargo(State state, Control control) {
		super(state, control);
		
		// SCargo default field values
		percentage = 0;
		speed = .15;
		maxHealth = 8;
		health = maxHealth;
		monetaryValue = 100;
		scoreValue = 500;
		attackDamage = 25;
		
		isVisible = true;
		isExpired = false;
		drawLevel = control.MAIN; // Draw at the normal object level
	}
	
	// Killable overrides
	@Override
	public void damage(int damage) {
		health -= damage;
	}
	@Override
	public void kill() {
		isVisible = false;
		isExpired = true;
		
		state.creditUser(monetaryValue);
		state.increaseScore(scoreValue);
	}

	/**
	 * SCargo update override.
	 * Checks if the object is dead and kills it if so.
	 * Otherwise, has the object travel a certain distance down the path based on its speed.
	 */
	@Override
	public void update(double elapsedTime) {
		// Check if SCargo is dead
		if (health <= 0) {
			// If so, kill the van and end the update
			this.kill();
			return;
		}
		
		// Update SCargo travel distance
		percentage += speed*elapsedTime;
		
		// Check if SCargo has reached the end of the path
		if (percentage >= 1.0) {
			// If SCargo reaches end of path, have it damage the user
			state.damageUser(attackDamage);
			isExpired = true;
			isVisible = false;
		}
	}
	
	/**
	 * Draws the SCargo at the given location, adjusting the location to keep the image center-justified.
	 * Also draws a rectangular healthbar above the sprite, indicating the object's health.
	 */
	@Override
	public void draw(Graphics g) {
		// Get the image coordinates
		Point loc = control.getPath().convertToCoordinates(percentage);
		
		// Load the image
		BufferedImage image = control.getImage("s-cargo.png");
		
		// Draws the image at the given point about its center
		g.drawImage(image, loc.x - image.getWidth()/2, loc.y - image.getHeight()/2, null);
		
		// Draw the snail's healthbar
		g.setColor(Color.BLACK);
		g.fillRect(loc.x - image.getWidth()/2, loc.y - image.getHeight()/2 - 8, image.getWidth(), 8);
		g.setColor(Color.RED);
		g.fillRect(loc.x - image.getWidth()/2 + 1, loc.y - image.getHeight()/2 - 7, ((image.getWidth()-2)*health)/maxHealth, 6);
	}

}
