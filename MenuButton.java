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
	private int x;
	private int y;
	private int xmin;
	private int xmax;
	private int ymin;
	private int ymax;
	
	// Checks if a tower is pending
	private boolean queueTower;
	
	// Button text field
	private String buttonText;
	private int cost;
	private String type;
	
	public MenuButton(State state, Control control, int x, int y, String text) {
		super(state, control);
		buttonText = text;
		
		// Initialize parameters
		outsideWidth = 120;
		outsideHeight = 60;
		insideWidth = 110;
		insideHeight = 50;
		
		this.x = x;
		this.y = y;
		xmin = x - outsideWidth/2;
		xmax = x + outsideWidth/2;
		ymin = y;
		ymax = y + outsideHeight;
		
		isVisible = true;
		isExpired = false;
		queueTower = false;
		drawLevel = control.SUPER_UI; // Top-level UI
		
		// Check what kind of tower the button was for and adjust fields appropriately
		if (text.equals("Basic Launcher")) {
			cost = 100;
			type = new String("basic");
		}
		else if (text.equals("Smart Launcher")) {
			cost = 500;
			type = new String("smart");
		}
	}
	/**
	 * Accessor method for the button's text.
	 * @return button text string.
	 */
	public String getText() { return buttonText; }
	
	@Override
	public void update(double elapsedTime) {
		// If a tower is queued to add, add the type that corresponds to the button that summoned it
		if (queueTower) {
			if (type.equals("basic"))
				state.addGameObject(new DumbTower(state, control));
			else if (type.equals("smart"))
				state.addGameObject(new SmartTower(state, control));
			
			queueTower = false;
		}
	}

	@Override
	public void draw(Graphics g) {
		// Draw button with gray border and black interior
		g.setColor(Color.GRAY);
		g.fillRoundRect(x - outsideWidth/2, y, outsideWidth, outsideHeight, 20, 20);
		
		g.setColor(Color.WHITE);
		g.fillRoundRect(x - insideWidth/2, y + (outsideHeight - insideHeight)/2, insideWidth, insideHeight, 16, 16);
		
		// Draw the button text
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.BOLD, 14));
		g.drawString(buttonText, x - insideWidth/2 + 1, y + insideHeight/2);
		
		g.setFont(new Font("Arial", Font.BOLD, 11));
		g.drawString("Cost: " + cost, x - insideWidth/2 + 25, y + insideHeight/2 + 20);
	}
	
	/**
	 * Implements the consumable interface consumeClick() method.
	 * If the button is clicked, queues a tower to be added to the game list.
	 */
	@Override
	public boolean consumeClick() {
		int x = control.getX();
		int y = control.getY();
		
		// Check if mouse click is within the button's borders
		if ((x > xmin && x < xmax) && (y > ymin && y < ymax)) {
			queueTower = true; // Queue a tower to be added at the next update.
			return true;
		}
		else
			return false;
	}

}
