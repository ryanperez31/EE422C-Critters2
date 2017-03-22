package assignment5;

/* CRITTERS GUI Critter3.java
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
 * Critter3 is the ultimate pacifist. If it is prompted to fight, it would rather kill itself than actually fighting
 * which is exactly what it does. It suicides by "walking" a lot of times which results in its death. It's goal is to solely reproduce as
 * much as possible before it dies. Despite the stupidity of this strategy (because of energy loss), reproducing is all
 * this poor little creatures knows how to do. However, if it meets a friend (another Critter 3) during an encounter it simply tries to move away
 * rather than killing itself or fighting. Also there exist some Critter3's that have deformities that make them unable to reproduce, so they act
 * exactly like normal Critter3's but they can't reproduce, and there has been studies that suggest this is about a 10% chance of happening.
 * 
 * @author Samuel Zhang
 */

public class Critter3 extends Critter {
	
	
	private static int suicides = 0;
	private static int timesReproduced = 0;
	private boolean canReproduce;
	
	@Override
	public String toString() { return "3"; }
	
	public Critter3() {
		// There is about a 10% chance that Critter3 can't reproduce
		canReproduce = Critter.getRandomInt(8) != 0;
	}

	@Override
	
	/**
	 * Critter3 endlessly reproduces until it runs out of energy.
	 */
	public void doTimeStep() {
		
		// All Critter3 can do is reproduce endlessly, it serves no other purpose
		if(canReproduce) {
			while(getEnergy() >= Params.min_reproduce_energy) {
				Critter3 baby = new Critter3();
				timesReproduced++;
				reproduce(baby, Critter.getRandomInt(8));
			}
		}
	}

	@Override
	
	/**
	 * If Critter3 sees another Critter3 is walks away, otherwise it will kill itself
	 * by trying to invoke walk until it runs out of energy
	 * @param opponent String identifying the opponent that this is fighting
	 * @return a boolean indicating if this Critter4 wishes to fight
	 */
	public boolean fight(String opponent) {
		
		// If it sees a Critter3 friend, it will simply walk away and avoid all conflict
		if(opponent.equals("3")) {
			walk(Critter.getRandomInt(8));
			return false;
		}
				
		// Otherwise it will see no other option but to kill itself to avoid violence, even against Algae
		else {
			suicides++;
			while(getEnergy() > 0) {
				walk(Critter.getRandomInt(8));
			}
			return false;
		}
		
	}
	
	/**
	 * Print out global variables and total amount of Critter3's still alive
	 * @param crit3 List of Critter3's from population 
	 */
	public static void runStats(java.util.List<Critter> crit3) {
		System.out.print("" + crit3.size() + " total Critter3's    ");
		System.out.print("Reproduced " + timesReproduced + " times   ");
		System.out.print("Total Suicides: " + suicides);
		System.out.println();
	}

}
