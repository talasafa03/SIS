package OOPProject;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class AdminPageGUI extends Application {
    private DBConnectivity dbConnectivity;
    private Stage primaryStage;

    private int id;

    public AdminPageGUI(int id, DBConnectivity dbConnectivity) {
        this.id = id;
        this.dbConnectivity = dbConnectivity;
    }

    public AdminPageGUI() { }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage; 
        primaryStage.setTitle("Admin Portal");

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


        Label titleLabel = new Label("Admin Portal");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: DODGERBLUE; -fx-font-family: 'Times New Roman';");
        titleLabel.setPadding(new Insets(120, 0, 0, 0));



        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(30);
        gridPane.setVgap(20);

        createAndAddRectangle(gridPane, "Manage Students", "file:C:/Users/Rayan/Downloads/TalaSafa_FatimaHashem_SIS/src/OOPProject/Photos/std3.png", 0, 0, () -> openAddStudentsPage(primaryStage));
        createAndAddRectangle(gridPane, "Manage Professors", "file:C:/Users/Rayan/Downloads/TalaSafa_FatimaHashem_SIS/src/OOPProject/Photos/Prof1.png", 1, 0, () -> openManageProf(primaryStage));
        createAndAddRectangle(gridPane, "Add Courses", "file:C:/Users/Rayan/Downloads/TalaSafa_FatimaHashem_SIS/src/OOPProject/Photos/courses.png", 2, 0, () -> openAddcourses(primaryStage));
        createAndAddRectangle(gridPane, "Edit and Delete Courses", "file:C:/Users/Rayan/Downloads/TalaSafa_FatimaHashem_SIS/src/OOPProject/Photos/managingcourses.png", 0, 1, () -> openeditdeletecourses(primaryStage));
        createAndAddRectangle(gridPane, "Add Classes", "file:C:/Users/Rayan/Downloads/TalaSafa_FatimaHashem_SIS/src/OOPProject/Photos/manage.png", 1, 1, () -> openAddclasses(primaryStage));
        createAndAddRectangle(gridPane, "Edit and Delete Classes", "file:C:/Users/Rayan/Downloads/TalaSafa_FatimaHashem_SIS/src/OOPProject/Photos/courses2.png", 2, 1, () -> openeditdeleteclasses(primaryStage));


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

        stackPane.setOnMouseEntered(e -> rectangle.setFill(Color.LIGHTSKYBLUE));
        stackPane.setOnMouseExited(e -> rectangle.setFill(Color.CORNFLOWERBLUE));

        stackPane.setOnMouseClicked(event -> onClick.run());
    }
    private void openLoginPage(Stage primaryStage) {
        LoginGUI loginGui = new LoginGUI();
        loginGui.start(primaryStage);
    }
    private void openAddStudentsPage(Stage primaryStage) {
        AdminAddStudentPage adminstudentPage = new AdminAddStudentPage(id, dbConnectivity);
        Stage adminstudentStage = new Stage();
        adminstudentPage.start(adminstudentStage);

        primaryStage.close();
    }

    private void openManageProf(Stage primaryStage) {
        AdminManageProf adminprofPage = new AdminManageProf(id,dbConnectivity);
        Stage adminprofStage = new Stage();
        adminprofPage.start(adminprofStage);

        primaryStage.close();
    }

    private void openAddcourses(Stage primaryStage) {
        AdminAddcourses admincoursePage = new AdminAddcourses(id, dbConnectivity);
        Stage admincourseStage = new Stage();
        admincoursePage.start(admincourseStage);

        primaryStage.close();
    }

    private void openeditdeletecourses(Stage primaryStage) {
        AdminEditDeleteCourse admineditcoursePage = new AdminEditDeleteCourse(id,dbConnectivity);
        Stage admineditcourseStage = new Stage();
        admineditcoursePage.start(admineditcourseStage);

        primaryStage.close();
    }

    private void openAddclasses(Stage primaryStage) {
        AdminAddClasses adminclassPage = new AdminAddClasses(id,dbConnectivity);
        Stage adminclassStage = new Stage();
        adminclassPage.start(adminclassStage);

        primaryStage.close();
    }

    private void openeditdeleteclasses(Stage primaryStage) {
        AdminEditDeleteClasses admineditdelclassPage = new AdminEditDeleteClasses(id,dbConnectivity);
        Stage admineditdelclassStage = new Stage();
        admineditdelclassPage.start(admineditdelclassStage);

        primaryStage.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
