package View;

import Controller.Controller;
import Domain.CloseExeption;
import Domain.Parking_lot;
import Repository.Repository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

public class Main extends Application {
    private Stage window;
    private Scene parkingScene;
    private ParkingLotWindow parkingController;
    private Controller ctrl;

    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ParkingLotWindow.fxml"));
        parkingScene = new Scene(loader.load(), 1500, 800);
        parkingController = loader.getController();

        Repository repo = new Repository(new Parking_lot());

        Stage popUpStage = new Stage();
        Button yesButton = new Button("Load");
        Button noButton = new Button("Start new day");
        Label saveText = new Label("Load the last save?");
        HBox hBox = new HBox(yesButton, noButton);
        VBox vBox = new VBox(saveText,hBox);
        vBox.setAlignment(Pos.CENTER);
        hBox.setAlignment(Pos.CENTER);
        Scene popUpScene = new Scene(vBox);
        yesButton.setOnAction(e -> {repo.getFromFile(); popUpStage.close();});
        noButton.setOnAction(e -> {popUpStage.close();});
        popUpStage.setTitle("File loader");
        popUpStage.setScene(popUpScene);
        popUpStage.setMinHeight(200);
        popUpStage.setMinWidth(300);
        popUpStage.showAndWait();

        this.ctrl = new Controller(repo);
        parkingController.initialize(ctrl, window);

        Button exit = parkingController.getExitButton();
        exit.setOnAction(e -> window.close());

        window.setTitle("GUI");
        window.setScene(parkingScene);
        window.show();

        parkingController.startSim();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
