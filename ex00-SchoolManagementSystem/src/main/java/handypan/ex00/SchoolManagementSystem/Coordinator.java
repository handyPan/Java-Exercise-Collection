/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package handypan.ex00.SchoolManagementSystem;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileNotFoundException;
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
import java.util.stream.Collectors;
import org.apache.commons.lang3.math.NumberUtils;

/**
 *
 * @author handyPan
 */
public class Coordinator extends Person {
    /*
        methods of coordinator 
        - offer, deoffer courses and interest groups
        - approve registration of courses and interest groups
    */
    
    // person - the data structure is: id, fName, lName, dob, addr, role
    // ex. {1,["Spider", "Man", "1926-3-12", "USA", "Administrator"], Administrator won't deal with the interest groups of person
    private Map<Integer, Object[]> person;
    
    // accounts -  the data structure is: id, username, password, personId
    // ex. 1, "admin", "1234", 1
    private Map<Integer, Object[]> accounts;
    
    // courses - the data structure is: id, name, department, hours, credits
    // ex. 1, "Machine Learning", "Computer Science", 30, 1.0
    private Map<Integer, Object[]> courses;
    
    // interest groups - the data structure is: id, name, category
    // ex. 1, "Hiking", "Sports"
    private Map<Integer, Object[]> interestGroups;
    
    // course offerings - the data structure is courseId, instructorId, capacity
    // ex. 1, 1, 10; 2, 4, 15; 3, 1, 20; 4, 3, 15;
    // course description for above example: 1 - "Pattern Recognition", 2 - "Machine Learning", 3 - "Computer Vision", 4 - "Big Data"
    private static Map<Integer, Object[]> courseOfferings;  
    
    // course enrollments - the data structure is courseId, students Id list
    // ex. 1={1, 3, 4, 7, 9}, 2={1, 2, 3, 7}
    private static Map<Integer, TreeSet<Integer>> courseEnrollments;
    
    // interest groups - the data structure is interestGroupId, leaderId, capacity
    // ex. 1, 1, 5; 2, 3, 10; 
    private static Map<Integer, Object[]> interestGroupOfferings;    
    
    // interest groups enrollments - the data structure is interestGroupId, person Id list
    // ex. 1={1, 3, 4, 7, 9}, 2={1, 2, 3, 7}
    private static Map<Integer, TreeSet<Integer>> interestGroupEnrollments;
    
    // student course results - the data structure is studentId, courseId, isApproved, isEnrolled, finalGrade
    // ex. 1={2={true, true, 82}, 8={true, true, 38}}, 2={1={true, false, -1}}
    private static Map<Integer, Object> studentCourseResults;
    
    // person interest group results - the data structure is personId, interstGroupId, isApproved, isEnrolled
    // ex. 1={1={true, true}, 3={true, true}}, 2={2={false, false}}
    private static Map<Integer, Object> personInterestGroupResults;
    
    // instructor course results - the data structure is instructorId, courseId, studentId, finalGrade
    // ex. 1={2={1={82}, 2={65}}, 4={1={38}, 2={76}}, 2={1={1={-1}, 2={81}}}
    private static Map<Integer, Object> instructorCourseResults;
    
    private Scanner scan = new Scanner(System.in);
    
    public Coordinator(String _role, boolean directlyAssign, String... params) throws ParseException, IOException {
        super(_role, directlyAssign, params);
        
        // populate person
        person = DataHandler.loadPerson();
        
        // populate accounts 
        accounts = DataHandler.loadAccounts();
        
        // populate courses
        courses = DataHandler.loadCourses();
        
        // populate interest groups
        interestGroups = DataHandler.loadInterestGroups();
        
        // populate course offerings
        courseOfferings = DataHandler.loadCourseOfferings();
        
        // populate course enrollments
        courseEnrollments = DataHandler.loadCourseEnrollments();
        
        // populate interest group offerings
        interestGroupOfferings = DataHandler.loadInterestGroupOfferings();
        
        // populate interest group enrollments
        interestGroupEnrollments = DataHandler.loadInterestGroupEnrollments();
        
        // populate student course results
        studentCourseResults = DataHandler.loadStudentCourseResults();
        
        // populate person interst group results
        personInterestGroupResults = DataHandler.loadPersonInterestGroupResults();
    
        // populate instructor course results
        instructorCourseResults = DataHandler.loadInstructorCourseResults();
    }

    public Map<Integer, Object[]> getCourseOfferings() {
        return courseOfferings;
    }
    
