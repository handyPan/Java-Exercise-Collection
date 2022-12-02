/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package handypan.ex00.SchoolManagementSystem;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 *
 * @author handyPan
 * Record the accounts, the students, the courses, the interest groups, the course offerings, the course enrollments, the interest group offerings, the interest group enrollments
 */
public class DataHandler {
    /*
     - data will be written to and read from the database file whenever change happens
    */
    
    /*
     List of data
     - accounts.data : Account Id, Username, Password, [Person Id]
     - person.data : Person Id, Firstname, Lastname, Dob, Address, Role
     - courses.data : Course Id, Name, Department, Hours, Credits
     - interestGroups.data : Interest Group Id, Name, Category
    
     - courseOfferings.data : [Course Id], [Instructor Id], Capacity
     - courseEnrollments.data : [Course Id], [Students Ids]
     - instructorCourseResults.data : [Instructor Id], [Course Id], [Student Id], Final Grade
     - studentCourseResults.data : [Student Id], [Course Id], IsApproved, IsEnrolled, FinalGrade
     - interestGroupOfferings.data : [Interest Group Id], [Leader Id], Capacity
     - interestGroupEnrollments.data : [Interest Group Id], [Person Ids]
     - personInterestGroupsResults.data : [Person Id], [Interest Group Id], IsApproved, IsEnrolled
    */
    
    /*
     - Account.java : id, username, password, personId 
        <- one record in accounts.data: Account Id, Username, Password, [Person Id]
     - AccountHandler.java : accounts
        <- all records in accounts.data
     - Administrator.java : person, accounts, courses, interestGroups
        <- all records in person.data : Person Id, Firstname, Lastname, Dob, Address, Role
        <- all records in accounts.data
        <- all records in courses.data
        <- all records in interestGroups.data
     - Coordinator.java : courseOfferings, courseEnrollments, interestGroupOfferings, interestGroupEnrollments
        <- all records in courseOfferings.data: [Course Id], [Instructor Id], Capacity
        <- all records in courseEnrollments.data : [Course Id], [Student Ids List]
        <- all records in interestGroupOfferings.data : [Interest Group Id], [Leader Id], Capacity
        <- all records in interestGroupEnrollments.data : [Interest Group Id], [Person Ids List]
     - Course.java : id, name, department, hours, credits
        <- one record in courses.data : Course Id, Name, Department, Hours, Credits
     - DataHandler.java : ???????
     - Instructor.java :  courseResults
        <- all records in instructorCourseResults.data : [Instructor Id], [Course Id], [Student Id], Final Grade
     - InterestGroup.java :
        <- one record in interestGroups.data : Interest Group Id, Name, Category
     - Person.java : id, fName, lName, dob, addr, role, interestGroups
        <- one record in person.data
        <- all records of one person in personInterestGroupsResults.data : Person Id], [Interest Group Id], IsApproved, IsEnrolled
     - Student.java : courseResults
        <- all records of one student in studentCourseResults.data : [Student Id], [Course Id], IsApproved, IsEnrolled, FinalGrade
    
    */
    
