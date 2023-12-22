package OOPProject;
import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox; 

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ProfTakeAttendance extends Application {
    private int profId;
    private Stage primaryStage;
    private DBConnectivity dbConnectivity;
    private int class_id;

    public ProfTakeAttendance(int profId, Stage primaryStage,DBConnectivity db) {
        this.profId = profId;
        this.primaryStage = primaryStage;
        this.dbConnectivity = db;
    }

    public void takeAttendance() {
        primaryStage.setTitle("Attendance System");

        Image backgroundImage = new Image("file:C:/Users/Rayan/Downloads/TalaSafa_FatimaHashem_SIS/src/OOPProject/Photos/blue2.jpeg");
        Background background = new Background(new BackgroundImage(
                backgroundImage,
                null, null, null,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        ));

        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(20, 20, 20, 20));
        vBox.setBackground(background);

        Label classcode = new Label("Class Code: ");
        List<ClassCourse> scheduledClasses = dbConnectivity.getScheduledClassesByProf(profId);

        List<String> classCodes = dbConnectivity.getScheduled2ClassesByProf(profId);
        ComboBox<String> ClassNameOptions = new ComboBox<>();
        ClassNameOptions.setItems(FXCollections.observableArrayList(classCodes));

        ClassNameOptions.setOnAction(event -> {
            String selectedclassName = ClassNameOptions.getValue();
            class_id=dbConnectivity.getClassIdFromCode(selectedclassName);
        });

        Button takeAttendanceButton = new Button("Next");
        takeAttendanceButton.setStyle(
                "-fx-background-color: rgb(21, 95, 170); " +
                        "-fx-text-fill: WHITE; " +
                        "-fx-font-size: 14px; " +
                        "-fx-padding: 8px 16px; " +
                        "-fx-background-radius: 5px;" +
                        "-fx-min-width: 200px; "
        );
        takeAttendanceButton.setOnAction(e -> viewStudents());

        vBox.getChildren().addAll(classcode, ClassNameOptions, takeAttendanceButton);

        Scene scene = new Scene(vBox, 300, 150);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void viewStudents() {
        VBox vBox = new VBox();
        Scene scene = new Scene(vBox, 800, 400);
        Image backgroundImage = new Image("file:C:/Users/Rayan/Downloads/TalaSafa_FatimaHashem_SIS/src/OOPProject/Photos/blue2.jpeg");
        Background background = new Background(new BackgroundImage(
                backgroundImage,
                null, null, null,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        ));

        dbConnectivity.Connect();
        List<User> students = dbConnectivity.getAllStudentsByClass(class_id);

        ObservableList<HBox> studentsItems = FXCollections.observableArrayList();
        for (User std : students) {
            HBox stdBox = new HBox(10);

            Label stdInfoLabel = new Label(std.getName());
            stdInfoLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #154DAC;"); 

            CheckBox checkBox = new CheckBox();
            checkBox.setSelected(true);
            checkBox.setStyle("-fx-font-size: 14px; -fx-text-fill: #154DAC; -fx-background-color: #E0E0E0;"); 
            checkBox.setOnAction(event -> {
                int selectedValue = checkBox.isSelected() ? 1 : 0;
                if (selectedValue == 0) {
                    dbConnectivity.updateAbsenceForStudent(std.getId(), class_id, 1);
                } else {
                    dbConnectivity.updateAbsenceForStudent(std.getId(), class_id, -1);
                }
            });

            stdBox.getChildren().addAll(stdInfoLabel, checkBox);
            stdBox.setAlignment(Pos.CENTER); 
            studentsItems.add(stdBox);
        }
        vBox.setBackground(background);
        vBox.getChildren().addAll(studentsItems);

        Stage classStage = new Stage();
        classStage.setScene(scene);
        classStage.show();
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        takeAttendance();
    }

   public static void main(String[] args) {
        launch(args);
    }
}