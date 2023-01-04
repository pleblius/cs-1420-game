/**
 * Control class for the Tower Defense game.
 * Controls the main game loop through the GUI thread using the run() method.
 * Contains fields for the path the game objects follow, the state and view objects, and the map with all cached images.
 * 
 * @author Tyler C. Wilcox
 * @version 19 November 2022
 */
package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import path.Path;

public class Control implements Runnable,
								ActionListener,
								MouseListener,
								MouseMotionListener
								{
	private State state;
	private View view;
	private Path path;
	private Map<String,BufferedImage> imageCache;
	
	// Initial user values
	private int startingMoney = 10000;
	private int startingHealth = 100;
	
	// Game object parameters
	private int towerCost = 100;
	
	// Mouse fields
	private int mouseX;
	private int mouseY;
	
	public Control() {
		// Start GUI thread
		SwingUtilities.invokeLater(this);
	}
	
	public void run() {
		// Create control objects
		state = new State();
		view = new View(state, this);
		
		// Load cache and path
		imageCache = new TreeMap<String,BufferedImage>();
		loadPath();
		
		// Initialize user stats
		state.creditUser(startingMoney);
		state.healUser(startingHealth);
		
		// Implement mouse listeners
		view.addMouseListener(this);
		view.addMouseMotionListener(this);
		
		// Begins creation of a new frame, adds a background a snail object to the game list, then finishes the frame and draws it
        state.startFrame();
        state.addGameObject(new Background(state, this));
        state.addGameObject(new Snail(state,this));
        state.addGameObject(new Menu(state, this));
        state.addGameObject(new MenuButton(state,this,"Salt Launcher"));
        state.finishFrame();
        view.repaint();
        
        // Creates a new timer that triggers every 16 milliseconds (60 FPS)
        Timer t = new Timer(16, this);
        t.start();
	}
	
	/**
	 * Every time an action event fires--i.e. every frame--begins creation of a new frame, updates the game objects,
	 * finishes creating the frame, and repaints the frame.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
        state.startFrame();
        
        for (GameObject go : state.getFrameObjects())
            go.update(0);    
        
        state.finishFrame();
        view.repaint();
	}
	
	/**
	 * Loads the desired game path and stores it in the path field.
	 */
	public void loadPath() {
		// Console print line to verify loading instances
		System.out.println("Loading path");
		
		// Load the path from this object's resource folder
		ClassLoader myLoader = this.getClass().getClassLoader();
		InputStream pathStream = myLoader.getResourceAsStream("resources/path_2.path");
		Scanner pathScanner = new Scanner(pathStream);
		
		path = new Path(pathScanner);
	}
	
	/**
	 * Returns the image file corresponding to the given filename.
	 * Checks if the image is in the image cache, and if not loads and stores it within the cache.
	 * 
	 * @param filename The filename of the image to be loaded
	 * @return The image to be returned
	 */
    public BufferedImage getImage (String filename)
    {
    	// Check if image is cached. If so, return it
    	if (imageCache.containsKey(filename))
    		return imageCache.get(filename);
    	
    	// If image is not cached, load it and save it to the cache. Includes a println statement to verify loading occurs once.
        try
        {
        	// Console print statement to verify loading instances
        	System.out.println("Loading " + filename);
        	
        	// Loads the image from the resource folder in this object's directory
        	ClassLoader myLoader = this.getClass().getClassLoader();
        	InputStream imageStream = myLoader.getResourceAsStream("resources/" + filename);
        	BufferedImage image = ImageIO.read(imageStream);
        	
        	// Store image in cache
        	imageCache.put(filename, image);
        	
        	return image;
        }
        catch (IOException e)
        {
        	System.out.println("Failed to load resources/" + filename);
        	System.exit(0); // If we fail to open the desired image, we crash the application
        	return null; 	// Never happens
        }
    }
    
    /*
     * Accessors
     */
    
    /**
     * Returns the game's path field.
     * @return The path field to be returned.
     */
	public Path getPath() { return path; }
	/**
	 * Returns the cost of a game tower.
	 * @return tower cost.
	 */
	public int getTowerCost() { return towerCost; }
	/**
	 * Returns the x location of the mouse in the game field.
	 * @return x coordinate of the mouse.
	 */
	public int getX() { return mouseX; }
	/**
	 * Returns the y location of the mouse in the game field.
	 * @return y coordinate of the mouse
	 */
	public int getY() { return mouseY; }
	
	@Override
	public void mouseDragged(MouseEvent e) {}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	
	/**
	 * Checks if any clickable objects have been clicked.
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		for (GameObject go : state.getFrameObjects())
			if (go instanceof Clickable)
				if (((Clickable) go).consumeClick())
					return;
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
}
