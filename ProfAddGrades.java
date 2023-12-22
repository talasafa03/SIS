package OOPProject;
import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ProfAddGrades extends Application {
    private int profId;
    private Stage primaryStage;
    int class_id;
    private DBConnectivity dbConnectivity;
    public ProfAddGrades(int profId, Stage primaryStage,DBConnectivity DB) {
        this.profId = profId;
        this.primaryStage = primaryStage;
        this.dbConnectivity = DB;

    }

    public void takeAttendance() {
        primaryStage.setTitle("Grading System");

        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(20, 20, 20, 20));
        Image backgroundImage = new Image("file:C:/Users/Rayan/Downloads/TalaSafa_FatimaHashem_SIS/src/OOPProject/Photos/blue2.jpeg");
        Background background = new Background(new BackgroundImage(
                backgroundImage,
                null, null, null,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        ));

        Label classcode=new Label("Class Code: ");
        List<ClassCourse> scheduledClasses = dbConnectivity.getScheduledClassesByProf(profId);

        List<String> classCodes = dbConnectivity.getScheduled2ClassesByProf(profId);
        ComboBox<String> ClassNameOptions = new ComboBox<>();
        ClassNameOptions.setItems(FXCollections.observableArrayList(classCodes));

        ClassNameOptions.setOnAction(event -> {
            String selectedclassName = ClassNameOptions.getValue();
            class_id=dbConnectivity.getClassIdFromCode(selectedclassName);
        });


        Button putgrades = new Button("Next");
        putgrades.setStyle(
                "-fx-background-color: rgb(21, 95, 170); " +
                        "-fx-text-fill: WHITE; " +
                        "-fx-font-size: 14px; " +
                        "-fx-padding: 8px 16px; " +
                        "-fx-background-radius: 5px;" +
                        "-fx-min-width: 200px; "
        );
        putgrades.setOnAction(e -> ViewStudents());

        vBox.setBackground(background);
        vBox.getChildren().addAll(classcode, ClassNameOptions, putgrades);

        Scene scene = new Scene(vBox, 300, 150);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        takeAttendance();
    }



 private void ViewStudents() {
     VBox vBox = new VBox();
     Scene scene = new Scene(vBox, 800, 400);

     DBConnectivity con = new DBConnectivity();
     con.Connect();
     List<User> students = con.getAllStudentsByClass(class_id);
     Image backgroundImage = new Image("file:C:/Users/Rayan/Downloads/TalaSafa_FatimaHashem_SIS/src/OOPProject/Photos/blue2.jpeg");
     Background background = new Background(new BackgroundImage(
             backgroundImage,
             null, null, null,
             new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
     ));

     ObservableList<HBox> StudentsItems = FXCollections.observableArrayList();
     for (User std : students) {
         HBox stdBox = new HBox(10);

         Label stdInfoLabel = new Label(std.getName());
         TextField grade= new TextField();
         grade.setPrefWidth(150);
         grade.setMaxWidth(150); 
         
         
         stdBox.getChildren().addAll(stdInfoLabel, grade);
         StudentsItems.add(stdBox);
         
         
         
     }
     Button saveButton = new Button("Save");
     
     saveButton.setOnAction(event -> {
    	    for (int i = 0; i < StudentsItems.size(); i++) {
    	        HBox stdBox = StudentsItems.get(i);
    	        TextField gradeTextField = (TextField) stdBox.getChildren().get(1);  
    	        String grade = gradeTextField.getText();
    	        User student = students.get(i);

    	        con.updateGradeForStudent(class_id, student.getId(), Double.parseDouble(grade));
    	    }
    	});

     vBox.setBackground(background);
     vBox.getChildren().addAll(StudentsItems);
     vBox.getChildren().add(saveButton);


     Stage classStage = new Stage();
     classStage.setScene(scene);
     classStage.show();
 }

}

    
