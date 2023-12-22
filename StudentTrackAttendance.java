package OOPProject;
import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StudentTrackAttendance extends Application {
	private int stdId;
    private Stage primaryStage;
	
	public StudentTrackAttendance(int stdId, Stage primaryStage) {
		this.stdId = stdId;
		this.primaryStage = primaryStage;
	}

	private void viewClasses() {
	    VBox vBox = new VBox();
	    Scene scene = new Scene(vBox, 800, 400);

	    ListView<HBox> classListView = new ListView<>();
	    classListView.setPrefHeight(550);

	    DBConnectivity con = new DBConnectivity();
	    con.Connect();

	    List<ClassAbsence> classes = con.getAbsences(stdId);

	    ObservableList<HBox> classItems = FXCollections.observableArrayList();
	    for (ClassAbsence classCrs : classes) {
	        HBox classBox = new HBox(10);
	        Label classInfoLabel = new Label("Course Name: " + classCrs.getCrsName() +
	                "\n\tDay: " + classCrs.getDay() +
	                "\n\tStart Time: " + classCrs.getStartTime() +
	                "\n\tEnd Time: " + classCrs.getEndTime() +
	                "\n\tAbsences: " + classCrs.getAbsence());

	        classBox.getChildren().addAll(classInfoLabel);
	        classItems.add(classBox);
	    }

	    classListView.setItems(classItems);

	    vBox.getChildren().add(classListView);

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
    
