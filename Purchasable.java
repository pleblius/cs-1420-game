/**
 * Interface for game objects that can be bought.
 * Contains methods to check if the user can afford the item and to charge the user when it is purchased.
 * 
 * @author Tyler C. Wilcox
 * @version 18 November 2022
 */
package game;

public interface Purchasable {
	/**
	 * Checks if the user can afford to purchase the object.
	 * @return true if the user can afford the object, false otherwise.
	 */
	public boolean canAfford();
	/**
	 * Charges the user the given amount of money when the object is purchased.
	 */
	public void chargeUser();
	/**
	 * Refunds the user for the cost of the object for whatever reason.
	 */
	public void refundUser();
}
