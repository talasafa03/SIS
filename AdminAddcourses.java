package OOPProject;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.ArrayList;
import java.util.List;

public class AdminAddcourses extends Application {
	
    private DBConnectivity dbConnectivity;
    private StackPane root;
    private Scene mainScene;
    private int c_id;
	private int id;
    private Stage primaryStage;


	public AdminAddcourses(int id, DBConnectivity dbConnectivity) {
	    this.id = id;
	    this.dbConnectivity = dbConnectivity; 
	}



    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;  

        primaryStage.setTitle("Add Course");

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

        Rectangle rectangle = new Rectangle(400, 400);
        rectangle.setArcHeight(20);
        rectangle.setArcWidth(20);
        rectangle.setFill(Color.WHITE);

        GridPane addCourseGridPane = new GridPane();
        addCourseGridPane.setPadding(new Insets(20, 20, 40, 20)); 
        addCourseGridPane.setHgap(10);
        addCourseGridPane.setVgap(10);
        addCourseGridPane.setStyle("-fx-background-color: transparent;");

        Label crs_name = new Label("Course Name");
        crs_name.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #153faa;");
        TextField nameField = new TextField();

        Label crs_creds = new Label("Course Credits");
        crs_creds.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #153faa;");
        TextField creditsField = new TextField();
        creditsField.setMaxWidth(200);
        nameField.setMaxWidth(200);

        int factID = dbConnectivity.getFactId(id);
        System.out.println("FactID: " + factID);
        String factName = dbConnectivity.getFactName(id);

        Label facultyName = new Label("Faculty Name");
        facultyName.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #153faa;");
        Label factNameLabel = new Label(factName);
        factNameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: normal; -fx-text-fill: BLACK;");

        List<String> nameProfs = new ArrayList<>();
        nameProfs = dbConnectivity.getInstructorsNamesFromDatabase(factName);

        Label coordinatorName = new Label("Coordinator Name");
        coordinatorName.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #153faa;");
        ComboBox<String> coordinatorNameOptions = new ComboBox<>();
        coordinatorNameOptions.setItems(FXCollections.observableArrayList(nameProfs));

        coordinatorNameOptions.setOnAction(event -> {
            String selectedCoordinatorName = coordinatorNameOptions.getValue();
            c_id = dbConnectivity.getUserIdByName(selectedCoordinatorName);
        });

        Button saveButton = new Button("Save");
        saveButton.setTextFill(Color.WHITE);
        saveButton.setStyle(
                "-fx-background-color: rgb(21, 95, 170); " +
                        "-fx-text-fill: WHITE; " +
                        "-fx-font-size: 14px; " +
                        "-fx-padding: 8px 16px; " +
                        "-fx-background-radius: 5px;" +
                        "-fx-min-width: 200px; "
        );

        saveButton.setOnAction(e -> {
            System.out.println("Course Name: " + nameField.getText());
            System.out.println("Credits: " + creditsField.getText());
            System.out.println("Faculty Name: " + factNameLabel.getText());
            System.out.println("Coordinator Name: " + coordinatorNameOptions.getValue());

            dbConnectivity.AddCourseToDB(nameField.getText(), Integer.parseInt(creditsField.getText()), factID, c_id);

            root.getChildren().clear();
            root.getChildren().add(mainScene.getRoot());
        });

        addCourseGridPane.add(crs_name, 0, 0);
        addCourseGridPane.add(nameField, 1, 0);
        addCourseGridPane.add(crs_creds, 0, 1);
        addCourseGridPane.add(creditsField, 1, 1);
        addCourseGridPane.add(facultyName, 0, 2);
        addCourseGridPane.add(factNameLabel, 1, 2);
        addCourseGridPane.add(coordinatorName, 0, 3);
        addCourseGridPane.add(coordinatorNameOptions, 1, 3);
        addCourseGridPane.add(saveButton, 0, 4, 2, 1);
        GridPane.setHalignment(saveButton, HPos.CENTER);

        VBox formVBox = new VBox(20);
        formVBox.getChildren().addAll(crs_name, nameField, crs_creds, creditsField, facultyName, factNameLabel, coordinatorName, coordinatorNameOptions, saveButton);
        formVBox.setAlignment(Pos.CENTER);

        StackPane fullPane = new StackPane(rectangle, formVBox);
        StackPane.setAlignment(formVBox, Pos.CENTER);

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

    public static void main(String[] args) {
        launch(args);
    }
}
