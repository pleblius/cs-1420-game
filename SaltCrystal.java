/**
 * SaltCrystal class, subclass of the GameObject class.
 * Represents a salt crystal projectile that is lobbed by the defense towers at snail enemies.
 * Deals damage to the enemies as it impacts them.
 */
package game;

import java.awt.Graphics;
import java.awt.Point;

public class SaltCrystal extends GameObject {
	// Trajectory fields
	private Point loc;
	private int totalDistance;
	private double xVelocity;
	private double yVelocity;
	private Enemy target;
	
	// Attack fields
	private int damage;

	public SaltCrystal(State state, Control control, Enemy target, Point origin, double xVelocity, double yVelocity, int damage) {
		super(state, control);
		
		this.target = target;
		loc = origin;
		totalDistance = 0;
		this.xVelocity = xVelocity;
		this.yVelocity = yVelocity;
		drawLevel = control.SKY;
		
		this.damage = damage;
		
		isVisible = true;
		isExpired = false;
		
		image = control.getImage("salt_crystals.png");
	}

	/**
	 * Implements GameObject.update().
	 * Updates the position of the salt crystal and checks if it has collided with an enemy object.
	 * If it does, it deals its damage to that enemy and despawns.
	 */
	@Override
	public void update(double elapsedTime) {
		double dx = xVelocity*state.getElapsedTime();
		double dy = yVelocity*state.getElapsedTime();
		
		double dist = Math.pow(dx*dx + dy*dy, 0.5);
		
		totalDistance += (int) dist;
		loc.x = loc.x + (int) dx;
		loc.y = loc.y + (int) dy;
		
		// If crystal travels too far, despawn it
		if (totalDistance > 300) {
			isVisible = false;
			isExpired = true;
		}
		
		// Check all enemy objects for collision, then deal damage to the first one it collides with
		for (GameObject go : state.getFrameObjects()) {
			if (go instanceof Enemy) {
				Enemy e = (Enemy) go;
				
				// Check if any corner is within the hitbox
				int left = loc.x - image.getWidth()/2;
				int right = loc.x + image.getWidth()/2;
				int top = loc.y - image.getHeight()/2;
				int bottom = loc.y + image.getHeight()/2;
				
				boolean hit = e.isWithinHitbox(left, top) || e.isWithinHitbox(left, bottom) ||
						e.isWithinHitbox(right, top) || e.isWithinHitbox(right, bottom) ||
						e.isWithinHitbox(loc.x, loc.y);
				if (hit) dealDamage(e);
			}
		}

	}
	
	/**
	 * Implements GameObject.draw().
	 * Draws the crystal at its given location.
	 */
	@Override
	public void draw(Graphics g) {
		g.drawImage(image, loc.x - image.getWidth()/2, loc.y - image.getHeight()/2, null);
	}
	
	/**
	 * Gets the location of the center of the salt crystal.
	 * @return the point object with the x and y coordinates of the crystal.
	 */
	public Point getLoc() { return loc; }
	
	/**
	 * Gets the enemy that this projectile is targeting, for AI purposes.
	 * Note: a projectile targeting an enemy does not guarantee that the projectile will hit that enemy.
	 * 
	 * @return The enemy that this projectile was launched at.
	 */
	public Enemy getTarget() { return target; }
	
	/**
	 * Gets the damage that this crystal is going to do.
	 * 
	 * @return int damage that this crystal will do when it hits.
	 */
	public int getDamage() { return damage; }
	
	/**
	 * Deals the projectile's damage to the enemy object that it impacts with, then despawns the projectile.
	 * 
	 * @param e The enemy object to be damaged
	 */
	public void dealDamage(Enemy e) {
		e.damage(damage);
		isExpired = true;
		isVisible = false;
	}
}
