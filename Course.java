package OOPProject;

public class Course {
    private int courseId;
    private int credits;
    private String courseName;
    private String coordinator;
    private String facultyName;


    public Course() {

    }

    public Course(int credits, String courseName, String coordinator, String facultyName) {
        this.credits = credits;
        this.courseName = courseName;
        this.coordinator = coordinator;
        this.facultyName = facultyName;
    }


    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCoordinatorName() {
        return coordinator;
    }

    public void setCoordinatorName(String coordinator) {
        this.coordinator = coordinator;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", credits=" + credits +
                ", courseName='" + courseName + '\'' +
                ", coordinator=" + coordinator +
                ", faculty=" + facultyName +
                '}';
    }
}

    
