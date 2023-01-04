/**
 * DumbTower class, a subclass of the Tower class..
 * Represents a salt-launching defense tower with dumb tower AI.
 * Will always attack the closest enemy, regardless of all other considerations.
 * 
 * @author Tyler C. Wilcox
 * @version 19 November 2022
 */
package game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class DumbTower extends Tower {
	public DumbTower(State state, Control control) {
		super(state, control);

		// Default dumb tower values
		cost = 100;
		attackDamage = 1;
		reloadTime = 1.75;			// 1.75 second per attack
		projectileSpeed = 400; 	// Pixels per second
		attackRange = 100;		// Pixels
		
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
		
		// Draw image and get its parameters
		image = control.getImage("dumb_tower.png");
		width = image.getWidth();
		height = image.getHeight();
	}
	
	/*
	 * Game Object methods
	 */
	
	/**
	 * Overrides the GameObject update() function.
	 * If the tower is currently awaiting placement, it follows the user's mouse.
	 * If the tower has been placed, it checks if it has finished loading another salt salvo and is ready to fire.
	 */
	@Override
	public void update(double elapsedTime) {
		// If in moving state, have it follow the mouse cursor and end the update
		if (isMoving) {
			loc = new Point(control.getX(), control.getY());
			return;
		}
		
		// If the tower hadn't already finished reloading, update the attack timer and compare it again.
		// Once the timer exceeds the reload time, set them equal (helps with drawing reload bar).
		if (!attackStored) {
			attackTimer += elapsedTime;
			attackStored = attackTimer >= reloadTime;
			if (attackStored)
				attackTimer = reloadTime;
		}
		
		// Once the tower can attack, get and attack a target if possible
		if (attackStored)
			attack(getTarget());
	}
	
	/*
	 * Tower methods
	 */
	
	/**
	 * Launches a projectile at the targeted enemy's current location based on this tower's parameters.
	 * Calculates the necessary trajectory data, generates a new projectile, and resets this tower's attack timer.
	 * 
	 * @param e The enemy being attacked.
	 */
	@Override
	public void attack(Enemy e) {
		// Check if target is null and break if so
		if (e == null)
			return;
		
		// Calculate targeting data
		double dist = distTo(e);
		double unitX = (e.getX() - this.loc.x)/dist;
		double unitY = (e.getY() - (this.loc.y - 3*height/4))/dist;
		
		double xVel = unitX*projectileSpeed;
		double yVel = unitY*projectileSpeed;
		
		// Generate a projectile
		state.addGameObject(new SaltCrystal(state, control, e, new Point(loc.x, loc.y - 3*height/4), xVel, yVel, attackDamage));
		
		// Reset attack statistics
		attackTimer = 0;
		attackStored = false;
	}
	
	/**
	 * Gets a target for the tower to attack, based on the tower's range and the location of all available enemies.
	 * Because these towers have no targeting AI, it searches all enemy objects for the closest and selects that one.
	 * @return The closest enemy that can be attacked, or null if there are no available targets.
	 */
	@Override
	public Enemy getTarget() {
		// No targeting AI, tower targets the closest enemy that's in range
		List<Enemy> potentialTargets = new ArrayList<Enemy>();
		
		for (GameObject go : state.getFrameObjects()) {
			if (go instanceof Enemy) {
				// Add all enemies to the potential target list
				Enemy e = (Enemy) go;
				potentialTargets.add(e);
			}
		}
		
		// Find the closest enemy, or return null if no enemy is in range
		Enemy closestEnemy = null;
		int closestRange = attackRange; // Set max range as default
		int dist;
		
		if (potentialTargets.isEmpty()) return null;
		for (Enemy e : potentialTargets)
			if (isInRange(e)) {
				dist = distTo(e);
				
				if (dist < closestRange) {
					closestEnemy = e;
					closestRange = dist;
				}
			}
		
		return closestEnemy;
	}
}
