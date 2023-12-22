package OOPProject;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminAddClasses extends Application {
    private DBConnectivity dbConnectivity;
    private StackPane root;
    private int id;
    private int c_id;
    private int crs_id;
    private Scene mainScene;
    private Stage primaryStage;

    public AdminAddClasses(int id, DBConnectivity dbConnectivity) {
        this.id = id;
        this.dbConnectivity = dbConnectivity;
        dbConnectivity.Connect();
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;  

        primaryStage.setTitle("Add Class");

        root = new StackPane();
        mainScene = createBackgroundPane();
        root.getChildren().add(mainScene.getRoot());

        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    private void addLogoutButton(StackPane backgroundPane) {
        Image logoutImage = new Image("file:C:/Users/Rayan/Downloads/TalaSafa_FatimaHashem_SIS/src/OOPProject/Photos/back.png");
        ImageView logoutImageView = new ImageView(logoutImage);
        logoutImageView.setFitHeight(30);
        logoutImageView.setPreserveRatio(true);

        Button logoutButton = new Button();
        logoutButton.setGraphic(logoutImageView);
        logoutButton.setStyle("-fx-background-color: transparent;");

        StackPane.setAlignment(logoutButton, Pos.TOP_LEFT);
        backgroundPane.getChildren().add(logoutButton);

        logoutButton.setOnAction(event -> {

        	openadminPage();
        });
    }

    private Scene createBackgroundPane() {
        StackPane backgroundPane = new StackPane();
        Image backgroundImage = new Image("file:C:/Users/Rayan/Downloads/TalaSafa_FatimaHashem_SIS/src/OOPProject/Photos/blue2.jpeg");
        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );
        Background backgroundObj = new Background(background);

        backgroundPane.setBackground(backgroundObj);

        Rectangle rectangle = new Rectangle(600, 400);
        rectangle.setArcHeight(20);
        rectangle.setArcWidth(20);
        rectangle.setFill(Color.WHITE);


        GridPane addClassGridPane = new GridPane();
        addClassGridPane.setPadding(new Insets(20, 20, 40, 20));
        addClassGridPane.setHgap(30);
        addClassGridPane.setVgap(10); 


        addClassGridPane.setAlignment(Pos.CENTER);

        DBConnectivity con = new DBConnectivity();
        con.Connect();

        String factName = con.getFactName(id);
        List<String> nameCourses = con.getCoursesNamesFromDatabase(factName);


        ComboBox<String> coursesComboBox = new ComboBox<>();
        coursesComboBox.setItems(FXCollections.observableArrayList(nameCourses));


        ComboBox<String> instructorsComboBox = new ComboBox<>();
        List<String> instructorNames = con.getInstructorsNamesFromDatabase(factName);
        instructorsComboBox.setItems(FXCollections.observableArrayList(instructorNames));


        ComboBox<String> daysComboBox = new ComboBox<>();
        daysComboBox.setItems(FXCollections.observableArrayList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"));


        coursesComboBox.setOnAction(event -> {
            String selectedCourseName = coursesComboBox.getValue();
            crs_id = con.getCourseIdByName(selectedCourseName);
        });

        instructorsComboBox.setOnAction(event -> {
            String selectedInstructorName = instructorsComboBox.getValue();
            c_id = con.getUserIdByName(selectedInstructorName);
        });

       

        TextField codeField = new TextField();
        codeField.setMaxWidth(80);

        TextField roomNBField = new TextField();
        roomNBField.setMaxWidth(80);
      
        TextField hourstart = new TextField();
        hourstart.setMaxWidth(50);
        hourstart.setPromptText("HH");

        TextField hourend = new TextField();
        hourend.setMaxWidth(50);
        hourend.setPromptText("HH");

        TextField startminuteField = new TextField();
        startminuteField.setMaxWidth(50);
        startminuteField.setPromptText("MM");

        TextField endminuteField = new TextField();
        endminuteField.setMaxWidth(50);
        endminuteField.setPromptText("MM");
        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            String roomNumberText = roomNBField.getText();
            
            
            int roomNumber = 0;

            if (!roomNumberText.isEmpty()) {
                try {
                    roomNumber = Integer.parseInt(roomNumberText);


                    String startTime = hourstart.getText() + ":" + startminuteField.getText() + ":00";
                    String endTime = hourend.getText() + ":" + endminuteField.getText() + ":00";
                    con.AddClassToDB(crs_id, codeField.getText(), roomNumber, daysComboBox.getValue(), startTime, endTime, c_id);
                } catch (NumberFormatException ex) {

                	ex.printStackTrace(); 
                }
            }
            System.out.println("Save button clicked!");
        });
        
        saveButton.setTextFill(Color.WHITE);
        saveButton.setStyle(
                "-fx-background-color: rgb(21, 95, 170); " +
                        "-fx-text-fill: WHITE; " +
                        "-fx-font-size: 14px; " +
                        "-fx-padding: 8px 16px; " +
                        "-fx-background-radius: 5px;" +
                        "-fx-min-width: 200px; "
        );

        addClassGridPane.add(createAlignedLabel("Class Code:"), 0, 0);
        addClassGridPane.add(codeField, 1, 0);
        addClassGridPane.add(createAlignedLabel("Room Number:"), 0, 1);
        addClassGridPane.add(roomNBField, 1, 1);
        addClassGridPane.add(createAlignedLabel("Instructor Name:"), 0, 2);
        addClassGridPane.add(instructorsComboBox, 1, 2);
        addClassGridPane.add(createAlignedLabel("Course:"), 0, 3);
        addClassGridPane.add(coursesComboBox, 1, 3);
        addClassGridPane.add(createAlignedLabel("Day:"), 0, 4);
        addClassGridPane.add(daysComboBox, 1, 4);
        addClassGridPane.add(createAlignedLabel("Start Time:"), 0, 5);
        addClassGridPane.add(hourstart, 1, 5);
        addClassGridPane.add(startminuteField, 2, 5);
        addClassGridPane.add(createAlignedLabel("End Time:"), 0, 6);
        addClassGridPane.add(hourend, 1, 6);
        addClassGridPane.add(endminuteField, 2, 6);
        addClassGridPane.add(saveButton, 0, 7, 2, 1);


        StackPane fullPane = new StackPane(rectangle, addClassGridPane);
        backgroundPane.getChildren().add(fullPane);

        String startTime=hourstart.getText()+":"+startminuteField.getText()+":00";
        String endTime=hourend.getText()+":"+endminuteField.getText()+":00";

        


        addLogoutButton(backgroundPane);

        return new Scene(backgroundPane);
    }

    private void openadminPage() {
        if (dbConnectivity != null) {
            int userId = dbConnectivity.UserID;
            AdminPageGUI adminPage = new AdminPageGUI(userId, dbConnectivity);

            Stage adminStage = new Stage();
            adminStage.setTitle("Admin Page");
            adminPage.start(adminStage);

            primaryStage.close(); 

        } else {

            System.out.println("Error: dbConnectivity or UserID is null.");
        }
    }


    private Label createAlignedLabel(String text) {
        Label label = new Label(text);
        label.setAlignment(Pos.CENTER_LEFT);
        label.setStyle("-fx-padding: 0 0 0 10; -fx-font-family: 'Arial';");
        return label;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
