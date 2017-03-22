package assignment5;

/* CRITTERS GUI Critter2.java
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
 * Critter2 behaves in a way you might expect average cynical beings to behave. These critters have "families", which can be thought of as heritage. Critters
 * birth from the same parent will belong to the same "family" as their parent Critter2. When a Critter2 encounters a critter of a different species, it feels 
 * threatened and attempts to stand its ground and fight. However, when it comes into contact with another critter of the same species, and the two critters are born
 * from different parents, the two Critter2s will mate and one of them will give birth to a child. Critter2s who come in contact but are born from the same
 * parent/family will not mate, since they do not believe in incest, but will also not fight, because they are family. Instaed, fate will choose which of the 
 * members gets to live.
 *
 * @author Grace Zhuang
 */



public class Critter2 extends Critter {
	
	// helps keep track of how many babies Critter2s produce
	private static int babies;
	
	// number of different families of Critter2s (every time a Critter 2 is explicitly made, it belongs to a brand new family
	private static int familyCount = 0;
	
	// to keep track of association between critters in contact
	private static String fam = "False";
	
	// each Critter2 has its heritage and identity
	private Integer myFamily;
	
	// called when Critter2 is explicitly made via "makeCritter"
	public Critter2() {
		familyCount++;
		myFamily = familyCount;
	}
	
	// called when Critter2 made from a parent existing Critter2
	public Critter2(int parent) {
		myFamily = parent;
	}
	
	@Override
	public void doTimeStep() {
		walk(getRandomInt(8));
	}

	/**
	 * Critter2 will check to see if the other critter encountered is a Critter2. If so, it will choose not to fight
	 * that critter. If both critters belong to different families, i.e. are not related, they will "mate" and one of
	 * the two will give birth to a new Critter2, who will belong to the same "family" as the Critter2 that gave birth
	 * to it.
	 * @param opponent String identifying the opponent that this is fighting
	 * @return boolean indicating if the Critter2 wishes to engage in battle
	 */
	@Override
	public boolean fight(String opponent) {
		
		// came in contact with a Critter2!
		if (opponent.equals("2")) {
			
			// first Critter2 in the location, haven't checked the other Critter2's heritage yet
			if (fam.equals("False")) {
				// save family of the first Critter2
				fam = myFamily.toString();
			}
			
			// we've already looked at the first Critter2 in the encounter
			else {
				// if this Critter2 is from a different family than the first Critter2 in the encounter
				if (!(myFamily.toString().equals(fam))) {
					// make a new child
					Critter2 child = new Critter2(myFamily);
					reproduce(child, getRandomInt(7));
					babies++;
					// reset static variable for the next Critter2 encounter
					fam = "False";
				}
			}
			// either way, Critter2s don't want to fight eachother
			return false;
		}
		
		else {
			return true;
		}
		
	}
	
	public String toString() {
		return "2";
	}
	
	public static void runStats(java.util.List<Critter> crit2) {
		System.out.print("" + crit2.size() + " total Critter2's    ");
		System.out.print("Critter2 babies: " + babies);
		System.out.println();
	}
}