/**
 * Snail class, a subclass of GameObject that implements the Killable interface.
 * Implements GameObject update() and draw() methods.
 * Implements Killable damage() and kill() methods.
 * Represents a snail enemy that moves along the path to attack the user when it reaches the end.
 * 
 * @author Tyler C. Wilcox
 * @version 19 November 2022
 */
package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class Snail extends GameObject implements Killable{
	private double percentage;	// Distance traversed
	
	// Parameter fields
	private double speed;		// Speed it travels at
	private int maxHealth;		// Max health
	private int health;			// Current health
	private int monetaryValue;	// Monetary worth when killed
	private int scoreValue;		// Score worth when killed
	private int attackDamage;	// Damage it inflicts
	
	public Snail(State state, Control control) {
		super(state, control);
		
		// Snail default field values
		percentage = 0;
		speed = 0.002;
		maxHealth = 3;
		health = maxHealth;
		monetaryValue = 25;
		scoreValue = 100;
		attackDamage = 5;
		
		isVisible = true;
		isExpired = false;
	}
	
	/*
	 * Killable Methods
	 */
	
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
	
	/*
	 * Game Object Methods
	 */
	
	@Override
	public void update(double elapsedTime) {
		// Check if snail is dead
		if (health <= 0) {
			// If so, kill the snail and end the update
			this.kill();
			return;
		}
		
		// Update snail travel distance
		percentage += speed;
		
		// Check if snail has reached the end of the path
		if (percentage >= 1.0) {
			// If snail reaches end of path, have it damage the user and then create a new one at the beginning of the path.
			state.damageUser(attackDamage);
			isExpired = true;
			isVisible = false;
			state.addGameObject(new Snail(state, control));
		}
	}
	@Override
	public void draw(Graphics g) {
		// Get the image coordinates
		Point loc = control.getPath().convertToCoordinates(percentage);
		
		// Load the image
		BufferedImage image = control.getImage("snail.png");
		
		// Draws the image at the given point about its center
		g.drawImage(image, loc.x - image.getWidth()/2, loc.y - image.getHeight()/2, null);
		
		// Draw the snail's healthbar
		g.setColor(Color.BLACK);
		g.fillRect(loc.x - image.getWidth()/2, loc.y - image.getHeight()/2 - 8, image.getWidth(), 8);
		g.setColor(Color.RED);
		g.fillRect(loc.x - image.getWidth()/2 + 1, loc.y - image.getHeight()/2 - 7, ((image.getWidth()-2)*health)/maxHealth, 6);
	}
}
