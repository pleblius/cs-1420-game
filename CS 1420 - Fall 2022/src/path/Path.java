/**
 * Path class for creation of new Path objects.
 * Used in conjunction with PathEditor to create paths for future checkpoints.
 * 
 * @author Tyler C. Wilcox
 * @version 04 November, 2022
 */

package path;

import java.util.ArrayList;
import java.util.Scanner;
import java.awt.Point;

/**
 * Path is a class that is used in the PathEditor class to create new paths
 */
public class Path {
	//Fields
	private ArrayList<Point> points;
	
	/**
	 * Empty constructor that calls no arguments.
	 * Creates an empty Path object.
	 */
	public Path() {
		points = new ArrayList<Point>(0);
	}
	
	/**
	 * Overloaded constructor that calls a scanner to load in a saved path.
	 * The scanner iterates over the information it receives and stores that information in the points ArrayList.
	 * 
	 * @param input A scanner object that can receive integer information to store in points.
	 */
	public Path(Scanner input) {
		int numPoints = input.nextInt();
		int nextX;
		int nextY;
		
		//Creates a new points ArrayList with capacity equal to the first integer passed to the scanner.
		points = new ArrayList<Point>(numPoints);
		
		//Scans through two integers at a time, storing them as the x and y values of a new point at the end of the points ArrayList.
		for (int i = 0; i < numPoints; i++) {
			nextX = input.nextInt();
			nextY = input.nextInt();
			
			points.add(i, new Point(nextX,nextY));
		}
	}
	
	//Accessors
	/**
	 * Gets the number of Point objects saved in the points array.
	 * 
	 * @return an integer with the size of the points array.
	 */
	public int getPointCount() {
		return points.size();
	}
	/**
	 * Gets the x value of the point at the given array index.
	 * Array is 0-indexed, meaning the first point is located at n = 0.
	 * 
	 * @param n An integer of the array index to be accessed.
	 * @return returns the x value of the point object at the given index.
	 */
	public int getX(int n) {
		return points.get(n).x;
	}
	/**
	 * Gets the y value of the point at the given array index.
	 * Array is 0-indexed, meaning the first point is located at n = 0.
	 * 
	 * @param n An integer of the array index to be accessed.
	 * @return returns the y value of the point object at the given index.
	 */
	public int getY(int n) {
		return points.get(n).y;
	}
	
	//Setters
	/**
	 * Appends a new point object to the end of the points ArrayList with the given x and y values.
	 *  
	 * @param x The x value of the point to be added.
	 * @param y The y value of the point to be added.
	 */
	public void add(int x, int y) {
		points.add(new Point(x,y));
	}
	/**
	 * Copies the list of points in the given Path object into this object.
	 * 
	 * @param newPath A Path object whose list of points is to be copied.
	 */
	public void setEqualTo(Path newPath) {
		points = new ArrayList<Point>(newPath.getPointCount());
		
		for (int i = 0; i < newPath.getPointCount(); i++)
			points.add(new Point(newPath.getX(i),newPath.getY(i)));
	}
	/**
	 * Creates a new string that contains all of the information for the path.
	 * String begins with the number of points in the array.
	 * String then contains the x and y coordinates of each point in the array.
	 * All points are separated by a line break.
	 * Example string:
	 * 2
	 * 1 3
	 * 4 5
	 * 
	 */
	public String toString() {
		String tempString = new String();
		
		//Prints the number of points followed by a line-break
		tempString = points.size() + "\n";
		
		//Prints out each x and y value, space separated, with a line-break afterwards
		for (int i = 0; i < points.size(); i++) {
			tempString += points.get(i).x + " " + points.get(i).y + "\n";
		}
		
		return tempString;
	}
}