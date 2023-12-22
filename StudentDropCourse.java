package OOPProject;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.control.Button;

public class StudentDropCourse {
	private int stdId;
	private Stage primaryStage; 

	public StudentDropCourse(int stdId, Stage primaryStage) {
		this.stdId = stdId;
		this.primaryStage=primaryStage;
	}

	void DropCourse(DBConnectivity con, int classId, Label result) {
		int res=con.dropCourse(classId);
		 if(res==1) {
			result.setText("Course Dropped Successfully");
		}else {
			result.setText("Error");
		}
		
	}

	public void start(Stage studentDropCourseStage) {
		VBox vbox = new VBox();
	    vbox.setSpacing(10);

	    StudentViewEnrolledCoursesToDrop stdView = new StudentViewEnrolledCoursesToDrop(stdId, studentDropCourseStage);
	    try {
	        stdView.start(studentDropCourseStage);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }  
       		
	}
}
