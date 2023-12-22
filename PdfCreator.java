package OOPProject;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.PieChart;
import javafx.scene.image.WritableImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PdfCreator {

    private int id;
    private PieChart pChart;
    public PdfCreator(int id, PieChart pChart) {
        this.id = id;
        this.pChart = pChart;
    }

    public void main() {
        createPdf();
    }

    public void createPdf() {
        DBConnectivity con = new DBConnectivity();

        User u = con.getStudentInfo(id);
        String s = "Name: " + u.getName() +
                "\nEmail: " + u.getEmail() +
                "\nFaculty: " + u.getFaculty() +
                "\nEnrolled Credits: " + u.getEnrolledCreds() +
                "\nPassed Credits: " + u.getPassedCreds() +
                "\nRemained Credits: " + (u.getTotalCreds() - u.getPassedCreds() - u.getEnrolledCreds()) +
                "\nCompleted For Now: " + u.getPercentage() + " %";
        
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("Report_With_Chart.pdf"));
            document.open();
            
            document.add(new Paragraph(s));
            document.add(new Paragraph("\n"));

            WritableImage chartImage = getChartImage();
            ImageIO.write(SwingFXUtils.fromFXImage(chartImage, null), "png", new File("chart.png"));

            Image chart = Image.getInstance("chart.png");
            document.add(chart);

            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    private void addChartToPdf() {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("Report_With_Chart.pdf"));
            document.open();

            WritableImage chartImage = getChartImage();
            ImageIO.write(SwingFXUtils.fromFXImage(chartImage, null), "png", new File("chart.png"));

            Image chart = Image.getInstance("chart.png");
            document.add(chart);

            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    private WritableImage getChartImage() {
        WritableImage image = new WritableImage(600, 450);
        pChart.snapshot(new SnapshotParameters(), image);
        return image;
    }
}
