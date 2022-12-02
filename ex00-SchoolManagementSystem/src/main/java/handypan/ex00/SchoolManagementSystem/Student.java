/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package handypan.ex00.SchoolManagementSystem;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Pattern;
import org.apache.commons.lang3.math.NumberUtils;

/**
 *
 * @author handyPan
 */
public class Student extends Person implements UndergraduateActivity, GraduateActivity {
    // student register a course, it will be approved by the coordinator or not, and it will have a final grade or not;
    
    // course offerings - the data structure is courseId, instructorId, capacity
    // ex. 1, 1, 10; 2, 4, 15; 3, 1, 20; 4, 3, 15;
    // course description for above example: 1 - "Pattern Recognition", 2 - "Machine Learning", 3 - "Computer Vision", 4 - "Big Data"
    private static Map<Integer, Object[]> courseOfferings; 
    
    // course enrollments - the data structure is courseId, students Id list
    // ex. 1={1, 3, 4, 7, 9}, 2={1, 2, 3, 7}
    private static Map<Integer, TreeSet<Integer>> courseEnrollments;
    
    // course results - the data structure is studentId, courseId, isApproved, isEnrolled, finalGrade
    // ex. 1, 1, True, True, 89; 1, 2, True, True, 0; 2, 3, True, False, -1; 3, 4, False, False, -1;
    // course description for above example: 1 - "Pattern Recognition", 2 - "Machine Learning", 3 - "Computer Vision", 4 - "Big Data"
    private Map<Integer, Object> studentCourseResults;     
    
    // instructor course results - the data structure is instructorId, courseId, studentId, finalGrade
    // ex. 1={2={1={82}, 2={65}}, 4={1={38}, 2={76}}, 2={1={1={-1}, 2={81}}}
    private static Map<Integer, Object> instructorCourseResults;
    
    private Scanner scan = new Scanner(System.in);

    public Student(String _role, boolean directlyAssign, String... params) throws ParseException, IOException {
        super(_role, directlyAssign, params);
        
        // populate course offerings
        courseOfferings = DataHandler.loadCourseOfferings();
        
        // populate course enrollments
        courseEnrollments = DataHandler.loadCourseEnrollments();
        
        // populate course results
        studentCourseResults = DataHandler.loadStudentCourseResults();
        
        // populate instructor course results
        instructorCourseResults = DataHandler.loadInstructorCourseResults();
    }
    
    public void registerCourse() throws UnsupportedEncodingException, IOException {
        while (true) {
            System.out.print("Continue with course registration?(Y/n)");
            String op = scan.nextLine();
            if (!(op.equals("Y") || op.equals("y") || op.equals(""))) {
                break;
            }
            int studentId = super.getId();
            if (courseOfferings.keySet().isEmpty()) {
                System.out.println("No course offering exists.");
                continue;
            }
            Utils.getCourseOfferingsInfo(courseOfferings);
            System.out.print("Input the course id to register:");
            String courseIdIn = scan.nextLine();
            if (courseIdIn.equals("") || !Pattern.matches("[1-9][0-9]*", courseIdIn)) {
                    System.out.println("Course Id is not correct!");
                    continue;
            }
            int courseId = Integer.parseInt(courseIdIn);
            if (!courseOfferings.keySet().contains(courseId)) {
                System.out.println(String.format("Course %d is not offered.", courseId));
                continue;
            }
            boolean courseRegistered = false;
            ObjectMapper om = new ObjectMapper();
            Map<Integer, Object[]> recordToSave = new TreeMap<Integer, Object[]>();
            for (Map.Entry<Integer, Object> courseResult : studentCourseResults.entrySet()) {
                Object obj = courseResult.getValue();
                Map<Integer, Object[]> records = om.convertValue(
                        obj,
                        new TypeReference<Map<Integer, Object[]>>(){}
                );
                if (courseResult.getKey() == studentId) {
                    recordToSave = records;
                    for (Map.Entry<Integer, Object[]> record : records.entrySet()) {
                        if (record.getKey() == courseId) {
                            courseRegistered = true;
                            break;
                        }
                    }
                }
            }
            recordToSave.put(courseId, new Object[]{false, false, -1});
            if (courseRegistered) {
                System.out.print("Course Already registered! Update it?(Y/n)");
                op = scan.nextLine();
                if (!(op.equals("Y") || op.equals("y") || op.equals(""))) {
                    continue;
                }
                // studentId, courseId, isApproved, isEnrolled, finalGrade
                studentCourseResults.put(studentId, recordToSave);
                DataHandler.saveStudentCourseResults(studentCourseResults);
                System.out.println("Student Course Registration updated successfully!");
                continue;
            }
            studentCourseResults.put(studentId, recordToSave);
            DataHandler.saveStudentCourseResults(studentCourseResults);
            System.out.println("Student Course Registration added successfully!");   
        }
        
    }
    
