/**
 * GameOver class, a subclass of GameObject.
 * Implements GameObject update() and draw() methods.
 * Displays the game over screen and suspends game-loop operation, preventing further updates.
 * 
 * @author Tyler C. Wilcox
 * @version 25 November 2022
 */
package game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class GameOver extends GameObject {
	
	/**
	 * Creates a new GameOver object and calls the state.setGameOver() method, suspending game updates.
	 * @param state
	 * @param control
	 */
	public GameOver(State state, Control control) {
		super(state, control);
		state.setGameOver(); // Set the game state to over
		
		isVisible = true;
		isExpired = false;
		drawLevel = control.TOP; // Supercedes all other elements
	}
	/**
	 * Empty
	 */
	@Override
	public void update(double elapsedTime) {}
	
	/**
	 * Draws the "Game Over" sprite on the top level.
	 */
	@Override
	public void draw(Graphics g) {
		BufferedImage image = control.getImage("game_over.png");
		
		g.drawImage(image, 0, 0, null);
	}

}
