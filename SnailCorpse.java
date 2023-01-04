/**
 * Extends the abstract Corpse class.
 * Represents the corpse of a snail.
 * Gets drawn whenever a snail enemy dies.
 * 
 * @author Tyler C. Wilcox
 * @version 11/28/2022
 */
package game;

import java.awt.Graphics;

public class SnailCorpse extends Corpse {

	public SnailCorpse(State state, Control control, int x, int y) {
		super(state, control, x, y);
		
		// Snail corpse default values
		lifeSpan = 3;
		
		image = control.getImage("splat.png");
	}

	/**
	 * Draws the snail corpse sprite at the given location.
	 */
	@Override
	public void draw(Graphics g) {
		// Draws the image at the given point about its center
		g.drawImage(image, loc.x - image.getWidth()/2, loc.y - image.getHeight()/2, null);
	}

}