    public void dropCourse() throws UnsupportedEncodingException, IOException {
        while (true) {
            System.out.print("Continue with dropping course?(Y/n)");
            String op = scan.nextLine();
            if (!(op.equals("Y") || op.equals("y") || op.equals(""))) {
                break;
            }
            int studentId = super.getId();
            ObjectMapper om = new ObjectMapper();
            Object obj = studentCourseResults.get(studentId);
            Map<Integer, Object[]> records = om.convertValue(
                    obj,
                    new TypeReference<Map<Integer, Object[]>>(){}
            );
            if (records.keySet().isEmpty()) {
                System.out.println(String.format("No course exists for student %d.", studentId));
                continue;
            }
            Utils.getCourseRegisrationInfo(studentCourseResults, studentId);
            System.out.print("Input the course id to drop:");
            String courseIdIn = scan.nextLine();
            if (courseIdIn.equals("") || !Pattern.matches("[1-9][0-9]*", courseIdIn)) {
                    System.out.println("Course Id is not correct!");
                    continue;
            }
            int courseId = Integer.parseInt(courseIdIn);
            if (!records.keySet().contains(courseId)) {
                System.out.println(String.format("Course %d does not exist.", courseId));
                continue;
            }
            System.out.println(String.format("Course Id %d registration info: Is Approved - %b, Is Enrolled - %b, Final Grade - %d", courseId, records.get(courseId)[0], records.get(courseId)[1], records.get(courseId)[2]));
            System.out.print(String.format("Please confirm to delete course %d of student %d: [y/N]", courseId, studentId));
            op = scan.nextLine();
            if (!op.equals("Y") && !op.equals("y")) {
                continue;
            }
            records.remove(courseId);
            studentCourseResults.put(studentId, records);
            
            // drop the record with the courseId and studentId
            TreeSet<Integer> courseEnrolledStudents = new TreeSet<Integer>();
            courseEnrolledStudents = courseEnrollments.get(courseId);
            courseEnrolledStudents.remove(studentId);
            courseEnrollments.put(courseId, courseEnrolledStudents);
            
            // remove course grades of the student in instructorCourseResults.data
            Map<Integer, Object> courseGrades = new TreeMap<Integer, Object>();
            
            int instructorId = (int) courseOfferings.get(courseId)[0];
            if (instructorCourseResults.get(instructorId) != null) {
                Object objCourseGrades = instructorCourseResults.get(instructorId);
                courseGrades = om.convertValue(
                    objCourseGrades,
                    new TypeReference<Map<Integer, Object>>(){}
                );
                if (courseGrades.get(courseId) != null) {
                    Object objStudentGrades = courseGrades.get(courseId);
                    Map<Integer, Integer> studentGrades = om.convertValue(
                        objStudentGrades,
                        new TypeReference<Map<Integer, Integer>>(){}
                    );
                    studentGrades.remove(studentId);
                    courseGrades.put(courseId, studentGrades);
                    instructorCourseResults.put(instructorId, courseGrades);
                }
            }
            
            // write to data file
            DataHandler.saveCourseEnrollments(courseEnrollments);
            DataHandler.saveStudentCourseResults(studentCourseResults);
            DataHandler.saveInstructorCourseResults(instructorCourseResults);
            System.out.println(String.format("Course %d dropped successfully!", courseId));
        }
    } 

    public Map<Integer, Object> getCourseResults() {
        return studentCourseResults;
    }
    
