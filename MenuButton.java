package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class MenuButton extends GameObject implements Clickable {
	// Size fields
	private int outsideWidth;
	private int outsideHeight;
	private int insideWidth;
	private int insideHeight;
	
	// Location fields
	private int xmin;
	private int xmax;
	private int ymin;
	private int ymax;
	
	// Checks if a tower is pending
	private boolean queueTower;
	
	// Button text field
	private String buttonText;
	
	public MenuButton(State state, Control control, String text) {
		super(state, control);
		buttonText = text;
		
		// Initialize parameters
		outsideWidth = 120;
		outsideHeight = 60;
		insideWidth = 110;
		insideHeight = 50;
		
		xmin = 700 - outsideWidth/2;
		xmax = 700 + outsideWidth/2;
		ymin = 400;
		ymax = 400 + outsideHeight;
		
		isVisible = true;
		isExpired = false;
		queueTower = false;
	}
	/**
	 * Accessor method for the button's text.
	 * @return button text string.
	 */
	public String getText() { return buttonText; }
	
	@Override
	public void update(double elapsedTime) {
		if (queueTower) {
			state.addGameObject(new SaltLauncher(state, control, true));
			queueTower = false;
		}
	}

	@Override
	public void draw(Graphics g) {
		// Draw button with gray border and black interior
		g.setColor(Color.GRAY);
		g.fillRoundRect(700 - outsideWidth/2, 400, outsideWidth, outsideHeight, 20, 20);
		
		g.setColor(Color.WHITE);
		g.fillRoundRect(700 - insideWidth/2, 400 + (outsideHeight - insideHeight)/2, insideWidth, insideHeight, 16, 16);
		
		// Draw the button text
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.BOLD, 16));
		g.drawString(buttonText, 700 - insideWidth/2 + 2, 400 + insideHeight/2 + 10);
		
		g.setFont(new Font("Arial", Font.BOLD, 12));
		g.drawString("Cost: " + control.getTowerCost(), 700 - insideWidth/2 + 2, 400 + insideHeight/2 + 25);
	}

	@Override
	public boolean consumeClick() {
		int x = control.getX();
		int y = control.getY();
		
		// Check if mouse click is within the button's borders
		if ((x > xmin && x < xmax) && (y > ymin && y < ymax)) {
			queueTower = !queueTower; // Queue a tower to be added at the next update. (Prevents double queueing.)
			return true;
		}
		else
			return false;
	}

}
