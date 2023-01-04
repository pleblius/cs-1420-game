package game;

import java.awt.Graphics;
import java.awt.Point;

public class Snail extends GameObject {
	private double percentage;
	private Control control;
	
	public Snail(Control c) {
		percentage = 0;
		
		control = c;
		
		isVisible = true;
		isExpired = false;
	}
	
	public void update(double elapsedTime) {
		percentage += 0.001;
	}
	
	public void draw(Graphics g) {
		Point loc = control.getPath().convertToCoordinates(percentage);
		g.drawImage(control.getImage("snail.png"), loc.x, loc.y, null);
	}
}
