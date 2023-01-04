package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Menu extends GameObject {
	private String title;
	
	public Menu(State state, Control control) {
		super(state, control);
		
		title = "GARDEN DEFENSE";
		isVisible = true;
		isExpired = false;
	}

	@Override
	public void update(double elapsedTime) {}

	@Override
	public void draw(Graphics g) {
		// Draw background
		g.setColor(Color.BLACK);
		g.fillRect(600, 0, 200, 600);
		
		// Draw text items
		g.setColor(Color.GREEN);
		g.setFont(new Font("Arial", Font.BOLD, 20));
		g.drawString("WELCOME TO", 630, 50);
		g.drawString(title, 605, 80);
		
		// User Score
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 16));
		g.drawString("Score: " + state.getScore(), 620, 160);
		
		// User Health
		g.setColor(Color.RED);
		g.drawString("Health: " + state.getHealth(), 620, 200);
		
		// User Money
		g.setColor(Color.YELLOW);
		g.drawString("Money: " + state.getMoney(), 620, 240);
		
	}
}