    /* data list
     ------- Accounts ------- accounts.data -> Account.java, AccountHandler.java, Administrator.java
     Account Id     Username    Password    [Person Id]
     1              admin       1234        1
     2              stu         abcd        9
     3              coo         qwer        3
     4              ins         asdf        6
     5              gst         1234        0
     
     ------- Person ------- person.data -> Administrator.java, Person.java
     Person Id      FirstName   LastName    Dob         Address     Role   
     1              Spider      Man         1926-3-12   USA         Administrator
     2              The         Hulk        1943-4-28   Britain     Student
     3              Professor   X           1908-1-19   Germany     Coordinator
     4              Dead        Pool        1968-10-4   USA         Instructor
     5              Black       Bolt        1921-3-18   Russia      Coordinator
     6              Black       Widow       1979-4-20   Russia      Instructor
     7              Doctor      Strange     1912-11-3   USA         Coordinator
     8              Luke        Cage        1984-3-17   France      Instructor
     9              Jessica     Jones       1949-9-23   Germany     Student
     10             Star        Lord        1938-7-16   Britain     Instructor
    
     ------- Courses ------- courses.data -> Administrator.java, Course.java
     Course Id      Name                            Department                  Hours       Credits
     1              Machine Learning                Computer Science            30          1.0
     2              Pattern Recognition             Computer Science            40          1.5
     3              Big Data                        Computer Science            30          1.0
     4              Deep Learning                   Computer Science            20          0.5
     5              Computer Vision                 Computer Science            40          1.0
     6              Natural Language Processing     Computer Science            40          1.0
     7              Data Structure                  Computer Science            40          1.0
     8              Algorithm                       Computer Science            30          1.0
     9              Computer Networks               Computer Science            30          1.0
     10             Optimization Method             Mathematics                 40          1.0
     11             Calculus                        Mathematics                 60          2.0
     12             Linear Algebra                  Mathematics                 50          1.5
     13             Numerical Method                Mathematics                 40          1.5
     14             Statistics                      Mathematics                 40          1.5
     15             Stochastic Process              Mathematics                 50          2.0
     16             Circuit Theory                  Electrical Engineering      60          2.0
     17             Electronics                     Electrical Engineering      50          2.0
     18             Signal and System               Electrical Engineering      40          1.5      
     
     ------ Interest Groups ------- interestGroups.data -> Administrator.java, InterestGroup.java
     Interest Group Id      Name            Category
     1                      Hiking          Sports
     2                      Basketball      Sports
     3                      Videogame       Games 
     4                      Chess           Games           
     5                      Cooking         Household       
     6                      Planting        Household       
     7                      Reading         Literature      
     8                      Blogging        Literature      
     9                      Movie           Entertainment   
     10                     Music           Entertainment
    
     ------- Course Offerings ------ courseOfferings.data -> Coordinator.java
     [Course Id]        [Instructor Id]     Capacity
     1                  8                   40
     2                  6                   45             
     3                  10                  35
     4                  6                   30
     5                  6                   45    
     6                  8                   35         
     7                  10                  30        
     8                  6                   40        
     9                  10                  35           
     10                 8                   40         
    
     ------- Course Enrollments ------ courseEnrollments.data -> Coordinator.java
     [Course Id]    [Student Ids List]
     1              1, 2, 4
     2              1, 3, 4, 9
     3              1, 3
     4              2
     5              4, 9
     6              3, 4, 9
     7              2, 3
     8              1, 9
     9              1, 2, 3, 9
     10             1, 2, 3, 4, 9
    
     ------- Instructor Course Results ------ instructorCourseResults.data -> Instructor.java
     [Instructor Id]    [Course Id]     [Student Id]    Final Grade
     6                  2               1               82
     6                  2               3               65
     6                  2               4               87
     6                  2               9               73
     8                  1               2               76
     8                  1               4               59
     8                  6               3               92
     8                  6               4               73
     8                  6               9               87
     8                  10              1               45
     8                  10              2               67
     10                 7               3               79
     10                 9               1               90
     10                 9               2               82
     10                 9               3               69
     10                 9               9               78
    
     ------- Student Course Results ------ studentCourseResults.data -> Student.java
     [Student Id]       [Course Id]    Is Approved     Is Enrolled     Final Grade
     1                  2              true            true            82
     3                  2              true            true            65
     4                  2              true            true            87
     9                  2              true            true            73
     2                  4              true            true            76
     4                  5              true            true            38
     9                  5              true            true            64
     1                  8              true            true            38
     9                  8              true            true            82
     1                  1              true            true            90 
     2                  1              true            true            76
     4                  1              true            true            59
     3                  6              true            true            92
    
     ------- Interest Group Offerings ------ interestGroupOfferings.data -> Coordinator.java
     [Interest Group Id]        [Leader Id]     Capacity
     1                          3               20
     2                          5               10
     3                          2               25
     4                          1               15
     5                          4               10
     6                          7               20
     7                          3               30
     8                          2               40
     9                          8               30
     10                         9               20
    
     ------- Interest Group Enrollments ------ interestGroupEnrollments.data -> Coordinator.java
     [Interest Group Id]    [Person Ids List]
     1                      1, 3, 5
     2                      2, 4, 6
     3                      1, 7, 9, 10
     4                      2, 3, 8, 9
     5                      3, 4, 7, 10
     6                      1, 2, 4, 7, 9
     7                      2, 3, 4, 7, 8
     8                      1, 2, 3, 5, 8, 9
     9                      1, 3, 7, 9
     10                     2, 6, 7, 8, 10
    
     ------- Person Interest Groups Results ------ personInterestGroupsResults.data -> Person.java
     [Person Id]    [Interest Group Id]   Is Approved     Is Enrolled
     1              1                     true            true
     3              1                     true            true
     5              1                     true            true
     2              2                     true            true
     4              2                     true            true
     6              2                     true            true
     1              3                     true            true
     7              3                     true            true
     9              3                     true            true
     10             3                     true            true
      
    */    
    
