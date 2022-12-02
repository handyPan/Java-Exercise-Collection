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
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.lang3.math.NumberUtils;

/**
 *
 * @author handyPan
 */

public class Instructor extends Person {
    // instructor report course results
    // the data structure is {courseId={studentId=finalGrade, studentId=finalGrade,...}, courseId={studentId=finalGrade, studentId=finalGrade,...},...}
    // ex. {1={1=82, 2=65, 3=72}, 2={1=76, 2=48, 3=90, 4=71}}
    // description fro above example: "Machine Learning", {"John Doe"=89, "Jack Dean"=72, "Josh Dawn"=45}; "Computer Vision", {"Jerry Daniels"=92, "John Doe"=74, "Jocob Duke"=43};     
    private Map<Integer, Object> courseResults = new TreeMap<Integer, Object>(); 
    
    // student course results - the data structure is studentId, courseId, isApproved, isEnrolled, finalGrade
    // ex. 1={2={true, true, 82}, 8={true, true, 38}}, 2={1={true, false, -1}}
    private static Map<Integer, Object> studentCourseResults;
    
    // instructor course results - the data structure is instructorId, courseId, studentId, finalGrade
    // ex. 1={2={1={82}, 2={65}}, 4={1={38}, 2={76}}, 2={1={1={-1}, 2={81}}}
    private static Map<Integer, Object> instructorCourseResults;
    
    private Scanner scan = new Scanner(System.in);

    public Instructor(String _role, boolean directlyAssign, String... params) throws ParseException, IOException {
        super(_role, directlyAssign, params);
        
        // populate student course results
        studentCourseResults = DataHandler.loadStudentCourseResults();
        
        // populate instructor course results
        instructorCourseResults = DataHandler.loadInstructorCourseResults();
        
    }

    public Map<Integer, Object> getCourseResults() {
        return courseResults;
    }
    
    public Set getCourses() {
        return courseResults.keySet();
    }

