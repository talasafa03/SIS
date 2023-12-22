package OOPProject;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

import java.util.ArrayList;
import java.util.List;

public class AdminEditDeleteCourse extends Application {
    private DBConnectivity dbConnectivity;
    private StackPane root;
    private int c_id;
    private int crs_id;
    private int id;
    private Stage primaryStage;

    public AdminEditDeleteCourse(int id, DBConnectivity dbConnectivity) {
        this.id = id;
        this.dbConnectivity = dbConnectivity;
        dbConnectivity.Connect();
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;  

        primaryStage.setTitle("Edit Or Delete Course");

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
        Button viewAllCoursesButton = new Button("View All Courses");
        viewAllCoursesButton.setOnAction(e -> redirectToAllClassesPage());


        String factName = dbConnectivity.getFactName(id);
        List<String> nameCourses = dbConnectivity.getCoursesNamesFromDatabase(factName);

        gridPane.add(viewAllCoursesButton, 0, 0);
        Button editCourse = new Button("Edit Course");
        editCourse.setStyle(
                "-fx-background-color: rgb(21, 95, 170); " +
                        "-fx-text-fill: WHITE; " +
                        "-fx-font-size: 14px; " +
                        "-fx-padding: 8px 16px; " +
                        "-fx-background-radius: 5px;" +
                        "-fx-min-width: 200px; "
        );
        editCourse.setOnAction(e -> SwitchToEditCourse(dbConnectivity, factName));


        Label CourseName = new Label("Course: ");
        CourseName.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #153faa;");

        ComboBox<String> CoursesNameOptions = new ComboBox<>();
        CoursesNameOptions.setItems(FXCollections.observableArrayList(nameCourses));

        CoursesNameOptions.setOnAction(event -> {
            String selectedCourseName = CoursesNameOptions.getValue();
            crs_id = dbConnectivity.getCourseIdByName(selectedCourseName);
        });


        Button deleteCourse = new Button("Delete Courses");
        deleteCourse.setStyle(
                "-fx-background-color: rgb(21, 95, 170); " +
                        "-fx-text-fill: WHITE; " +
                        "-fx-font-size: 14px; " +
                        "-fx-padding: 8px 16px; " +
                        "-fx-background-radius: 5px;" +
                        "-fx-min-width: 200px; "
        );
        Label res=new Label();
        deleteCourse.setOnAction(e -> DeleteCourse(dbConnectivity, res, CoursesNameOptions)); 

        gridPane.setVgap(10); 
        gridPane.add(CourseName, 1, 0);
        gridPane.add(CoursesNameOptions, 1, 1);
        gridPane.add(editCourse, 1, 2);
        gridPane.add(deleteCourse, 1, 3);

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
    private void SwitchToEditCourse(DBConnectivity con, String factName) {
        List<String> updatedFields = new ArrayList<>();

        Label creditsLabel = new Label("Credits");
        TextField creditsField = new TextField();

        List<String> nameProfs = con.getInstructorsNamesFromDatabase(factName);

        Label coordinatorName = new Label("Coordinator Name");
        ComboBox<String> coordinatorNameOptions = new ComboBox<>();
        coordinatorNameOptions.setItems(FXCollections.observableArrayList(nameProfs));

        coordinatorNameOptions.setOnAction(event -> {
            String selectedCoordinatorName = coordinatorNameOptions.getValue();
            updatedFields.add(coordinatorNameOptions.getValue());
            c_id = con.getUserIdByName(selectedCoordinatorName);
        });

        Button save = new Button("Save");
        save.setStyle(
            "-fx-background-color: rgb(21, 95, 170); " +
            "-fx-text-fill: WHITE; " +
            "-fx-font-size: 14px; " +
            "-fx-padding: 8px 16px; " +
            "-fx-background-radius: 5px;" +
            "-fx-min-width: 200px; "
        );
        save.setOnAction(e -> {
            updatedFields.add(creditsField.getText());
            System.out.print(updatedFields);
    	    if (updatedFields.size() == 2) {
    	        if (!creditsField.getText().isEmpty()) {
    		        System.out.print("Both");
    	            con.updateCourseBothFields(crs_id, c_id, Integer.parseInt(creditsField.getText()));
    	        } else {
    		        System.out.print("Coordinator");
    		        con.updateCourseCoordinatorFields(crs_id, c_id);
    	        }updatedFields.clear();
    	    } else if (!updatedFields.isEmpty()) {
    	        System.out.print("Creds");
    	          con.updateCourseCreditFields(crs_id, Integer.parseInt(creditsField.getText()));
    	          updatedFields.clear();
    	    }

        });

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
        gridPane.setVgap(10);

        gridPane.add(creditsLabel, 0, 1);
        gridPane.add(creditsField, 1, 1);
        gridPane.add(coordinatorName, 0, 2);
        gridPane.add(coordinatorNameOptions, 1, 2);
        gridPane.add(save, 1, 3);

        StackPane.setAlignment(gridPane, Pos.CENTER);
        StackPane.setAlignment(rectangle, Pos.CENTER);

        StackPane fullPane = new StackPane(rectangle, gridPane);
        
        backgroundPane.getChildren().add(fullPane);
        addLogoutButton(backgroundPane);

        root.getChildren().clear();
        root.getChildren().add(backgroundPane);
    }
    
    private void DeleteCourse(DBConnectivity con, Label label, ComboBox<String> comboBox ) {
        if (con.removeCourseFromDB(crs_id)) {
            label.setText("Deletion Done Successfully");
            comboBox.getItems().remove(comboBox.getValue());
        } else
            label.setText("Deletion Failed ");
    }

    private void redirectToAllClassesPage() {
        DBConnectivity con = new DBConnectivity();
        con.Connect();

        int factID = con.getFactId(id);

        Stage stage = new Stage();
        stage.setTitle("Course List");

        VBox vBox = new VBox();
        Scene scene = new Scene(vBox, 500, 500);

        ListView<HBox> courseListView = new ListView<>();
        courseListView.setPrefHeight(550);
        List<Course> courses = con.getAllCoursesByFaculty(factID);

        ObservableList<HBox> courseItems = FXCollections.observableArrayList();

        for (Course crs : courses) {
            HBox courseBox = new HBox(10);

            Label label = new Label("Course Name: " + crs.getCourseName()
                    + "\n\tCredits: " + crs.getCredits()
                    + "\n\tCoordinator: " + crs.getCoordinatorName());
            Button viewClassesButton = new Button("View Classes");

            viewClassesButton.setOnAction(event -> viewClasses(crs.getCourseId()));

            courseBox.getChildren().addAll(label, viewClassesButton);

            courseItems.add(courseBox);
            vBox.getChildren().add(courseBox);
        }

        courseListView.setItems(courseItems);
        vBox.getChildren().add(courseListView);

        stage.setScene(scene);
        stage.show();
    }

    private void viewClasses(int courseId) {
        Stage stage = new Stage();
        stage.setTitle("Class List");

        VBox vBox = new VBox();
        Scene scene = new Scene(vBox, 400, 600);
        ListView<HBox> classListView = new ListView<>();
        classListView.setPrefHeight(550);
        DBConnectivity con = new DBConnectivity();
        con.Connect();
        List<ClassCourse> classes = con.getAllClassesByCourse(courseId);

        ObservableList<HBox> classItems = FXCollections.observableArrayList();
        for (ClassCourse crs : classes) {
            HBox classBox = new HBox(10);
            Label classInfoLabel = new Label("Course Name: " + crs.getCrsName() +
                    "\n\tClass Code: " + crs.getClassCode() +
                    "\n\tDay: " + crs.getDay() +
                    "\n\tStart Time: " + crs.getStartTime() +
                    "\n\tEnd Time: " + crs.getEndTime() +
                    "\n\tInstructor Name: " + crs.getInstName());

            classBox.getChildren().addAll(classInfoLabel);

            classItems.add(classBox);
        }

        classListView.setItems(classItems);
        vBox.getChildren().add(classListView);

        stage.setScene(scene);
        stage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}
