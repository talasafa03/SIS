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

public class ProfViewCourses extends Application{
	private int profId;
    private Stage primaryStage;
	
    
	public ProfViewCourses(int profId, Stage primaryStage) {
		this.profId = profId;
		this.primaryStage = primaryStage;
	}

	private void viewClasses() {
	    VBox vBox = new VBox();
	    Scene scene = new Scene(vBox, 800, 400);

	    TextField searchField = new TextField();
	    searchField.setPromptText("Search by Class Code");
	    searchField.setMaxWidth(200);

	    ListView<HBox> classListView = new ListView<>();
	    classListView.setPrefHeight(550);
	    DBConnectivity con = new DBConnectivity();
	    con.Connect();
	    List<ClassCourse> classes = con.getScheduledClassesByProf(profId);

	    ObservableList<HBox> classItems = FXCollections.observableArrayList();
	    for (ClassCourse classCrs : classes) {
	        HBox classBox = new HBox(10);
	        Label classInfoLabel = new Label("Course Name: " + classCrs.getCrsName() +
	                "\n\tClass Code: " + classCrs.getClassCode() +
	                "\n\tDay: " + classCrs.getDay() +
	                "\n\tStart Time: " + classCrs.getStartTime() +
	                "\n\tEnd Time: " + classCrs.getEndTime() );

	        Button ViewEnrolledStudentsButton = new Button("Viw Enrolled Students");
	        Label l = new Label();

	        ViewEnrolledStudentsButton.setOnAction(e -> ViewStudents(con, classCrs.getClassId()));

	        classBox.getChildren().addAll(classInfoLabel, ViewEnrolledStudentsButton, l);
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

	private void ViewStudents(DBConnectivity con, int classId) {
	    VBox vBox = new VBox();
	    Scene scene = new Scene(vBox, 800, 400);

	    List<User> students = con.getAllStudentsByClass(classId);

	    ObservableList<HBox> StudentsItems = FXCollections.observableArrayList();
	    for (User std : students) {
	        HBox stdBox = new HBox(10);
	        Label stdInfoLabel = new Label(" Name: " + std.getName() +
	                "\tEmail: " + std.getEmail());

	        stdBox.getChildren().addAll(stdInfoLabel);
	        StudentsItems.add(stdBox);
	    }

	    vBox.getChildren().addAll(StudentsItems);

	    Stage classStage = new Stage();
	    classStage.setScene(scene);
	    classStage.show();
	}


	@Override
	public void start(Stage primaryStage) throws Exception {
		
		this.primaryStage=primaryStage;
		viewClasses();
	}
	
}
    