    public Set getOfferedCourses() {
        return courseOfferings.keySet();
    }
    
    public static Set getOfferedCoursesByInstructorId(Integer instructorId) {
        Set<Integer> s = new TreeSet<Integer>();
        // traverse courseOffering map, store courseId in the set if the instructorId matches the target
        for (Map.Entry<Integer, Object[]> courseOffering : courseOfferings.entrySet()) {
            if (courseOffering.getValue()[0]==instructorId) {
                s.add(courseOffering.getKey());
            }
        }
        return s;
    }
    
    public Set getOfferedCoursesByCapacity(Integer capacity) {
        Set<Integer> s = new TreeSet<Integer>();
        for (Map.Entry<Integer, Object[]> courseOffering : courseOfferings.entrySet()) {
            if (courseOffering.getValue()[1]==capacity) {
                s.add(courseOffering.getKey());
            }
        }
        return s;
    }
    
    public static Set getEnrolledStudentsByCourseId(Integer courseId) {
        return courseEnrollments.get(courseId);
    }
    
    public void offerCourse() throws UnsupportedEncodingException, IOException {
        while (true) {
            System.out.print("Continue with adding/modifying course offering?(Y/n)");
            String op = scan.nextLine();
            if (!(op.equals("Y") || op.equals("y") || op.equals(""))) {
                break;
            }
            if (courses.keySet().isEmpty()) {
                System.out.println("No course exists.");
                continue;
            }
            Utils.getCoursesInfo(courses);
            System.out.print("Input the course id to offer:");
            String courseIdIn = scan.nextLine();
            if (courseIdIn.equals("") || !Pattern.matches("[1-9][0-9]*", courseIdIn)) {
                    System.out.println("Course Id is not correct!");
                    continue;
            }
            int courseId = Integer.parseInt(courseIdIn);
            if (!courses.keySet().contains(courseId)) {
                System.out.println(String.format("Course %d does not exist.", courseId));
                continue;
            }
            if (Utils.getPersonIdsOfSpecifiedRole(person, "Instructor").isEmpty()) {
                System.out.println("No instructor exists.");
                continue;
            }
            Utils.getInstructorsInfo(person);
            System.out.print("Input the instructor id to offer:");
            String instructorIdIn = scan.nextLine();
            if (instructorIdIn.equals("") || !Pattern.matches("[1-9][0-9]*", instructorIdIn)) {
                    System.out.println("Instructor Id is not correct!");
                    continue;
            }
            int instructorId = Integer.parseInt(instructorIdIn);
            if (!Utils.getPersonIdsOfSpecifiedRole(person, "Instructor").contains(instructorId)) {
                System.out.println(String.format("Instructor %d does not exist.", instructorId));
                continue;
            }
            System.out.print("Input the course capacity to offer:");
            String capacityIn = scan.nextLine();
            if (capacityIn.equals("") || !Pattern.matches("[1-9][0-9]*", capacityIn)) {
                    System.out.println("Course capacity is not correct!");
                    continue;
            }
            int capacity = Integer.parseInt(capacityIn);
            // verify whether the course is already offered, if yes, ask whether to modify it or cancel
            boolean courseOffered = false;
            for (Map.Entry<Integer, Object[]> courseOffering : courseOfferings.entrySet()) {    
                if (((int) courseOffering.getKey() == courseId) && ( (int) courseOffering.getValue()[0] == instructorId)) {
                    courseOffered = true;
                    break;
                }
            }
            if (courseOffered) {
                System.out.print("Course Already offered! Update it?(Y/n)");
                op = scan.nextLine();
                if (!(op.equals("Y") || op.equals("y") || op.equals(""))) {
                    continue;
                }
                // courseId, instructorId, capacity
                courseOfferings.put(courseId, new Object[]{instructorId, capacity});
                // courseEnrollments.put(courseId, new TreeSet<Integer>());
                // DataHandler.saveCourseEnrollments(courseEnrollments);
                DataHandler.saveCourseOfferings(courseOfferings);
                System.out.println("Course offering updated successfully!");
                continue;
            }
            
            courseOfferings.put(courseId, new Object[]{instructorId, capacity});
            courseEnrollments.put(courseId, new TreeSet<Integer>());
            DataHandler.saveCourseEnrollments(courseEnrollments);
            DataHandler.saveCourseOfferings(courseOfferings);
            System.out.println("Course offering added successfully!");   
        }
        
    }
    
