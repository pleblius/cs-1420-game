/**
 * SaltLauncher class, a subclass of GameObject that implements the Purchasable interface.
 * Implements GameObject update() and draw() methods.
 * Implements Purchaseable canAfford() and chargeUser() methods.
 * Represents a salt-launching defense tower to attack the enemy snail army as it tries to attack the user.
 * 
 * @author Tyler C. Wilcox
 * @version 19 November 2022
 */
package game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class SaltLauncher extends GameObject implements Purchasable,
														Clickable
														{
	private boolean isMoving; // Is tower selected or placed?
	
	// Drawing fields
	private int x;
	private int y;
	private int width;
	private int height;
	
	// Parameter fields
	private int cost;			// Monetary cost
	
	// Combat fields
	private int attackDamage;	// Damage it inflicts
	private double attackTime;	// How many seconds between attacks
	private double timeSinceLastAttack;	// How long since the tower last attacked
	private boolean canAttack;	// If the tower has an attack queued or not
	
	public SaltLauncher(State state, Control control, boolean moving) {
		super(state, control);
		
		isMoving = moving;
		
		// Default salt tower values
		cost = 100;
		attackDamage = 1;
		attackTime = 1f;
		
		// Check if the user can actually afford to build the tower
		if (canAfford()) {
			isVisible = true;
			isExpired = false;
			state.chargeUser(cost);
		}
		else {
			isVisible = false;
			isExpired = true;
		}
		
		x = control.getX();
		y = control.getY();
	}
	
	/**
	 * Checks if the tower is moving with the mouse or is locked in place.
	 * @return Boolean flag for whether tower is moving or not.
	 */
	public boolean isMoving() { return isMoving; }
	/**
	 * Returns the cost of a salt-launcher tower.
	 * @return tower's cost.
	 */
	public int getCost() { return cost; }
	
	/*
	 * Game Object methods
	 */
	
	@Override
	public void update(double elapsedTime) {
		// If in moving state, have it follow the mouse cursor
		if (isMoving) {
			x = control.getX();
			y = control.getY();
		}
		
		if (timeSinceLastAttack > attackTime)
			canAttack = true;
	}

	@Override
	public void draw(Graphics g) {
		BufferedImage image = control.getImage("salt.png");
		width = image.getWidth();
		height = image.getHeight();
		
		g.drawImage(image, x - width/2, y - height, null);
	}

	/**
	 * Gets the tower's height.
	 * @return tower height.
	 */
	public int getHeight() { return height; }
	/**
	 * Gets the tower's width.
	 * @return tower width.
	 */
	public int getWidth() { return width; }
	
	/*
	 * Purchaseable Methods
	 */
	
	@Override
	public boolean canAfford() {
		return state.getMoney() >= this.getCost();
	}

	@Override
	public void chargeUser() {
		state.chargeUser(cost);
	}
	
	@Override
	public void refundUser() {
		state.creditUser(cost);
	}

	/*
	 * Clickable Methods
	 */
	
	@Override
	public boolean consumeClick() {
		// If the tower is moving and is in a valid location buy the tower
		if (isMoving && isValidPlacementLocation()) {
			isMoving = false;
			return true;
		}
		// Otherwise, if it's moving but the placement is invalid, expire the selected tower
		else if (isMoving && !isValidPlacementLocation()) {
			refundUser();
			isExpired = true;
			isVisible = false;
			return true;
		}
		// If tower is not moving, return false
		else
			return false;
	}
	
	/**
	 * Checks if the placement location for the tower is valid.
	 * @return a boolean value indicating a valid placement.
	 */
	private boolean isValidPlacementLocation() {
		if ((x < width/2 || x > 600 - width/2) || (y < height || y > 600))
			return false;
		else
			return true;
	}
}
