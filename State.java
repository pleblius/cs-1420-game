package game;

import java.util.ArrayList;
import java.util.List;

public class State {
	private List<GameObject> currentFrameGameObjects;
	private List<GameObject> nextFrameGameObjects;
	
	/**
	 * Constructor
	 * Only creates an empty currentFrame list. The nextFrame will be generated when it's needed
	 */
	public State() {
		currentFrameGameObjects = new ArrayList<GameObject>();
	}
	
	/**
	 * Begins creating the next frame to draw by copying the current object list into the next object list
	 */
	public void startFrame() {
		nextFrameGameObjects = new ArrayList<GameObject>();
		nextFrameGameObjects.addAll(currentFrameGameObjects);
	}
	
	/**
	 * Finishes creating a new frame by setting the current object list to the next object list and deleting the next object list
	 */
	public void finishFrame() {
		currentFrameGameObjects = nextFrameGameObjects;
		nextFrameGameObjects = null;
	}
	
	/**
	 * Method to add a GameObject to the next frame
	 * 
	 * @param go The GameObject to be added
	 */
	public void addGameObject(GameObject go) {
		nextFrameGameObjects.add(go);
	}
	
	/**
	 * Accessor to return the full list of current game objects
	 * 
	 * @return currentFrameGameObjects, the List of game objects in the current frame
	 */
	public List<GameObject> getFrameObjects() {
		return currentFrameGameObjects;
	}
}