    public void deofferCourse() throws UnsupportedEncodingException, IOException {
        while (true) {
            System.out.print("Continue with deoffering course?(Y/n)");
            String op = scan.nextLine();
            if (!(op.equals("Y") || op.equals("y") || op.equals(""))) {
                break;
            }
            if (courses.keySet().isEmpty()) {
                System.out.println("No course exists.");
                continue;
            }
            Utils.getCoursesInfo(courses);
            System.out.print("Input the course id to de-offer:");
            String courseIdIn = scan.nextLine();
            if (courseIdIn.equals("") || !Pattern.matches("[1-9][0-9]*", courseIdIn)) {
                    System.out.println("Course Id is not correct!");
                    continue;
            }
            int courseId = Integer.parseInt(courseIdIn);
            if (!courses.keySet().contains(courseId)) {
                System.out.println(String.format("Course %d does not exist.", courseId));
                continue;
            }
            if (!courseOfferings.keySet().contains(courseId)) {
                System.out.println(String.format("Course %d does not exist in the offered courses.", courseId));
                continue;
            }
            System.out.println(String.format("Course Id %d offering info: Instructor Id - %d, Capacity - %d", courseId, courseOfferings.get(courseId)[0], courseOfferings.get(courseId)[1]));
            System.out.print("Please confirm to delete this course: [y/N]");
            op = scan.nextLine();
            if (!op.equals("Y") && !op.equals("y")) {
                continue;
            }
            
            int instructorId = (int) courseOfferings.get(courseId)[0];
            courseOfferings.remove(courseId);
            courseEnrollments.remove(courseId);
            // dis-approve the student course registrations that have this courseId
            ObjectMapper om = new ObjectMapper();
            for (Map.Entry<Integer, Object> studentCourseResult : studentCourseResults.entrySet()) {
                int studentId = studentCourseResult.getKey();
                Object obj = studentCourseResult.getValue();
                Map<Integer, Object[]> records = om.convertValue(
                    obj,
                    new TypeReference<Map<Integer, Object[]>>(){}
                );
                if (records.keySet().contains(courseId)) {
                    records.put(courseId, new Object[]{false, false, -1});
                    studentCourseResults.put(studentId, records);
                }
            }
            // remove course grades in instructorCourseResults.data
            Map<Integer, Object> courseGrades = new TreeMap<Integer, Object>();
            if (instructorCourseResults.get(instructorId) != null) {
                Object objCourseGrades = instructorCourseResults.get(instructorId);
                courseGrades = om.convertValue(
                    objCourseGrades,
                    new TypeReference<Map<Integer, Object>>(){}
                );
                courseGrades.remove(courseId);
                instructorCourseResults.put(instructorId, courseGrades);
            }
            
            // write to data file
            DataHandler.saveCourseOfferings(courseOfferings);
            DataHandler.saveCourseEnrollments(courseEnrollments);
            DataHandler.saveStudentCourseResults(studentCourseResults);
            DataHandler.saveInstructorCourseResults(instructorCourseResults);
            System.out.println(String.format("Course %d offering deleted successfully!", courseId));
        }
    }
    
    public int getCourseInstructor(Integer courseId) {
        return (Integer) courseOfferings.get(courseId)[0];
    }
    
    public int getCourseCapacity(Integer courseId) {
        return (Integer) courseOfferings.get(courseId)[1];
    }
    
