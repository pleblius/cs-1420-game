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
	// Control fields
	private State state;
	private View view;
	private Path path;
	private Scanner enemyScanner;
	private Map<String,BufferedImage> imageCache;
	
	// Enemy fields
	private double waveTime; // Time between enemy spawns
	
	// Initial user values
	private int startingMoney = 10000;
	private int startingHealth = 100;
	
	// Game object parameters
	private int towerCost = 100;
	
	// Mouse fields
	private int mouseX;
	private int mouseY;
	
	// Draw level fields
	public final int BACKGROUND = 0;
	public final int GROUND_LEVEL = 1;
	public final int MAIN = 2;
	public final int SKY = 3;
	public final int UI = 4;
	public final int SUPER_UI = 5;
	public final int TOP = 6;
	
	/**
	 * Constructor - creates a new GUI thread to run the game application on.
	 */
	public Control() {
		// Start GUI thread
		SwingUtilities.invokeLater(this);
	}
	
	/**
	 * Game thread method. Loads all necessary objects and creates the timer that 
	 */
	public void run() {
		// Create control objects
		state = new State();
		view = new View(state, this);
		
		// Load image cache, path, and enemy spawn order
		imageCache = new TreeMap<String,BufferedImage>();
		loadPath();
		loadWave();
		
		// Initialize user stats
		state.creditUser(startingMoney);
		state.healUser(startingHealth);
		
		// Initialize wave loading values
		waveTime = 2; // First wave: load a new enemy every 15 seconds
		
		// Implement mouse listeners
		view.addMouseListener(this);
		view.addMouseMotionListener(this);
		
		// Begins creation of a new frame with the background and menu.
        state.startFrame();
        state.addGameObject(new Background(state, this));
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
        
        // Check if gameflag is over. If not, check if it needs to be. If not, update the game state
        if (!state.isGameOver()) {
        	if (state.getHealth() <= 0 && state.getTotalTime() > 30)
        		state.addGameObject(new GameOver(state,this));
        	else
        		// Check if we need to load a new enemy
        		if (state.getTimeSinceLastEnemy() > waveTime)
        			loadNextEnemy();
        		for (GameObject go : state.getFrameObjects())
        			go.update(state.getElapsedTime());
        }
        
        state.finishFrame();
        view.repaint();
	}
	
	/**
	 * Loads the desired game path and stores it in the path field.
	 */
	private void loadPath() {
		// Console print line to verify loading instances
		System.out.println("Loading path");
		
		// Load the path from this object's resource folder
		ClassLoader myLoader = this.getClass().getClassLoader();
		InputStream pathStream = myLoader.getResourceAsStream("resources/path_2.path");
		Scanner pathScanner = new Scanner(pathStream);
		
		path = new Path(pathScanner);
	}
	
	/**
	 * Loads the enemy order from the given text file "enemy_order.txt". Stores the scanner
	 * in the enemyScanner field for later access.
	 */
	private void loadWave() {
		System.out.println("Loading enemy order");
		
		// Load the enemy order into the enemyScanner field from the text file
		ClassLoader myLoader = this.getClass().getClassLoader();
		InputStream orderStream = myLoader.getResourceAsStream("resources/enemy_order.txt");
		enemyScanner = new Scanner(orderStream);
	}
	
	/**
	 * Loads the next enemy needed for the game state, using the enemy scanner loaded in from the enemy order text file.
	 * Checks if there is another enemy available in the current "level," and if so loads it into the wave.
	 * If the "level" is over, a new scanner is loaded to reset the spawn order
	 * and increases the difficulty by decreasing the time between each spawn.
	 */
	private void loadNextEnemy() {
		// Check if the enemy scanner has another object to load, and if so, add the corresponding game object
		if (enemyScanner.hasNext()) {
			if (enemyScanner.next().equals("s"))
				state.addGameObject(new Snail(state, this));
			else if (enemyScanner.next().equals("c"))
				state.addGameObject(new SCargo(state, this));
			
			state.resetPrevEnemyTime();
		}
		// If the scanner is empty, decrease the wave time and load a new wave into the scanner
		else {
			waveTime = waveTime*0.6d; // Speed up wave time by 40%
			loadWave();
		}
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
	/**
	 * Gets the x and y coordinates of the mouse every time that it moves and stores them in the mouseX and mouseY fields.
	 */
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
