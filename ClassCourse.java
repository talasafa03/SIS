package OOPProject;
public class ClassCourse {

	
	private int classId;
	private String classCode;
	private String crsName;
	private int classRoom;
	private String day;
	private String startTime;
	private String endTime;
	private String instName;
	private int absence;
	private double grade;
	
	

	public ClassCourse(int classId, String classCode, String crsName, int classRoom, String day, String startTime,
			String endTime, String instName) {
		this.classId = classId;
		this.classCode = classCode;
		this.crsName = crsName;
		this.classRoom = classRoom;
		this.day = day;
		this.startTime = startTime;
		this.endTime = endTime;
		this.instName = instName;
	}
	
	public ClassCourse(int classId, String classCode, String crsName, int classRoom, String day, String startTime,
			String endTime, String instName, double grade) {
		this.classId = classId;
		this.classCode = classCode;
		this.crsName = crsName;
		this.classRoom = classRoom;
		this.day = day;
		this.startTime = startTime;
		this.endTime = endTime;
		this.instName = instName;
		this.grade=grade;
	}
	
	
	

	public ClassCourse(int classId, String classCode, String crsName, int classRoom, String day, String startTime,
			String endTime) {
		this.classId = classId;
		this.classCode = classCode;
		this.crsName = crsName;
		this.classRoom = classRoom;
		this.day = day;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	public String toString1() {
	    return classCode;
	}

	public double getGrade() {
		return grade;
	}

	public void setGrade(double grade) {
		this.grade = grade;
	}

	public int getClassId() {
		return classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public String getCrsName() {
		return crsName;
	}

	public void setCrsName(String crsName) {
		this.crsName = crsName;
	}

	public int getClassRoom() {
		return classRoom;
	}

	public void setClassRoom(int classRoom) {
		this.classRoom = classRoom;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getInstName() {
		return instName;
	}

	public void setInstName(String instName) {
		this.instName = instName;
	}

	@Override
	public String toString() {
		return "ClassCourse [classId=" + classId + ", classCode=" + classCode + ", crsName=" + crsName + ", classRoom="
				+ classRoom + ", Day=" + day + ", startTime=" + startTime + ", endTime=" + endTime + ", instName="
				+ instName + "]";
	}

	public int getAbsence() {
		return absence;
	}

	public void setAbsence(int absence) {
		this.absence = absence;
	}
	
	
	
	
}

    