    public void approveCourseRegistration() throws UnsupportedEncodingException, IOException {
        while (true) {
            System.out.print("Continue with approving course registration?(Y/n)");
            String op = scan.nextLine();
            if (!(op.equals("Y") || op.equals("y") || op.equals(""))) {
                break;
            }
            if (studentCourseResults.keySet().isEmpty()) {
                System.out.println("No course registration exists.");
                continue;
            }
            Utils.getCourseRegisrationInfo(studentCourseResults);
            System.out.print("Input the student id to approve:");
            String studentIdIn = scan.nextLine();
            if (studentIdIn.equals("") || !Pattern.matches("[1-9][0-9]*", studentIdIn)) {
                    System.out.println("Student Id is not correct!");
                    continue;
            }
            int studentId = Integer.parseInt(studentIdIn);
            if (!studentCourseResults.keySet().contains(studentId)) {
                System.out.println(String.format("Student %d does not exist.", studentId));
                continue;
            }
            ObjectMapper om = new ObjectMapper();
            Object obj = studentCourseResults.get(studentId);
            Map<Integer, Object[]> records = om.convertValue(
                obj,
                new TypeReference<Map<Integer, Object[]>>(){}
            );
            if (records.keySet().isEmpty()) {
                System.out.println(String.format("No course registration exists for student %d", studentId));
                continue;
            }
            Utils.getCourseRegisrationInfo(studentCourseResults, studentId);
            Map<Integer, Object[]> recordToSave = new TreeMap<Integer, Object[]>();
            // recordToSave = records;
            recordToSave = om.convertValue(
                obj,
                new TypeReference<Map<Integer, Object[]>>(){}
            );
            System.out.print("Input the course id to approve:");
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
            System.out.print(String.format("Enroll student %d in course %d?(Y/n)", studentId, courseId));
            op = scan.nextLine();
            if (!(op.equals("Y") || op.equals("y") || op.equals("N") || op.equals("n") || op.equals(""))) {
                continue;
            }
            boolean isEnrolled = (op.equals("Y") || op.equals("y") || op.equals("")) ? true : false;
            
            // determine whether the student can be enrolled
            // case 1 - the course is not available, the result will be courseId={approved, not enrolled, -1}
            if (!courseOfferings.keySet().contains(courseId)) {
                System.out.println(String.format("Course %d does not exist.", courseId));
                isEnrolled = false;
                continue;
            }
            // case 2 - the course is full
            if (courseEnrollments.get(courseId) != null) {
                if (courseEnrollments.get(courseId).size() >= (int) courseOfferings.get(courseId)[1]) {
                    System.out.println(String.format("Course %d is full.", courseId));
                    isEnrolled = false;
                }
            }
            recordToSave.put(courseId, new Object[]{true, isEnrolled, isEnrolled ? 0 : -1});
            // verify whether the course registration is already approved, if yes, ask whether to modify it or cancel
            boolean courseRegistrationApproved = false;
            
            for (Map.Entry<Integer, Object[]> record : records.entrySet()) {
                
                if (((int) record.getKey() == courseId) && ((boolean) record.getValue()[0] == true)) {
                    courseRegistrationApproved = true;
                    break;
                }
            }
            
            // to add or update instructorCourseResults
            Map<Integer, Integer> studentGrades = new TreeMap<Integer, Integer>();
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
                    studentGrades = om.convertValue(
                        objStudentGrades,
                        new TypeReference<Map<Integer, Integer>>(){}
                    );
                }
            }
            
            if (courseRegistrationApproved) {
                System.out.print("Course Already approved! Update it?(Y/n)");
                op = scan.nextLine();
                if (!(op.equals("Y") || op.equals("y") || op.equals(""))) {
                    continue;
                }
                // studentId, courseId, isApproved, isEnrolled, finalGrade
                studentCourseResults.put(studentId, recordToSave);
                if (isEnrolled) {
                    if (courseEnrollments.get(courseId) == null) {
                        courseEnrollments.put(courseId, new TreeSet<Integer>());
                    }
                    courseEnrollments.get(courseId).add(studentId);
                    System.out.println(String.format("Student %d enrolled to course %d successfully!", studentId, courseId));
                    // add student to instructorCourseResults.data
                    studentGrades.put(studentId, -1);
                    courseGrades.put(courseId, studentGrades);
                    instructorCourseResults.put(instructorId, courseGrades);
                } else {
                    if (courseEnrollments.get(courseId) != null) {
                        if (courseEnrollments.get(courseId).contains(studentId)) {
                            courseEnrollments.get(courseId).remove(studentId);
                            // remove student from instructorCourseResults.data
                            studentGrades.remove(studentId);
                            courseGrades.put(courseId, studentGrades);
                            instructorCourseResults.put(instructorId, courseGrades);
                        }
                    }
                }
                                
                DataHandler.saveCourseEnrollments(courseEnrollments);
                DataHandler.saveStudentCourseResults(studentCourseResults);
                DataHandler.saveInstructorCourseResults(instructorCourseResults);
                System.out.println("Student course registration approval updated successfully!");
                continue;
            }
            
            studentCourseResults.put(studentId, recordToSave);
            