    public void reportCourseResult() throws UnsupportedEncodingException, IOException {
        while (true) {
            System.out.print("Continue with reporting course result?(Y/n)");
            String op = scan.nextLine();
            if (!(op.equals("Y") || op.equals("y") || op.equals(""))) {
                break;
            }
            // determine whether the instructor has courses results to report
            int instructorId = super.getId();
            if (!instructorCourseResults.keySet().contains(instructorId)) {
                System.out.println(String.format("No course results to report for instructor %d", instructorId));
                continue;
            }
            Utils.getInstructorCourseResultsInfo(instructorCourseResults, instructorId);
            ObjectMapper om = new ObjectMapper();
            Object objCourseGrades = instructorCourseResults.get(instructorId);
            Map<Integer, Object> courseGrades = om.convertValue(
                objCourseGrades,
                new TypeReference<Map<Integer, Object>>(){}
            );
            System.out.print("Input the course id to report:");
            String courseIdIn = scan.nextLine();
            if (courseIdIn.equals("") || !Pattern.matches("[1-9][0-9]*", courseIdIn)) {
                    System.out.println("Course Id is not correct!");
                    continue;
            }
            int courseId = Integer.parseInt(courseIdIn);
            if (!courseGrades.keySet().contains(courseId)) {
                System.out.println(String.format("No course %d to report results for instructor %d", courseId, instructorId));
                continue;
            }
            Object objStudentGrades = courseGrades.get(courseId);
            Map<Integer, Integer> studentGrades = om.convertValue(
                objStudentGrades,
                new TypeReference<Map<Integer, Integer>>(){}
            );
            Map<Integer, Integer> studentGradesToSave = om.convertValue(
                objStudentGrades,
                new TypeReference<Map<Integer, Integer>>(){}
            );
            System.out.print("Input the student id to report:");
            String studentIdIn = scan.nextLine();
            if (studentIdIn.equals("") || !Pattern.matches("[1-9][0-9]*", studentIdIn)) {
                    System.out.println("Student Id is not correct!");
                    continue;
            }
            int studentId = Integer.parseInt(studentIdIn);
            if (!studentGrades.keySet().contains(studentId)) {
                System.out.println(String.format("No course %d and student %d to report results for instructor %d", courseId, studentId, instructorId));
                continue;
            }
            System.out.print(String.format("Input the final grade of student %d of course %d:", studentId, courseId));
            String finalGradeIn = scan.nextLine();
            if (finalGradeIn.equals("") || !Pattern.matches("^(0|[1-9][0-9]*)$", finalGradeIn)) {
                    System.out.println("Final grade is not correct!");
                    continue;
            }
            studentGradesToSave.put(studentId, Integer.parseInt(finalGradeIn));
            // verify whether the student grade is already reported, if yes, ask whether to modify it or cancel
            boolean studentGradeReported = false;
            
            for (Map.Entry<Integer, Integer> studentGrade : studentGrades.entrySet()) {
                if ((studentGrade.getKey() == studentId) && (studentGrade.getValue() != -1)) {
                    studentGradeReported = true;
                    break;
                }
            }
            if (studentGradeReported) {
                System.out.print("Course Already reported! Update it?(Y/n)");
                op = scan.nextLine();
                if (!(op.equals("Y") || op.equals("y") || op.equals(""))) {
                    continue;
                }
                // instructorId, courseId, studentId, finalGrade
                courseGrades.put(courseId, studentGradesToSave);
                instructorCourseResults.put(instructorId, courseGrades);
                Object objStudentCourseResult = studentCourseResults.get(studentId);
                Map<Integer, Object[]> mapStudentCourseResult = om.convertValue(
                    objStudentCourseResult,
                    new TypeReference<Map<Integer, Object[]>>(){}
                );
                mapStudentCourseResult.put(courseId, new Object[]{mapStudentCourseResult.get(courseId)[0], mapStudentCourseResult.get(courseId)[1], Integer.parseInt(finalGradeIn)});
                studentCourseResults.put(studentId, mapStudentCourseResult);
                
                DataHandler.saveInstructorCourseResults(instructorCourseResults);
                DataHandler.saveStudentCourseResults(studentCourseResults);
                System.out.println(String.format("Final grade updated successfully for student %d of course %d!", studentId, courseId));
                continue;
            }
            
            courseGrades.put(courseId, studentGradesToSave);
            instructorCourseResults.put(instructorId, courseGrades);
            Object objStudentCourseResult = studentCourseResults.get(studentId);
            Map<Integer, Object[]> mapStudentCourseResult = om.convertValue(
                objStudentCourseResult,
                new TypeReference<Map<Integer, Object[]>>(){}
            );
            mapStudentCourseResult.put(courseId, new Object[]{mapStudentCourseResult.get(courseId)[0], mapStudentCourseResult.get(courseId)[1], Integer.parseInt(finalGradeIn)});
            studentCourseResults.put(studentId, mapStudentCourseResult);

            DataHandler.saveInstructorCourseResults(instructorCourseResults);
            DataHandler.saveStudentCourseResults(studentCourseResults);
            System.out.println(String.format("Final grade reported successfully for student %d of course %d!", studentId, courseId));
        }
        
    }

    @Override
    public String toString() {
        return super.toString() + "\n" + "Instructor2{" + "courseResults=" + courseResults + '}';
    }

    @Override
    public String getInfo() {
        String info = super.getInfo() + "\n";
        // traverse courseResouts - map
        info += "Course Results - ";
        for (Map.Entry<Integer, Object> courseResult : courseResults.entrySet()) {
            info += courseResult.getKey().toString() + ", ";
            Object obj = courseResult.getValue();
            ObjectMapper om = new ObjectMapper();
            // Map<Integer, Integer> studentResults = om.convertValue(obj, Map.class);
            Map<Integer, Integer> studentResults = om.convertValue(
                    obj, 
                    new TypeReference<Map<Integer, Integer>>(){}
            );
            // traverse studentResults - map
            for (Map.Entry<Integer, Integer> studentResult : studentResults.entrySet()) {
                info += studentResult.getKey().toString() + ", ";
                info += studentResult.getValue().toString() + "; ";
            }
        }
        return info;
    }
    
    public static void main(String[] args) throws ParseException, IOException {
        // test the class
        // Instructor ins = new Instructor("Instructor", true, "0005", "Doctor", "Strange", "1912-11-3", "USA");
        Instructor ins = new Instructor("Instructor", true, "0002", "Dead", "Pool", "1968-10-4", "USA");
        System.out.println(ins.toString());
        System.out.println(ins.getInfo());
        
        ins.reportCourseResult();
    }
     
}
