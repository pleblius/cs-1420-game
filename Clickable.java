/**
 * Interface for objects that are designed to be clicked on.
 * Contains a single method consumeClick() that determines what should be happened when the Clickable object is clicked.
 * 
 * @author Tyler C. Wilcox
 * @version 19 November 2022
 */
package game;

public interface Clickable {
	/**
	 * Determines what should happen if the object is clicked.
	 * Returns a boolean flag for whether the click should be consumed or not.
	 * 
	 * @return boolean flag for whether or not this object should consume the click.
	 */
	public boolean consumeClick();
}
