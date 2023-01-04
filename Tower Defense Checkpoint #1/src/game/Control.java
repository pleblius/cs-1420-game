package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import path.Path;

public class Control implements Runnable,
								ActionListener
								{
	private State state;
	private View view;
	private Path path;
	
	public Control() {
		SwingUtilities.invokeLater(this);
	}
	
	public void run() {
		//Load the path from this object's resource folder
		ClassLoader myLoader = this.getClass().getClassLoader();
		InputStream pathStream = myLoader.getResourceAsStream("resources/path_2.path");
		Scanner pathScanner = new Scanner(pathStream);
		path = new Path(pathScanner);
		
		state = new State();
		view = new View(this,state);
		
		//Begins creation of a new frame, adds a background a snail object to the game list, then finishes the frame and draws it
        state.startFrame();
        state.addGameObject(new Background(this));
        state.addGameObject(new Snail(this));
        state.finishFrame();
        view.repaint();
        
        //Creates a new timer that triggers every 16 milliseconds
        Timer t = new Timer(16, this);
        t.start();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
        state.startFrame();
        for (GameObject go : state.getFrameObjects())
            go.update(0);    
        state.finishFrame();
        view.repaint();
	}
	
	/**
	 * Loads and returns an image with the given filename from this object's current resource directory
	 * 
	 * @param filename The filename of the image to be loaded
	 * @return The image to be returned
	 */
    public BufferedImage getImage (String filename)
    {
        try
        {
        	//Loads the image from the resource folder in this object's directory
        	ClassLoader myLoader = this.getClass().getClassLoader();
        	InputStream imageStream = myLoader.getResourceAsStream("resources/" + filename);
        	BufferedImage image = ImageIO.read(imageStream);
        	return image;
        }
        catch (IOException e)
        {
        	System.out.println("Could not find or load resources/" + filename);
        	System.exit(0);  // Close the frame, bail out.
        	return null;  // Does not happen, the application has exited.
        }
    }
    
    /**
     * Accessor to return this object's current path field
     * 
     * @return The path field to be returned
     */
	public Path getPath() {
		return path;
	}
}
