/**
 * Abstract class representing a tower object that extends the GameObject class.
 * Contains fields and accessors to get the unit's location (center of mass), height, and width.
 * Also contains a method that checks if a given coordinate is within the unit's hitbox, based on its location and size.
 * 
 * @author Tyler C. Wilcox
 * @version 11/28/2022
 */
package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import path.Path;

public abstract class Tower extends GameObject implements Clickable {
	protected boolean isMoving; // Field to represent if item is placed or is being placed
	
	// Drawing fields
	protected Point loc;
	protected int width;
	protected int height;
	
	// Parameter fields - Implemented by subclasses
	protected int cost;					// Monetary cost
	
	// Combat fields - Implemented by subclasses
	protected int attackDamage;			// Damage it inflicts
	protected int attackRange;			// Max range tower can attack at
	protected int projectileSpeed;		// Speed of the projectiles this tower launches
	protected double reloadTime;		// How many seconds between attacks
	protected double attackTimer;		// The computer time that the tower last attacked at
	protected boolean attackStored;		// If the tower has an attack ready or not
	
	public Tower(State state, Control control) {
		super(state, control);
		
		isMoving = true;
		loc = new Point(control.getX(), control.getY());
		
		drawLevel = control.SUPER_UI;
		
		attackTimer = 0;
		attackStored = false;
	}
	
	/*
	 * Attack methods - Abstract
	 */
	
	/**
	 * Performs the tower's attack on the given enemy, e.
	 * Implemented by subclasses.
	 * @param e The enemy to be attacked.
	 */
	abstract public void attack(Enemy e);
	/**
	 * Gets a target for this tower to attack.
	 * Implemented by subclasses.
	 * @return the enemy for this tower to target.
	 */
	abstract public Enemy getTarget();
	
	/**
	 * Checks if the given enemy is in range of the tower
	 * @param e The enemy whose range is to be checked.
	 * @return true if the enemy is within the tower's range, false otherwise.
	 */
	public boolean isInRange(Enemy e) {
		int distx = e.getX() - this.loc.x;
		int disty = e.getY() - this.loc.y;
		
		// If the linear distance is greater than the attack range, the target is out of range
		if (distx*distx + disty*disty > attackRange*attackRange)
			return false;
		
		// If we reach this point the target is in range
		return true;
	}
	
	// Accessor Methods
	
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
	 * Clickable Methods
	 */
	
	/**
	 * When a click is registered with this object, it checks if the placement location is valid or not.
	 * If the location is valid, it changes the moving boolean flag to false places the object.
	 * If the location is invalid, the user is refunded the cost of the tower and the tower is deleted from the game, freeing up the user's
	 * mouse.
	 * @return Boolean flag, representing whether the click was consumed or not.
	 */
	@Override
	public boolean consumeClick() {
		// If the tower is moving and is in a valid location, place the tower
		if (isMoving && isWithinGameField() && isValidLocation()) {
			isMoving = false;
			drawLevel = control.SKY;
			return true;
		}
		// If the tower is moving and the placement location is outside the game field, delete the tower and refund its cost to the user
		else if (isMoving && !isWithinGameField()) {
			refundUser();
			isExpired = true;
			isVisible = false;
			return true;
		}
		// If this line is reached, conditions haven't been met to consume click
		return false;
	}
	
	/**
	 * Checks if the placement location for the tower is within the field of play.
	 * @return true if tower hitbox is completely within the field of play, false otherwise.
	 */
	private boolean isWithinGameField() {	
		if ((loc.x < width/2 || loc.x > 600 - width/2) || (loc.y < height || loc.y > 600))
			return false;
		else
			return true;
	}
	
