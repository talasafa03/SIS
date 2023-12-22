package OOPProject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBConnectivity {
	
	String jdbcUrl = "jdbc:mysql://localhost:3306/OOPProject";
    String username = "root";
    String password = "oracle";
    int UserID;
    Connection connection ;
public void Connect() {
	    try {
	        connection = DriverManager.getConnection(jdbcUrl, username, password);
	        System.out.println("Connected to the database!");
	    } catch (SQLException e) {
	        System.err.println("Error connecting to the database: " + e.getMessage());
	    }
    }
    
public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Disconnected from the database");
            }
        } catch (SQLException e) {
	        System.err.println("Error disconnecting to the database: " + e.getMessage());
        }
    }
public String authenticate(String email, String pass) {
    String role = "";
    try {
        String sqlQuery = "SELECT * FROM user WHERE email=? AND password=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

        preparedStatement.setString(1, email);
        preparedStatement.setString(2, pass);

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            System.out.println("Authenticated!");
            int isAdmin = resultSet.getInt("isAdmin");
            System.out.println("Status: " + isAdmin);
            UserID = resultSet.getInt("userId");


            if (isAdmin == 1) {
                role = "admin";
            } else if (isAdmin == 0){
                role = "student"; 
            }
         else if (isAdmin == 2){
            role = "user"; 
        }
        } else {
            System.out.println("Authentication failed!");
        }

        resultSet.close();
        preparedStatement.close();

    } catch (SQLException e) {
        System.err.println("Error during authentication");
    }
    return role;
}

public int dropCourse(int classId) {
	int res=0;
    try {
        String query = "DELETE FROM std_Course WHERE classId = ? and stat=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, classId);
            preparedStatement.setInt(2, 0);
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Course dropped successfully");
                res=1;
            } else {
                System.out.println("Course not found with classId: " + classId);
            }
        }
    } catch (SQLException e) {
        System.out.println("Error dropping course: " + e.getMessage());
    }
    return res;
}

public List<ClassCourse> getPassedClassesByStudent(int stdId) {
	
	List<ClassCourse> classes = new ArrayList<>();

	try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
	String query = "SELECT c.classId, c.classCode, c.roomNb, c.classDay, c.classStartTime, c.classEndTime,sc.grade, co.courseName, i.name " +
            "FROM std_Course sc " +
            "JOIN class c ON sc.classId = c.classId " +
            "JOIN course co ON c.crsId = co.courseId " +
            "JOIN instructor i ON c.instId = i.instId " +
            "WHERE sc.StdId = ? and stat=?";

    try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, stdId);
        statement.setInt(2, 1);

        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                ClassCourse class1 = new ClassCourse(resultSet.getInt("classId"), 
                		resultSet.getString("classCode"),resultSet.getString("courseName"),
                		resultSet.getInt("roomNb"), resultSet.getString("classDay"),
                		resultSet.getString("classStartTime"),
                		resultSet.getString("classEndTime"), resultSet.getString("name"),
                		resultSet.getDouble("grade"));
                
                classes.add(class1);
            }
        }
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}

return classes;
}





public List<ClassCourse> getEnrolledClassesByStudent(int stdId) {
	
	List<ClassCourse> classes = new ArrayList<>();

	try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
	String query = "SELECT c.classId, c.classCode, c.roomNb, c.classDay, c.classStartTime, c.classEndTime, co.courseName, i.name " +
            "FROM std_Course sc " +
            "JOIN class c ON sc.classId = c.classId " +
            "JOIN course co ON c.crsId = co.courseId " +
            "JOIN instructor i ON c.instId = i.instId " +
            "WHERE sc.StdId = ? and stat=?";

    try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, stdId);
        statement.setInt(2, 0);

        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                ClassCourse class1 = new ClassCourse(resultSet.getInt("classId"), resultSet.getString("classCode"),resultSet.getString("courseName"),  resultSet.getInt("roomNb"), resultSet.getString("classDay"), resultSet.getString("classStartTime"), resultSet.getString("classEndTime"), resultSet.getString("name"));
                
                classes.add(class1);
            }
        }
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}

