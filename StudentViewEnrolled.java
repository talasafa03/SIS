package OOPProject;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class StudentViewEnrolled extends Application {
    private int stdId;
    private Stage primaryStage;

    public StudentViewEnrolled(int stdId, Stage primaryStage) {
        this.stdId = stdId;
        this.primaryStage = primaryStage;
    }

    private void viewClasses() {
        DBConnectivity con = new DBConnectivity();
        con.Connect();

        List<ClassCourse> classes = con.getEnrolledClassesByStudent(stdId);

        VBox vBox = new VBox();
        Scene scene = new Scene(vBox, 800, 400);

        TextField searchField = new TextField();
        searchField.setPromptText("Search by Instructor Name or Class Code");
        searchField.setMaxWidth(200);

        ListView<HBox> classListView = new ListView<>();
        classListView.setPrefHeight(550);

        ObservableList<HBox> classItems = FXCollections.observableArrayList();
        for (ClassCourse classCrs : classes) {
            HBox classBox = new HBox(10);
            Label classInfoLabel = new Label("Course Name: " + classCrs.getCrsName() +
                    "\n\tClass Code: " + classCrs.getClassCode() +
                    "\n\tDay: " + classCrs.getDay() +
                    "\n\tStart Time: " + classCrs.getStartTime() +
                    "\n\tEnd Time: " + classCrs.getEndTime() +
                    "\n\tInstructor Name: " + classCrs.getInstName());

            classBox.getChildren().addAll(classInfoLabel);
            classItems.add(classBox);
        }

        FilteredList<HBox> filteredClasses = new FilteredList<>(classItems, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) ->
                filteredClasses.setPredicate(classBox -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    String lowerCaseFilter = newValue.toLowerCase();
                    return classBox.getChildren().get(0).toString().toLowerCase().contains(lowerCaseFilter);
                }));

        classListView.setItems(filteredClasses);
        vBox.getChildren().addAll(searchField, classListView);

        openViewEnrolledCoursesPage();

        primaryStage.show();
    }

    private void openViewEnrolledCoursesPage() {
        StudentViewEnrolledCourses stdView = new StudentViewEnrolledCourses(stdId);

        try {
            stdView.start(primaryStage);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        viewClasses();
    }
}
