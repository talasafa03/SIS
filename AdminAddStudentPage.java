package OOPProject;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;
import java.util.ArrayList;
import java.util.List;
public class AdminAddStudentPage extends Application {
    private DBConnectivity dbConnectivity;
    private StackPane root;
    private int id;
    private Stage primaryStage;

    public AdminAddStudentPage(int id, DBConnectivity dbConnectivity) {
        this.id = id;
        this.dbConnectivity = dbConnectivity;
        dbConnectivity.Connect();
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;  

        primaryStage.setTitle("Add Student");

        root = new StackPane();
        Scene mainScene = createBackgroundPane(); 

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

        GridPane addStudentGridPane = new GridPane();
        addStudentGridPane.setPadding(new Insets(20, 20, 20, 20));
        addStudentGridPane.setHgap(10);
        addStudentGridPane.setVgap(10);
        addStudentGridPane.setStyle("-fx-background-color: transparent;");

        int Factid=dbConnectivity.getFactId(id) ;

        Label std_name = new Label("Student Name");
        std_name.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #153faa;");
        TextField nameField = new TextField();

        Label std_email = new Label("Email");
        std_email.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #153faa;");
        TextField emailField = new TextField();

        Label passwordLabel = new Label("Password");
        passwordLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #153faa;");
        PasswordField passwordStdField = new PasswordField();

        nameField.setMaxWidth(200);
        emailField.setMaxWidth(200);
        passwordStdField.setMaxWidth(200);
        
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
            System.out.println("Name: " + nameField.getText());
            System.out.println("Email: " + emailField.getText());
            System.out.println("Password: " + passwordStdField.getText());
            dbConnectivity.AddStudentToDB(nameField.getText(), emailField.getText(), passwordStdField.getText(), Factid);


            nameField.clear();
            emailField.clear();
            passwordStdField.clear();
        });

        addStudentGridPane.add(std_name, 0, 0);
        addStudentGridPane.add(nameField, 1, 0);
        addStudentGridPane.add(std_email, 0, 1);
        addStudentGridPane.add(emailField, 1, 1);
        addStudentGridPane.add(passwordLabel, 0, 2);
        addStudentGridPane.add(passwordStdField, 1, 2);
        addStudentGridPane.add(saveButton, 0, 4, 2, 1);
        GridPane.setHalignment(saveButton, HPos.CENTER);

        VBox formVBox = new VBox(20);
        formVBox.getChildren().addAll(std_name, nameField, std_email, emailField, passwordLabel, passwordStdField, saveButton);
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