	/**
	 * Checks if the tower is in a valid location, meaning that it isn't covering another tower and is not blocking the path.
	 * @return true if the location is valid, false otherwise.
	 */
	private boolean isValidLocation() {
		// Check if the tower is being placed within the hitbox of another tower
		for (GameObject go : state.getFrameObjects()) {
			if (go instanceof Tower) {
				Tower t = (Tower) go;

				// Skip the tower if it's checking against itself
				if (t == this) continue;
				
				// Check x coordinates
				if (this.loc.x <= t.getX() + t.getWidth()/2 && this.loc.x >= t.getX() - t.getWidth()/2) {
					// Check y coordinates
					if (this.loc.y <= t.getY() + t.getHeight() - 10 && this.loc.y >= t.getY() - t.getHeight() + 10)
						return false;
				}
			}
		}

		// Check if any point of the hitbox is within the given radius of any point on the path
		
		Path path = control.getPath();
		int radius = 22;
		
		// Get coordinates for the corners of the hitbox
		Point topLeft = new Point(loc.x - width/4, loc.y - height + 16);
		Point topRight = new Point(loc.x + width/4, loc.y - height + 16);
		Point bottomLeft = new Point(loc.x - width/4, loc.y);
		Point bottomRight = new Point(loc.x + width/4, loc.y);
		
		// For each point, check if the collision occurs with the each boundary of the hitbox in turn, returning false if it does
		for (int i = 0; i < path.getPointCount(); i++) {
			// Top line
			if (checkCollision(topLeft, topRight, path.getX(i), path.getY(i), radius)) {
				return false;
			}
			// Left line
			if (checkCollision(topLeft, bottomLeft, path.getX(i), path.getY(i), radius)) {
				return false;
			}
			// Bottom line
			if (checkCollision(bottomLeft, bottomRight, path.getX(i), path.getY(i), radius)) {
				return false;
			}
			// Right line
			if (checkCollision(bottomRight, topRight, path.getX(i), path.getY(i), radius)) {
				return false;
			}
		}
		// If we've reached this point, all checks have been passed
		return true;
	}
	
	/**
	 * Checks if the line between points 1 and 2 collides with the circle defined by the points (circx, circy) with the given radius.
	 * Begins by checking if the ends of the line (point1 and point2) collide with the given circle, and if not checks if any other point on the line
	 * collides with the circle.
	 * 
	 * @param point1 The first endpoint of the line being checked.
	 * @param point2 The second endpoint of the line being checked.
	 * @param circx The x-coordinate of the center of the circle being checked.
	 * @param circy The y-coordinate of the center of the circle being checked.
	 * @param radius The radius of the circle being checked.
	 * 
	 * @return true if the line collides with the circle, false otherwise.
	 */
	private boolean checkCollision(Point point1, Point point2, int circx, int circy, int radius) {
		// Check if the ends of the line collide with the circle - returning true if it does
		if (pointCollision(point1.x, point1.y, circx, circy, radius) || pointCollision(point2.x, point2.y, circx, circy, radius))
			return true;
		
		// Check if the line collides with the circle - returning true if it does
		if (lineCollision(point1, point2, circx, circy, radius))
			return true;
		
		// If we reach this point, all collision checks have been avoided
		return false;
	}
	
	/**
	 * Checks if a point at the given x and y location collide with a circle of the given radius at the given x and y location.
	 * 
	 * @param pointx x-coordinate of the point in question.
	 * @param pointy y-coordinate of the point in question.
	 * @param circx x-coordinate of the circle's center.
	 * @param circy y-coordinate of the cirlce's center.
	 * @param radius Radius of the circle.
	 * 
	 * @return true if the point is within the circle's area, false otherwise.
	 */
	private boolean pointCollision(int pointx, int pointy, int circx, int circy, int radius) {
		// Find the distance between the point and the circle using the Pythagorean theorem
		int distX = pointx - circx;
		int distY = pointy - circy;
		
		// If dx^2 + dy^2 <= r^2, point is within the radius
		if (distX*distX + distY*distY <= radius*radius)
			return true;
		
		return false;
	}
	
