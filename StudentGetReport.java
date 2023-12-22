package OOPProject;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class StudentGetReport extends Application {
    private int stdId;

    public StudentGetReport(int stdId) {
        this.stdId = stdId;
    }

    @Override
    public void start(Stage primaryStage) {
        StackPane rootPane = createRootPane();
        DBConnectivity con = new DBConnectivity();
        int PassedCreds = con.getPassedCreditsStudent(stdId);
        int EnrolledCreds = con.getEnrolledCreditsStudent(stdId);
        int total = con.getTotalCredByFaculty(stdId);

        double enrolled = ((EnrolledCreds * 100.0) / (total - PassedCreds));
        double passed = ((PassedCreds * 100.0) / total);
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
                new PieChart.Data("Enrolled", enrolled),
                new PieChart.Data("Passed", passed),
                new PieChart.Data("Remained", 100 - passed - enrolled)
        );

        
        PieChart pChart = new PieChart(pieData);
        pChart.applyCss();

        VBox vbox = new VBox();
        vbox.getChildren().addAll(new Group(pChart), createButton(pChart));
        rootPane.getChildren().add(vbox);

        Scene scene = new Scene(rootPane, 600, 450);
        primaryStage.setTitle("Academic Progress");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private StackPane createRootPane() {
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

        return backgroundPane;
    }

    private Button createButton(PieChart pChart) {
        Button downloadButton = new Button("Download PDF");
        downloadButton.setStyle(
                "-fx-background-color: rgb(21, 95, 170); " +
                        "-fx-text-fill: WHITE; " +
                        "-fx-font-size: 14px; " +
                        "-fx-padding: 8px 16px; " +
                        "-fx-background-radius: 5px;" +
                        "-fx-min-width: 170px; "
        );
        downloadButton.setOnAction(event -> {
            PdfCreator pdf = new PdfCreator(stdId, pChart); 
            pdf.createPdf(); 
            System.out.println("PDF Downloaded!");
        });
        return downloadButton;
    }

    public static void main(String[] args) {
        launch(args);
    }
}