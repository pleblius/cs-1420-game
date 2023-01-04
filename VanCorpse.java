/**
 * Extends the abstract Corpse class.
 * Represents the corpse of a van.
 * Gets drawn whenever a van enemy dies.
 * 
 * @author Tyler C. Wilcox
 * @version 11/28/2022
 */
package game;

import java.awt.Graphics;

public class VanCorpse extends Corpse {
	
	public VanCorpse(State state, Control control, int x, int y) {
		super(state, control, x, y);
		
		// Van corpse default values
		lifeSpan = 5;
		
		image = control.getImage("crash.png");
	}

	/**
	 * Draws the van's corpse at the location the van died at.
	 */
	@Override
	public void draw(Graphics g) {		
		// Draws the image at the given point about its center
		g.drawImage(image, loc.x - image.getWidth()/2, loc.y - image.getHeight()/2, null);
	}

}
