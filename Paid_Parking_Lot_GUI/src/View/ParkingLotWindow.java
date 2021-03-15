package View;

import Controller.Controller;
import Domain.Car;
import Domain.CloseExeption;
import Domain.Parking_spot;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.text.Font;

import java.awt.*;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

public class ParkingLotWindow {

    public ListView<String> queueView;
    public BorderPane borderPane;
    public Button fastForwardButton;
    public Button playButton;
    public Button pauseButton;
    public Button normalSpeedButton;
    public Button saveButton;
    private Controller ctrl;
    private int playSpeed = 1;
    private Timeline timeDelay;
    private Button exitButton;
    private Stage parent;

    @FXML
    public TilePane parkingView;
    @FXML
    public Label profitLabel;
    @FXML
    public Label clockLabel;


    public void initialize(Controller ctrl, Stage parent) throws InterruptedException {
        this.ctrl = ctrl;
        this.parent = parent;

        clockLabel.setFont(new Font("", 20));
        profitLabel.setFont(new Font("", 20));
        profitLabel.setText(ctrl.getProfit());
        clockLabel.setText(ctrl.getTime());

        parkingView.setPrefColumns(10);
        parkingView.setMaxWidth(Region.USE_PREF_SIZE);
        Queue<Car> queueList = ctrl.getQueue();
        for (Car c: queueList)
        {
            queueView.getItems().add(c.toString());
        }
        ArrayList<Parking_spot> spots= ctrl.getTotalSpots();
        int col=0,row=0;
        for(Parking_spot p : spots){
            Label pLabel = new Label(p.toString());
            VBox pBox = new VBox(pLabel);
            pBox.setAlignment(Pos.CENTER);
            pBox.setPadding(new Insets(10,10,10,10));
            pBox.setMinWidth(120);
            pBox.setMinHeight(150);
            pBox.setBackground(new Background(new BackgroundFill(Color.rgb(0,255,0), new CornerRadii(10), new Insets(10,10,10,10))));
            parkingView.getChildren().add(pBox);
        }

        playButton.setOnAction(e -> setSpeed(0));
        pauseButton.setOnAction(e -> setSpeed(-1));
        fastForwardButton.setOnAction(e -> setSpeed(this.playSpeed * 2));
        normalSpeedButton.setOnAction(e -> setSpeed(1));
        saveButton.setOnAction(e -> ctrl.saveToFile());

        update();
        //startSim();
        //nextButton.setOnAction(e -> startSim());
        timeDelay = new Timeline(new KeyFrame(Duration.seconds(1), (ActionEvent event1) -> {
            startSim();
        }));
        timeDelay.setCycleCount(Timeline.INDEFINITE);
        timeDelay.playFromStart();

        exitButton = new Button("Close");
    }

    public Button getExitButton(){
        return this.exitButton;
    }

    private void setSpeed(int newSpeed){
        if(newSpeed == -1){
            timeDelay.pause();
            return;
        }
        if(newSpeed == 0){
            timeDelay.play();
            return;
        }
        this.playSpeed = newSpeed;
        timeDelay.setRate(playSpeed);
    }

    private void update(){
        clockLabel.setFont(new Font("", 17));
        profitLabel.setFont(new Font("", 17));
        profitLabel.setText(ctrl.getProfit());
        clockLabel.setText(ctrl.getTime());
        ArrayList<Parking_spot> spots= ctrl.getTotalSpots();
        parkingView.getChildren().clear();
        queueView.getItems().clear();
        Queue<Car> queueList = ctrl.getQueue();
        for (Car c: queueList)
        {
            queueView.getItems().add(c.toString());
        }
        for(Parking_spot p : spots){
            Label pLabel = new Label(p.toString());
            VBox pBox = new VBox(pLabel);
            pBox.setAlignment(Pos.CENTER);
            pBox.setPadding(new Insets(10,10,10,10));
            pBox.setMinWidth(120);
            pBox.setMinHeight(150);
            if(p.isAvailable()){
                pBox.setBackground(new Background(new BackgroundFill(Color.rgb(0,255,0), new CornerRadii(10), new Insets(10,10,10,10))));
            }
            else{
                pBox.setBackground(new Background(new BackgroundFill(Color.rgb(255,0,0), new CornerRadii(10), new Insets(10,10,10,10))));
            }
            parkingView.getChildren().add(pBox);
        }
    }

    public void startSim() {
        update();
        try{
            ctrl.simulateNextStep();
        }
        catch (CloseExeption e){
            update();
            timeDelay.stop();
            //pop-up
            endWindow(ctrl.getProfit());
        }
    }

    public void endWindow(String profit) {

        Label secondLabel = new Label("The parking is closed");
        Label label1 = new Label("Total profit: " + profit + " RON");
        VBox box = new VBox(secondLabel,label1);
        box.getChildren().add(exitButton);
        box.setAlignment(Pos.CENTER);

        StackPane secondaryLayout = new StackPane();
        secondaryLayout.getChildren().add(box);

        Scene secondScene = new Scene(secondaryLayout, 230, 100);

        // New window (Stage)
        Stage newWindow = new Stage();
        newWindow.setTitle("End of the day");
        newWindow.setScene(secondScene);

        newWindow.initOwner(parent);
        // Specifies the modality for new window.
        newWindow.initModality(Modality.WINDOW_MODAL);
        newWindow.show();

    }
}
