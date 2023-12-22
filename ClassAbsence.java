package OOPProject;

public class ClassAbsence {
	private String crsName;
	private String day;
	private String startTime;
	private String endTime;
	public ClassAbsence(String crsName, String day, String startTime, String endTime, int absence) {
		this.crsName = crsName;
		this.day = day;
		this.startTime = startTime;
		this.endTime = endTime;
		this.absence = absence;
	}
	
	@Override
	public String toString() {
		return "ClassAbsence [crsName=" + crsName + ", day=" + day + ", startTime=" + startTime + ", endTime=" + endTime
				+ ", absence=" + absence + "]";
	}

	public String getCrsName() {
		return crsName;
	}
	public void setCrsName(String crsName) {
		this.crsName = crsName;
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
	private int absence;
	public int getAbsence() {
		return absence;
	}
	public void setAbsence(int absence) {
		this.absence = absence;
	}
	

}

    
