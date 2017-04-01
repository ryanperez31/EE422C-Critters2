package assignment5;

/* CRITTERS GUI Main.java
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
import java.io.File;
import java.io.IOException;
import java.lang.reflect.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javafx.application.Application;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.control.CheckBox;
import javafx.scene.text.Font;
import javafx.animation.AnimationTimer;

public class Main extends Application{

	private static String myPackage;
	
	private TextArea statistics = new TextArea();
	List<String> validCritters = new ArrayList<String>();
	static GridPane worldGrid = new GridPane();
	double speedInd = 0.0;
	private int timeStep = 0;
	
	// notification labels
	private Label critterNotif = new Label();
	private Label randomNotif = new Label();
	private Label tsNotif = new Label();
	private Label animateNotif = new Label();

	// creating grid for displaying world
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
		
		//set columns
		for(int i = 0; i < Params.world_width; i++) {
			worldGrid.getColumnConstraints().add(new ColumnConstraints(World.pixels));
		}
		for(int i = 0; i < Params.world_height; i++) {
			worldGrid.getRowConstraints().add(new RowConstraints(World.pixels));
		}
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
	

	public void start(Stage primaryStage) {
		
		//for window positioning
		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
				
		
		primaryStage.setTitle("Critters");
		
		// other stages (for displaying world and showing Critter stats
		Stage secondaryStage = new Stage();
		secondaryStage.setTitle("Critter Stats");
		StackPane stats = new StackPane();
		stats.getChildren().add(statistics);
		Scene statScene = new Scene(stats,800 , primScreenBounds.getHeight()-800);
		secondaryStage.setScene(statScene);
		secondaryStage.show();
		
		Stage worldStage = new Stage();
		worldStage.setTitle("World");
		Critter.displayWorld(worldGrid);
		worldStage.setScene(new Scene(worldGrid, Params.world_width*World.pixels, Params.world_height*World.pixels));
		worldStage.show();

		
		//Control Panel
        primaryStage.setX(0); 
        primaryStage.setY(0);
        
        //World Stage
        worldStage.setX(primScreenBounds.getWidth() - worldStage.getWidth());
        worldStage.setY(0);
        
        //stats Stage
        secondaryStage.setX(0);
        secondaryStage.setY(primScreenBounds.getHeight()-secondaryStage.getHeight());
        

		// retrieving list of valid Critters in package
		Class[] arr;
		try {
			arr = getClasses(myPackage);
			for(int i = 0; i < arr.length; i++) {
				if((Critter.class.isAssignableFrom(arr[i])) && (!Modifier.isAbstract(arr[i].getModifiers()))) {
					validCritters.add(arr[i].getName().substring(myPackage.length() + 1));
				}
			}
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}

		// setting up basics of the control panel
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setVgap(15);
		grid.setHgap(10);
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(25);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(30);
		grid.getColumnConstraints().addAll(col1, col2);
		
		grid.add(critterNotif, 0 , 6);
		grid.add(randomNotif, 0, 8);
		grid.add(tsNotif, 0 ,10);
		grid.add(animateNotif, 0, 12);

		Label title = new Label("Welcome to Critters!");
		title.setFont(Font.font("Bradley Hand ITC", 50));
		GridPane.setConstraints(title, 0, 0, 4, 4);

		
		// "Adding Critters" function
		Label addCritter = new Label("Add a Critter: ");
		GridPane.setConstraints(addCritter, 0, 5);

		ChoiceBox<String> critters = new ChoiceBox<>();
		critters.getItems().addAll(validCritters);
		GridPane.setConstraints(critters, 2, 5);

		TextField number = new TextField();
		number.setPromptText("Num of Critters");
		GridPane.setConstraints(number, 1, 5);
		
		Button makeCritter = new Button("Make Critters!");
		makeCritter.setOnAction(e -> getCritterResults(critters, number));
		makeCritter.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		GridPane.setConstraints(makeCritter, 3, 5);


		// "Set Random Seed" function
		Label setSeed = new Label("Set Random Seed: ");
		GridPane.setConstraints(setSeed, 0, 7);

		TextField number3 = new TextField();
		number3.setPromptText("Random Seed");
		GridPane.setConstraints(number3, 1, 7);

		Button performSeed = new Button("Randomize!");
		performSeed.setOnAction(e -> getRandomSeed(number3));
		performSeed.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		GridPane.setConstraints(performSeed, 3, 7);


		// "Show Statistics" function
		Label runStats = new Label("Show Stats: ");
		GridPane.setConstraints(runStats, 0, 15);

		CheckBox[] classes = new CheckBox[validCritters.size()];
		for (int i = 0; i < validCritters.size(); i++) {
			classes[i] = new CheckBox(validCritters.get(i));
			grid.add(classes[i], 1, i + 15);
		}


		// "Perform Time Steps" function
		Label timeSteps = new Label("Perform Time Steps:");
		GridPane.setConstraints(timeSteps, 0, 9);

		TextField number2 = new TextField();
		number2.setPromptText("Num of Time Steps");
		GridPane.setConstraints(number2, 1, 9);
		
		Label numSteps = new Label("Time Step: " + timeStep);
		grid.add(numSteps, 1, 10);

		Button performTime = new Button("Time Happened!");
		performTime.setOnAction(e -> getTimeResults(number2, classes, numSteps));
		performTime.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		GridPane.setConstraints(performTime, 3, 9);
		
		// "Reset World" function
		Button reset = new Button("Reset World");
		reset.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				resetLabels();
				Critter.clearWorld();
				Critter.displayWorld(worldGrid);
				timeStep = 0;
				numSteps.setText("Time Step: " + timeStep);

			}
		});
		reset.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		GridPane.setConstraints(reset, 3, 19);


		// "Animation Speed" function
		Label speed = new Label("Animate:");
		GridPane.setConstraints(speed, 0, 11);

		ChoiceBox<String> speedChoice = new ChoiceBox<>();
		speedChoice.getItems().addAll("1FPS", "5FPS", "10FPS");
		GridPane.setConstraints(speedChoice, 1, 11);

		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {

				long startTime = System.nanoTime();

				Critter.worldTimeStep();
				Critter.displayWorld(worldGrid);
				timeStep++;
				numSteps.setText("Time Step: " + timeStep);
				getRunStatsResults(classes);

				// delay until next iteration of speedInd
				while (System.nanoTime() - startTime < speedInd) {}
			}
		};

		Button stop = new Button("Stop");
		stop.setDisable(true);
		GridPane.setConstraints(stop, 3, 12);
		stop.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				resetLabels();
				timer.stop();
				// disable all other buttons on control panel
				stop.setDisable(true);
				makeCritter.setDisable(false);
				performTime.setDisable(false);
				performSeed.setDisable(false);
				reset.setDisable(false);
				animateNotif.setText("");
			}
		});

		
		
		Button start = new Button("Start");
		GridPane.setConstraints(start, 3, 11);
		start.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				resetLabels();
				if (speedChoice.getValue()!=null) {
					animateNotif.setText("Animating");
					animateNotif.setTextFill(javafx.scene.paint.Color.BLUE);
					// disable other buttons on control panel
					stop.setDisable(false);
					makeCritter.setDisable(true);
					performTime.setDisable(true);
					performSeed.setDisable(true);
					reset.setDisable(true);
					
					String chosen = speedChoice.getValue();
					if (chosen.equals("Slow")) speedInd = 1000000000;
					else if (chosen.equals("Medium")) speedInd = 500000000;
					else if (chosen.equals("Fast")) speedInd = 100000000;
					timer.start();
				}
				else {
					animateNotif.setText("Must Set Speed");
					animateNotif.setTextFill(javafx.scene.paint.Color.RED);
				}
			}
		});

		
		


		// "Exit Critters" function
		Button exit = new Button("Exit Critters");
		exit.setOnAction(new EventHandler <ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.exit(0);
			}
		});
		exit.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		GridPane.setConstraints(exit, 3, 20);

		// add all nodes to the control panel
		grid.getChildren().addAll(title, addCritter, critters, number, makeCritter,
				timeSteps, number2, performTime, runStats, setSeed, number3, performSeed,
				speed, speedChoice, start, stop, reset, exit);


		Scene scene = new Scene(grid, 500, 700);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	

	/**
	 * This method retrieves the user's input into the "Make Critter" function on the control panel and 
	 * executes the command.
	 * 
	 * @param critters - ChoidBox holds the user's input for type of Critter to create
	 * @param number - TextField contains the user's input for number of Critters to create
	 */
	private void getCritterResults(ChoiceBox<String> critters, TextField number) {
		
		resetLabels();
		
		// there's a Critter type selected
		if (critters.getValue() != null) {
			
			// there's a quantity typed in
			if (number.getText()!= null && !number.getText().isEmpty()) {
				
				int num;
				
				try {
					num = Integer.parseInt(number.getText());
				}
				catch (Exception e) {
					critterNotif.setText("Invalid Input");
					critterNotif.setTextFill(javafx.scene.paint.Color.RED);
					return;
				}
				
				// invalid input
				if(num <= 0) {
					critterNotif.setText("Invalid Input");
					critterNotif.setTextFill(javafx.scene.paint.Color.RED);
				}

				// create Critters of selected type and quantity
				while (num > 0) {
					try {
						Critter.makeCritter(critters.getValue());
						critterNotif.setText("Made " + number.getText() + " " + (critters.getValue()) + "(s)");
						critterNotif.setTextFill(javafx.scene.paint.Color.GREEN);
					} catch (Exception e) {
						critterNotif.setText("Invalid Input");
						critterNotif.setTextFill(javafx.scene.paint.Color.RED);
					}
					num--;
				}
			}
		}
		
		Critter.displayWorld(worldGrid);
		
	}

	/**
	 * This method retrieves the user's input request for the "Perform Time Steps" function on the 
	 * control panel and executes the corresponding number of time steps.
	 * 
	 * @param number - Textfield containing user's input for number of Time Steps user wants to perform
	 * @param classes - CheckBox array containing user's input for which Critters the user would like to display
	 * 				statistics for
	 * @param numTimeSteps - Label displaying how many time steps have passed
	 */
	private void getTimeResults(TextField number, CheckBox[] classes, Label numTimeSteps) {
		
		resetLabels();
		
		// there's a number typed in
		if (number.getText()!=null && !number.getText().isEmpty()) {
			
			int num;
			
			try {
				num = Integer.parseInt(number.getText());
			}
			catch (Exception e) {
				tsNotif.setText("Invalid Input");
				tsNotif.setTextFill(javafx.scene.paint.Color.RED);
				return;
			}

			// invalid input
			if (num <= 0) {
				tsNotif.setText("Invalid Input");
				tsNotif.setTextFill(javafx.scene.paint.Color.RED);
				return;
			}
			
			// update on-screen dispaly of time steps passed
			tsNotif.setText("Ran " + num + " Time Steps");
			tsNotif.setTextFill(javafx.scene.paint.Color.GREEN);
			
			// perform time steps
			while (num > 0) {
				Critter.worldTimeStep();
				timeStep++;
				numTimeSteps.setText("Time Step: " + timeStep);
				num--;
			}
		}
		
		Critter.displayWorld(worldGrid);
		// run stats for specified Critters
		getRunStatsResults(classes);
	}
	
	/**
	 * This function will output the statistics for the Critters the user has selected to
	 * show stats for.
	 * 
	 * @param classes - CheckBox array containing user's input for which Critters the user would like to display
	 */
	private void getRunStatsResults(CheckBox[] classes) {
		
		resetLabels();
		statistics.clear();
		
		// iterate through all Critters
		for (int i = 0; i < classes.length; i++) {
			
			String critterName = validCritters.get(i);
			
			// user has selected to display stats for this critter
			if (classes[i].isSelected()) {
				
				List<Critter> populace;
				
				try {
					populace = Critter.getInstances(critterName);
				} catch (InvalidCritterException e) {
					//will never happen
					return;
				}
				
				// invoke that critter's runStats method, if it exists
				Class<?> critter;
				try {
					critter = Class.forName(myPackage + "." + critterName);
					Method m = critter.getMethod("runStats", List.class);
					statistics.appendText(m.invoke(null,  populace) + "\n");
				} catch (NoSuchMethodException e) {
					Critter.runStats(populace);
				} catch (Exception e) {
					// will never occur
				}
			}
		}
	}

	/**
	 * This function determines the user's input request for the "Set Random Seed" function on 
	 * the control panel and executes that request.
	 * 
	 * @param number - TextField containing user's input for what the user would like the random seed to be
	 */
	private void getRandomSeed(TextField number) {
		
		resetLabels();
		
		// there is a number entered
		if (number.getText()!=null && !number.getText().isEmpty()) {
			
			int n;
			
			try{
				n = Integer.parseInt(number.getText());
			}
			// invalid inputs
			catch (Exception e) {
				randomNotif.setText("Invalid Input");
				randomNotif.setTextFill(javafx.scene.paint.Color.RED);
				return;
			}
			
			Critter.setSeed(n);
			randomNotif.setText("Seed: " + n);
			randomNotif.setTextFill(javafx.scene.paint.Color.GREEN);
		}
	}
	
	/**
	 * This function resets all clears the notification labels on the screen between actions.
	 */
	private void resetLabels() {
		critterNotif.setText("");
		randomNotif.setText("");
		tsNotif.setText("");
	}

	/**
	 * OBTAINED FROM: http://stackoverflow.com/a/520344
	 * This function utilizes Java's built-in website resources to obtain a list of
	 * all necessary files for the project to run. The function then goes through the list
	 * and filters out all non-class files and returns a list of Class objects.
	 * 
	 * @param packageName String of name of package
	 * @return Class array 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private static Class[] getClasses(String packageName) throws ClassNotFoundException, IOException {

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null;
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();

		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}

		ArrayList<Class> classes = new ArrayList<Class>();

		for (File directory : dirs) {
			classes.addAll(findClasses(directory, packageName));
		}

		return classes.toArray(new Class[classes.size()]);
	}

	/**
	 * OBTAINED FROM: http://stackoverflow.com/a/520344
	 * Given a directory, and using java's File package, find all class files.
	 * @param directory
	 * @param packageName
	 * @return
	 * @throws ClassNotFoundException
	 */
	private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {

		List<Class> classes = new ArrayList<Class>();

		if (!directory.exists()) {
			return classes;
		}

		File[] files = directory.listFiles();

		for (File file : files) {

			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file, packageName + "." + file.getName()));
			} 
			else if (file.getName().endsWith(".class")) {
				classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
			}
		}

		return classes;
	}


}
