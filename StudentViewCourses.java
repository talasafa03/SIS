package OOPProject;
import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StudentViewCourses extends Application{
	private int stdId;
    private Stage primaryStage;
	
	public StudentViewCourses(int stdId, Stage primaryStage) {
		this.stdId = stdId;
		this.primaryStage = primaryStage;
	}

	private void displayCourses(Stage primaryStage) {
	    DBConnectivity con = new DBConnectivity();
	    con.Connect();

	    int factID = con.getFactId(stdId);
System.out.println(factID);
	    VBox vBox = new VBox();
	    Scene scene = new Scene(vBox, 800, 400);

	    TextField searchField = new TextField();
	    searchField.setPromptText("Search by Course Name");
	    searchField.setMaxWidth(200);

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

	        viewClassesButton.setOnAction(event -> viewClasses(primaryStage, crs.getCourseId()));

	        courseBox.getChildren().addAll(label, viewClassesButton);
	        courseItems.add(courseBox);
	        vBox.getChildren().add(courseBox);
	    }

	    FilteredList<HBox> filteredCourses = new FilteredList<>(courseItems, p -> true);
	    searchField.textProperty().addListener((observable, oldValue, newValue) ->
	            filteredCourses.setPredicate(courseBox -> {
	                if (newValue == null || newValue.isEmpty()) {
	                    return true;
	                }

	                String lowerCaseFilter = newValue.toLowerCase();
	                return courseBox.getChildren().get(0).toString().toLowerCase().contains(lowerCaseFilter);
	            }));

	    courseListView.setItems(filteredCourses);
	    vBox.getChildren().addAll(searchField, courseListView);

	    primaryStage.setScene(scene);
	    primaryStage.show();
	}
	private void viewClasses(Stage primaryStage,int courseId) {
	    StudentAddCourse s = new StudentAddCourse(stdId, primaryStage);
	    VBox vBox = new VBox();
	    Scene scene = new Scene(vBox, 800, 400);

	    TextField searchField = new TextField();
	    searchField.setPromptText("Search by Instructor Name or Class Code");
	    searchField.setMaxWidth(200);

	    ListView<HBox> classListView = new ListView<>();
	    classListView.setPrefHeight(550);
	    DBConnectivity con = new DBConnectivity();
	    con.Connect();
	    List<ClassCourse> classes = con.getAllClassesByCourse(courseId);

	    ObservableList<HBox> classItems = FXCollections.observableArrayList();
	    for (ClassCourse classCrs : classes) {
	        HBox classBox = new HBox(10);
	        Label classInfoLabel = new Label("Course Name: " + classCrs.getCrsName() +
	                "\n\tClass Code: " + classCrs.getClassCode() +
	                "\n\tDay: " + classCrs.getDay() +
	                "\n\tStart Time: " + classCrs.getStartTime() +
	                "\n\tEnd Time: " + classCrs.getEndTime() +
	                "\n\tInstructor Name: " + classCrs.getInstName());

	        Button enrollButton = new Button("Enroll");
	        Label l = new Label();

	        enrollButton.setOnAction(e -> s.EnrollCourse(con, classCrs.getClassId(), l));

	        classBox.getChildren().addAll(classInfoLabel, enrollButton, l);
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

	    Stage classStage = new Stage();
	    classStage.setScene(scene);
	    classStage.show();
	}


	@Override
	public void start(Stage primaryStage) throws Exception {
		
		displayCourses(primaryStage);
	}
}

    
