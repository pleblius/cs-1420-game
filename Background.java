/**
 * Background object that extends the GameObject class.
 * Object remains static during game operation.
 * 
 * 
 * @author Tyler C. Wilcox
 * @version 19 November 2022
 */
package game;

import java.awt.Graphics;

public class Background extends GameObject{

	public Background(State state, Control control) {
		super(state, control);
		
		isVisible = true;
		isExpired = false;
	}
	
	@Override
	public void update(double elapsedTime) {}
	
	@Override
	public void draw(Graphics g) {
		g.drawImage(control.getImage("background.jpg"), 0, 0, null);
	}
}
