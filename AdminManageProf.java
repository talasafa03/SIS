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
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;


public class AdminManageProf extends Application {
    private DBConnectivity dbConnectivity;
    private StackPane root;
    private int id;
    private Scene mainScene;
    private Stage primaryStage;

    public AdminManageProf(int id, DBConnectivity dbConnectivity) {
        this.id = id;
        this.dbConnectivity = dbConnectivity;
        dbConnectivity.Connect();
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage; 
        primaryStage.setTitle("Add Prof");

        root = new StackPane();
        mainScene = createBackgroundPane();
        root.getChildren().add(mainScene.getRoot());

        Scene scene = new Scene(root, 800, 600);

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
        rectangle.setFill(javafx.scene.paint.Color.WHITE);

        GridPane addInstructorGridPane = new GridPane();
        addInstructorGridPane.setPadding(new Insets(20, 20, 20, 20));
        addInstructorGridPane.setHgap(10);
        addInstructorGridPane.setVgap(10);
        addInstructorGridPane.setStyle("-fx-background-color: #f0f0f0;");

        Label inst_name = new Label("Instructor Name");
        inst_name.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #153faa;");

        TextField nameField = new TextField();
        nameField.setMaxWidth(200);
        Label inst_email = new Label("Email");
        inst_email.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #153faa;");

        TextField emailField = new TextField();
        emailField.setMaxWidth(200);

        Label passwordLabel = new Label("Password");
        PasswordField passwordInstField = new PasswordField();
        passwordLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #153faa;");
        passwordInstField.setMaxWidth(200);
        int factId = dbConnectivity.getFactId(id);

        System.out.println("FactID: " + factId);
        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            System.out.println("Name: " + nameField.getText());
            System.out.println("Email: " + emailField.getText());
            System.out.println("Password: " + passwordInstField.getText());
            dbConnectivity.AddInstructorToDB(nameField.getText(), emailField.getText(), passwordInstField.getText(), factId);
            root.getChildren().clear();
            root.getChildren().add(mainScene.getRoot());
        });

        addInstructorGridPane.add(inst_name, 0, 0);
        addInstructorGridPane.add(nameField, 1, 0);
        addInstructorGridPane.add(inst_email, 0, 1);
        addInstructorGridPane.add(emailField, 1, 1);
        addInstructorGridPane.add(passwordLabel, 0, 2);
        addInstructorGridPane.add(passwordInstField, 1, 2);
        addInstructorGridPane.add(saveButton, 0, 4, 2, 1);
        GridPane.setHalignment(saveButton, HPos.CENTER);

        VBox formVBox = new VBox(20);
        formVBox.getChildren().addAll(inst_name, nameField, inst_email, emailField, passwordLabel, passwordInstField, saveButton);
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
