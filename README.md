# EE422C-Critters2
Part 2 of Critters Project - GUI Implementation

This part of the Critters Project built upon the simple implementation in Part 1 of the project and added a GUI using JavaFX. 

## Code Structure
### Critter.java
This abstract class holds the brunt of the code that Critters requires to run properly. It holds the static Collections that holds the "world" of Critters. It also contains actions that each Critter will follow such as walk() and reproduce(). Each subclass of Critter must properly implement and adhere to the Critter methods in order for the user interface and controller to work properly. 

### Params.java
This class holds all the static variables used for various energy costs, base Critter values, and the world parameters. These parameters can be edited by the client for debugging and for different values, but will not be changed by our code.

### Main.java
This part is completely revamped for part 2. Main implements all the JavaFX animations and the console that allows for user control. Main will contain all the necessary components for user I/O.

### World.java
This class holds a 2D array of points of the Critter world. Each "point" in the array is another array of Critter objects at each location. This class was added for convenience and for helping out with displaying the World

### Critter Subclasses
Currently, our implementation has two given subclasses: Algae and Craig, which are used for testing. We also created Critter1, Critter2, Critter3, and Critter4, each with different characteristics.

## User Control (I/O)
### User Interface
The structure of the user interface comprises of 3 windows: one for user input/control, one to display the world continually, and a third window that displays the "stats" of Critters.

### Console
The console has 5 main components: adding Critters, setting the random seed, performing a set amount of time steps, animation, and displaying the stats of the Critters.

### Diagrams
<Insert Diagrams Here>


* **Samuel Zhang** -  [sahzhang](https://github.com/sahzhang)
* **Grace Zhuang** -  [grace-zhuang](https://github.com/grace-zhuang)