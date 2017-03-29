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
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

public class Main extends Application{

	private static String myPackage;
	private TextArea statistics = new TextArea();
	List<String> validCritters = new ArrayList<String>();
	GridPane worldGrid = new GridPane();

	
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
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
		
		Stage worldStage = new Stage();
		worldStage.setTitle("World");
		Critter.displayWorld(worldGrid);
		worldStage.setScene(new Scene(worldGrid, Params.world_width*World.pixels, Params.world_height*World.pixels));
		worldStage.show();
		
		Class[] arr;
		try {
			arr = getClasses(myPackage);
			for(int i = 0; i < arr.length; i++) {
				if((Critter.class.isAssignableFrom(arr[i])) && (!Modifier.isAbstract(arr[i].getModifiers()))) {
					System.out.println("true");
					validCritters.add(arr[i].getName().substring(myPackage.length() + 1));
				}
			}
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}

		
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setVgap(8);
		grid.setHgap(10);
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(30);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(25);
		grid.getColumnConstraints().addAll(col1, col2);
		
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
		makeCritter.setOnAction(e -> getCritterResults(critters, number));
		GridPane.setConstraints(makeCritter, 3, 5);
		
		
		
		// set random seed
		Label setSeed = new Label("Set Random Seed: ");
		GridPane.setConstraints(setSeed, 0, 9);
		
		TextField number3 = new TextField();
		number3.setPromptText("Random Seed");
		GridPane.setConstraints(number3, 1, 9);
		
		Button performSeed = new Button("Randomize!");
		performSeed.setOnAction(e -> getRandomSeed(number3));
		GridPane.setConstraints(performSeed, 3, 9);
			
		// runStats
		Label runStats = new Label("Show Stats: ");
		GridPane.setConstraints(runStats, 0, 11);
		
		CheckBox[] classes = new CheckBox[validCritters.size()];
		for (int i = 0; i < validCritters.size(); i++) {
			classes[i] = new CheckBox(validCritters.get(i));
			grid.add(classes[i], 1, i + 11);
		}
		
		
		
		
		
		// perform time steps
		Label timeSteps = new Label("Perform Time Steps:");
		GridPane.setConstraints(timeSteps, 0, 7);
		
		TextField number2 = new TextField();
		number2.setPromptText("Num of Time Steps");
		GridPane.setConstraints(number2, 1, 7);
		
		Button performTime = new Button("Time Happened!");
		performTime.setOnAction(e -> getTimeResults(number2, classes));
		GridPane.setConstraints(performTime, 3, 7);
		
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
				timeSteps, number2, performTime, runStats, setSeed, number3, performSeed, exit);
		
		
		Scene scene = new Scene(grid, 500, 650);
		primaryStage.setScene(scene);
		primaryStage.show();
		

		
	}
	
	private void getCritterResults(ChoiceBox<String> critters, TextField number) {
		if (critters.getValue() != null) {
			if (number.getText()!= null && !number.getText().isEmpty()) {
				int num;
				try {
					num = Integer.parseInt(number.getText());
				}
				catch (Exception e) {
					// TO DO HERE
					return;
				}
				
				while (num > 0) {
					try {
						Critter.makeCritter(critters.getValue());
					} catch (InvalidCritterException e) {
						// will never occur
					}
					num--;
				}

			}
		}
		Critter.displayWorld(worldGrid);
	}
	
	private void getTimeResults(TextField number, CheckBox[] classes) {
		if (number.getText()!=null && !number.getText().isEmpty()) {
			int num;
			try {
				num = Integer.parseInt(number.getText());
			}
			catch (Exception e) {
				// TO DO HERE
				return;
			}
			
			while (num > 0) {
				Critter.worldTimeStep();
				num--;
			}
		}
		Critter.displayWorld(worldGrid);
		getRunStatsResults(classes);
	}
	
	private void getRunStatsResults(CheckBox[] classes) {
		for (int i = 0; i < classes.length; i++) {
			String critterName = validCritters.get(i);
			if (classes[i].isSelected()) {
				try {
					Class<?> critter = Class.forName(critterName);
				} catch (ClassNotFoundException e) {
					//will never happen
				}
				// statistics.appendText(Critter.runStats(critterName));
				statistics.appendText(critterName + "\n");
			}
		}
	}
	
	private void getRandomSeed(TextField number) {
		if (number.getText()!=null && !number.getText().isEmpty()) {
			int n;
			try{
				n = Integer.parseInt(number.getText());
			}
			catch (Exception e) {
				// TO DO 
				return;
			}
			Critter.setSeed(n);
		}
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
