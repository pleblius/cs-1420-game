/**
 * Interface for game objects that can be damaged and killed.
 * Contains methods to damage and kill the implementing object.
 * 
 * @author Tyler C. Wilcox
 * @version 18 November 2022
 */
package game;

public interface Killable {
	/**
	 * Causes damage to the given object, reducing its health by the given amount.
	 * @param damage Damage dealt to the receiving object.
	 */
	public void damage(int damage);
	
	/**
	 * Kills the object.
	 */
	public void kill();
}
