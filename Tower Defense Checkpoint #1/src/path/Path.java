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
import java.awt.Graphics;
import java.awt.Point;

/**
 * Path is a class that is used in the PathEditor class to create new paths
 */
public class Path {
	//Fields
	private ArrayList<Point> points;
	private double totalLength;
	
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
		
		totalLength = this.getLength(0, this.getPointCount() - 1);
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
	 * Takes the percentage along the path and returns a point value associated with that location.
	 * The percentage should be passed as a number between 0.0 and 1.0.
	 * If the percentage is less than 0, the start point will be returned.
	 * If the percentage is greater than 1, the end point will be returned.
	 * 
	 * @param percentTraveled Percent distance traveled along the path between successive points. Should be a double between 0.0 and 1.0.
	 * @return The coordinate point of the location that corresponds to the percentage distance traveled.
	 */
	public Point convertToCoordinates(double percentTraveled) {
		//Check for out of bounds inputs
		if (percentTraveled < 0) return new Point(this.getX(0), this.getY(0));
		if (percentTraveled > 1) return new 
				Point(this.getX(this.getPointCount()-1),this.getY(this.getPointCount()-1));
		
		//Get line segment the snail is currently on
		double distTraveled = percentTraveled*this.totalLength;
		double segLength = 1; //Placeholder value of 1 prevents division by 0 if an error occurs in the loop
		
		//Get the line segment that the object is currently on
		int i = -1; //Iterator
		while (distTraveled >= 0) {
			//Increment at start of loop. When distTraveled becomes negative, iterator will point at start of line segment
			i++;
			if (i == this.getPointCount()) return null; //Check if index exceeds bounds
			
			//Get the length of the current line segment
			segLength = getLength(i, i+1);
			//Subtract current length from the distance traveled. When it becomes negative, exit the loop
			distTraveled -= segLength;
		}
		
		int curX, curY; //Hold the current x and y values of the object
		double segPercent = (distTraveled + segLength)/segLength; //Get percent along current line segment
		
		curX = (int)((1 - segPercent)*this.getX(i) + segPercent*this.getX(i+1));
		curY = (int)((1 - segPercent)*this.getY(i) + segPercent*this.getY(i+1));
		
		return new Point(curX,curY);
	}
	
	/**
	 * Gets the length of the path between the points at the given array indeces.
	 * 
	 * @param firstIndex The first index, indicating where the calculation should begin.
	 * @param lastIndex The last index, indicating where the calculation should end.
	 * @return The total length of the path between the two points.
	 */
	public double getLength(int firstIndex, int lastIndex) {
		double l = 0.0;
		double x_sq;
		double y_sq;
		
		//Iterate from start to end points, getting length of each section
		for (int i = firstIndex; i < lastIndex; i++) {
			x_sq = this.getX(i+1) - this.getX(i);
			x_sq = x_sq*x_sq;
			
			y_sq = this.getY(i+1) - this.getY(i);
			y_sq = y_sq*y_sq;
			
			l += Math.pow(x_sq + y_sq, 0.5);
		}
		
		return l;
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
	
	/**
	 * Draws the path to the JFrame using the graphics object g.
	 * 
	 * @param g The Graphics object used to draw the path to the screen.
	 */
	public void draw(Graphics g) {
		int circDiameter = 8;
		
		//Draw circular dots at the selected points
		for (int i = 0; i < this.getPointCount(); i++)
			g.fillOval(this.getX(i) - circDiameter/2, this.getY(i) - circDiameter/2, circDiameter, circDiameter);
		//Draw lines between successive points
		for (int i = 0; i < this.getPointCount() - 1; i++)
			g.drawLine(this.getX(i), this.getY(i), this.getX(i+1), this.getY(i+1));
	}
}