	/**
	 * Checks if a line between points point1 and point 2 collides with the circle of the given radius at the given x and y location.
	 * 
	 * @param point1 First endpoint of the line to be checked.
	 * @param point2 Second endpoint of the line to be checked.
	 * @param circx x-coordinate of the circle in question.
	 * @param circy y-coordinate of the circle in question.
	 * @param radius Radius of the circle in question.
	 * 
	 * @return true if the line collides with the circle, false otherwise.
	 */
	private boolean lineCollision(Point point1, Point point2, int circx, int circy, int radius) {
		// Get the min and max x and y for the line between points 1 and 2
		int minX, maxX;
		if (point1.x < point2.x) {
			minX = point1.x;
			maxX = point2.x;
		}
		else {
			minX = point2.x;
			maxX = point1.x;
		}
		
		// Get the length of the line between points 1 and 2
		int dx = point2.x - point1.x;
		int dy = point2.y - point1.y;
		
		int len_sq = dx*dx + dy*dy;
		
		// Find the dot product of the line-circle displacement vector and the perpendicular direction of the line
		float dot = (circx - point1.x)*(point2.x - point1.x);
		dot += (circy - point1.y)*(point2.y - point1.y);
		dot = dot/len_sq;
		
		// Find the closest point on the line to the circle
		int closestX = point1.x + (int) (dot*(point2.x - point1.x));
		int closestY = point1.y + (int) (dot*(point2.y - point1.y));
		
		// Check if the closest point is between the two endpoints - If not the line does not collide with the circle
		// Since the closest point is farther away than the endpoints, which were established as being outside of the collision.
		boolean withinX;
		withinX = closestX > minX && closestX < maxX;
		if (!withinX)
			return false;
		
		// Finally, check distance of closest point. If it's within the radius of the circle, the line collides with the circle
		dx = circx - closestX;
		dy = circy - closestY;
		len_sq = dx*dx + dy*dy;
		if (len_sq <= radius*radius)
			return true;
		
		// If we reach this point, collision has been avoided
		return false;
	}
	
	// Purchasing Methods
	
	/**
	 * Checks if the user currently has enough money to afford the tower.
	 */
	public boolean canAfford() {
		return state.getMoney() >= this.getCost();
	}

	/**
	 * Charges the user for the cost of the tower.
	 */
	public void chargeUser() {
		state.chargeUser(cost);
	}
	
	/**
	 * Refunds the cost of the tower to the user.
	 */
	public void refundUser() {
		state.creditUser(cost);
	}
	
	/*
	 * Accessors
	 */
	
	/**
	 * Get the x coordinate of the tower's center.
	 * @return x coordinate of tower's center.
	 */
	public int getX() { return loc.x; }
	
	/**
	 * Get the y coordinate of the tower's center.
	 * @return y coordinate of the tower's center.
	 */
	public int getY() { return loc.y; }
	
	/**
	 * Get the width of the tower.
	 * @return width of the tower.
	 */
	public int getWidth() { return width; }
	
	/**
	 * Get the height of the tower.
	 * @return height of the tower.
	 */
	public int getHeight() { return height; }
	
	/**
	 * Gets the distance to the given enemy.
	 * @param e The enemy whose distance is to be found.
	 * @return the distance to the enemy.
	 */
	protected int distTo(Enemy e) {
		int distx = e.getX() - this.loc.x;
		int disty = e.getY() - this.loc.y;
		
		return (int) Math.pow((double) (distx*distx + disty*disty), 0.5);
	}
	
	/**
	 * Draws the tower's sprite at the given location, adjusted so the center of the base is at the user's mouse point.
	 */
	@Override
	public void draw(Graphics g) {	
		g.drawImage(image, loc.x - width/2, loc.y - height + 8, null);
		
		// Draw range arcs - red if dumb tower, blue if smart tower
		if (this instanceof DumbTower)
			g.setColor(Color.RED);
		else if (this instanceof SmartTower)
			g.setColor(Color.BLUE);
		g.drawOval(loc.x - attackRange, loc.y - attackRange, 2*attackRange, 2*attackRange);
	}
}
