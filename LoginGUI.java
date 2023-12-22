package OOPProject;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class LoginGUI extends Application {
    private DBConnectivity dbConnectivity;
    private Stage primaryStage;

    public LoginGUI() {
        this.dbConnectivity = new DBConnectivity();
        dbConnectivity.Connect();
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Login Page");

        StackPane rootPane = createRootPane();
        addUIControls(rootPane);

        Scene scene = new Scene(rootPane, 1000, 600);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private StackPane createRootPane() {
        StackPane rootPane = new StackPane();
        rootPane.setId("rootPane");
        return rootPane;
    }

    private void addUIControls(StackPane rootPane) {
        StackPane backgroundPane = createBackgroundPane();
        GridPane loginFormPane = createLoginFormPane(dbConnectivity);

        rootPane.getChildren().addAll(backgroundPane, loginFormPane);
        StackPane.setAlignment(loginFormPane, Pos.CENTER);
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

        return backgroundPane;
    }

    private GridPane createLoginFormPane(DBConnectivity dbConnectivity) {
        GridPane gridPane = new GridPane();
        gridPane.setId("loginFormPane");
        gridPane.setPadding(new Insets(40, 40, 40, 40));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setStyle("-fx-background-color: transparent;");

        Rectangle rectangle = new Rectangle(400, 400);
        rectangle.setArcHeight(20);
        rectangle.setArcWidth(20);
        rectangle.setFill(Color.WHITE);

        Image ualogoo = new Image("file:C:/Users/Rayan/Downloads/TalaSafa_FatimaHashem_SIS/src/OOPProject/Photos/UALOGO1.png");
        ImageView logoImageView = new ImageView(ualogoo);
        logoImageView.setFitHeight(50);
        logoImageView.setPreserveRatio(true);

        Label loginLabel2 = new Label("UNIVERSITY ANTONINE");
        loginLabel2.setTextFill(Color.rgb(21, 95, 170));
        loginLabel2.setFont(Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 24));
        Label loginLabel = new Label("Log In");
        loginLabel.setFont(Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 20));
        loginLabel.setTextFill(Color.rgb(21, 95, 170));

        TextField usernameTextField = new TextField();
        PasswordField passwordField = new PasswordField();

        usernameTextField.setPromptText("Email");
        passwordField.setPromptText("Password");

        usernameTextField.setStyle(
                "-fx-font-family: 'Arial'; " +
                        "-fx-font-size: 12px; " +
                        "-fx-text-fill: BLACK;"
        );
        passwordField.setStyle(
                "-fx-font-family: 'Arial'; " +
                        "-fx-font-size: 12px; " +
                        "-fx-text-fill: BLACK;"
        );
        usernameTextField.setEditable(true);
        passwordField.setEditable(true);
        usernameTextField.setMaxWidth(200);
        passwordField.setMaxWidth(200);

        Button signInButton = new Button("Sign in");
        signInButton.setTextFill(Color.WHITE);

        signInButton.setStyle(
                "-fx-background-color: rgb(21, 95, 170); " +
                        "-fx-text-fill: WHITE; " +
                        "-fx-font-size: 14px; " +
                        "-fx-padding: 8px 16px; " +
                        "-fx-background-radius: 5px;" +
                        "-fx-min-width: 200px; "
        );

        HBox buttonBox = new HBox(30);
        buttonBox.getChildren().addAll(signInButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox formVBox = new VBox(20);
        formVBox.getChildren().addAll(logoImageView, loginLabel2, loginLabel, usernameTextField, passwordField, buttonBox);
        formVBox.setAlignment(Pos.CENTER);
        StackPane fullLoginFormPane = new StackPane(rectangle, formVBox);
        StackPane.setAlignment(formVBox, Pos.CENTER);

        gridPane.add(fullLoginFormPane, 0, 0);
        GridPane.setHalignment(fullLoginFormPane, HPos.CENTER);

        gridPane.setAlignment(Pos.CENTER);

        signInButton.setOnAction(e -> {
            String email = usernameTextField.getText();
            String pass = passwordField.getText();
            String role = dbConnectivity.authenticate(email, pass);

            if (role.equals("admin")) {
                System.out.println("Redirecting to admin page");
                int userId = dbConnectivity.UserID;
                AdminPageGUI adminGui = new AdminPageGUI(userId, dbConnectivity);

                openAdminPage(primaryStage);
            } else if (role.equals("user")) {
                System.out.println("Redirecting to professor page");
                int userId = dbConnectivity.UserID;
                InstructorPageGUI adminGui = new InstructorPageGUI(userId, dbConnectivity);

                openInstructorPage(primaryStage);
            } else if (role.equals("student")) {
                System.out.println("Redirecting to user page");
                int userId = dbConnectivity.UserID;
                StudentPageGUI studentgui = new StudentPageGUI(userId, dbConnectivity);

                openStudentPage(primaryStage);
            }
        });

        return gridPane;
    }
    private void openAdminPage(Stage loginStage) {
        int userId = dbConnectivity.UserID;
        AdminPageGUI adminPage = new AdminPageGUI(userId, dbConnectivity);
        Stage adminStage = new Stage();
        adminPage.start(adminStage);

        loginStage.close();
    }
    private void openStudentPage(Stage loginStage) {
        int userId = dbConnectivity.UserID;
        StudentPageGUI studentgui = new StudentPageGUI(userId, dbConnectivity);
        Stage adminStage = new Stage();
        studentgui.start(adminStage);

        loginStage.close();
    }
    private void openInstructorPage(Stage loginStage) {
        int userId = dbConnectivity.UserID;
        InstructorPageGUI INSTgui = new InstructorPageGUI(userId, dbConnectivity);
        Stage adminStage = new Stage();
        INSTgui.start(adminStage);

        loginStage.close();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