return classes;
}
private int getCourseCredits(int classId) {
    int credits = 0;
    try {
        String query = "SELECT credits FROM course c " +
                       "JOIN class cl ON c.courseId = cl.crsId " +
                       "WHERE cl.classId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, classId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                credits = resultSet.getInt("credits");
            }
        }
    } catch (SQLException e) {
        System.out.println("Error getting course credits: " + e.getMessage());
    }
    return credits;
}
public int enrollCourseForStd(int classId, int stdID) {
    int res = 0;
    try {

    	String creditsQuery = "SELECT SUM(co.credits) as totalCredits " +
                "FROM std_Course sc " +
                "JOIN class cl ON sc.classId = cl.classId " +
                "JOIN course co ON cl.crsId = co.courseId " +
                "WHERE sc.StdId = ? AND sc.stat = ?";

        try (PreparedStatement creditsStatement = connection.prepareStatement(creditsQuery)) {

            creditsStatement.setInt(1, stdID);
            creditsStatement.setInt(2, 0);
            ResultSet creditsResult = creditsStatement.executeQuery();

            int totalCredits = 0;
            if (creditsResult.next()) {
                totalCredits = creditsResult.getInt("totalCredits");
            }


            if (totalCredits + getCourseCredits(classId) > 21) {
                System.out.println("Cannot enroll. Exceeds maximum allowed credits (21).");
                res = -2; 
            } else {


                String enrollmentQuery = "SELECT * FROM std_course sc1 " +
                        "JOIN class cl1 ON sc1.classId = cl1.classId " +
                        "WHERE sc1.StdId = ? AND cl1.crsId = (SELECT crsId FROM class WHERE classId = ?)";
                
                try (PreparedStatement enrollmentStatement = connection.prepareStatement(enrollmentQuery)) {
                    enrollmentStatement.setInt(1, stdID);
                    enrollmentStatement.setInt(2, classId);
                    ResultSet enrollmentResult = enrollmentStatement.executeQuery();

                    if (enrollmentResult.next()) {
                        System.out.println("Already enrolled in a class related to the same course");
                        res = 0;
                    } else {

                        String insertQuery = "INSERT INTO std_Course (classId, StdId) VALUES (?, ?)";
                        try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery,
                                Statement.RETURN_GENERATED_KEYS)) {
                            insertStatement.setInt(1, classId);
                            insertStatement.setInt(2, stdID);
                            
                            insertStatement.executeUpdate();
                            System.out.println("Class enrolled successfully");
                            res = 1;
                        }
                    }
                }
            }
        }
    } catch (SQLException e) {
        res = -1;
        System.out.println("SQL State: " + e.getSQLState());
        System.out.println("Error Code: " + e.getErrorCode());
        System.out.println("Error Message: " + e.getMessage());
    }
    return res;
}



public String getFactName(int UID ) {
	String name;
	try {
		 String sqlQuery = "SELECT facultyName FROM user INNER JOIN faculty ON user.facultyId = faculty.facultyId WHERE userId=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        
        preparedStatement.setLong(1, UID);
        
        ResultSet resultSet= preparedStatement.executeQuery();
        if (resultSet.next()) {
            name=resultSet.getString("facultyName");
        } else {
            name="Not Found";
        }

        resultSet.close();
        preparedStatement.close();

	}catch (SQLException e) {
       System.err.println("Error getFactNAme");
       name="";
   }
	return name;
}

public List<String> getFacultyNamesFromDatabase() {
    List<String> facultyNames = new ArrayList<>();

    try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
        String query = "SELECT facultyName FROM faculty";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String facultyName = resultSet.getString("facultyName");
                    facultyNames.add(facultyName);
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return facultyNames;
	}
	
