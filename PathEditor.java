/**
 * PathEditor class as part of checkpoint 1 for CS 1420.
 * Creates a GUI for an application to create and modify Path objects for use in later checkpoints.
 * 
 * @author Tyler C. Wilcox
 * @version 04 November, 2022
 */

package path;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * PathEditor is a class that extends the JPanel. It is used for implementing the
 * Path class, either through loading a saved Path or drawing and saving a new Path.
 */
public class PathEditor extends JPanel implements 	Runnable,
													MouseListener,
													ActionListener 
{
	/*
	 * Fields
	 */
	private static final long serialVersionUID = 0;
	
	//Primary Path object
	private Path path = new Path();
	
	//Undo-Redo fields
	private Path undoPath = new Path();
	private Path redoPath = new Path();
	private boolean undid = false;
	
	//File fields
	private JMenuItem loadPath;
	private JMenuItem savePath;
	private JMenuItem newPath;
	
	//Color fields
	private JMenuItem colorRed;
	private JMenuItem colorBlue;
	private JMenuItem colorPink;
	private JMenuItem colorBlack;
	
	//Background image fields
	private BufferedImage backdrop;
	private JMenuItem background1;
	private JMenuItem background2;
	
	//Button fields
	private JButton undoButton;
	private JButton redoButton;
	
	//Drawing Fields
	private Color pointColor = Color.BLUE; //default value
	
	/**
	 * PathEditor main method. Creates a new PathEditor object and begins running the application.
	 * 
	 * @param args Unused
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new PathEditor());
	}

	/**
	 * Run method for the application. Creates a new JFrame, JPanel, JMenu, and JButtons.
	 * Also loads in the background image for the application.
	 */
	public void run() {
		//Create a new JFrame
		JFrame f = new JFrame("Path Editor");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Set the minimum size of the new JPanel
		this.setMinimumSize(new Dimension(600, 600));
		this.setPreferredSize(new Dimension(600, 600));
        
		//Add this object as a mouselistener to itself
        this.addMouseListener(this);
        
        /*
         * Construct JPanel objects
         */
        Container content = new JPanel();
        content.setLayout(new BorderLayout());
        
        JPanel topLevel = new JPanel();
        topLevel.setLayout(new BorderLayout());
        topLevel.add(this, BorderLayout.CENTER);
        
        /*
         * Construct button panel
         */
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 0));
        
        undoButton = new JButton("Undo");
        redoButton = new JButton("Redo");
        
        buttonPanel.add(undoButton);
        buttonPanel.add(redoButton);
        
        //Add ActionListeners for the buttons
        undoButton.addActionListener(this);
        redoButton.addActionListener(this);
        
        /*
         * Construct JMenus and JMenuBar for the JFrame
         */
        JMenuBar menuBar = new JMenuBar();
        
        /*
         * File JMenu
         */
        JMenu fileMenu = new JMenu("File");
        
        loadPath = new JMenuItem("Load");
        savePath = new JMenuItem("Save");
        newPath = new JMenuItem("New");
        
        menuBar.add(fileMenu);
        fileMenu.add(newPath);
        fileMenu.add(loadPath);
        fileMenu.add(savePath);
        
        /*
         * Options JMenu
         */
        JMenu optionsMenu = new JMenu("Options");
        
        //Color menu to change the drawing color
        JMenu colorMenu = new JMenu("Color");
        
        colorRed = new JMenuItem("Red");
        colorBlue = new JMenuItem("Blue");
        colorPink = new JMenuItem("Pink");
        colorBlack = new JMenuItem("Black");
        
        menuBar.add(optionsMenu);
        optionsMenu.add(colorMenu);
        colorMenu.add(colorRed);
        colorMenu.add(colorBlue);
        colorMenu.add(colorPink);
        colorMenu.add(colorBlack);
        
        //Background image menu
        JMenu backgroundMenu = new JMenu("Background");
        
        background1 = new JMenuItem("Flowers");
        background2 = new JMenuItem("Garden");
        
        optionsMenu.add(backgroundMenu);
        backgroundMenu.add(background1);
        backgroundMenu.add(background2);
        
        //Add action listeners for all of the JMenuItems
        loadPath.addActionListener(this);
        savePath.addActionListener(this);
        newPath.addActionListener(this);
        colorRed.addActionListener(this);
        colorBlue.addActionListener(this);
        colorPink.addActionListener(this);
        colorBlack.addActionListener(this);
        background1.addActionListener(this);
        background2.addActionListener(this);
        
        content.add(topLevel, BorderLayout.CENTER);
        content.add(buttonPanel, BorderLayout.SOUTH);
        
        //Construct and pack the JFrame
        f.setContentPane(content);
        f.setJMenuBar(menuBar);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        
        //Load the background image
        try 
        {
            backdrop = ImageIO.read(new File("path_1.jpg"));
        } 
        catch (IOException e) 
        {
            System.err.println("Could not read image at path_1.jpg.");
        }
	}
	
	/**
	 * Draws dots at the points selected by the user, and then draws the path between those points.
	 */
	@Override
	public void paint(Graphics g) {
		g.setColor(pointColor);
		
        //Check if buttons are available
        redoButton.setEnabled(undid);
        undoButton.setEnabled(!undid);
		
		int circDiameter = 8;
		
		//Draw the background image
		g.drawImage(backdrop, 0, 0, null);
		
		//Draw circular dots at the selected points
		for (int i = 0; i < path.getPointCount(); i++)
			g.fillOval(path.getX(i) - circDiameter/2, path.getY(i) - circDiameter/2, circDiameter, circDiameter);
		//Draw lines between successive points
		for (int i = 0; i < path.getPointCount() - 1; i++)
			g.drawLine(path.getX(i), path.getY(i), path.getX(i+1), path.getY(i+1));
		//Draw circles around each point
		int radius = 45;
		g.setColor(Color.RED);
		for (int i = 0; i < path.getPointCount(); i++)
			g.drawOval(path.getX(i) - radius/2, path.getY(i) - radius/2, radius, radius);
	}
	
	//Unimplemented methods
	public void mouseExited(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	
	/**
	 * Gets the location of the mouse when the click is released, then re-draws the image.
	 */
	public void mouseReleased(MouseEvent e) {
		addPoint(e.getX(), e.getY());
	}
	
	/**
	 * Method to control menu actions.
	 * When a menu option is accessed by the user, this method calls the relevant action method.
	 */
	public void actionPerformed(ActionEvent e) {
		//File menu
		if (e.getSource() == loadPath) {
			loadPath();
		}
		else if (e.getSource() == savePath) {
			savePath();
		}
		else if (e.getSource() == newPath) {
			newPath();
		}
		
		//Options menu
		
		//Color options to change the draw color
		if (e.getSource() == colorRed) {
			pointColor = Color.RED;
			repaint();
		}
		else if (e.getSource() == colorBlue) {
			pointColor = Color.BLUE;
			repaint();
		}
		else if (e.getSource() == colorPink) {
			pointColor = Color.PINK;
			repaint();
		}
		else if (e.getSource() == colorBlack) {
			pointColor = Color.BLACK;
			repaint();
		}
		
		//Background Options
		if (e.getSource() == background1) {
	        try {
	            backdrop = ImageIO.read(new File("path_1.jpg"));
	            repaint();
	        } 
	        catch (IOException err) {
	            System.err.println("Could not read image at path_1.jpg.");
	        }
		}
		else if (e.getSource() == background2) {
			try {
				backdrop = ImageIO.read(new File("path_2.jpg"));
				repaint();
			}
			catch (IOException err) {
				System.err.println("Could not read image at path_2.jpg");
			}
		}
		
		//Button options
		
		if (e.getSource() == undoButton) {
			undo();
		}
		else if (e.getSource() == redoButton) {
			redo();
		}
	}
	
	/*
	 * Load save functions
	 */
	
	/**
	 * Method to load a saved Path object from a .path file into the PathEditor. Creates an undo object beforehand and redraws the image.
	 */
	private void loadPath() {
		getUndoPath();
		
		JFileChooser fileChooser = new JFileChooser();
		
		//Set the file filter to .path files
		FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("Path files", "path");
		fileChooser.setFileFilter(extensionFilter);
		
		int result = fileChooser.showOpenDialog(this);
		
		//If choice is invalid, close the file dialog
		if (result != JFileChooser.APPROVE_OPTION)
			return;
		
		//Open the file and load it into a scanner to construct a new Path object
		File file = fileChooser.getSelectedFile();
		try (Scanner input = new Scanner(file)) {
			path = new Path(input);
		}
		catch (IOException e) {
			System.err.println("Unable to load from selected file.");
		}
		
		repaint();
	}
	/**
	 * Method to save a new or edited Path from the PathEditor to a .path file.
	 */
	private void savePath() {
		JFileChooser fileChooser = new JFileChooser();
		
		//If file choice is invalid, close the dialog box
		int result = fileChooser.showOpenDialog(this);
		if (result != JFileChooser.APPROVE_OPTION)
			return;
		
		//Load the selected file location into a file object
		File file = fileChooser.getSelectedFile();
		String filePath = file.getAbsolutePath();
		
		//Append the the .path extension if the filename doesn't end with .path
		if(!filePath.endsWith(".path"))
			file = new File(filePath + ".path");
		
		//Write the Path object to the file, using the toString() method
		try (PrintWriter output = new PrintWriter(file)) {
			output.print(path.toString());
		}
		catch (IOException e) {
			System.err.println("Could not write to selected file.");
		}
	}
	/**
	 * Clears all current path data and creates a new path object, functionally resetting the editor, then redraws the image.
	 */
	private void newPath() {
		getUndoPath();
		path = new Path();
		
		repaint();
	}
	
	/**
	 * Adds a new point to the Path field path, redraws the frame, and sets the boolean flag undid to false
	 * 
	 * @param x The x coordinate of the point to be added
	 * @param y The y coordinate of the point to be added
	 */
	private void addPoint(int x, int y) {
		getUndoPath();
		path.add(x, y);
		repaint();
		
		undid = false;
	}
	
	/*
	 * Undo/Redo functions
	 */
	
	/**
	 * Creates a copy of the current path Array and saves it in the undo Array for possible future use.
	 * Sets boolean flag to false, disallowing redos.
	 */
	private void getUndoPath() {
		undoPath.setEqualTo(path);
		
		undid = false;
	}
	/**
	 * Sets the path field back to the previous value and redraws the image.
	 * Undo can only be performed once.
	 * 
	 * Sets boolean flag to true, allowing redos.
	 */
	private void undo() {
		getRedoPath();
		path.setEqualTo(undoPath);
		
		repaint();
		
		undid = true;
	}
	
	/**
	 * Creates a saved version of the previous path field before undoing it,
	 * allowing it to be restored if the user decides to cancel their undo action.
	 */
	private void getRedoPath() {
		redoPath.setEqualTo(path);
	}
	/**
	 * Allows the user to cancel the undo action, resetting the current path back to its previous value, and redraws the image.
	 * Sets boolean flag to false, disallowing further redos.
	 */
	private void redo() {
		path.setEqualTo(redoPath);
		repaint();
		
		undid = false;
	}
}