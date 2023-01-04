/**
 * SCargo class, a subclass of the Enemy abstract class.
 * Represents a van of snail enemies that moves quickly along the path to attack the user when it reaches the end.
 * Implements the Enemy.addCorpse() method.
 * 
 * @author Tyler C. Wilcox
 * @version 25 November 2022
 */
package game;

public class SCargo extends Enemy {
	public SCargo(State state, Control control) {
		super(state, control);
		
		// SCargo default field values
		speed = 1.0/12.0;
		maxHealth = control.getVanHealth();
		health = maxHealth;
		monetaryValue = 50;
		scoreValue = 500;
		attackDamage = 25;

		// Load the image sprite and get its parameters
		image = control.getImage("s-cargo.png");
		width = image.getWidth();
		height = image.getHeight();
		
		// Set draw fields
		isVisible = true;
		isExpired = false;
		drawLevel = control.VEHICLE;
	}
	
	/**
	 * Creates a snail corpse to replace the living snail.
	 * Uses the snail's x and y coordinates to generate a new corpse sprite at the given location.
	 */
	@Override
	public void addCorpse() {
		state.addGameObject(new VanCorpse(state, control, loc.x, loc.y));
	}
}
