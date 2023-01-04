package game;

import java.awt.Graphics;

public class Background extends GameObject{
	private Control control;
	
	public Background(Control c) {
		isVisible = true;
		isExpired = false;
		
		control = c;
	}
	
	@Override
	public void update(double elapsedTime) {}
	
	@Override
	public void draw(Graphics g) {
		g.drawImage(control.getImage("path_2.jpg"), 0, 0, null);
	}
}