public List<String> getInstructorsNamesFromDatabase(String nameFact) {

	    List<String> ProfsNames = new ArrayList<>();

	    try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
	        String query = "SELECT name FROM instructor INNER JOIN faculty ON instructor.facultyId = faculty.facultyId WHERE facultyName = ?";
	        
	        try (PreparedStatement statement = connection.prepareStatement(query)) {
	         
	            statement.setString(1, nameFact);
	            try (ResultSet resultSet = statement.executeQuery()) {
	                while (resultSet.next()) {
	                    String ProfName = resultSet.getString("name");
	                    ProfsNames.add(ProfName);
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return ProfsNames;
	}


public List<ClassCourse> getScheduledClassesByProf(int profId) {
	
	List<ClassCourse> classes = new ArrayList<>();

	try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
	String query = "SELECT c.classId, c.classCode, c.roomNb, c.classDay, c.classStartTime, c.classEndTime, co.courseName " +
            "FROM  class c " +
            "JOIN course co ON c.crsId = co.courseId " +
            "JOIN instructor i ON c.instId = i.instId " +
            "WHERE i.instId = ?";

    try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, profId);

        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                ClassCourse class1 = new ClassCourse(resultSet.getInt("classId"), resultSet.getString("classCode"),resultSet.getString("courseName"),  resultSet.getInt("roomNb"), resultSet.getString("classDay"), resultSet.getString("classStartTime"), resultSet.getString("classEndTime"));
                
                classes.add(class1);
            }
        }
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}

return classes;
}

public List<String> getScheduled2ClassesByProf(int profId) {
    List<String> classCODES = new ArrayList<>();

    try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
        String query = "SELECT c.classCode" +
                " FROM class c " +
                " JOIN course co ON c.crsId = co.courseId " +
                " JOIN instructor i ON c.instId = i.instId " +
                " WHERE i.instId = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, profId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String classCode = resultSet.getString("classCode");
                    classCODES.add(classCode);
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return classCODES;
}

public int getClassIdFromCode(String classCode) {
    int classId = -1; 
    try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
        String query = "SELECT classId FROM class WHERE classCode=?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, classCode);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                	classId = resultSet.getInt("classId");
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return classId;
}

public int dropCourse(int classId, int stdId) {
	int res=0;
    try {
        String query = "DELETE FROM std_Course WHERE classId = ? and StdId=? and stat=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, classId);
            preparedStatement.setInt(2, stdId);
            preparedStatement.setInt(3, 0);
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Course dropped successfully");
                res=1;
            } else {
                System.out.println("Course not found with classId: " + classId);
            }
        }
    } catch (SQLException e) {
        System.out.println("Error dropping course: " + e.getMessage());
    }
    return res;
}

public void updateAbsenceForStudent(int id, int cId, int i) {
	String updateQuery = "UPDATE std_course SET abscences = abscences + ? WHERE StdId = ? AND classId=?";
	if (i==-1)
		updateQuery = "UPDATE std_course SET abscences = abscences - ? WHERE StdId = ? AND classId=?";
	String selectQuery = "SELECT sc.abscences, co.credits "
	        + "FROM std_Course sc "
	        + "JOIN class c ON sc.classId = c.classId "
	        + "JOIN course co ON c.crsId = co.courseId "
	        + "WHERE sc.classId = ? AND sc.StdId = ?";

			

	try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
	    updateStatement.setInt(1, 1);
	    updateStatement.setLong(2, id);
	    updateStatement.setLong(3, cId);
	    int rowsAffected = updateStatement.executeUpdate();

	    if (rowsAffected > 0) {
	        try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
	            selectStatement.setLong(1, cId); 
	            selectStatement.setLong(2, id);
	            try (ResultSet resultSet = selectStatement.executeQuery()) {
	                if (resultSet.next()) {
	                    int newAbsence = resultSet.getInt("abscences");
	                    int creds=resultSet.getInt("credits");
	                    if (newAbsence>3*creds)
	                    	dropCourse(cId,id);
	                    	
	                    System.out.println("Absence updated successfully. New absence: " + newAbsence);
	                }
	            }
	        }
	    } else {
	        System.out.println("No rows updated. Student with id " + id + " not found.");
	    }
	} catch (SQLException e) {
	    System.out.println("SQL State: " + e.getSQLState());
	    System.out.println("Error Code: " + e.getErrorCode());
	    System.out.println("Error Message: " + e.getMessage());
	}


}


public List<ClassAbsence> getAbsences(int stdId) {
    
    List<ClassAbsence> classes = new ArrayList<>();

    try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
    	String query = "SELECT c.classDay, c.classStartTime, c.classEndTime, sc.abscences, co.courseName " +
                "FROM std_Course sc " +
                "JOIN class c ON sc.classId = c.classId " +
                "JOIN course co ON c.crsId = co.courseId " +
                "WHERE sc.StdId = ? AND sc.stat = ? AND sc.abscences IS NOT NULL";


        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, stdId);
            statement.setInt(2, 0);

            try (ResultSet resultSet = statement.executeQuery()) {
            	ClassAbsence class1;
            	while (resultSet.next()) {
            	     class1= new ClassAbsence(
            	        resultSet.getString("courseName"),
            	        resultSet.getString("classDay"),
            	        resultSet.getString("classStartTime"),
            	        resultSet.getString("classEndTime"),
            	        resultSet.getInt("abscences"));

            	    System.out.print(class1.toString());
            	    classes.add(class1);
            	}
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return classes;
}


