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
	
	private Label critterNotif = new Label();
	private Label randomNotif = new Label();
	private Label tsNotif = new Label();
	private Label animateNotif = new Label();


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
		primaryStage.setTitle("Critters");

		Stage secondaryStage = new Stage();
		secondaryStage.setTitle("Critter Stats");
		StackPane stats = new StackPane();
		stats.getChildren().add(statistics);
		Scene statScene = new Scene(stats, 400, 400);
		secondaryStage.setScene(statScene);
		secondaryStage.show();

		
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

		// adding critters to the world
		Label addCritter = new Label("Add a Critter: ");
		GridPane.setConstraints(addCritter, 0, 5);

		ChoiceBox<String> critters = new ChoiceBox<>();
		critters.getItems().addAll(validCritters);
		GridPane.setConstraints(critters, 2, 5);

		TextField number = new TextField();
		number.setPromptText("Num of Critters");
		GridPane.setConstraints(number, 1, 5);
		
	
		Button makeCritter = new Button("Make Critters!");
		makeCritter.setOnAction(e -> getCritterResults(critters, number, critterNotif));
		makeCritter.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		GridPane.setConstraints(makeCritter, 3, 5);

		

		// set random seed
		Label setSeed = new Label("Set Random Seed: ");
		GridPane.setConstraints(setSeed, 0, 7);

		TextField number3 = new TextField();
		number3.setPromptText("Random Seed");
		GridPane.setConstraints(number3, 1, 7);
		
		

		Button performSeed = new Button("Randomize!");
		performSeed.setOnAction(e -> getRandomSeed(number3, randomNotif));
		performSeed.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		GridPane.setConstraints(performSeed, 3, 7);



		// runStats
		Label runStats = new Label("Show Stats: ");
		GridPane.setConstraints(runStats, 0, 15);

		CheckBox[] classes = new CheckBox[validCritters.size()];
		for (int i = 0; i < validCritters.size(); i++) {
			classes[i] = new CheckBox(validCritters.get(i));
			grid.add(classes[i], 1, i + 15);
		}





		// perform time steps
		Label timeSteps = new Label("Perform Time Steps:");
		GridPane.setConstraints(timeSteps, 0, 9);

		TextField number2 = new TextField();
		number2.setPromptText("Num of Time Steps");
		GridPane.setConstraints(number2, 1, 9);
		
		
		Label numSteps = new Label("Time Step: " + timeStep);
		grid.add(numSteps, 1, 10);


		Button performTime = new Button("Time Happened!");
		performTime.setOnAction(e -> getTimeResults(number2, classes, tsNotif, numSteps));
		performTime.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		GridPane.setConstraints(performTime, 3, 9);

		// animation speed
		Label speed = new Label("Animate:");
		GridPane.setConstraints(speed, 0, 11);

/*		Slider slide = new Slider();
		slide.setMin(0);
		slide.setMax(10);
		slide.setValue(0);
		slide.setShowTickLabels(true);
		slide.setShowTickMarks(true);
		slide.setMajorTickUnit(5);
		slide.setBlockIncrement(1);
		GridPane.setConstraints(slide, 1, 12);
		
		slide.valueProperty().addListener(new ChangeListener() {

			@Override
			public void changed(ObservableValue arg0, Object arg1, Object arg2) {
				try {
				speedInd = 1/slide.getValue() *1000000000;
				}
				catch (Exception e) {
					speedInd = -1;
				}
			}
		});
*/
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

				while (System.nanoTime() - startTime < speedInd) {


				}
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
				stop.setDisable(true);
				makeCritter.setDisable(false);
				performTime.setDisable(false);
				performSeed.setDisable(false);
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
					stop.setDisable(false);
					makeCritter.setDisable(true);
					performTime.setDisable(true);
					performSeed.setDisable(true);
					
					// disable other buttons

					String chosen = speedChoice.getValue();
					if (chosen.equals("Slow")) speedInd = 1000000000;
					else if (chosen.equals("Medium")) speedInd = 500000000;
					else if (chosen.equals("Fast")) speedInd = 100000000;
					// slide.setDisable(true);
					timer.start();
				}
				else {
					animateNotif.setText("Must Set Speed");
					animateNotif.setTextFill(javafx.scene.paint.Color.RED);
				}


			}
		});

		
		// reset button
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


		// exit button
		Button exit = new Button("Exit Critters");
		exit.setOnAction(new EventHandler <ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.exit(0);
			}
		});
		exit.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		GridPane.setConstraints(exit, 3, 20);

		grid.getChildren().addAll(title, addCritter, critters, number, makeCritter,
				timeSteps, number2, performTime, runStats, setSeed, number3, performSeed,
				speed, speedChoice, start, stop, reset, exit);


		Scene scene = new Scene(grid, 500, 750);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		Stage worldStage = new Stage();
		worldStage.setTitle("World");
		Critter.displayWorld(worldGrid);
		worldStage.setScene(new Scene(worldGrid, Params.world_width*World.pixels, Params.world_height*World.pixels));
		worldStage.show();

		
		//set window positioning
		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
		
		//Control Panel
        primaryStage.setX(0); 
        primaryStage.setY(0);
        
        //World Stage
        worldStage.setX(primScreenBounds.getWidth() - worldStage.getWidth());
        worldStage.setY(0);



	}

	private void getCritterResults(ChoiceBox<String> critters, TextField number, Label critterNotif) {
		resetLabels();
		if (critters.getValue() != null) {
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
				
				// Invalid input
				if(num <= 0) {
					critterNotif.setText("Invalid Input");
					critterNotif.setTextFill(javafx.scene.paint.Color.RED);
				}

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

	private void getTimeResults(TextField number, CheckBox[] classes, Label notif, Label tSteps) {
		resetLabels();
		if (number.getText()!=null && !number.getText().isEmpty()) {
			int num;
			
			try {
				num = Integer.parseInt(number.getText());
			}
			catch (Exception e) {
				notif.setText("Invalid Input");
				notif.setTextFill(javafx.scene.paint.Color.RED);
				return;
			}

			if (num <= 0) {
				notif.setText("Invalid Input");
				notif.setTextFill(javafx.scene.paint.Color.RED);
				return;
			}
			
			notif.setText("Ran " + num + " Time Steps");
			notif.setTextFill(javafx.scene.paint.Color.GREEN);
			
			while (num > 0) {
				Critter.worldTimeStep();
				timeStep++;
				tSteps.setText("Time Step: " + timeStep);
				num--;
			}
		}
		Critter.displayWorld(worldGrid);
		getRunStatsResults(classes);
	}

	private void getRunStatsResults(CheckBox[] classes) {
		resetLabels();
		statistics.clear();
		for (int i = 0; i < classes.length; i++) {
			String critterName = validCritters.get(i);
			if (classes[i].isSelected()) {
				List<Critter> populace;
				try {
					populace = Critter.getInstances(critterName);
				} catch (InvalidCritterException e) {
					//will never happen
					return;
				}
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

	private void getRandomSeed(TextField number, Label notif) {
		resetLabels();
		if (number.getText()!=null && !number.getText().isEmpty()) {
			int n;
			try{
				n = Integer.parseInt(number.getText());
			}
			catch (Exception e) {
				notif.setText("Invalid Input");
				notif.setTextFill(javafx.scene.paint.Color.RED);
				return;
			}
			Critter.setSeed(n);
			notif.setText("Seed: " + n);
			notif.setTextFill(javafx.scene.paint.Color.GREEN);
		}
	}
	
	private void resetLabels() {
		critterNotif.setText("");
		randomNotif.setText("");
		tsNotif.setText("");
	}


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
