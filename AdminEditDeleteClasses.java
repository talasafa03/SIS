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

public class AdminEditDeleteClasses extends Application {
    private DBConnectivity dbConnectivity;
    private StackPane root;
    private int c_id;
    private int crs_id;
    private int id;
    private Stage primaryStage;

    public AdminEditDeleteClasses(int id, DBConnectivity dbConnectivity) {
        this.id = id;
        this.dbConnectivity = dbConnectivity;
        dbConnectivity.Connect();
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;  

        primaryStage.setTitle("Edit Or Delete Class");

        root = new StackPane();
        Scene scene = createBackgroundPane();
        root.getChildren().add(scene.getRoot());

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

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20, 20, 40, 20));
        gridPane.setHgap(30);
        gridPane.setAlignment(Pos.CENTER);
        DBConnectivity con = new DBConnectivity();
        con.Connect();

        String factName = con.getFactName(id);
        List<String> nameCourses = con.getCoursesNamesFromDatabase(factName);

        Label courseNameLabel = new Label("Course: ");
        ComboBox<String> coursesNameOptions = new ComboBox<>();
        coursesNameOptions.setItems(FXCollections.observableArrayList(nameCourses));

        Label classCodeLabel = new Label("Class Code: ");
        ComboBox<String> classCodesOptions = new ComboBox<>();

        coursesNameOptions.setOnAction(event -> {
            String selectedCourseName = coursesNameOptions.getValue();
            crs_id = con.getCourseIdByName(selectedCourseName);
            List<String> classesCode = con.getClassesCodesFromDatabase(crs_id);
            classCodesOptions.setItems(FXCollections.observableArrayList(classesCode));
        });

        Button editCourse = new Button("Edit Class");
        editCourse.setStyle(
                "-fx-background-color: rgb(21, 95, 170); " +
                        "-fx-text-fill: WHITE; " +
                        "-fx-font-size: 14px; " +
                        "-fx-padding: 8px 16px; " +
                        "-fx-background-radius: 5px;" +
                        "-fx-min-width: 200px; "
        );
        editCourse.setOnAction(e -> SwitchToEditClass(con, classCodesOptions.getValue()));

        Button deleteCourse = new Button("Delete Class");
        deleteCourse.setStyle(
                "-fx-background-color: rgb(21, 95, 170); " +
                        "-fx-text-fill: WHITE; " +
                        "-fx-font-size: 14px; " +
                        "-fx-padding: 8px 16px; " +
                        "-fx-background-radius: 5px;" +
                        "-fx-min-width: 200px; "
        );
        Label res = new Label();
        deleteCourse.setOnAction(e -> DeleteClass(con, res, classCodesOptions, classCodesOptions.getValue()));

        gridPane.setVgap(10); 

        gridPane.add(courseNameLabel, 0, 0);
        gridPane.add(coursesNameOptions, 1, 0);
        gridPane.add(classCodeLabel, 0, 1);
        gridPane.add(classCodesOptions, 1, 1);
        gridPane.add(editCourse, 0, 2);
        gridPane.add(deleteCourse, 1, 2);

        StackPane.setAlignment(gridPane, Pos.CENTER);
        StackPane.setAlignment(rectangle, Pos.CENTER);

        StackPane fullPane = new StackPane(rectangle, gridPane);
        backgroundPane.getChildren().add(fullPane);

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
    private void SwitchToEditClass(DBConnectivity con, String code) {
        String factName = con.getFactName(id);

        Label roomNumberLabel = new Label("Room Number:");
        TextField roomNumberField = new TextField();

        List<String> instructorNames = con.getInstructorsNamesFromDatabase(factName);

        Label instructorLabel = createAlignedLabel("Instructor Name:");
        ComboBox<String> instructorOptions = new ComboBox<>();
        instructorOptions.setItems(FXCollections.observableArrayList(instructorNames));

        instructorOptions.setOnAction(event -> {
            String selectedCoordinatorName = instructorOptions.getValue();
            c_id = con.getUserIdByName(selectedCoordinatorName);
        });

        Label startTimeLabel = createAlignedLabel("Start Time:");
        TextField hourStartField = new TextField();
        hourStartField.setPromptText("HH");
        TextField minuteStartField = new TextField();
        minuteStartField.setPromptText("MM");

        Label endTimeLabel = new Label("End Time:");
        TextField hourEndField = new TextField();
        hourEndField.setPromptText("HH");
        TextField minuteEndField = new TextField();
        minuteEndField.setPromptText("MM");

        Label dayLabel = new Label("Day:");
        ComboBox<String> dayOptions = new ComboBox<>();
        dayOptions.setItems(FXCollections.observableArrayList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"));

        Button saveButton = new Button("Save");
        saveButton.setStyle(
                "-fx-background-color: rgb(21, 95, 170); " +
                        "-fx-text-fill: WHITE; " +
                        "-fx-font-size: 14px; " +
                        "-fx-padding: 8px 16px; " +
                        "-fx-background-radius: 5px;" +
                        "-fx-min-width: 200px; "
        );
        saveButton.setOnAction(e -> {
            try {
                Map<String, Object> map = con.GetClassInfo(code);

                if (!roomNumberField.getText().isEmpty()) {
                    int num = Integer.parseInt(roomNumberField.getText());
                    map.put("Room Number", num);
                }

                if (!hourStartField.getText().isEmpty() && !minuteStartField.getText().isEmpty()
                        && !hourEndField.getText().isEmpty() && !minuteEndField.getText().isEmpty()) {
                    String startTime = hourStartField.getText() + ":" + minuteStartField.getText() + ":00";
                    String endTime = hourEndField.getText() + ":" + minuteEndField.getText() + ":00";

                    LocalTime startTimeValue = LocalTime.parse(startTime);
                    LocalTime endTimeValue = LocalTime.parse(endTime);

                    map.put("Class Start Time", startTimeValue);
                    map.put("Class End Time", endTimeValue);
                }

                if (dayOptions.getValue() != null && !dayOptions.getValue().isEmpty()) {
                    String day = dayOptions.getValue();
                    map.put("Class Day", day);
                }

                if (instructorOptions.getValue() != null && !instructorOptions.getValue().isEmpty()) {
                    map.put("Instructor ID", c_id);
                }

                con.updateClassFields(map.get("Room Number"), map.get("Class Start Time"), map.get("Class End Time"), map.get("Class Day"), map.get("Instructor ID"), code);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        });
       
        
       
        
    
        GridPane editClassGridPane = new GridPane();
        editClassGridPane.setPadding(new Insets(20, 20, 40, 20));
        editClassGridPane.setHgap(10);
        editClassGridPane.setVgap(10);
        editClassGridPane.setStyle("-fx-background-color: transparent;");
        editClassGridPane.setAlignment(Pos.CENTER); 

        
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER_LEFT); 

        vbox.getChildren().addAll(
                createAlignedLabel("Room Number:"), roomNumberField,
                createAlignedLabel("Instructor Name:"), instructorOptions,
                createAlignedLabel("Day:"), dayOptions,
                createAlignedLabel("Start Time:"), hourStartField, minuteStartField,
                createAlignedLabel("End Time:"), hourEndField, minuteEndField,
                saveButton
        );


        editClassGridPane.add(vbox, 0, 0);


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

        Rectangle rectangle = new Rectangle(400, 550); 
        rectangle.setArcHeight(20);
        rectangle.setArcWidth(20);
        rectangle.setFill(Color.WHITE);


        BorderPane fullPane = new BorderPane(rectangle);


     Image logoutImage = new Image("file:C:/Users/Rayan/Downloads/TalaSafa_FatimaHashem_SIS/src/OOPProject/Photos/back.png");
     ImageView logoutImageView = new ImageView(logoutImage);
     logoutImageView.setFitHeight(30);
     logoutImageView.setPreserveRatio(true);

     Button logoutButton = new Button();
     logoutButton.setGraphic(logoutImageView);
     logoutButton.setStyle("-fx-background-color: transparent;");
         

     logoutButton.setOnAction(event -> {
         switchToMainScreen();
     });
     fullPane.setTop(logoutButton);
     fullPane.setCenter(editClassGridPane);

     root.getChildren().clear();
     root.getChildren().add(backgroundPane);
     root.getChildren().add(fullPane);




    }

    private Label createAlignedLabel(String text) {
        Label label = new Label(text);
        label.setAlignment(Pos.CENTER_LEFT);
        label.setStyle("-fx-padding: 0 0 0 10; -fx-font-family: 'Arial';"); 
        return label;
    }
    private void switchToMainScreen() {

        root.getChildren().clear();
        Scene scene = createBackgroundPane();
        root.getChildren().add(scene.getRoot());
    }
    private void DeleteClass(DBConnectivity con, Label label, ComboBox<String> comboBox, String code) {
        if (con.removeClassFromDB(code)) {
            label.setText("Deletion Done Successfully");
            comboBox.getItems().remove(comboBox.getValue());
        } else
            label.setText("Deletion Failed ");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