    public DataHandler() {
        
    }

    // AccountHandler - load accounts
    public static Map<Integer, Object[]> loadAccounts() throws IOException {
        // read accounts.data line by line and parse the string to fields
        Map<Integer, Object[]> accounts = new TreeMap<Integer, Object[]>();
        Path path = Paths.get("./data/accounts.data");
        if (Files.exists(path)) {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                // skip the 1st line
                if (lines.indexOf(line) == 0) {
                    continue;
                }
                List<String> fields = Arrays.asList(line.split(";"));
                // accounts: id, username, password, personId
                accounts.put(Integer.parseInt(fields.get(0)), new Object[]{fields.get(1), fields.get(2), Integer.parseInt(fields.get(3))});
            }
            
        }
        return accounts;
    }
    
    // AccountHandler - save accounts
    public static void saveAccounts(Map<Integer, Object[]> accounts) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        String dirName = "./data";
        File dir = new File(dirName);
        // System.out.println(dir.mkdir()? String.format("%s created.", dirName) : String.format("%s already exists.", dirName));
        File f = new File(dirName + "/accounts.data");
        FileOutputStream fos = new FileOutputStream(f);
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
        osw.append("Id,Username,Password,Person ID");
        osw.append("\r\n");
        // loop through accounts
        for (Map.Entry<Integer, Object[]> acc : accounts.entrySet()) {
            osw.append(String.format("%d;%s;%s;%d", acc.getKey(), acc.getValue()[0], acc.getValue()[1], acc.getValue()[2]));
            osw.append("\r\n");
        }
        osw.close();
        fos.close();
    }
    
    // Administrator - load person
    /* // this is a backup for the previous design of data structure of storing 1;3;5;7 as interest grups in person.data as a field, it's updated to the new method below
    public static Map<Integer, Object[]> loadPerson() throws IOException {
        // read person.data line by line and parse the string to fields
        Map<Integer, Object[]> person = new TreeMap<Integer, Object[]>();
        Path path = Paths.get("./data/person.data");
        if (Files.exists(path)) {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                // skip the 1st line
                if (lines.indexOf(line) == 0) {
                    continue;
                }
                List<String> fields = Arrays.asList(line.split(";"));
                // person: id, firstName, lastName, dob, address, interest groups, role
                ArrayList<String> interestGroups = new ArrayList<String>((List<String>)Arrays.asList(fields.get(5).split(",")));
                person.put(Integer.parseInt(fields.get(0)), new Object[]{fields.get(1), fields.get(2), fields.get(3), fields.get(4), interestGroups, fields.get(6)});
            }
        }
        return person;
    }
    */
    public static Map<Integer, Object[]> loadPerson() throws IOException {
        // read person.data line by line and parse the string to fields
        Map<Integer, Object[]> person = new TreeMap<Integer, Object[]>();
        Path path = Paths.get("./data/person.data");
        if (Files.exists(path)) {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                // skip the 1st line
                if (lines.indexOf(line) == 0) {
                    continue;
                }
                List<String> fields = Arrays.asList(line.split(";"));
                // person: id, firstName, lastName, dob, address, role
                person.put(Integer.parseInt(fields.get(0)), new Object[]{fields.get(1), fields.get(2), fields.get(3), fields.get(4), fields.get(5)});
            }
        }
        return person;
    }
    
    // Administrator - save person
    public static void savePerson(Map<Integer, Object[]> person) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        String dirName = "./data";
        File dir = new File(dirName);
        // System.out.println(dir.mkdir()? String.format("%s created.", dirName) : String.format("%s already exists.", dirName));
        File f = new File(dirName + "/person.data");
        FileOutputStream fos = new FileOutputStream(f);
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
        osw.append("Id,FirstName,LastName,Dob,Address,Role");
        osw.append("\r\n");
        // loop through accounts
        for (Map.Entry<Integer, Object[]> p : person.entrySet()) {
            osw.append(String.format("%d;%s;%s;%s;%s;%s", p.getKey(), p.getValue()[0], p.getValue()[1], p.getValue()[2], p.getValue()[3], p.getValue()[4]));
            osw.append("\r\n");
        }
        osw.close();
        fos.close();
    }
    
    // Administrator - load courses
    public static Map<Integer, Object[]> loadCourses() throws IOException {
        // read courses.data line by line and parse the string to fields
        Map<Integer, Object[]> courses = new TreeMap<Integer, Object[]>();
        Path path = Paths.get("./data/courses.data");
        if (Files.exists(path)) {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                // skip the 1st line
                if (lines.indexOf(line) == 0) {
                    continue;
                }
                List<String> fields = Arrays.asList(line.split(";"));
                // courses: id, name, department, hours, credits
                courses.put(Integer.parseInt(fields.get(0)), new Object[]{fields.get(1), fields.get(2), Integer.parseInt(fields.get(3)), Double.parseDouble(fields.get(4))});
            }
        }
        return courses;
    }
    
    // Administrator - save courses
    public static void saveCourses(Map<Integer, Object[]> courses) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        String dirName = "./data";
        File dir = new File(dirName);
        // System.out.println(dir.mkdir()? String.format("%s created.", dirName) : String.format("%s already exists.", dirName));
        File f = new File(dirName + "/courses.data");
        FileOutputStream fos = new FileOutputStream(f);
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
        osw.append("Id,Name,Department,Hours,Credits");
        osw.append("\r\n");
        // loop through courses
        for (Map.Entry<Integer, Object[]> course : courses.entrySet()) {
            osw.append(String.format("%d;%s;%s;%d;%f", course.getKey(), course.getValue()[0], course.getValue()[1], course.getValue()[2], course.getValue()[3]));
            osw.append("\r\n");
        }
        osw.close();
        fos.close();
    }
    
    // Administrator - load interest groups
    public static Map<Integer, Object[]> loadInterestGroups() throws IOException {
        // read interestGroups.data line by line and parse the string to fields
        Map<Integer, Object[]> interestGroups = new TreeMap<Integer, Object[]>();
        Path path = Paths.get("./data/interestGroups.data");
        if (Files.exists(path)) {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                // skip the 1st line
                if (lines.indexOf(line) == 0) {
                    continue;
                }
                List<String> fields = Arrays.asList(line.split(";"));
                // interestGroups: id, name, category
                interestGroups.put(Integer.parseInt(fields.get(0)), new Object[]{fields.get(1), fields.get(2)});
            }
        }
        return interestGroups;
    }
    
    // Administrator - save interest groups
    public static void saveInterestGroups(Map<Integer, Object[]> interestGroups) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        String dirName = "./data";
        File dir = new File(dirName);
        // System.out.println(dir.mkdir()? String.format("%s created.", dirName) : String.format("%s already exists.", dirName));
        File f = new File(dirName + "/interestGroups.data");
        FileOutputStream fos = new FileOutputStream(f);
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
        osw.append("Id,Name,Category");
        osw.append("\r\n");
        // loop through interest groups
        for (Map.Entry<Integer, Object[]> interestGroup : interestGroups.entrySet()) {
            osw.append(String.format("%d;%s;%s", interestGroup.getKey(), interestGroup.getValue()[0], interestGroup.getValue()[1]));
            osw.append("\r\n");
        }
        osw.close();
        fos.close();
    }
    
    // Coordinator - load course offerings
    public static Map<Integer, Object[]> loadCourseOfferings() throws IOException {
        // read courseOfferings.data line by line and parse the string to fields
        Map<Integer, Object[]> courseOfferings = new TreeMap<Integer, Object[]>();
        Path path = Paths.get("./data/courseOfferings.data");
        if (Files.exists(path)) {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                // skip the 1st line
                if (lines.indexOf(line) == 0) {
                    continue;
                }
                List<String> fields = Arrays.asList(line.split(";"));
                // courseOfferings: courseId, instructorId, capacity
                courseOfferings.put(Integer.parseInt(fields.get(0)), new Object[]{Integer.parseInt(fields.get(1)), Integer.parseInt(fields.get(2))});
            }
            
        }
        return courseOfferings;
    }
    
    // Coordinator - save course offerings
    public static void saveCourseOfferings(Map<Integer, Object[]> courseOfferings) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        String dirName = "./data";
        File dir = new File(dirName);
        // System.out.println(dir.mkdir()? String.format("%s created.", dirName) : String.format("%s already exists.", dirName));
        File f = new File(dirName + "/courseOfferings.data");
        FileOutputStream fos = new FileOutputStream(f);
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
        osw.append("Course ID,Instructor ID,Capacity");
        osw.append("\r\n");
        // loop through courseOfferings
        for (Map.Entry<Integer, Object[]> courseOffering : courseOfferings.entrySet()) {
            osw.append(String.format("%d;%d;%d", courseOffering.getKey(), courseOffering.getValue()[0], courseOffering.getValue()[1]));
            osw.append("\r\n");
        }
        osw.close();
        fos.close();
    }
    
    // Coordinator - load course enrollments
    public static Map<Integer, TreeSet<Integer>> loadCourseEnrollments() throws IOException {
        // read courseEnrollments.data line by line and parse the string to fields
        Map<Integer, TreeSet<Integer>> courseEnrollments = new TreeMap<Integer, TreeSet<Integer>>();
        Path path = Paths.get("./data/courseEnrollments.data");
        if (Files.exists(path)) {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                // skip the 1st line
                if (lines.indexOf(line) == 0) {
                    continue;
                }
                List<String> fields = Arrays.asList(line.split(";"));
                List<String> fields2 = new ArrayList<String>();
                Set<Integer> studentIds = new TreeSet<Integer>();
                if (line.contains(",")) {
                    fields2 = Arrays.asList(fields.get(1).split(","));
                    for (String studentId : fields2) {
                        studentIds.add(Integer.parseInt(studentId));
                    }
                }
                // courseEnrollments: courseId, students Id list
                courseEnrollments.put(Integer.parseInt(fields.get(0)), (TreeSet<Integer>) studentIds);
            }
            
        }
        return courseEnrollments;
    }
    
    // Coordinator - save course enrollments
    public static void saveCourseEnrollments(Map<Integer, TreeSet<Integer>> courseEnrollments) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        String dirName = "./data";
        File dir = new File(dirName);
        // System.out.println(dir.mkdir()? String.format("%s created.", dirName) : String.format("%s already exists.", dirName));
        File f = new File(dirName + "/courseEnrollments.data");
        FileOutputStream fos = new FileOutputStream(f);
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
        osw.append("Course ID,Student IDs");
        osw.append("\r\n");
        // loop through courseEnrollments
        for (Map.Entry<Integer, TreeSet<Integer>> courseEnrollment : courseEnrollments.entrySet()) {
            // convert set of integer to set of string
            Set<String> studentIds = courseEnrollment.getValue().stream().map(String::valueOf).collect(Collectors.toSet());
            osw.append(String.format("%d;%s", courseEnrollment.getKey(), String.join(",", studentIds)));
            osw.append("\r\n");
        }
        osw.close();
        fos.close();
    }
    
    // Coordinator - load interest group offerings
    public static Map<Integer, Object[]> loadInterestGroupOfferings() throws IOException {
        // read interestGroupOfferings.data line by line and parse the string to fields
        Map<Integer, Object[]> interestGroupOfferings = new TreeMap<Integer, Object[]>();
        Path path = Paths.get("./data/interestGroupOfferings.data");
        if (Files.exists(path)) {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                // skip the 1st line
                if (lines.indexOf(line) == 0) {
                    continue;
                }
                List<String> fields = Arrays.asList(line.split(";"));
                // courseOfferings: interestGroupId, leaderId, capacity
                interestGroupOfferings.put(Integer.parseInt(fields.get(0)), new Object[]{Integer.parseInt(fields.get(1)), Integer.parseInt(fields.get(2))});
            }
            
        }
        return interestGroupOfferings;
    }
    
    // Coordinator - save interest group offerings
    public static void saveInterestGroupOfferings(Map<Integer, Object[]> interestGroupOfferings) throws FileNotFoundException, UnsupportedEncodingException, IOException  {
        String dirName = "./data";
        File dir = new File(dirName);
        // System.out.println(dir.mkdir()? String.format("%s created.", dirName) : String.format("%s already exists.", dirName));
        File f = new File(dirName + "/interestGroupOfferings.data");
        FileOutputStream fos = new FileOutputStream(f);
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
        osw.append("Interest Group ID,Leader ID,Capacity");
        osw.append("\r\n");
        // loop through interestGroupOfferings
        for (Map.Entry<Integer, Object[]> interestGroupOffering : interestGroupOfferings.entrySet()) {
            osw.append(String.format("%d;%d;%d", interestGroupOffering.getKey(), interestGroupOffering.getValue()[0], interestGroupOffering.getValue()[1]));
            osw.append("\r\n");
        }
        osw.close();
        fos.close();
    }
    
    // Coordinator - load interest group enrollments
    public static Map<Integer, TreeSet<Integer>> loadInterestGroupEnrollments() throws IOException  {
        // read interestGroupEnrollments.data line by line and parse the string to fields
        Map<Integer, TreeSet<Integer>> interestGroupEnrollments = new TreeMap<Integer, TreeSet<Integer>>();
        Path path = Paths.get("./data/interestGroupEnrollments.data");
        if (Files.exists(path)) {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                // skip the 1st line
                if (lines.indexOf(line) == 0) {
                    continue;
                }
                List<String> fields = Arrays.asList(line.split(";"));
                List<String> fields2 = new ArrayList<String>();
                Set<Integer> personIds = new TreeSet<Integer>();
                if (line.contains(",")) {
                    fields2 = Arrays.asList(fields.get(1).split(","));
                    for (String personId : fields2) {
                        personIds.add(Integer.parseInt(personId));
                    }
                }
                // interestGroupEnrollments: interestGroupId, person Id list
                interestGroupEnrollments.put(Integer.parseInt(fields.get(0)), (TreeSet<Integer>) personIds);
            }
            
        }
        return interestGroupEnrollments;
    }
    
    // Coordinator - save interest group enrollments
    public static void saveInterestGroupEnrollments(Map<Integer, TreeSet<Integer>> interestGroupEnrollments) throws FileNotFoundException, UnsupportedEncodingException, IOException  {
        String dirName = "./data";
        File dir = new File(dirName);
        // System.out.println(dir.mkdir()? String.format("%s created.", dirName) : String.format("%s already exists.", dirName));
        File f = new File(dirName + "/interestGroupEnrollments.data");
        FileOutputStream fos = new FileOutputStream(f);
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
        osw.append("Interest Group ID,Person IDs");
        osw.append("\r\n");
        // loop through interestGroupEnrollments
        for (Map.Entry<Integer, TreeSet<Integer>> interestGroupEnrollment : interestGroupEnrollments.entrySet()) {
            // convert set of integer to set of string
            Set<String> personIds = interestGroupEnrollment.getValue().stream().map(String::valueOf).collect(Collectors.toSet());
            osw.append(String.format("%d;%s", interestGroupEnrollment.getKey(), String.join(",", personIds)));
            osw.append("\r\n");
        }
        osw.close();
        fos.close();
    }
    
    // Coordinator - load student course results
    /*
    public static Map<Integer, Object> loadStudentCourseResults() throws IOException {
        // read studentCourseResults.data line by line and parse the string to fields
        Map<Integer, Object[]> studentCourseResult = new TreeMap<Integer, Object[]>();
        Map<Integer, Object> studentCourseResults = new TreeMap<Integer, Object>();
        Path path = Paths.get("./data/studentCourseResults.data");
        if (Files.exists(path)) {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                // skip the 1st line
                if (lines.indexOf(line) == 0) {
                    continue;
                }
                List<String> fields = Arrays.asList(line.split(";"));
                // studentCourseResults: student id, course id, is approved, is enrolled, final grade
                studentCourseResult.put(Integer.parseInt(fields.get(1)), new Object[]{Boolean.parseBoolean(fields.get(2)), Boolean.parseBoolean(fields.get(3)), Integer.parseInt(fields.get(4))});
                studentCourseResults.put(Integer.parseInt(fields.get(0)), studentCourseResult);
            }
        }
        System.out.println(studentCourseResults);
        return studentCourseResults;
    }
    */
    
    public static Map<Integer, Object> loadStudentCourseResults() throws IOException {
        // read studentCourseResults.data line by line and parse the string to fields
        Map<Integer, Object[]> studentCourseResult = new TreeMap<Integer, Object[]>();
        Map<Integer, Object> studentCourseResults = new TreeMap<Integer, Object>();
        Path path = Paths.get("./data/studentCourseResults.data");
        if (Files.exists(path)) {
            List<String> lines = Files.readAllLines(path);
            Set<String> studentIds = new TreeSet<String>();
            // obtain student ids
            for (String line : lines) {
                // skip the 1st line
                if (lines.indexOf(line) == 0) {
                    continue;
                }
                List<String> fields = Arrays.asList(line.split(";"));
                studentIds.add(fields.get(0));
            }
            // for each student id, create the map
            for (String studentId : studentIds) {
                studentCourseResults.put(Integer.parseInt(studentId), studentCourseResult);
            }
            // parse the course registration and save to map 
            for (String line : lines) {
                // skip the 1st line
                if (lines.indexOf(line) == 0) {
                    continue;
                }
                List<String> fields = Arrays.asList(line.split(";"));
                ObjectMapper om = new ObjectMapper();
                Object obj = studentCourseResults.get(Integer.parseInt(fields.get(0)));
                Map<Integer, Object[]> courseResult = om.convertValue(
                    obj,
                    new TypeReference<Map<Integer, Object[]>>(){}
                );
                courseResult.put(Integer.parseInt(fields.get(1)), new Object[]{Boolean.parseBoolean(fields.get(2)), Boolean.parseBoolean(fields.get(3)), Integer.parseInt(fields.get(4))});
                studentCourseResults.put(Integer.parseInt(fields.get(0)), courseResult); 
            }
        }
        return studentCourseResults;
    }
    
    // Coordinator - save student course results
    public static void saveStudentCourseResults(Map<Integer, Object> studentCourseResults) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        String dirName = "./data";
        File dir = new File(dirName);
        // System.out.println(dir.mkdir()? String.format("%s created.", dirName) : String.format("%s already exists.", dirName));
        File f = new File(dirName + "/studentCourseResults.data");
        FileOutputStream fos = new FileOutputStream(f);
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
        osw.append("Student Id,Course Id,Is Approved,Is Enrolled,Final Grade");
        osw.append("\r\n");
        ObjectMapper om = new ObjectMapper();
        // loop through student course results
        for (Map.Entry<Integer, Object> studentCourseResult : studentCourseResults.entrySet()) {
            Object obj = studentCourseResult.getValue();
            //Map<Integer, Object[]> record = om.convertValue(obj, Map.class);
            Map<Integer, Object[]> records = om.convertValue(
                    obj,
                    new TypeReference<Map<Integer, Object[]>>(){}
            );
            for (Map.Entry<Integer, Object[]> record : records.entrySet()) {
                osw.append(String.format("%d;%d;%b;%b;%d", studentCourseResult.getKey(), record.getKey(), record.getValue()[0], record.getValue()[1], record.getValue()[2]));
                osw.append("\r\n");
            }
        }
        osw.close();
        fos.close();
    }
    
    // Coordinator - load person interest group results
    public static Map<Integer, Object> loadPersonInterestGroupResults() throws IOException {
        // read personInterestGroupResults.data line by line and parse the string to fields
        Map<Integer, Object[]> personInterestGroupResult = new TreeMap<Integer, Object[]>();
        Map<Integer, Object> personInterestGroupResults = new TreeMap<Integer, Object>();
        Path path = Paths.get("./data/personInterestGroupResults.data");
        if (Files.exists(path)) {
            List<String> lines = Files.readAllLines(path);
            Set<String> personIds = new TreeSet<String>();
            // obtain person ids
            for (String line : lines) {
                // skip the 1st line
                if (lines.indexOf(line) == 0) {
                    continue;
                }
                List<String> fields = Arrays.asList(line.split(";"));
                personIds.add(fields.get(0));
            }
            // for each person id, create the map
            for (String personId : personIds) {
                personInterestGroupResults.put(Integer.parseInt(personId), personInterestGroupResult);
            }
            // parse the interest group registration and save to map
            for (String line : lines) {
                // skip the 1st line
                if (lines.indexOf(line) == 0) {
                    continue;
                }
                List<String> fields = Arrays.asList(line.split(";"));
                ObjectMapper om = new ObjectMapper();
                Object obj = personInterestGroupResults.get(Integer.parseInt(fields.get(0)));
                Map<Integer, Object[]> interestGroupResult = om.convertValue(
                        obj, 
                        new TypeReference<Map<Integer, Object[]>>(){}
                );
                // personInterestGroupsResults: person id, interest group id, is approved, is enrolled
                interestGroupResult.put(Integer.parseInt(fields.get(1)), new Object[]{Boolean.parseBoolean(fields.get(2)), Boolean.parseBoolean(fields.get(3))});
                personInterestGroupResults.put(Integer.parseInt(fields.get(0)), interestGroupResult);
            }
        }
        return personInterestGroupResults;
    }
    
    // Coordinator - save person interest group results
    public static void savePersonInterestGroupResults(Map<Integer, Object> personInterestGroupResults) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        String dirName = "./data";
        File dir = new File(dirName);
        // System.out.println(dir.mkdir()? String.format("%s created.", dirName) : String.format("%s already exists.", dirName));
        File f = new File(dirName + "/personInterestGroupResults.data");
        FileOutputStream fos = new FileOutputStream(f);
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
        osw.append("Person Id,Interest Group Id,Is Approved,Is Enrolled");
        osw.append("\r\n");
        ObjectMapper om = new ObjectMapper();
        // loop through person interest group results
        for (Map.Entry<Integer, Object> personInterestGroupResult : personInterestGroupResults.entrySet()) {
            Object obj = personInterestGroupResult.getValue();
            Map<Integer, Object[]> records = om.convertValue(
                    obj,
                    new TypeReference<Map<Integer, Object[]>>(){}
            );
            for (Map.Entry<Integer, Object[]> record : records.entrySet()) {
                osw.append(String.format("%d;%d;%b;%b", personInterestGroupResult.getKey(), record.getKey(), record.getValue()[0], record.getValue()[1]));
                osw.append("\r\n");
            }
        }
        osw.close();
        fos.close();
    }
    
    // Instructor - load instructor course results
    public static Map<Integer, Object> loadInstructorCourseResults() throws IOException {
        // read instructorCourseResults.data line by line and parse the string to fields
        // for each instructor id, create the maps, the structure is as follows:
        //      instructorGrades = {instructorId:courseGrades}
        //      courseGrades = {courseId:studentGrades}
        //      studentGrades = {studentId:finalGrade}
        Map<Integer, Integer> studentGrades = new TreeMap<Integer, Integer>();
        Map<Integer, Object> courseGrades = new TreeMap<Integer, Object>();
        Map<Integer, Object> instructorGrades = new TreeMap<Integer, Object>();
        
        ObjectMapper om = new ObjectMapper();
        Path path = Paths.get("./data/instructorCourseResults.data");
        if (Files.exists(path)) {
            List<String> lines = Files.readAllLines(path);
            Set<String> instructorIds = new TreeSet<String>();
            // obtain instructor ids
            for (String line : lines) {
                // skip the 1st line
                if (lines.indexOf(line) == 0) {
                    continue;
                }
                List<String> fields = Arrays.asList(line.split(";"));
                if (instructorGrades.get(Integer.parseInt(fields.get(0))) == null) {
                    instructorGrades.put(Integer.parseInt(fields.get(0)), new TreeMap<Integer, Object>());
                }
                Object objCourseGrades = instructorGrades.get(Integer.parseInt(fields.get(0)));
                courseGrades = om.convertValue(
                    objCourseGrades,
                    new TypeReference<Map<Integer, Object>>(){}
                );
                if (courseGrades.get(Integer.parseInt(fields.get(1))) == null) {
                    courseGrades.put(Integer.parseInt(fields.get(1)), new TreeMap<Integer, Integer>());
                }
                Object objStudentGrades = courseGrades.get(Integer.parseInt(fields.get(1)));
                studentGrades = om.convertValue(
                    objStudentGrades,
                    new TypeReference<Map<Integer, Integer>>(){}
                );
                studentGrades.put(Integer.parseInt(fields.get(2)), Integer.parseInt(fields.get(3)));
                courseGrades.put(Integer.parseInt(fields.get(1)), studentGrades);
                instructorGrades.put(Integer.parseInt(fields.get(0)), courseGrades);
            }
        }
        // System.out.println(instructorGrades);
        return instructorGrades;
    }
    
    // Instructor - save instructor course results
    public static void saveInstructorCourseResults(Map<Integer, Object> instructorCourseResults) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        String dirName = "./data";
        File dir = new File(dirName);
        // System.out.println(dir.mkdir()? String.format("%s created.", dirName) : String.format("%s already exists.", dirName));
        File f = new File(dirName + "/instructorCourseResults.data");
        FileOutputStream fos = new FileOutputStream(f);
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
        osw.append("Instructor Id,Course Id,Student Id,Final Grade");
        osw.append("\r\n");
        ObjectMapper om = new ObjectMapper();
        // loop through instructor course results
        for (Map.Entry<Integer, Object> instructorCourseResult : instructorCourseResults.entrySet()) {
            Object objCourseGrades = instructorCourseResult.getValue();
            Map<Integer, Object> courseGrades = om.convertValue(
                    objCourseGrades,
                    new TypeReference<Map<Integer, Object>>(){}
            );
            for (Map.Entry<Integer, Object> courseGrade : courseGrades.entrySet()) {
                Object objStudentGrades = courseGrade.getValue();
                Map<Integer, Integer> studentGrades = om.convertValue(
                        objStudentGrades,
                        new TypeReference<Map<Integer, Integer>>(){}
                );
                for (Map.Entry<Integer, Integer> studentGrade : studentGrades.entrySet()) {
                    osw.append(String.format("%d;%d;%d;%d", instructorCourseResult.getKey(), courseGrade.getKey(), studentGrade.getKey(), studentGrade.getValue()));
                    osw.append("\r\n");
                }
            }
        }
        osw.close();
        fos.close();
    }
    
}
