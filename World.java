package assignment5;

/* CRITTERS GUI World.java
 * EE422C Project 5 submission by
 * Samuel Zhang
 * shz96
 * 16225
 * Grace Zhuang
 * gpz68
 * 16215
 * Slip days used: <0>
 * Spring 2017
 */

import java.util.ArrayList;
import java.util.List;

/**
 * This class holds the world of Critters. It utilizes a 2D array based off the coordinate system
 * defined in Critter. In each x-y coordinate pair, there is an ArrayList of Critters. Methods can be used
 * to access/remove/move Critters around
 * @author Samuel Zhang
 *
 */
public class World {
	private static List<List<List<Critter>>> array = new ArrayList<List<List<Critter>>>();

	/**
	 * Initializes static array, there should be one instance of World regardless of runs.
	 */
	public World() {
		for(int i = 0; i < Params.world_width; i++) {
			array.add(new ArrayList<List<Critter>>());
			for(int j = 0; j < Params.world_height; j++) {
				array.get(i).add(new ArrayList<Critter>());
			}
		}
	}

	/**
	 * This method takes a Critter c and adds it to the ArrayList at (x_coord, y_coord)
	 * @param c The Critter that is to be added
	 * @param x_coord The x coordinate
	 * @param y_coord The y coordinate
	 */
	public void addCritter(Critter c, int x_coord, int y_coord) {
		array.get(x_coord).get(y_coord).add(c);
	}

	/**
	 * This method returns the ArrayList of Critters for the respective (x_coord, y_coord)
	 * @param x_coord The x coordinate
	 * @param y_coord The y coordinate
	 * @return an ArrayList of Critters
	 */
	public ArrayList<Critter> getCritters(int x_coord, int y_coord) {
		return (ArrayList<Critter>) array.get(x_coord).get(y_coord);
	}

	/**
	 * This method moves Critter c from the ArrayList at its original coordinates (x_orig, y_orig) by removing it and
	 * then adding it to the ArrayList at (x_new, y_new)
	 * @param c The Critter to be moved
	 * @param x_orig The original x coordinate
	 * @param y_orig The original y coordinate
	 * @param x_new The new x coordinate
	 * @param y_new The new y coordinate
	 */
	public void moveCritter(Critter c, int x_orig, int y_orig, int x_new, int y_new) {
		removeCritter(c, x_orig, y_orig);
		addCritter(c, x_new, y_new);
	}

	/**
	 * This method removes Critter c from the ArrayList at (x_coord, y_coord)
	 * @param c The Critter to be removed
	 * @param x_coord The x coordinate
	 * @param y_coord The y coordinate
	 */
	public void removeCritter(Critter c, int x_coord, int y_coord) {
		array.get(x_coord).get(y_coord).remove(c);
	}

	/**
	 * This method prints out the World array based off the dimensions in Params in an ASCII interface, outlined
	 * in the Critters PDF guidelines
	 */
	public void printWorld() {
		// Print top border
		System.out.print("+");
		for (int i = 0; i < Params.world_width; i++)
			System.out.print("-");
		System.out.println("+");

		// Print actual stuff
		for (int j = 0; j < Params.world_height; j++) {
			System.out.print("|");
			for(int i = 0; i < Params.world_width; i++) {
				if(!array.get(i).get(j).isEmpty()) 
					System.out.print(array.get(i).get(j).get(0).toString());
				else
					System.out.print(" ");
			}
			System.out.println("|");
		}

		// Print bottom border
		System.out.print("+");
		for (int i = 0; i < Params.world_width; i++)
			System.out.print("-");
		System.out.println("+");
	}

	/**
	 * This method clears all the ArrayLists at all the coordinates in the World
	 */
	public void clearWorld() {
		for(int i = 0; i < Params.world_width; i++) {
			for(int j = 0; j < Params.world_height; j++) {
				array.get(i).get(j).clear();
			}
		}
	}

}
