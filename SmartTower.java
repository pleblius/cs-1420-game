/**
 * SmartTower class, a subclass of the Tower class.
 * Overrides GameObject update() and draw() methods.
 * Represents a salt-launching defense tower with smart tower AI.
 * Will 
 * 
 * @author Tyler C. Wilcox
 * @version 19 November 2022
 */
package game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class SmartTower extends Tower {

	public SmartTower(State state, Control control) {
		super(state, control);

		// Default smart tower values
		cost = 500;
		attackDamage = 2;
		reloadTime = 1.0;			// 1 second per attack
		projectileSpeed = 600; 	// Pixels per second
		attackRange = 250;			// Pixels
		
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
		double timeToEnemy = dist/(double)projectileSpeed;
		
		double futurePercentage = e.getPercentage() + e.getSpeed()*timeToEnemy;
		Point futurePoint = control.getPath().convertToCoordinates(futurePercentage);
		
		double unitX = (futurePoint.getX() - this.loc.x)/dist;
		double unitY = (futurePoint.getY() - (this.loc.y - 3*height/4))/dist;
		
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
	 * This is a smart tower that uses priority targeting to simulate intelligent target selection.
	 * Defaults to closest enemy if no priorities are engaged.
	 * 
	 * @return The highest priority enemy that can be targeted.
	 */
	@Override
	public Enemy getTarget() {
		Enemy target = null;
		// Create a list of all enemies that are in range
		List<Enemy> potentialTargets = new ArrayList<Enemy>();
		
		int pendingDamage;
		for (GameObject go : state.getFrameObjects()) {
			if (go instanceof Enemy) {
				Enemy e = (Enemy) go;
				
				// For all the enemies in range, check if they are about to die
				if (isInRange(e)) {
					pendingDamage = 0;
					
					if (e.isTracked())
						for (SaltCrystal sc : e.getTrackers())
							pendingDamage += sc.getDamage();
					
					// If enemy is in range and is not about to die, add it to the list of potential targets
					if (pendingDamage < e.getHealth())
						potentialTargets.add(e);
				}
			}
		}
		
		// If no viable targets in range, return null
		if (potentialTargets.isEmpty()) return null;
		
		// Loop through the list of potential targets, eliminating lower priority targets until only one target remains.
		// If more than one target remain at the end of the loop, default to the one that's furthest along the path.
		List<Enemy> newTargets = new ArrayList<Enemy>();
		newTargets.addAll(potentialTargets);
		
		int i = 0;
		while (newTargets.size() > 1) {
			potentialTargets = new ArrayList<Enemy>();
			potentialTargets.addAll(newTargets);
			
			i++;
			
			newTargets = priorityCheck(potentialTargets, i);
		}
		
		// If all targets were eliminated in the last step, take the remaining enemies and select the one with the highest percentage
		if (newTargets.isEmpty()) {
			double highestPercent = 0.0;
			for (Enemy e : potentialTargets) {
				if (e.getPercentage() > highestPercent) {
					highestPercent = e.getPercentage();
					target = e;
				}
			}
		}
		else
			target = newTargets.get(0);
		
		return target;
	}
	
	 /** Overrides the GameObject update() function.
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
	
	/**
	 * Removes all targets that don't match the given priority level from the passed list of targets.
	 * 
	 * First, looks for targets that have more than 1 health.
	 * Tower does 2 damage and will try to maximize damage output by not wasting damage on an enemy that could be killed by a regular tower.
	 * 
	 * Second, prioritize vans over snails.
	 * Vans have more health and move faster and the higher damage of this tower negates their threat more than a basic tower could.
	 * 
	 * Third, prioritize enemies with exactly 2 health.
	 * Since the tower does two damage, this will instantly kill the enemy. It will reduce the chance that base towers waste their shots killing it.
	 * 
	 * Fourth, prioritize the highest health vans.
	 * If we cannot kill the van instantly, it would be best to soak as much damage as possible into them to soften them up for base towers.
	 * 
	 * @param targets The array list of potential targets to be parsed.
	 * @param i index numeral indicating priority level to check against.
	 * @return a new list with the targets that passed the priority check.
	 */
	public List<Enemy> priorityCheck(List<Enemy> targets, int i) {
		List<Enemy> newList = new ArrayList<Enemy>();
		if (i > 4) return newList;
		
		switch (i) {
		// Prioritize targets with more than 1 health
		case 1:
			for (Enemy e : targets) {
				if (e.getHealth() > 1)
					newList.add(e);
			}
			break;
		// Prioritize vans
		case 2:
			for (Enemy e : targets) {
				if (e instanceof SCargo)
					newList.add(e);
			}
			break;
			// Prioritize targets with exactly 2 health
		case 3:
			for (Enemy e : targets) {
				if (e.getHealth() == 2)
					newList.add(e);
			}
			break;
		// Prioritize enemies at max health
		case 4:
			int maxHealth = 0;
			for (Enemy e : targets) {
				if (e.getHealth() > maxHealth) maxHealth = e.getHealth();
			}
			for (Enemy e : targets) {
				if (e.getHealth() == maxHealth)
					newList.add(e);
			}
		}
		return newList;
	}
}
