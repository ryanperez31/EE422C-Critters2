package assignment5;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class CritterMenu extends Application{
	
	private static String myPackage;
	
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		launch(args);
	}

	public void start(Stage primaryStage) {
		ComboBox<Class> cmb = new ComboBox<Class>();
		GridPane gp = new GridPane();
		Class[] arr;
		List<Class> validCritters = new ArrayList<Class>();

		try {
			arr = getClasses(myPackage);
			for(int i = 0; i < arr.length; i++) {
				if((Critter.class.isAssignableFrom(arr[i])) && (!Modifier.isAbstract(arr[i].getModifiers()))) {
					System.out.println("true");
					validCritters.add(arr[i]);
				}
			}
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}

		cmb.getItems().addAll(validCritters);
		gp.add(cmb,0,0);
		primaryStage.setScene(new Scene(gp, 300, 250));
		primaryStage.show();
	}




	private static Class[] getClasses(String packageName)
			throws ClassNotFoundException, IOException {
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
			} else if (file.getName().endsWith(".class")) {
				classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
			}
		}
		return classes;
	}
}
