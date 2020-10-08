package by.peekhovsky.tsosi.lab1;

import java.net.URL;
import java.util.Objects;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class UIApplication extends Application {

  @Override
  public void start(Stage stage) throws Exception {
    URL rootURL = Objects.requireNonNull(getClass().getClassLoader().getResource("ui.fxml"));
    URL styleURL = Objects.requireNonNull(getClass().getClassLoader().getResource("styles.css"));

    Parent root = FXMLLoader.load(rootURL);

    Scene scene = new Scene(root);
    scene.getStylesheets().add(styleURL.toExternalForm());

    stage.setTitle("TSOSI Lab1");
    stage.setScene(scene);
    stage.setResizable(false);
    stage.show();
  }

  public static void launchFX(String[] args) {
    launch(args);
  }
}
