/**
 * State class for the tower defense game. Contains all necessary state functions and fields to operate the game.
 * Contains fields for the user's health, score, and money.
 * Contains all game objects in two lists, one for the current frame and one for the next frame.
 * Contains methods to create and update frames, as well as get-setters for the game object lists.
 * 
 * @author Tyler C. Wilcox
 * @version 19 November 2022
 */
package game;

import java.util.ArrayList;
import java.util.List;

public class State {
	//Game object lists
	private List<GameObject> currentFrameGameObjects;
	private List<GameObject> nextFrameGameObjects;
	
	//User fields
	private int userHealth = 0;
	private int userMoney = 0;
	private int userScore = 0;
	
	/**
	 * Constructor.
	 * Creates an empty currentFrame list. The nextFrame will be generated when it's needed.
	 */
	public State() {
		currentFrameGameObjects = new ArrayList<GameObject>();
	}
	
	/**
	 * Begins creating the next frame to draw by copying the current object list into the next object list.
	 */
	public void startFrame() {
		nextFrameGameObjects = new ArrayList<GameObject>();
		nextFrameGameObjects.addAll(currentFrameGameObjects);
	}
	
	/**
	 * Finishes creating a new frame by checking if any objects in the current frame have become expired.
	 * All non-expired objects are stored in the list of next-frame objects, which is then set to be the current frame.
	 */
	public void finishFrame() {
		for (GameObject go : currentFrameGameObjects)
			if (go.isExpired())
				nextFrameGameObjects.remove(go);
		currentFrameGameObjects = nextFrameGameObjects;
		nextFrameGameObjects = null;
	}
	
	/**
	 * Method to add a GameObject to the next frame.
	 * 
	 * @param go The GameObject to be added.
	 */
	public void addGameObject(GameObject go) {
		nextFrameGameObjects.add(go);
	}
	
	/**
	 * Accessor to return the full list of current game objects.
	 * 
	 * @return currentFrameGameObjects, the list of game objects in the current frame.
	 */
	public List<GameObject> getFrameObjects() {
		return currentFrameGameObjects;
	}
	
	/*
	 * User get-setters
	 */
	
	/**
	 * Returns the user's current health.
	 * @return User's health.
	 */
	public int getHealth() { return userHealth; }
	/**
	 * Returns the user's current money.
	 * @return User's money.
	 */
	public int getMoney() { return userMoney; }
	/**
	 * Returns the user's current score.
	 * @return User's score.
	 */
	public int getScore() { return userScore; }
	
	/**
	 * Reduces the user's health by the given amount.
	 * @param Amount of damage done to user.
	 */
	public void damageUser(int mod) { userHealth -= mod; }
	/**
	 * Reduces the user's money by the given amount.
	 * @param Amount of money taken from the user.
	 */
	public void chargeUser(int mod) { userMoney -= mod; }
	
	/**
	 * Increases the user's health by the given amount.
	 * @param Amount of healing done to user.
	 */
	public void healUser(int mod) { userHealth += mod; }
	/**
	 * Increases the user's money by the given amount.
	 * @param Amount of money given to the user.
	 */
	public void creditUser(int mod) { userMoney += mod; }
	/**
	 * Increases the user's score by the given amount.
	 * @param Amount to increase score.
	 */
	public void increaseScore(int mod) { userScore += mod; }
}
