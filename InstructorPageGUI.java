package OOPProject;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Button;

import javafx.stage.Stage;

public class InstructorPageGUI extends Application {
    private DBConnectivity dbConnectivity;
    private Stage primaryStage;
    private int id;

    public InstructorPageGUI(int id, DBConnectivity dbConnectivity) {
        this.id = id;
        this.dbConnectivity = dbConnectivity;
    }
    public InstructorPageGUI() { }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage; 
        primaryStage.setTitle("Instructor Portal");

        StackPane adminPane = createBackgroundPane();
        Scene scene = new Scene(adminPane, 800, 600);

        primaryStage.setScene(scene);
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

        	openLoginPage(primaryStage);
        });
    }
    private StackPane createBackgroundPane() {

    	Image backgroundImage = new Image("file:C:/Users/Rayan/Downloads/TalaSafa_FatimaHashem_SIS/src/OOPProject/Photos/blue2.jpeg");

        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );
        Background backgroundObj = new Background(background);

        StackPane backgroundPane = new StackPane();
        backgroundPane.setAlignment(Pos.CENTER);
        backgroundPane.setBackground(backgroundObj);

        Label titleLabel = new Label("Professor Portal");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: DODGERBLUE; -fx-font-family: 'Times New Roman';");
        titleLabel.setPadding(new Insets(120, 0, 0, 0)); 


        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(30);
        gridPane.setVgap(20);

        createAndAddRectangle(gridPane, "Add Grades", "file:C:/Users/Rayan/Downloads/TalaSafa_FatimaHashem_SIS/src/OOPProject/Photos/grading.png", 0, 0, () -> openAddGradesPage(primaryStage,dbConnectivity));
        createAndAddRectangle(gridPane, "View Assigned Courses", "file:C:/Users/Rayan/Downloads/TalaSafa_FatimaHashem_SIS/src/OOPProject/Photos/schedule.png", 1, 0, () -> openViewSchedule(primaryStage));
        createAndAddRectangle(gridPane, "Take Attendance", "file:C:/Users/Rayan/Downloads/TalaSafa_FatimaHashem_SIS/src/OOPProject/Photos/attendance.png", 1, 1, () -> openTakeAttendance(primaryStage,dbConnectivity));

        StackPane overlayPane = new StackPane(gridPane, titleLabel);
        StackPane.setAlignment(titleLabel, Pos.TOP_CENTER);

        backgroundPane.getChildren().add(overlayPane);
        addLogoutButton(backgroundPane); 
        return backgroundPane;
    }

    private void createAndAddRectangle(GridPane gridPane, String labelText, String imagePath, int col, int row, Runnable onClick) {
        Rectangle rectangle = new Rectangle(150, 100);
        rectangle.setArcHeight(20);
        rectangle.setArcWidth(20);
        rectangle.setFill(Color.CORNFLOWERBLUE);

        StackPane stackPane = new StackPane();

        VBox contentVBox = new VBox(5);
        contentVBox.setAlignment(Pos.CENTER);

        try {

            Image logoImage = new Image(imagePath);
            ImageView imageView = new ImageView(logoImage);
            imageView.setFitWidth(50);
            imageView.setFitHeight(50);
            imageView.setPreserveRatio(true);

            Label label = new Label(labelText);
            label.setTextFill(Color.WHITE);

            contentVBox.getChildren().addAll(imageView, label);

            StackPane.setAlignment(contentVBox, Pos.CENTER);

            stackPane.getChildren().addAll(rectangle, contentVBox);
        } catch (Exception ex) {
            System.out.println("Error loading image: " + ex.getMessage());
        }

        stackPane.setUserData(contentVBox);

        gridPane.add(stackPane, col, row);
        stackPane.setOnMouseClicked(event -> onClick.run());

        stackPane.setOnMouseEntered(e -> rectangle.setFill(Color.LIGHTSKYBLUE));
        stackPane.setOnMouseExited(e -> rectangle.setFill(Color.CORNFLOWERBLUE));
    }


    private void openLoginPage(Stage primaryStage) {
        LoginGUI loginGui = new LoginGUI();
        loginGui.start(primaryStage);
    }
    private void openAddGradesPage(Stage primaryStage,DBConnectivity db) {
    	ProfAddGrades PROFADDGR = new ProfAddGrades(id, primaryStage,db);

        try {
        	PROFADDGR.start(new Stage()); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openViewSchedule(Stage primaryStage) {
        DBConnectivity con = new DBConnectivity();
        con.Connect();

        Label resultLabel = new Label(); 
        ProfViewCourses assignedcourses = new ProfViewCourses(id, primaryStage);

        try {
        	assignedcourses.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void openTakeAttendance(Stage primaryStage,DBConnectivity db) {
    	ProfTakeAttendance studentatt = new ProfTakeAttendance(id, primaryStage,db);
        Stage courseHistoryStage = new Stage();
        try {
        	studentatt.start(courseHistoryStage);
		} catch (Exception e) {
			e.printStackTrace();
		}

    }




    public static void main(String[] args) {
        launch(args);
    }
}


