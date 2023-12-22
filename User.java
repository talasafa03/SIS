package OOPProject;
public class User {
	private int id;
	private String name;
	private String email;
	private String Faculty;
	private int EnrolledCreds;
	private int PassedCreds;
	private int TotalCreds;
	private double percentage;
	public User(String name, String email, String faculty, int enrolledCreds, int passedCreds, int totalCreds,
			double percentage) {
		this.name = name;
		this.email = email;
		Faculty = faculty;
		EnrolledCreds = enrolledCreds;
		PassedCreds = passedCreds;
		TotalCreds = totalCreds;
		this.percentage = percentage;
	}
	
	


	public User(int id, String name, String email) {
		this.id=id;
		this.name = name;
		this.email = email;
	}



	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFaculty() {
		return Faculty;
	}
	public void setFaculty(String faculty) {
		Faculty = faculty;
	}
	public int getEnrolledCreds() {
		return EnrolledCreds;
	}
	public void setEnrolledCreds(int enrolledCreds) {
		EnrolledCreds = enrolledCreds;
	}
	public int getPassedCreds() {
		return PassedCreds;
	}
	public void setPassedCreds(int passedCreds) {
		PassedCreds = passedCreds;
	}
	public int getTotalCreds() {
		return TotalCreds;
	}
	public void setTotalCreds(int totalCreds) {
		TotalCreds = totalCreds;
	}
	public double getPercentage() {
		return percentage;
	}
	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}




	public int getId() {
		return id;
	}




	public void setId(int id) {
		this.id = id;
	}




	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", Faculty=" + Faculty + ", EnrolledCreds="
				+ EnrolledCreds + ", PassedCreds=" + PassedCreds + ", TotalCreds=" + TotalCreds + ", percentage="
				+ percentage + "]";
	}
	
	
}

    
