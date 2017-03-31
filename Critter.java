package assignment5;

import java.util.HashSet;

/* CRITTERS GUI Critter.java
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

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.scene.layout.GridPane;

import java.util.Iterator;



public abstract class Critter {

	private static String myPackage;
	private static List<Critter> babies = new java.util.ArrayList<Critter>();
	private static Set<Critter> population = new HashSet<Critter>();
	private static World world = new World();
	private boolean baby = false;
	private boolean hasMoved = false;
	private boolean inFight = false;

	/* NEW FOR PROJECT 5 */
	public enum CritterShape {
		CIRCLE,
		SQUARE,
		TRIANGLE,
		DIAMOND,
		STAR
	}

	/* the default color is white, which I hope makes critters invisible by default
	 * If you change the background color of your View component, then update the default
	 * color to be the same as you background 
	 * 
	 * critters must override at least one of the following three methods, it is not 
	 * proper for critters to remain invisible in the view
	 * 
	 * If a critter only overrides the outline color, then it will look like a non-filled 
	 * shape, at least, that's the intent. You can edit these default methods however you 
	 * need to, but please preserve that intent as you implement them. 
	 */
	public javafx.scene.paint.Color viewColor() { 
		return javafx.scene.paint.Color.WHITE; 
	}

	public javafx.scene.paint.Color viewOutlineColor() { return viewColor(); }
	public javafx.scene.paint.Color viewFillColor() { return viewColor(); }

	public abstract CritterShape viewShape(); 


	// Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}

	protected final String look(int direction, boolean steps) {return "";}

	/* rest is unchanged from Project 4 */


	private static java.util.Random rand = new java.util.Random();
	public static int getRandomInt(int max) {
		return rand.nextInt(max);
	}

	public static void setSeed(long new_seed) {
		rand = new java.util.Random(new_seed);
	}


	/* a one-character long string that visually depicts your critter in the ASCII interface */
	public String toString() { return ""; }

	private int energy = 0;
	protected int getEnergy() { return energy; }

	private int x_coord;
	private int y_coord;

	/**
	 * This method moves the critter one unit in the direction given by the integer value
	 * passed in. This method checks whether or not a unit has moved in the time step
	 * and then also checks for in fight conditions.
	 * @param direction An integer representing the desired direction of movement
	 */
	protected final void walk(int direction) {

		boolean badLocation = false;

		// checks while Critter is fighting, if there are alive Critters in the position
		// it is trying to move to, if there is then it can't move there
		if (inFight) {
			for(Critter c: world.getCritters(getNewX(this.x_coord,direction), getNewY(this.y_coord, direction))) {
				if (c.energy > 0)
					badLocation = true;
			}
		}

		if(!this.hasMoved && !badLocation) {
			move(direction);
			this.hasMoved = true;
		}
		energy -= Params.walk_energy_cost;

	}

	/**
	 * This method moves the critter two units in the direction given by the integer value
	 * passed in. This method checks whether or not a unit has moved in the time step 
	 * and then also checks for in fight conditions.
	 * @param direction An integer representing the desired direction of movement
	 */
	protected final void run(int direction) {

		boolean badLocation = false;

		// checks while Critter is fighting, if there are alive Critters in the position
		// it is trying to move to, if there is then it can't move there
		if (inFight) {

			//invoke getNewX and get getNewY twice because run moves twice
			int newX = getNewX(getNewX(this.x_coord, direction), direction);
			int newY = getNewY(getNewY(this.y_coord, direction), direction);

			for(Critter c: world.getCritters(newX, newY)) {
				if (c.energy > 0)
					badLocation = true;
			}
		}

		if(!this.hasMoved && !badLocation) {
			move(direction);
			move(direction);
			this.hasMoved = true;
		}
		energy -= Params.run_energy_cost;
	}

	/**
	 * Move this Critter in one unit in a certain cardinal direction (and checks for wraps). 
	 * 0 is straight right, 2 is straight up, 4 is straight left, 6 is straight down.
	 * The odd numbers are diagonal directions between each one (up to 7).
	 * @param direction An integer representing the desired direction of movement
	 */
	private void move(int direction) {
		// if direction is even it is a cardinal direction
		if (direction % 2 == 0)
			moveCardinal(direction);
		else if (direction == 1) {
			moveCardinal(0);
			moveCardinal(2);
		} else if (direction == 3) {
			moveCardinal(2);
			moveCardinal(4);
		} else if (direction == 5) {
			moveCardinal(4);
			moveCardinal(6);
		} else {
			moveCardinal(6);
			moveCardinal(0);
		}
	}


	/**
	 * Move this Critter in one unit in a certain cardinal direction (and checks for wraps). 
	 * 0 is straight right, 2 is straight up, 4 is straight left, 6 is straight down.
	 * @param direction An integer representing the desired direction of movement
	 */
	private void moveCardinal(int direction) {


		if (direction == 0) {
			if (x_coord != Params.world_width - 1) {
				if (!this.baby) world.moveCritter(this, x_coord, y_coord, x_coord + 1, y_coord);
				x_coord += 1;
			} else {
				if (!this.baby) world.moveCritter(this, x_coord, y_coord, 0, y_coord);
				x_coord = 0;
			}
		} else if (direction == 2) {
			if (y_coord != 0) {
				if (!this.baby) world.moveCritter(this, x_coord, y_coord, x_coord, y_coord - 1);
				y_coord -= 1; 
			} else {
				if (!this.baby) world.moveCritter(this, x_coord, y_coord, x_coord, Params.world_height - 1);
				y_coord = Params.world_height - 1;
			}
		} else if (direction == 4) {
			if (x_coord != 0) {
				if (!this.baby) world.moveCritter(this, x_coord, y_coord, x_coord - 1, y_coord);
				x_coord -= 1;
			} else {
				if (!this.baby) world.moveCritter(this, x_coord, y_coord, Params.world_width - 1, y_coord);
				x_coord = Params.world_width - 1;
			}
		} else if (direction == 6) {
			if (y_coord != Params.world_height - 1) {
				if (!this.baby) world.moveCritter(this, x_coord, y_coord, x_coord, y_coord + 1);
				y_coord += 1; 
			} else {
				if (!this.baby) world.moveCritter(this, x_coord, y_coord, x_coord, 0);
				y_coord = 0;
			}
		}
	}

	/**
	 * This is a helper function for the move command that gives the new x_coord
	 * of a Critter given the direction it is slated to move to.
	 * @param x_coord Current x coordinate of a Critter
	 * @param direction An integer representing the desired direction of movement
	 * @return the integer for the new x_coord after direction is invoked
	 */
	private static int getNewX(int x_coord, int direction) {
		if(direction == 2 || direction == 6) {
			return x_coord;
		} else if(direction == 0 || direction == 1 || direction == 7) {
			if(x_coord != Params.world_width - 1)
				return x_coord + 1;
			else
				return 0;
		} else if(direction == 3 || direction == 4 || direction == 5) {
			if(x_coord != 0)
				return x_coord - 1;
			else
				return Params.world_width - 1;
		}

		return -1;
	}

	/**
	 * This is a helper function for the move command that gives the new y_coord
	 * of a Critter given the direction it is slated to move to.
	 * @param y_coord Current y coordinate of a Critter
	 * @param direction An integer representing the desired direction of movement
	 * @return the integer for the new x_coord after direction is invoked
	 */
	private static int getNewY(int y_coord, int direction) {
		if(direction == 0 || direction == 4) {
			return y_coord;
		} else if(direction == 5 || direction == 6 || direction == 7) {
			if(y_coord != Params.world_height - 1)
				return y_coord + 1;
			else
				return 0;
		} else if(direction == 1 || direction == 2 || direction == 3) {
			if(y_coord != 0)
				return y_coord - 1;
			else
				return Params.world_height - 1;
		}

		return -1;
	}

	/**
	 * This method takes in a "reproduced" Critter and instantiates it by setting its energy, giving it a location and placing it
	 * in the babies static array so that it will be added during the next time step.
	 * @param offspring Critter that has been created prior
	 * @param direction An integer representing the desired direction of movement
	 */
	protected final void reproduce(Critter offspring, int direction) {

		// if the Critter doesn't have enough energy to do reproduce, don't reproduce
		if (this.energy < Params.min_reproduce_energy) return;

		// distribute energy
		offspring.energy = this.energy/2;
		this.energy = (this.energy + 1)/2;

		offspring.x_coord = this.x_coord;
		offspring.y_coord = this.y_coord;

		// instantiate baby flag so move doesn't actually add baby to the World
		offspring.baby = true;
		offspring.move(direction);
		babies.add(offspring);
	}

	public abstract void doTimeStep();
	public abstract boolean fight(String oponent);


	/**
	 * This method simulates "time" within the World. In each time step, it will invoke each Critter's doTimeStep
	 * method. Then it will check each location for possible encounters between Critters. Then it will update each
	 * Critter by deducting the rest energy, followed by generating new Critters, placing the babies in the World
	 * and removing all dead Critters
	 */
	public static void worldTimeStep() {

		// do each Critter's doTimeStep
		for (Critter c : population) {
			c.hasMoved = false;
			c.doTimeStep();
		}

		// check for Encounters at each x, y location
		for (int i = 0; i < Params.world_width; i++) {
			for (int j = 0; j < Params.world_height; j++) {
				doEncounter(world.getCritters(i, j));
			}
		}

		updateRestEnergy();

		// Generate Algae
		for (int i = 0; i < Params.refresh_algae_count; i++) {
			try {
				makeCritter("Algae");
			} catch (InvalidCritterException e) {
				// This should never happen.
			}
		}

		// add babies from babies array at the end of the TimeStep
		addBabies();

		removeDead();

	}

	/**
	 * Goes through the population array and removes all dead Critters
	 */
	public static void removeDead(){
		Iterator<Critter> i = population.iterator();
		while (i.hasNext()) {
			Critter here = i.next();
			if (here.getEnergy() <= 0) {
				world.removeCritter(here, here.x_coord, here.y_coord);
				i.remove();
			}
		}
	}

	/**
	 * Goes through the static babies array and adds any babies to the population and the world
	 */
	public static void addBabies() {
		Iterator<Critter> c = babies.iterator();
		while (c.hasNext()) {
			Critter growing = c.next();
			population.add(growing);
			world.addCritter(growing, growing.x_coord, growing.y_coord);
			c.remove();
			growing.baby = false;
		}
	}

	/**
	 * Goes through population array and deducts rest energy from every Critter
	 */
	public static void updateRestEnergy() {
		for (Critter c : population) {
			c.energy -= Params.rest_energy_cost;
		}
	}

	/**
	 * Given an ArrayList of Critters, checks to see if there exist more than one Critter there. If so,
	 * make them battle until there is only one left within the ArrayList
	 * @param present ArrayList of Critters at an x_coord, y_coord pair
	 */
	public static void doEncounter(ArrayList<Critter> present) {
		if (present.size() < 2) return;
		else {
			while (present.size() > 1) {
				for (int i = 0; i < present.size() - 1; i++) {
					Critter one = present.get(0);
					Critter two = present.get(1);
					battle(one,two);

				}
			}
		}
	}

	/**
	 * This method calls the fight of each method and given that generates a random power of each Critter. Whoever has the
	 * greater power (determined by their remaining energy), will "win" the fight and take half of the losers energy. Then the
	 * loser will be removed from the World and population.
	 * @param A First Critter that is fighting
	 * @param B Second Critter that is fighting
	 */
	public static void battle(Critter A, Critter B) {

		// inFight is a boolean used to check for fleeing conditions in run/walk
		A.inFight = true; B.inFight = true;
		boolean Afight = A.fight(B.toString());
		boolean Bfight = B.fight(A.toString());
		A.inFight = false; B.inFight = false;

		int Apower;
		int Bpower;

		// remove dead Critters, they will die regardless 
		if (A.energy <= 0) {
			world.removeCritter(A, A.x_coord, A.y_coord);
			population.remove(A);
		}

		if (B.energy <= 0) {
			world.removeCritter(B, B.x_coord, B.y_coord);
			population.remove(B);
		}

		// if the Critters still haven't moved and are still alive, then make them fight
		if (A.x_coord == B.x_coord && A.y_coord == B.y_coord && A.energy > 0 && B.energy > 0) {
			if (!Afight) Apower = 0;
			else Apower = Critter.getRandomInt(A.energy);
			if (!Bfight) Bpower = 0;
			else Bpower = Critter.getRandomInt(B.energy);
			if (Apower > Bpower) {
				A.energy += B.energy/2;
				population.remove(B);
				world.removeCritter(B,  B.x_coord, B.y_coord);
			}
			else {
				B.energy += A.energy/2;
				population.remove(A);
				world.removeCritter(A, A.x_coord, A.y_coord);
			}
		}
	}


	public static void displayWorld(Object pane) {
		world.printWorld((GridPane) pane);
	}
	
	/* Alternate displayWorld, where you use Main.<pane> to reach into your
	   display component.
	   // public static void displayWorld() {}
	 */

	/* create and initialize a Critter subclass
	 * critter_class_name must be the name of a concrete subclass of Critter, if not
	 * an InvalidCritterException must be thrown
	 */
	/**
	 * create and initialize a Critter subclass.
	 * critter_class_name must be the unqualified name of a concrete subclass of Critter, if not,
	 * an InvalidCritterException must be thrown.
	 * (Java weirdness: Exception throwing does not work properly if the parameter has lower-case instead of
	 * upper. For example, if craig is supplied instead of Craig, an error is thrown instead of
	 * an Exception.)
	 * @param critter_class_name An unqualified String that has the name of the Critter subclass to be made
	 * @throws InvalidCritterException if String isn't a subclass of Critter
	 */
	public static void makeCritter(String critter_class_name) throws InvalidCritterException {

		Critter c;

		// call getCritterFromString and try to create a new Critter
		try {
			c = getCritterFromString(myPackage + "." + critter_class_name); 
		}
		catch (InvalidCritterException e) {
			throw e;
		}
		catch (Exception e) {
			return;
		}

		// give all starting values and add to World and population array
		c.energy = Params.start_energy;
		c.x_coord = getRandomInt(Params.world_width);
		c.y_coord = getRandomInt(Params.world_height);
		world.addCritter(c, c.x_coord, c.y_coord);
		population.add(c);
	}

	/**
	 * Given a String critter_class_name this will use reflection to find if there exists an instance
	 * of the qualified name of the Critter. If it does exist, it will call its constructor and return
	 * an instance of the Critter subclass.
	 * @param critter_class_name Qualified string of Critter subclass to be created
	 * @return instance of a Critter subclass of the type critter_class_name
	 * @throws InvalidCritterException if String isn't a subclass of Critter
	 * @throws Exception if any other exceptions are thrown
	 */
	private static Critter getCritterFromString (String critter_class_name) throws InvalidCritterException, Exception {
		Class<?> myCritter = null;
		Constructor<?> constructor = null;
		Object instanceOfMyCritter = null;

		try {
			myCritter = Class.forName(critter_class_name); 	// Class object of specified name
		} catch (ClassNotFoundException e) {
			throw new InvalidCritterException(critter_class_name);
		} 

		try {
			constructor = myCritter.getConstructor();		// No-parameter constructor object
			instanceOfMyCritter = constructor.newInstance();	// Create new object using constructor
		} catch (InvocationTargetException e) {
			throw (Exception) e.getCause();

		}

		// make sure that the string points to an actual Critter subclass
		if (!(instanceOfMyCritter instanceof Critter)) {
			throw new InvalidCritterException(critter_class_name);
		}

		Critter me = (Critter)instanceOfMyCritter;		// Cast to Critter
		return me;
	}

	/**
	 * Gets a list of critters of a specific type.
	 * @param critter_class_name What kind of Critter is to be listed.  Unqualified class name.
	 * @return List of Critters.
	 * @throws InvalidCritterException The String isn't a subclass of Critter
	 */
	public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {

		List<Critter> instances = new ArrayList<Critter>();
		Class<?> sampleType = null;

		try {
			sampleType = Class.forName(myPackage + "." + critter_class_name); // Class object of specified name
		} catch (ClassNotFoundException e) {
			throw new InvalidCritterException(critter_class_name);
		}


		for(Critter c: population) {
			if(sampleType.isInstance(c))
				instances.add(c);

		}

		return instances;
	}

	/**
	 * Prints out how many Critters of each type there are on the board.
	 * @param critters List of Critters.
	 */
	public static String runStats(List<Critter> critters) {
		String stat = "";
		stat += "" + critters.size() + " critters as follows -- ";
		java.util.Map<String, Integer> critter_count = new java.util.HashMap<String, Integer>();
		for (Critter crit : critters) {
			String crit_string = crit.toString();
			Integer old_count = critter_count.get(crit_string);
			if (old_count == null) {
				critter_count.put(crit_string,  1);
			} else {
				critter_count.put(crit_string, old_count.intValue() + 1);
			}
		}
		String prefix = "";
		for (String s : critter_count.keySet()) {
			stat += prefix + s + ":" + critter_count.get(s);
			prefix = ", ";
		}
		return stat;	
	}


	/* the TestCritter class allows some critters to "cheat". If you want to 
	 * create tests of your Critter model, you can create subclasses of this class
	 * and then use the setter functions contained here. 
	 * 
	 * NOTE: you must make sure thath the setter functions work with your implementation
	 * of Critter. That means, if you're recording the positions of your critters
	 * using some sort of external grid or some other data structure in addition
	 * to the x_coord and y_coord functions, then you MUST update these setter functions
	 * so that they correctup update your grid/data structure.
	 */
	static abstract class TestCritter extends Critter {

		/**
		 * Sets the energy of TestCritter given an integer value new_energy_value. If the parameter
		 * is 0 or less, then the TestCritter is removed from the world and the population ArrayList
		 * @param new_energy_value Energy value to set
		 */
		protected void setEnergy(int new_energy_value) {
			super.energy = new_energy_value;
			if(super.energy <= 0) {
				world.removeCritter(this, super.x_coord, super.y_coord);
				population.remove(this);
			}
		}

		/**
		 * Sets the x_coord of the TestCritter to new_x_coord.
		 * @param new_x_coord X coordinate to change
		 */
		protected void setX_coord(int new_x_coord) {
			world.moveCritter(this, super.x_coord, super.y_coord, new_x_coord, super.y_coord);
			super.x_coord = new_x_coord;
		}

		/**
		 * Sets the y_coord of the TestCritter to new_y_coord.
		 * @param new_y_coord Y coordinate to change
		 */
		protected void setY_coord(int new_y_coord) {
			world.moveCritter(this, super.x_coord, super.y_coord, super.x_coord, new_y_coord);
			super.y_coord = new_y_coord;
		}

		/**
		 * @return the x_coord of the TestCritter
		 */
		protected int getX_coord() {
			return super.x_coord;
		}

		/**
		 * @return the y_coord of the TestCritter
		 */
		protected int getY_coord() {
			return super.y_coord;
		}


		/*
		 * This method getPopulation has to be modified by you if you are not using the population
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.
		 */
		/**
		 * Takes the static Set of Critter and converts it into an ArrayList of Critters
		 * @return the population of all Critters in an ArrayList
		 */
		protected static List<Critter> getPopulation() {
			List<Critter> pop = new ArrayList<Critter>();
			for(Critter c: population) {
				pop.add(c);
			}
			return pop;
		}

		/*
		 * This method getBabies has to be modified by you if you are not using the babies
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.  Babies should be added to the general population 
		 * at either the beginning OR the end of every timestep.
		 */
		/**
		 * @return the ArrayList of babies
		 */
		protected static List<Critter> getBabies() {
			return babies;
		}
	}


	/**
	 * Clear the world of all critters, dead and alive
	 */
	public static void clearWorld() {
		world.clearWorld();
		population.clear();
		babies.clear();
	}


}