public List<String> getCoursesNamesFromDatabase(String nameFact) {

	    List<String> crsNames = new ArrayList<>();

	    try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
	        String query = "SELECT courseName FROM course INNER JOIN faculty ON course.facultyId = faculty.facultyId WHERE facultyName = ?";
	        
	        try (PreparedStatement statement = connection.prepareStatement(query)) {
	         
	            statement.setString(1, nameFact);
	            try (ResultSet resultSet = statement.executeQuery()) {
	                while (resultSet.next()) {
	                    String ProfName = resultSet.getString("courseName");
	                    crsNames.add(ProfName);
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return crsNames;
	}


public int getFactId(int id) {
    int facultyId = -1; 
    System.out.println(id);
    try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT facultyId FROM user WHERE userId=?")) {
        preparedStatement.setLong(1, id);

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                facultyId = resultSet.getInt("facultyId");
            }
        }
    } catch (SQLException e) {
        System.err.println("Error getting facultyId: " + e.getMessage());
    }

    return facultyId;
}


public void AddCourseToDB(String crsName, int crsCreds, int factId, int CoordID) {
	
	String query = "INSERT INTO course (credits, courseName,coordinator_id, facultyId) VALUES (?,?,?,?)";
	
    try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
        preparedStatement.setLong(1, crsCreds);
        preparedStatement.setString(2, crsName); 
        preparedStatement.setLong(3, CoordID); 
        preparedStatement.setLong(4, factId);
        preparedStatement.executeUpdate();
        
        try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                System.out.println("Course inserted successfully");
             
            } else {
                System.out.println("Failed to retrieve auto-generated key.");
               
            }
        }
    } catch (SQLException e) {
    		System.out.println("SQL State: " + e.getSQLState());
    	    System.out.println("Error Code: " + e.getErrorCode());
    	    System.out.println("Error Message: " + e.getMessage());
  
    }
	
}
public void AddStudentToDB(String stdName, String email, String password, int factID) {
    String userInsertQuery = "INSERT INTO user (name, email, password, facultyId, isAdmin) VALUES (?, ?, ?, ?, ?)";
    String studentInsertQuery = "INSERT INTO student (stdId, name, email, password, facultyId) VALUES (?, ?, ?, ?, ?)";

    try (PreparedStatement userInsertStatement = connection.prepareStatement(userInsertQuery, Statement.RETURN_GENERATED_KEYS);
         PreparedStatement studentInsertStatement = connection.prepareStatement(studentInsertQuery, Statement.RETURN_GENERATED_KEYS)) {

        userInsertStatement.setString(1, stdName);
        userInsertStatement.setString(2, email);
        userInsertStatement.setString(3, password);
        userInsertStatement.setLong(4, factID);
        userInsertStatement.setLong(5, 0);
        
        userInsertStatement.executeUpdate();

        int affectedRows = userInsertStatement.getUpdateCount();

        if (affectedRows == 0) {
            System.out.println("Failed to insert user. No rows affected.");
            return;
        }

        try (ResultSet generatedKeys = userInsertStatement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);

                studentInsertStatement.setInt(1, id);
                studentInsertStatement.setString(2, stdName);
                studentInsertStatement.setString(3, email);
                studentInsertStatement.setString(4, password);
                studentInsertStatement.setLong(5, factID);

                int studentAffectedRows = studentInsertStatement.executeUpdate();

                if (studentAffectedRows == 0) {
                    System.out.println("Failed to insert student. No rows affected.");
                } else {
                    System.out.println("Student inserted successfully with ID: " + id);
                }
            } else {
                System.out.println("Failed to retrieve auto-generated key for user.");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}




public int getUserIdByName(String userName) {
    int userId = -1; 
    try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
        String query = "SELECT instId FROM instructor WHERE name=?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, userName);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    userId = resultSet.getInt("instId");
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return userId;
}
public int getCourseIdByName(String crsName) {
    int crsId = -1; 
    try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
        String query = "SELECT courseId FROM course WHERE courseName=?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, crsName);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    crsId = resultSet.getInt("courseId");
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return crsId;
}
public void AddInstructorToDB(String name, String email, String password, int factID) {
    String userInsertQuery = "INSERT INTO user (name, email, password, facultyId, isAdmin) VALUES (?, ?, ?, ?, ?)";
    String instructorInsertQuery = "INSERT INTO instructor (instId, name, email, password, facultyId) VALUES (?, ?, ?, ?, ?)";

    try (PreparedStatement userInsertStatement = connection.prepareStatement(userInsertQuery, PreparedStatement.RETURN_GENERATED_KEYS);
         PreparedStatement instructorInsertStatement = connection.prepareStatement(instructorInsertQuery)) {

        userInsertStatement.setString(1, name);
        userInsertStatement.setString(2, email);
        userInsertStatement.setString(3, password);
        userInsertStatement.setInt(4, factID);
        userInsertStatement.setInt(5, 2);
        
        int affectedRows = userInsertStatement.executeUpdate();

        if (affectedRows == 0) {
            System.out.println("Failed to insert user. No rows affected.");
            return;
        }

        int instId = getUserID(email, password); 
        
        instructorInsertStatement.setInt(1, instId);
        instructorInsertStatement.setString(2, name);
        instructorInsertStatement.setString(3, email);
        instructorInsertStatement.setString(4, password);
        instructorInsertStatement.setInt(5, factID);

        int instructorAffectedRows = instructorInsertStatement.executeUpdate();

        if (instructorAffectedRows == 0) {
            System.out.println("Failed to insert instructor. No rows affected.");
        } else {
            System.out.println("Instructor inserted successfully with ID: " + instId);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

private int getUserID(String email, String password) {
	int res;
	try {
		 String sqlQuery = "SELECT * FROM user where email=? and password=?";
         PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
         
         preparedStatement.setString(1, email);
         preparedStatement.setString(2, password);
         
         ResultSet resultSet= preparedStatement.executeQuery();
         if (resultSet.next()) {
             UserID=resultSet.getInt("userId");
             res=UserID;
         } else {
             res=-1;
         }

         resultSet.close();
         preparedStatement.close();

	}catch (SQLException e) {
        res=-1;
    }
	return res;
}


public void AddClassToDB(int crs_id, String code, int room, String day, String startTime, String endTime, int c_id) {

    LocalTime startTimeValue = LocalTime.parse(startTime);
    LocalTime endTimeValue = LocalTime.parse(endTime);
    String query = "INSERT INTO class (crsId,classCode, roomNb,classDay, classStartTime, classEndTime,instId) VALUES (?,?,?,?,?,?,?)";

	
    try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
        preparedStatement.setLong(1, crs_id);
        preparedStatement.setString(2, code); 
        preparedStatement.setLong(3, room); 
        preparedStatement.setString(4, day); 
        preparedStatement.setObject(5, startTimeValue);
        preparedStatement.setObject(6, endTimeValue);
        preparedStatement.setLong(7, c_id);
        preparedStatement.executeUpdate();
        
        try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                System.out.println("Class inserted successfully");
             
            } else {
                System.out.println("Failed to retrieve auto-generated key.");
               
            }
        }
    } catch (SQLException e) {
    		System.out.println("SQL State: " + e.getSQLState());
    	    System.out.println("Error Code: " + e.getErrorCode());
    	    System.out.println("Error Message: " + e.getMessage());
  
    }
	
    
    
    
	
}

public void updateCourseBothFields(int crs_id,int c_id, int credits) {
	String query= "Update course set credits=? ,  coordinator_id =? WHERE courseId=?";
	
	try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
		preparedStatement.setLong(1, credits);
		preparedStatement.setLong(2, c_id);
		preparedStatement.setLong(3, crs_id);
		int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Course updated successfully");
        } else {
            System.out.println("No rows updated. Course with courseId " + crs_id + " not found.");
        }
		
		
	}catch (SQLException e) {
		System.out.println("SQL State: " + e.getSQLState());
	    System.out.println("Error Code: " + e.getErrorCode());
	    System.out.println("Error Message: " + e.getMessage());

}
	
}


