package com.denissomfalean.polynomialcalculator.views;

import static com.denissomfalean.polynomialcalculator.constants.CalculatorConstants.*;
import static com.denissomfalean.polynomialcalculator.utils.CalculatorUtils.getResourceFileURL;

import com.denissomfalean.polynomialcalculator.controllers.CalculatorController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CalculatorView extends Application {
  public static void run() {
    launch();
  }

  @Override
  public void start(Stage stage) throws Exception {
    FXMLLoader fxmlLoader = new FXMLLoader(getResourceFileURL(RESOURCE_PATH_CALCULATOR_WINDOW_GUI));

    Scene scene = new Scene(fxmlLoader.load(), 561, 623);
    scene.setFill(Color.TRANSPARENT);

    Image image = new Image(getResourceFileURL(APPLICATION_ICON_IMAGE_PATH).toString());
    stage.getIcons().add(image);

    stage.setResizable(false);
    stage.setScene(scene);
    stage.initStyle(StageStyle.TRANSPARENT);
    stage.setTitle(POLYNOMIAL_CALCULATOR_TITLE);
    stage.show();

    ((CalculatorController) fxmlLoader.getController()).init(stage);
  }
}
