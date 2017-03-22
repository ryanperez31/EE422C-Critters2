package assignment5;

/* CRITTERS GUI Critter1.java
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
 * Critter1 is the ultimate minimalist critter. It stays in place, doesn't move, and doesn't care about its surroundings.
 * When another critter encroaches on its space, Critter1 will politely move one space to the right in an attempt to avoid
 * conflict. If it is not able to move to the space to the right, Critter1, in its laziness, will essentially forfeit the 
 * fight (return false to fight()). However, if Critter1 is "bothered" (i.e. encounters another Critter) more than 2 times
 * in a span of 10 time steps, it will lash out and attack on the third encounter. 
 *
 * @author Grace Zhuang
 */

public class Critter1 extends Critter {
	
	// keep track of how many time steps have passed
	private int timeSteps;
	
	// keep track of how many times the Critter1 has been annoyed
	private int annoyed;
	
	public Critter1() {
		annoyed = 0;
		timeSteps = 0;
	}
	
	@Override
	public void doTimeStep() {
		
		// if 10 timeSteps have passed, the Critter1's annoyance will be reset
		if (timeSteps == 10) {
			timeSteps = 0;
			annoyed = 0;
		}
		else timeSteps++;
	}

	/**
	 * Critter1 will passively move to the side in most cases and forfeit the fight. However, if its annoynace
	 * level reaches a certain point (when it's bothered twice in a span of 10 time steps already), it will choose
	 * to fight the encroaching critter.
	 * @param opponent String identifying the opponent that this is fighting
	 * @return boolean value indicating if the Critter1 would like to fight
	 */
	@Override
	public boolean fight(String opponent) {
		if (annoyed < 2) {
			walk(0);
			annoyed++;
			// if space to the right is already occupied, Critter1 cannot move there, essentially forfeits fight
			return false;
		}
		else return true;
	}
	
	public String toString() {
		return "1";
	}
	
	public static void runStats(java.util.List<Critter> crit1) {
		System.out.print("" + crit1.size() + " total Critter1's    ");	
		System.out.println();
	}
}
