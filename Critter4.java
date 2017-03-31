package assignment5;


/* CRITTERS GUI Critter4.java
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

/**
 * Critter4 is modeled after chameleons. They start off with a random color from the static color array.
 * If they encounter a fellow Critter4, one of the Critter4's will change its color to the other one. Critter4
 * are solely interested in conquest and getting all of the other ones to be their same color, so they will
 * try to avoid fighting by walking away.
 *
 * @author Samuel Zhang
 */


public class Critter4 extends Critter{

	// Static array of possible colors
	private static String[] colors = {"Red", "Blue", "Green", "Yellow", "Purple", "Burnt Orange"};
	private String color;

	// If encounterColor is "False" then we are not in an encounter, else it will be the color that the
	// Critter4 should be set to.
	private static String encounterColor = "False";

	@Override
	public String toString() { return "4"; }

	public Critter4() {
		color = colors[Critter.getRandomInt(colors.length)];
	}

	@Override
	/**
	 * 25% of the time the Critter4 will walk and 10% of the time it will reproduce a baby of the same color
	 */
	public void doTimeStep() {

		// 25% of the time, walk around
		if(Critter.getRandomInt(4) == 3) {
			walk(Critter.getRandomInt(8));
		}

		// 10% of the time reproduce a baby of the same color
		if(Critter.getRandomInt(10) == 9) {
			Critter4 baby = new Critter4();
			baby.color = this.color;
			reproduce(baby, Critter.getRandomInt(8));
		}

	}

	@Override

	/**
	 * If this Critter4 encounters another Critter4 it will try to change the color of the other
	 * Critter4, depending on the order one of the Critter4's colors will change. If the Critter4 encounters
	 * an Algae it will eat it, otherwise it will try to walk away and not fight.
	 * @param opponent String identifying the opponent that this is fighting
	 * @return a boolean indicating if this Critter4 wishes to fight
	 */
	public boolean fight(String opponent) {

		// if it encounters a fellow Critter4, change its color
		if(opponent.equals("4")) {

			if(encounterColor.equals("False"))
				encounterColor = color;
			else {
				color = encounterColor;
				encounterColor = "False";
			}
			return false;
		}

		// if it encounters an Algae, consume the Algae
		else if(opponent.equals("@")) {
			return true;
		}

		// or else try to walk away and not fight
		else {
			walk(Critter.getRandomInt(8));
			return false;
		}
	}

	/**
	 * Print out the number of Critter4's in the population and the amount of each color.
	 * @param crit4 List of Critter4's from population 
	 */
	public static String runStats(java.util.List<Critter> crit4) {
		String stats = "";
		stats += "" + crit4.size() + " total Critter4's    ";

		for(int i = 0; i < colors.length; i++ ){
			int count = 0;
			for(Object o: crit4) {

				// type cast Critter as Critter4
				Critter4 c4 = (Critter4) o;

				if(c4.color.equals(colors[i])) 
					count++;
			}

			stats += "" + count + " " + colors[i] + " Critter4's   ";
		}
		return stats;
	} 	
	
	@Override
	public CritterShape viewShape() {
		return CritterShape.DIAMOND;
	}
	
	
	public javafx.scene.paint.Color viewColor() {
		if (color.equals("Red"))
			return javafx.scene.paint.Color.RED;
		else if (color.equals("Blue"))
			return javafx.scene.paint.Color.BLUE;
		else if (color.equals("Green"))
			return javafx.scene.paint.Color.GREEN;
		else if (color.equals("Yellow"))
			return javafx.scene.paint.Color.YELLOW;
		else if (color.equals("Purple"))
			return javafx.scene.paint.Color.PURPLE;
		else if (color.equals("Burnt Orange"))
			return javafx.scene.paint.Color.DARKORANGE;
		else
			return javafx.scene.paint.Color.WHITE;
	}

}
