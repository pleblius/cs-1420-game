package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EnemyOrder {
	private List<String> enemyList;
	
	/**
	 * Default Constructor - Generates an empty enemy list.
	 */
	public EnemyOrder() {
		enemyList = new ArrayList<String>();
	}
	
	/**
	 * Scanner constructor. Takes a scanner with a linked string (usually from a text file) and loads
	 * the characters in sequential order to determine the order of enemies generated in each wave.
	 * 
	 * @param input The scanner linked to the string containing the enemy list.
	 */
	public EnemyOrder(Scanner input) {
		String nextEnemy;
		while (input.hasNext()) {
			nextEnemy = input.next();
			
			if (nextEnemy == "s")
				enemyList.add("Snail");
			else if (nextEnemy == "c")
				enemyList.add("SCargo");
		}
	}
}
