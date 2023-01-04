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
	// Game object lists - representing the currently displayed frame and the next frame to be generated.
	private List<GameObject> currentFrameGameObjects;
	private List<GameObject> nextFrameGameObjects;
	
	// User fields
	private int userHealth = 0;
	private int userMoney = 0;
	private int userScore = 0;
	
	// State Fields
	private boolean isGameOver;
	private boolean isGameStarted;
	
	// Time fields
	private double elapsedTime;
	private double totalTime;
	private double prevComputerTime;
	private double prevEnemyTime;
	
	/**
	 * Constructor.
	 * Creates an empty currentFrame list. The nextFrame will be generated when it's needed.
	 */
	public State() {
		currentFrameGameObjects = new ArrayList<GameObject>();
		
		isGameOver = false; // Game is running
		isGameStarted = false; // Give user time to set up
		
		// Set timing values
		prevComputerTime = System.currentTimeMillis()/1000.0;
		resetPrevEnemyTime();
		totalTime = 0;
		elapsedTime = 0;
	}
	
	/**
	 * Begins creating the next frame to draw by copying the current object list into the next object list.
	 */
	public void startFrame() {
		// Calculate frame timing
		elapsedTime = System.currentTimeMillis()/1000.0 - prevComputerTime;
		prevComputerTime = System.currentTimeMillis()/1000.0;

		totalTime += elapsedTime;
		
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
	public List<GameObject> getFrameObjects() { return currentFrameGameObjects; }
	
	/*
	 * Game Over Get/Set
	 */
	
	/**
	 * Accessor that returns the game's status. Will return true if the game over state has been achieved.
	 * Will return false if the game is still running.
	 * @return game over status.
	 */
	public boolean isGameOver() { return isGameOver; }
	/**
	 * Checks if the game has started. (10-second delay to give user time to set up.)
	 * @return true if game has started. false otherwise.
	 */
	public boolean isGameStarted() { return isGameStarted; }
	/**
	 * Sets the boolean flag for the game over status, allowing the game to be ended or restarted.
	 * @param b the boolean flag to set the game over status to.
	 */
	public void setGameOver(boolean b) { isGameOver = b; }
	/**
	 * Sets the boolean flag for the game started status, allowing the game to be begun and reset.
	 * @param b the boolean flag to set the game started status to.
	 */
	public void setGameStarted(boolean b) { isGameStarted = b; }
	
	/*
	 * Timer GET/SET
	 */
	
	/**
	 * Gets the elapsed time since the last frame.
	 * @return elapsed time since the last frame.
	 */
	public double getElapsedTime() { return elapsedTime; }
	/**
	 * Gets the total time since the game launched.
	 * @return total time since the game launched.
	 */
	public double getTotalTime() { return totalTime; }
	/**
	 * Gets the time that the last enemy was loaded at.
	 * @return the previous time (in seconds) that the enemy was loaded at.
	 */
	public double getPrevEnemyTime() { return prevEnemyTime; }
	
	/**
	 * Returns the time (in seconds) since the last enemy was loaded into the wave.
	 * @return Time since last enemy (in seconds)
	 */
	public double getTimeSinceLastEnemy() {
		return System.currentTimeMillis()/1000.0 - prevEnemyTime;
	}
	
	/**
	 * When a new enemy is loaded, call this to reset the timer on when the next enemy should be loaded. (Measured in seconds.)
	 */
	public void resetPrevEnemyTime() {
		prevEnemyTime = System.currentTimeMillis()/1000.0;
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