public void updateCourseCreditFields(int crs_id,int credits) {
	String query= "Update course set credits=?  WHERE courseId=?";
	
	try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
		preparedStatement.setLong(1, credits);
		preparedStatement.setLong(2, crs_id);
		int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Course updated successfully");
        } else {
            System.out.println("No rows updated. Course with courseId " + crs_id + " not found.");
        }
		
		
	}catch (SQLException e) {
		System.out.println("SQL State: " + e.getSQLState());
	    System.out.println("Error Code: " + e.getErrorCode());
	    System.out.println("Error Message: " + e.getMessage());

}
}

public void updateCourseCoordinatorFields(int crs_id,int c_id) {
String query= "Update course set coordinator_id=?  WHERE courseId=?";
	
	try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
		preparedStatement.setLong(1, c_id);
		preparedStatement.setLong(2, crs_id);
		int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Course updated successfully");
        } else {
            System.out.println("No rows updated. Course with courseId " + crs_id + " not found.");
        }
		
		
	}catch (SQLException e) {
		System.out.println("SQL State: " + e.getSQLState());
	    System.out.println("Error Code: " + e.getErrorCode());
	    System.out.println("Error Message: " + e.getMessage());

}
}
public boolean removeCourseFromDB(int crs_id) {
    boolean res=false;
    try {
        connection.setAutoCommit(false); 

        String removeStdCourseQuery = "DELETE FROM std_course WHERE classId IN (SELECT classId FROM class WHERE crsId = ?)";
        try (PreparedStatement removeStdCourseStmt = connection.prepareStatement(removeStdCourseQuery)) {
            removeStdCourseStmt.setInt(1, crs_id);
            removeStdCourseStmt.executeUpdate();
        }
        String removeClassQuery = "DELETE FROM class WHERE crsId = ?";
        try (PreparedStatement removeClassStmt = connection.prepareStatement(removeClassQuery)) {
            removeClassStmt.setInt(1, crs_id);
            removeClassStmt.executeUpdate();
        }

        String removeCourseQuery = "DELETE FROM course WHERE courseId = ?";
        try (PreparedStatement removeCourseStmt = connection.prepareStatement(removeCourseQuery)) {
            removeCourseStmt.setInt(1, crs_id);
            removeCourseStmt.executeUpdate();
        }
        res=true;
        connection.commit(); 

    } catch (SQLException e) {
        try {
            if (connection != null) {
                connection.rollback();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        e.printStackTrace();
    } finally {
        try {
            if (connection != null) {
                connection.setAutoCommit(true); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    return res;
}
public List<String> getClassesCodesFromDatabase(int crs_id) {
    List<String> classCodes = new ArrayList<>();

    try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
        String query = "SELECT classCode FROM class WHERE crsId = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, crs_id);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String classCode = resultSet.getString("classCode");
                    classCodes.add(classCode);
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return classCodes;

}
public Map<String, Object> GetClassInfo(String code) {
    Map<String, Object> classData = new HashMap<>();

    try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
        String query = "SELECT roomNb, classDay, classStartTime, classEndTime, instId FROM class WHERE classCode=?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, code);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int roomNb = resultSet.getInt("roomNb");
                    String classDay = resultSet.getString("classDay");
                    String classStartTime = resultSet.getString("classStartTime");
                    String classEndTime = resultSet.getString("classEndTime");
                    int instId = resultSet.getInt("instId");

                    classData.put("Room Number", roomNb);
                    classData.put("Class Day", classDay);
                    classData.put("Class Start Time", classStartTime);
                    classData.put("Class End Time", classEndTime);
                    classData.put("Instructor ID", instId);

                } else {
                    System.out.println("No matching records found for the given course code.");
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return classData;
}
public void updateClassFields(Object room, Object start, Object end, Object day, Object inst, String code) {
    String query = "UPDATE class SET roomNb=?, classDay=?, classStartTime=?, classEndTime=?, instId=? WHERE classCode=?";

    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        preparedStatement.setInt(1, (int) room);
        preparedStatement.setString(2, (String) day);
        preparedStatement.setObject(3, start);
        preparedStatement.setObject(4, end);
        preparedStatement.setInt(5, (int) inst);

        preparedStatement.setString(6, code);

        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Class updated successfully");
        } else {
            System.out.println("No rows updated. Class with courseId " + code + " not found.");
        }
    } catch (SQLException e) {
        System.out.println("SQL State: " + e.getSQLState());
        System.out.println("Error Code: " + e.getErrorCode());
        System.out.println("Error Message: " + e.getMessage());
    }
}

public boolean removeClassFromDB(String code) {
    try {
        String getClassIdQuery = "SELECT classId FROM class WHERE classCode=?";
        try (PreparedStatement getClassIdStatement = connection.prepareStatement(getClassIdQuery)) {
            getClassIdStatement.setString(1, code);
            try (ResultSet classIdResultSet = getClassIdStatement.executeQuery()) {
                if (classIdResultSet.next()) {
                    int classId = classIdResultSet.getInt("classId");

                    String removeFromClassQuery = "DELETE FROM std_Course WHERE classId=?";
                    try (PreparedStatement removeFromClassStatement = connection.prepareStatement(removeFromClassQuery)) {
                        removeFromClassStatement.setInt(1, classId);
                        removeFromClassStatement.executeUpdate();
                    }


                    String removeClassQuery = "DELETE FROM class WHERE classId=?";
                    try (PreparedStatement removeClassStatement = connection.prepareStatement(removeClassQuery)) {
                        removeClassStatement.setInt(1, classId);
                        removeClassStatement.executeUpdate();
                    }
                    return true;
                } else {
                    System.out.println("No matching records found for the given class code.");
                    return false;
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

public List<Course> getAllCoursesByFaculty(int factID) {
    List<Course> courses = new ArrayList<>();

    try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
        String query = "SELECT courseId, credits, courseName, coordinator.name AS coordinatorName, faculty.facultyName " +
                       "FROM course JOIN faculty ON course.facultyId = faculty.facultyId " +
                       "JOIN instructor AS coordinator ON course.coordinator_id = coordinator.instId " +
                       "WHERE faculty.facultyId = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, factID);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Course course = new Course();
                    course.setCourseId(resultSet.getInt("courseId"));
                    course.setCredits(resultSet.getInt("credits"));
                    course.setCourseName(resultSet.getString("courseName"));
                    course.setCoordinatorName(resultSet.getString("coordinatorName"));
                    course.setFacultyName(resultSet.getString("facultyName"));

                    courses.add(course);
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return courses;
}
public int getTotalCredByFaculty(int StdId) {
    int creds = -1;
    try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
        String query = "SELECT f.totalcreds " +
                "FROM faculty f " +
                "JOIN student s ON s.facultyId = f.facultyId " +
                "WHERE s.StdId = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, StdId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    creds = resultSet.getInt("totalcreds");
                } else {
                    System.out.println("Student Not Found");
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return creds;
}
public int getEnrolledCreditsStudent(int StdID) {
	int creds=0;
	try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
		String query = "SELECT SUM(co.credits) as totalCreds " +
	            "FROM std_Course sc " +
	            "JOIN class c ON sc.classId = c.classId " +
	            "JOIN course co ON c.crsId = co.courseId " +
	            "WHERE sc.StdId = ? and stat=?";

	    try (PreparedStatement statement = connection.prepareStatement(query)) {
	        statement.setInt(1, StdID);
	        statement.setInt(2, 0);

	        try (ResultSet resultSet = statement.executeQuery()) {
	        	if(resultSet.next()) {
	        		creds=resultSet.getInt("totalCreds");
	        	}
	        	else {

	                System.out.println("Student Not Found " );
	        	}
	        }
		    }
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	return creds;
}
public int getPassedCreditsStudent(int StdID) {
	int creds=0;
	try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
		String query = "SELECT SUM(co.credits) as totalCreds " +
	            "FROM std_Course sc " +
	            "JOIN class c ON sc.classId = c.classId " +
	            "JOIN course co ON c.crsId = co.courseId " +
	            "WHERE sc.StdId = ? and stat=?";

	    try (PreparedStatement statement = connection.prepareStatement(query)) {
	        statement.setInt(1, StdID);
	        statement.setInt(2, 1);

	        try (ResultSet resultSet = statement.executeQuery()) {
	        	if(resultSet.next()) {
	        		creds=resultSet.getInt("totalCreds");
	        	}
	        	else {

	                System.out.println("Student Not Found " );
	        	}
	        }
		    }
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	return creds;
}

public User getStudentInfo(int StdId) {
    User user = null;
    try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
        String query = "SELECT s.name, s.email, f.facultyName " +
                "FROM faculty f " +
                "JOIN student s ON s.facultyId = f.facultyId " +
                "WHERE s.StdId = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, StdId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int totalcreds = getTotalCredByFaculty(StdId);
                    int enrolledcreds = getEnrolledCreditsStudent(StdId);
                    int passedcreds = getPassedCreditsStudent(StdId);
                    double percentage = (enrolledcreds * 100.0) / totalcreds;
                    user = new User(resultSet.getString("name"), resultSet.getString("email"),
                            resultSet.getString("facultyName"),  enrolledcreds, passedcreds, totalcreds,percentage);
                } else {
                    System.out.println("Student Not Found");
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return user;
}
public List<User> getAllStudentsByClass(int classId) {
	
	List<User> students = new ArrayList<>();

	try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
		String query = "SELECT s.stdId, s.name, s.email " +
			    "FROM std_course sc " +
			    "JOIN student s ON s.stdId = sc.StdId " +
			    "WHERE sc.classId = ? AND sc.stat=?";

    try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, classId);
        statement.setInt(2, 0);

        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
            	int id=resultSet.getInt("stdId");
            	String name = resultSet.getString("name");
            	String email = resultSet.getString("email");

            	System.out.println("Name: " + name + ", Email: " + email);

            	User user = new User(id,name, email);
            	students.add(user);
            }
        }
	    }
    
	} catch (SQLException e) {
	    e.printStackTrace();
	}

	return students;
}


public void updateGradeForStudent(int classId, int id, double grade) {
	   String query = "UPDATE std_Course SET grade=? WHERE classId=? AND StdId=?";
	   
	   try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	        preparedStatement.setDouble(1, grade);
	        preparedStatement.setInt(2, classId);
	        preparedStatement.setInt(3, id);
	        int rowsAffected = preparedStatement.executeUpdate();

	        if (rowsAffected > 0) {
	            System.out.println("Grade updated successfully");
	            if (grade>60) {
	            String queryStat = "UPDATE std_Course SET stat=? WHERE classId=? AND StdId=?";
			            try (PreparedStatement preparedStatementStat = connection.prepareStatement(queryStat)) {
			            	preparedStatementStat.setInt(1, 1);
			            	preparedStatementStat.setInt(2, classId);
			            	preparedStatementStat.setInt(3, id);
			    	        int rowsAffectedSat = preparedStatementStat.executeUpdate();
		
			    	        if (rowsAffectedSat > 0) {
			    	            System.out.println("Status updated successfully");
			    	            
			    	        } else {
			    	            System.out.println("No rows updated. Class with courseId " + classId + " not found.");
			    	        }
			            
	    	     }
			            }
	            else {
	            	dropCourse(classId,id);
	            }

	        } else {
	            System.out.println("No rows updated. Class with courseId " + classId + " not found.");
	        }
	    } catch (SQLException e) {
	        System.out.println("SQL State: " + e.getSQLState());
	        System.out.println("Error Code: " + e.getErrorCode());
	        System.out.println("Error Message: " + e.getMessage());
	    }
	
	
}




public List<ClassCourse> getAllClassesByCourse(int crsID) {
    List<ClassCourse> classes = new ArrayList<>();

    try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
    	String query = "SELECT classId, classCode, roomNb, classDay, classStartTime, classEndTime, courseName, name " +
                "FROM class " +
                "JOIN course ON course.courseId = class.crsId " +
                "JOIN instructor ON class.instId = instructor.instId " +
                "WHERE class.crsId = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, crsID);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ClassCourse class1 = new ClassCourse(resultSet.getInt("classId"), resultSet.getString("classCode"),resultSet.getString("courseName"),  resultSet.getInt("roomNb"), resultSet.getString("classDay"), resultSet.getString("classStartTime"), resultSet.getString("classEndTime"), resultSet.getString("name"));
                    
                    classes.add(class1);
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return classes;
}

}

    