            if (isEnrolled) {
                if (courseEnrollments.get(courseId) == null) {
                    courseEnrollments.put(courseId, new TreeSet<Integer>());
                }
                courseEnrollments.get(courseId).add(studentId);
                System.out.println(String.format("Student %d enrolled to course %d successfully!", studentId, courseId));
                // add student to instructorCourseResults.data
                studentGrades.put(studentId, -1);
                courseGrades.put(courseId, studentGrades);
                instructorCourseResults.put(instructorId, courseGrades);
            } else {
                if (courseEnrollments.get(courseId) != null) {
                    if (courseEnrollments.get(courseId).contains(studentId)) {
                        courseEnrollments.get(courseId).remove(studentId);
                        // remove student from instructorCourseResults.data
                        studentGrades.remove(studentId);
                        courseGrades.put(courseId, studentGrades);
                        instructorCourseResults.put(instructorId, courseGrades);
                    }
                }
            }
            DataHandler.saveCourseEnrollments(courseEnrollments);
            DataHandler.saveStudentCourseResults(studentCourseResults);
            DataHandler.saveInstructorCourseResults(instructorCourseResults);
            System.out.println("Student course registration approved successfully!"); 
        }
       
    }

    public Map<Integer, Object[]> getInterestGroupOfferings() {
        return interestGroupOfferings;
    }
    
    public Set getOfferingInterestGroups() {
        return interestGroupOfferings.keySet();
    }
    
    public Set getInterestGroupByLeaderId(Integer leaderId) {
        Set<Integer> s = new TreeSet<Integer>();
        for (Map.Entry<Integer, Object[]> interestGroupOffering : interestGroupOfferings.entrySet()) {
            if (interestGroupOffering.getValue()[0]==leaderId) {
                s.add(interestGroupOffering.getKey());
            }
        }
        return s;
    }
    
    public Set getInterestGroupByCapacity(Integer capacity) {
        Set<Integer> s = new TreeSet<Integer>();
        for (Map.Entry<Integer, Object[]> interestGroupOffering : interestGroupOfferings.entrySet()) {
            if (interestGroupOffering.getValue()[1]==capacity) {
                s.add(interestGroupOffering.getKey());
            }
        }
        return s;
    }

    public void offerInterestGroup() throws UnsupportedEncodingException, IOException {   
        while (true) {
            System.out.print("Continue with adding/modifying interest group offering?(Y/n)");
            String op = scan.nextLine();
            if (!(op.equals("Y") || op.equals("y") || op.equals(""))) {
                break;
            }
            if (interestGroups.keySet().isEmpty()) {
                System.out.println("No interest group exists.");
                continue;
            }
            Utils.getInterestGroupsInfo(interestGroups);
            System.out.print("Input the interest group id to offer:");
            String interestGroupIdIn = scan.nextLine();
            if (interestGroupIdIn.equals("") || !Pattern.matches("[1-9][0-9]*", interestGroupIdIn)) {
                System.out.println("Interest group Id is not correct!");
                continue;
            }
            int interestGroupId = Integer.parseInt(interestGroupIdIn);
            if (!interestGroups.keySet().contains(interestGroupId)) {
                System.out.println(String.format("Interest group %d does not exist.", interestGroupId));
                continue;
            }
            if (person.keySet().isEmpty()) {
                System.out.println("No leader exists.");
                continue;
            }
            Utils.getLeadersInfo(person);
            System.out.print("Input the leader id to offer:");
            String leaderIdIn = scan.nextLine();
            if (leaderIdIn.equals("") || !Pattern.matches("[1-9][0-9]*", leaderIdIn)) {
                    System.out.println("Leader Id is not correct!");
                    continue;
            }
            int leaderId = Integer.parseInt(leaderIdIn);
            if (!person.keySet().contains(leaderId)) {
                System.out.println(String.format("Leader %d does not exist.", leaderId));
                continue;
            }
            System.out.print("Input the interest group capacity to offer:");
            String capacityIn = scan.nextLine();
            if (capacityIn.equals("") || !Pattern.matches("[1-9][0-9]*", capacityIn)) {
                    System.out.println("Course capacity is not correct!");
                    continue;
            }
            int capacity = Integer.parseInt(capacityIn);
            // verify whether the interest group is already offered, if yes, ask whether to modify it or cancel
            boolean interestGroupOffered = false;
            for (Map.Entry<Integer, Object[]> interestGroupOffering : interestGroupOfferings.entrySet()) {    
                if (((int) interestGroupOffering.getKey() == interestGroupId) && ( (int) interestGroupOffering.getValue()[0] == leaderId)) {
                    interestGroupOffered = true;
                    break;
                }
            }
            if (interestGroupOffered) {
                System.out.print("Interest Group Already offered! Update it?(Y/n)");
                op = scan.nextLine();
                if (!(op.equals("Y") || op.equals("y") || op.equals(""))) {
                    continue;
                }
                // interestGroupId, leaderId, capacity
                interestGroupOfferings.put(interestGroupId, new Object[]{leaderId, capacity});
                DataHandler.saveInterestGroupOfferings(interestGroupOfferings);
                System.out.println("Interest Group offering updated successfully!");
                continue;
            }
            
            interestGroupOfferings.put(interestGroupId, new Object[]{leaderId, capacity});
            interestGroupEnrollments.put(interestGroupId, new TreeSet<Integer>());      
            DataHandler.saveInterestGroupEnrollments(interestGroupEnrollments);
            DataHandler.saveInterestGroupOfferings(interestGroupOfferings);
            System.out.println("Interest Group offering added successfully!");   
        }
        
    }
    
    public void deofferInterestGroup() throws UnsupportedEncodingException, IOException {
        while (true) {
            System.out.print("Continue with deoffering interest group?(Y/n)");
            String op = scan.nextLine();
            if (!(op.equals("Y") || op.equals("y") || op.equals(""))) {
                break;
            }
            if (interestGroups.keySet().isEmpty()) {
                System.out.println("No interest group exists.");
                continue;
            }
            Utils.getInterestGroupsInfo(interestGroups);
            System.out.print("Input the interest group id to de-offer:");
            String interestGroupIdIn = scan.nextLine();
            if (interestGroupIdIn.equals("") || !Pattern.matches("[1-9][0-9]*", interestGroupIdIn)) {
                    System.out.println("Interest Group Id is not correct!");
                    continue;
            }
            int interestGroupId = Integer.parseInt(interestGroupIdIn);
            if (!interestGroups.keySet().contains(interestGroupId)) {
                System.out.println(String.format("Interest Group %d does not exist.", interestGroupId));
                continue;
            }
            if (!interestGroupOfferings.keySet().contains(interestGroupId)) {
                System.out.println(String.format("Interest Group %d does not exist in the offered interest groups.", interestGroupId));
                continue;
            }
            System.out.println(String.format("Interest Group Id %d offering info: Leader Id - %d, Capacity - %d", interestGroupId, interestGroupOfferings.get(interestGroupId)[0], interestGroupOfferings.get(interestGroupId)[1]));
            System.out.print("Please confirm to delete this interest group: [y/N]");
            op = scan.nextLine();
            if (!op.equals("Y") && !op.equals("y")) {
                continue;
            }
            interestGroupOfferings.remove(interestGroupId);
            interestGroupEnrollments.remove(interestGroupId);
            // dis-approve the person interest group registrations that have this courseId
            ObjectMapper om = new ObjectMapper();
            for (Map.Entry<Integer, Object> personInterestGroupResult : personInterestGroupResults.entrySet()) {
                int personId = personInterestGroupResult.getKey();
                Object obj = personInterestGroupResult.getValue();
                Map<Integer, Object[]> records = om.convertValue(
                    obj,
                    new TypeReference<Map<Integer, Object[]>>(){}
                );
                if (records.keySet().contains(interestGroupId)) {
                    records.put(interestGroupId, new Object[]{false, false});
                    personInterestGroupResults.put(personId, records);
                }
            }
            // write to data file
            DataHandler.saveInterestGroupOfferings(interestGroupOfferings);
            DataHandler.saveInterestGroupEnrollments(interestGroupEnrollments);
            DataHandler.savePersonInterestGroupResults(personInterestGroupResults);
            System.out.println(String.format("Interest Group %d offering deleted successfully!", interestGroupId));
        }
        
    }
    
    public int getInterestGroupLeader(Integer interestGroupId) {
        return (Integer) interestGroupOfferings.get(interestGroupId)[0];
    }
    
    public int getInterestGroupCapacity(Integer interestGroupId) {
        return (Integer) interestGroupOfferings.get(interestGroupId)[1];
    }
    
    public void approveInterestGroupRegistration() throws UnsupportedEncodingException, IOException {
        while (true) {
            System.out.print("Continue with approving interest group registration?(Y/n)");
            String op = scan.nextLine();
            if (!(op.equals("Y") || op.equals("y") || op.equals(""))) {
                break;
            }
            if (personInterestGroupResults.keySet().isEmpty()) {
                System.out.println("No interest group registration exists.");
                continue;
            }
            Utils.getInterestGroupRegisrationInfo(personInterestGroupResults);
            System.out.print("Input the person id to approve:");
            String personIdIn = scan.nextLine();
            if (personIdIn.equals("") || !Pattern.matches("[1-9][0-9]*", personIdIn)) {
                    System.out.println("Person Id is not correct!");
                    continue;
            }
            int personId = Integer.parseInt(personIdIn);
            if (!personInterestGroupResults.keySet().contains(personId)) {
                System.out.println(String.format("Person %d does not exist.", personId));
                continue;
            }
            ObjectMapper om = new ObjectMapper();
            Object obj = personInterestGroupResults.get(personId);
            Map<Integer, Object[]> records = om.convertValue(
                obj,
                new TypeReference<Map<Integer, Object[]>>(){}
            );
            if (records.keySet().isEmpty()) {
                System.out.println(String.format("No Interest Group registration exists for Person %d", personId));
                continue;
            }
            Utils.getInterestGroupRegisrationInfo(personInterestGroupResults, personId);
            Map<Integer, Object[]> recordToSave = new TreeMap<Integer, Object[]>();
            // recordToSave = records;
            recordToSave = om.convertValue(
                obj,
                new TypeReference<Map<Integer, Object[]>>(){}
            );
            System.out.print("Input the Interest Group id to approve:");
            String interestGroupIdIn = scan.nextLine();
            if (interestGroupIdIn.equals("") || !Pattern.matches("[1-9][0-9]*", interestGroupIdIn)) {
                    System.out.println("Interest Group Id is not correct!");
                    continue;
            }
            int interestGroupId = Integer.parseInt(interestGroupIdIn);
            if (!records.keySet().contains(interestGroupId)) {
                System.out.println(String.format("Interest Group %d does not exist.", interestGroupId));
                continue;
            }
            System.out.print(String.format("Enroll person %d in interestGroup %d?(Y/n)", personId, interestGroupId));
            op = scan.nextLine();
            if (!(op.equals("Y") || op.equals("y") || op.equals("N") || op.equals("n") || op.equals(""))) {
                continue;
            }
            boolean isEnrolled = (op.equals("Y") || op.equals("y") || op.equals("")) ? true : false;
            
            // determine whether the person can be enrolled
            // case 1 - the interest group is not available, the result will be interestGroupId={approved, not enrolled}
            if (!interestGroupOfferings.keySet().contains(interestGroupId)) {
                System.out.println(String.format("Interst Group %d does not exist.", interestGroupId));
                isEnrolled = false;
            }
            // case 2 - the interest group is full
            if (interestGroupEnrollments.get(interestGroupId) != null) {
                if (interestGroupEnrollments.get(interestGroupId).size() >= (int) interestGroupOfferings.get(interestGroupId)[1]) {
                    System.out.println(String.format("Interest Group %d is full.", interestGroupId));
                    isEnrolled = false;
                }
            }
            recordToSave.put(interestGroupId, new Object[]{true, isEnrolled});
            // verify whether the interest group registration is already approved, if yes, ask whether to modify it or cancel
            boolean interestGroupRegistrationApproved = false;
            for (Map.Entry<Integer, Object[]> record : records.entrySet()) {    
                if (((int) record.getKey() == interestGroupId) && ((boolean) record.getValue()[0] == true)) {
                    interestGroupRegistrationApproved = true;
                    break;
                }
            }
            if (interestGroupRegistrationApproved) {
                System.out.print("Interest Group Already approved! Update it?(Y/n)");
                op = scan.nextLine();
                if (!(op.equals("Y") || op.equals("y") || op.equals(""))) {
                    continue;
                }
                // personId, interestGroupId, isApproved, isEnrolled
                personInterestGroupResults.put(personId, recordToSave);
                if (isEnrolled) {
                    if (interestGroupEnrollments.get(interestGroupId) == null) {
                        interestGroupEnrollments.put(interestGroupId, new TreeSet<Integer>());
                    }
                    interestGroupEnrollments.get(interestGroupId).add(personId);
                    System.out.println(String.format("Person %d enrolled to interest group %d successfully!", personId, interestGroupId));
                } else {
                    if (interestGroupEnrollments.get(interestGroupId).contains(personId)) {
                        interestGroupEnrollments.get(interestGroupId).remove(personId);
                    }
                }
                DataHandler.saveInterestGroupEnrollments(interestGroupEnrollments);
                DataHandler.savePersonInterestGroupResults(personInterestGroupResults);
                System.out.println("Person Interest Group registration approval updated successfully!");
                continue;
            }
            
            personInterestGroupResults.put(personId, recordToSave);
            if (isEnrolled) {
                if (interestGroupEnrollments.get(interestGroupId) == null) {
                    interestGroupEnrollments.put(interestGroupId, new TreeSet<Integer>());
                }
                interestGroupEnrollments.get(interestGroupId).add(personId);
                System.out.println(String.format("Person %d enrolled to interest group %d successfully!", personId, interestGroupId));
            } else {
                if (interestGroupEnrollments.get(interestGroupId) != null) {
                    if (interestGroupEnrollments.get(interestGroupId).contains(personId)) {
                        interestGroupEnrollments.get(interestGroupId).remove(personId);
                    }
                }
            }
            DataHandler.saveInterestGroupEnrollments(interestGroupEnrollments);
            DataHandler.savePersonInterestGroupResults(personInterestGroupResults);
            System.out.println("Person Interest Group registration approved successfully!");
        }
        
    }

    @Override
    public String toString() {
        return super.toString() + "\n" + "Coordinator22{" + "courseOfferings=" + courseOfferings + ", interestGroupOfferings=" + interestGroupOfferings + '}';
    }
    
    public static String getCourseOfferingInfo() {
        String info = "Course Offering - ";
        for (Map.Entry<Integer, Object[]> courseOffering : courseOfferings.entrySet()) {
            info += courseOffering.getKey() + ", " + courseOffering.getValue()[0].toString() + ", " + courseOffering.getValue()[1].toString() + ";";
        }
        return info;
    }
    
    public static String getCourseEnrollmentInfo() {
        String info = "Course Enrollment - ";
        for (Map.Entry<Integer, TreeSet<Integer>> courseEnrollment : courseEnrollments.entrySet()) {
            Set<String> courseEnrollmentValue = courseEnrollment.getValue().stream().map(String::valueOf).collect(Collectors.toSet());
            info += courseEnrollment.getKey() + ":" + String.join(", ", courseEnrollmentValue) + ";";
        }
        return info;
    }
    
    public static String getInterestGroupOfferingInfo() {
        String info = "Interest Group Offering - ";
        for (Map.Entry<Integer, Object[]> interestGroupOffering : interestGroupOfferings.entrySet()) {
            info += interestGroupOffering.getKey() + ", " + interestGroupOffering.getValue()[0].toString() + ", " + interestGroupOffering.getValue()[1].toString() + ";";
        }
        return info;
    }
    
    public static String getInterestGroupEnrollmentInfo() {
        String info = "Interest Group Enrollment - ";
        for (Map.Entry<Integer, TreeSet<Integer>> interestGroupEnrollment : interestGroupEnrollments.entrySet()) {
            Set<String> interestGroupEnrollmentValue = interestGroupEnrollment.getValue().stream().map(String::valueOf).collect(Collectors.toSet());
            info += interestGroupEnrollment.getKey() + ":" + String.join(", ", interestGroupEnrollmentValue) + ";";
        }
        return info;
    }

    @Override
    public String getInfo() {
        /*
        String info = super.getInfo() + "\n";
        info += "Course Offering - ";
        for (Map.Entry<Integer, Object[]> courseOffering : courseOfferings.entrySet()) {
        info += courseOffering.getKey() + ", " + courseOffering.getValue()[0].toString() + ", " + courseOffering.getValue()[1].toString() + ";";
        }
        info += "\n";
        info += "Interest Group Offering - ";
        for (Map.Entry<Integer, Object[]> interestGroupOffering : interestGroupOfferings.entrySet()) {
        info += interestGroupOffering.getKey() + ", " + interestGroupOffering.getValue()[0].toString() + ", " + interestGroupOffering.getValue()[1].toString() + ";";
        }
        return info;
        */
        return super.getInfo() + "\n" + getCourseOfferingInfo() + "\n" + getInterestGroupOfferingInfo();
    }
    
    public static void main(String[] args) throws ParseException, IOException {
        // test the class
        Coordinator coo = new Coordinator("Coordinator", true, "0007", "Professor", "X", "1908-1-19", "Germany");
        System.out.println(coo.toString());
        System.out.println(coo.getInfo());
   
        Scanner scan = new Scanner(System.in);
        
        coo.offerCourse();
        coo.deofferCourse();
        coo.approveCourseRegistration();
        coo.offerInterestGroup();
        coo.deofferInterestGroup();
        coo.approveInterestGroupRegistration();
        
        
        // output results
        System.out.println(coo.toString());
        System.out.println(coo.getInfo());
        System.out.println("Interests - " + coo.getInterests());
        System.out.println("Personal Interest Groups - " + coo.getPersonInterestGroups());
        System.out.println("Course Offerings - " + coo.getCourseOfferings());
        System.out.println("Offering Interests - " + coo.getOfferingInterestGroups());
        System.out.println("Interest Group Offerings - " + coo.getInterestGroupOfferings());
    }
    
}
