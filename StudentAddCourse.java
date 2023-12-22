package OOPProject;


import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Button;
public class StudentAddCourse {
	private int stdId;
	private Stage primaryStage; 

	public StudentAddCourse(int stdId, Stage primaryStage) {
		this.stdId = stdId;
		this.primaryStage=primaryStage;
	}

	void EnrollCourse(DBConnectivity con, int classId, Label result) {
		int res=con.enrollCourseForStd(classId, stdId);
		if (res==0) {
			result.setText("Course Already Enrolled");
		}else if(res==1) {
			result.setText("Course Enrolled Successfully");
		}else {
			result.setText("Error");
		}
		
		
	}

	public void start(Stage studentAddCourseStage) {
		VBox vbox = new VBox();
	    vbox.setSpacing(10);

	    StudentViewCourses stdView = new StudentViewCourses(stdId, studentAddCourseStage);
	    try {
	        stdView.start(studentAddCourseStage);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }  
       		
	}

}

    
