/**
 * Snail class, a subclass of the abstract Enemy class.
 * Implements the Enemy.addCorpse() method.
 * Represents a snail enemy that moves along the path to attack the user when it reaches the end.
 * 
 * @author Tyler C. Wilcox
 * @version 19 November 2022
 */
package game;

public class Snail extends Enemy {
	public Snail(State state, Control control) {
		super(state, control);
		
		// Snail default field values
		speed = 1.0/20.0;
		maxHealth = control.getSnailHealth();
		health = maxHealth;
		monetaryValue = 25;
		scoreValue = 100;
		attackDamage = 5;
		
		// Load the image sprite and get its parameters
		image = control.getImage("snail.png");
		width = image.getWidth();
		height = image.getHeight();
		
		// Set draw fields
		isVisible = true;
		isExpired = false;
		drawLevel = control.MAIN;
	}
	
	/**
	 * Creates a snail corpse to replace the living snail.
	 * Uses the snail's x and y coordinates to generate a new corpse sprite at the given location.
	 */
	@Override
	public void addCorpse() {
		state.addGameObject(new SnailCorpse(state, control, loc.x, loc.y));
	}
}