    public Set getCourses() {
        return studentCourseResults.keySet();
    }
    
    public void registerCourse(Integer courseId) {
        // ex. 4, False, False, -1;
        // 4 - "Advanced Python"
        studentCourseResults.put(courseId, new Object[]{false, false, -1});   
    }
    
    public void dropCourse(Integer courseId) {
        studentCourseResults.remove(courseId);
    }
    
    public Object getCourseResult(Integer courseId) {
        return studentCourseResults.get(courseId);
    }
    
    @Override
    public String toString() {
        return super.toString() + "\n" + "Student2{" + "studentCourseResults=" + studentCourseResults  + '}';
    }
    
    @Override
    public String getInfo() {
        String info =  super.getInfo() + "\n";
        // traverse studentCourseResults - map
        info += "Course Results - ";
        ObjectMapper om = new ObjectMapper();
        for (Map.Entry<Integer, Object> courseResult : studentCourseResults.entrySet()) {
            Object obj = courseResult.getValue();
            Map<Integer, Object[]> records = om.convertValue(
                    obj,
                    new TypeReference<Map<Integer, Object[]>>(){}
            );
            for (Map.Entry<Integer, Object[]> record : records.entrySet()) {
                info += String.format("%d, %d, %b, %b, %d;", courseResult.getKey(), record.getKey(), record.getValue()[0], record.getValue()[1], record.getValue()[2]);
            }
        }
        return info;
    }
    
    public static void main(String[] args) throws ParseException, IOException {
        // test the class
        Student stu = new Student("Student", true, "0003", "Black", "Bolt", "1921-3-18", "Russia");
        System.out.println(stu.toString());
        System.out.println(stu.getInfo());
        
        stu.registerCourse();
        stu.dropCourse();
        
        stu = new Student("Student", true, "0004", "Black", "Widow", "1979-4-20", "Russia");
        System.out.println(stu.toString());
        System.out.println(stu.getInfo());
        stu.registerCourse();
        stu.dropCourse();
        
        stu = new Student("Student", true, "0006", "The", "Hulk", "1943-4-28", "Britain");
        System.out.println(stu.toString());
        System.out.println(stu.getInfo());
        stu.registerCourse();
        stu.dropCourse();
        
        // output results
        System.out.println(stu.toString());
        System.out.println(stu.getInfo());
        System.out.println("Courses - " + stu.getCourses());
        System.out.println("Course Results - " + stu.getCourseResults());
        System.out.println("Interests - " + stu.getInterests());
        System.out.println("Interest Groups - " + stu.getPersonInterestGroups());
        
        // test implementation of the interface methods
        stu.haveMeals();
        stu.doSports();
        stu.goShopping();
        
        stu.takeLectures(60);
        stu.doAssignments(4);
        stu.writeExams(2);
        
        stu.doCapstone("Application of Machine Learning");
        stu.doCoop(200);
        
        stu.readLiterature();
        stu.writePaper(2);
        stu.doDefense();
    
    }
    
    // below are all methods from the interfaces

    @Override
    public void doCapstone(String topic) {
        System.out.println(String.format("Capstone topic: %s", topic));
    }

    @Override
    public void doCoop(int hours) {
        System.out.println(String.format("Coop hours: %d", hours));
    }

    @Override
    public void haveMeals() {
        System.out.println("Have meals");
    }

    @Override
    public void doSports() {
        System.out.println("Do sports");
    }

    @Override
    public void goShopping() {
        System.out.println("Go shopping");
    }

    @Override
    public void takeLectures(int hours) {
        System.out.println(String.format("Lecture hours: %d", hours));
    }

    @Override
    public void doAssignments(int amount) {
        System.out.println(String.format("Assignment times: %d", amount));
    }

    @Override
    public void writeExams(int amount) {
        System.out.println(String.format("Exam times: %d", amount));
    }

    @Override
    public void readLiterature() {
        System.out.println("Read Literature");
    }

    @Override
    public void writePaper(int amount) {
        System.out.println(String.format("Write paper: %d", amount));
    }

    @Override
    public void doDefense() {
        System.out.println("Do defense");
    }
    
}
