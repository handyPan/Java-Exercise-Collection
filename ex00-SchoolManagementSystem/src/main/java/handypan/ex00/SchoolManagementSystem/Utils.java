/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package handypan.ex00.SchoolManagementSystem;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


/**
 *
 * @author handyPan
 */
public class Utils {
    
    public static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");   // dd-MM-yyyy HH:mm:ss:ms
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }
    
    // check whether an id exists in a map object
    public static boolean checkMapObjIdExist(Map<Integer, Object[]> map, int id) {
        if (getMapObjIds(map).contains(id)) {
            return true;
        }
        return false;
    }
    
    public static Set<Integer> getMapObjIds(Map<Integer, Object[]> map) {
        Set<Integer> idSet = new TreeSet<Integer>();
        for (Map.Entry<Integer, Object[]> m : map.entrySet()) {
            idSet.add(m.getKey());
        }
        return idSet;
    }
    
    public static Set<Integer> getPersonIdsOfSpecifiedRole(Map<Integer, Object[]> person, String role) {
        if (role.equals("")) {
            return person.keySet();
        }
        Set<Integer> idSet = new TreeSet<Integer>();
        for (Map.Entry<Integer, Object[]> per : person.entrySet()) {
            if (per.getValue()[4].equals(role)) {
                idSet.add(per.getKey());
            }
        }
        return idSet;
    }
    
    // courses: id, name, department, hours, credits
    public static void getCoursesInfo(Map<Integer, Object[]> courses) {
        String info = "Courses List - \n";
        for (Map.Entry<Integer, Object[]> course : courses.entrySet()) {
            info += String.format("Id:%d, Name:%s, Department:%s, Hours:%d, Credits:%f;\n", course.getKey(), course.getValue()[0], course.getValue()[1], course.getValue()[2], course.getValue()[3]);
        }
        System.out.println(info);
    }
    
    // person: id, firstName, lastName, dob, address, role
    public static void getInstructorsInfo(Map<Integer, Object[]> person) {
        String info = "Instructors List - \n";
        for (Map.Entry<Integer, Object[]> per : person.entrySet()) {
            if (per.getValue()[4].equals("Instructor")) {
                info += String.format("Id:%d, First Name:%s, Last Name:%s, Birth Date:%s, Address:%s, Role:%s;\n", per.getKey(), per.getValue()[0], per.getValue()[1], per.getValue()[2], per.getValue()[3], per.getValue()[4]);
            }
        }
        System.out.println(info);
    }
    
    // interest groups: id, name, category
    public static void getInterestGroupsInfo(Map<Integer, Object[]> interestGroups) {
        String info = "Interest Groups List - \n";
        for (Map.Entry<Integer, Object[]> interestGroup : interestGroups.entrySet()) {
            info += String.format("Id:%d, Name:%s, Category:%s;\n", interestGroup.getKey(), interestGroup.getValue()[0], interestGroup.getValue()[1]);
        }
        System.out.println(info);
    }
    
    // person: id, firstName, lastName, dob, address, role
    public static void getLeadersInfo(Map<Integer, Object[]> person) {
        String info = "Leaders List - \n";
        for (Map.Entry<Integer, Object[]> per : person.entrySet()) {
            info += String.format("Id:%d, First Name:%s, Last Name:%s, Birth Date:%s, Address:%s, Role:%s;\n", per.getKey(), per.getValue()[0], per.getValue()[1], per.getValue()[2], per.getValue()[3], per.getValue()[4]);
        }
        System.out.println(info);
    }
    
    // student course results: studentId, courseId, isApproved, isEnrolled, finalGrade
    public static void getCourseRegisrationInfo(Map<Integer, Object> studentCourseResults) {
        ObjectMapper om = new ObjectMapper();
        String info = "Student Course Regisration List - \n";
        for (Map.Entry<Integer, Object> studentCourseResult : studentCourseResults.entrySet()) {
            Object obj = studentCourseResult.getValue();
            Map<Integer, Object[]> records = om.convertValue(
                    obj,
                    new TypeReference<Map<Integer, Object[]>>(){}
            );
            for (Map.Entry<Integer, Object[]> record : records.entrySet()) {
                info += String.format("Student Id:%d, Course Id:%d, Is Approved:%b, Is Enrolled:%b, Final Grade:%d;\n", studentCourseResult.getKey(), record.getKey(), record.getValue()[0], record.getValue()[1], record.getValue()[2]);
            }
        }
        System.out.println(info);
    }
    
    public static void getCourseRegisrationInfo(Map<Integer, Object> studentCourseResults, Integer studentId) {
        ObjectMapper om = new ObjectMapper();
        String info = String.format("Student %d Course Regisration List - \n", studentId);
        Object obj = studentCourseResults.get(studentId);
        Map<Integer, Object[]> records = om.convertValue(
            obj,
            new TypeReference<Map<Integer, Object[]>>(){}
        );
        for (Map.Entry<Integer, Object[]> record : records.entrySet()) {
            info += String.format("Course Id:%d, Is Approved:%b, Is Enrolled:%b, Final Grade:%d;\n", record.getKey(), record.getValue()[0], record.getValue()[1], record.getValue()[2]);
        }
        System.out.println(info);
    }
    
    // person interest group results: personId, interestGroupId, isApproved, isEnrolled
    public static void getInterestGroupRegisrationInfo(Map<Integer, Object> personInterestGroupResults) {
        ObjectMapper om = new ObjectMapper();
        String info = "Person Interest Group Regisration List - \n";
        for (Map.Entry<Integer, Object> personInterestGroupResult : personInterestGroupResults.entrySet()) {
            Object obj = personInterestGroupResult.getValue();
            Map<Integer, Object[]> records = om.convertValue(
                    obj,
                    new TypeReference<Map<Integer, Object[]>>(){}
            );
            for (Map.Entry<Integer, Object[]> record : records.entrySet()) {
                info += String.format("Person Id:%d, Interest Group Id:%d, Is Approved:%b, Is Enrolled:%b;\n", personInterestGroupResult.getKey(), record.getKey(), record.getValue()[0], record.getValue()[1]);
            }
        }
        System.out.println(info);
    }
    
    public static void getInterestGroupRegisrationInfo(Map<Integer, Object> personInterestGroupResults, Integer personId) {
        ObjectMapper om = new ObjectMapper();
        String info = String.format("Person %d Interest Group Regisration List - \n", personId);
        Object obj = personInterestGroupResults.get(personId);
        Map<Integer, Object[]> records = om.convertValue(
            obj,
            new TypeReference<Map<Integer, Object[]>>(){}
        );
        for (Map.Entry<Integer, Object[]> record : records.entrySet()) {
            info += String.format("Interst Group Id:%d, Is Approved:%b, Is Enrolled:%b;\n", record.getKey(), record.getValue()[0], record.getValue()[1]);
        }
        System.out.println(info);
    }
    
    // course offerings: courseId, instructorId, capacity
    public static void getCourseOfferingsInfo(Map<Integer, Object[]> courseOfferings) throws IOException { 
        Map<Integer, Object[]> courses = DataHandler.loadCourses();
        Map<Integer, Object[]> person = DataHandler.loadPerson();
        String info = "Offered Courses List - \n";
        for (Map.Entry<Integer, Object[]> courseOffering : courseOfferings.entrySet()) {
            info += String.format("Course Id:%d, Course Name:%s, Department:%s, Hours:%d, Credits:%f, Instructor Id:%d, Instructor Name:%s, Capacity:%d;\n", courseOffering.getKey(), courses.get(courseOffering.getKey())[0], courses.get(courseOffering.getKey())[1], courses.get(courseOffering.getKey())[2],courses.get(courseOffering.getKey())[3], courseOffering.getValue()[0], person.get(courseOffering.getValue()[0])[0]+" "+person.get(courseOffering.getValue()[0])[1], courseOffering.getValue()[1]);
        }
        System.out.println(info);
    }
    
    // interest group offerings: interestGroupId, leaderId, capacity
    public static void getInterestGroupOfferingsInfo(Map<Integer, Object[]> interestGroupOfferings) throws IOException { 
        Map<Integer, Object[]> interestGroups = DataHandler.loadInterestGroups();
        Map<Integer, Object[]> person = DataHandler.loadPerson();
        String info = "Offered Interest Groups List - \n";
        for (Map.Entry<Integer, Object[]> interestGroupOffering : interestGroupOfferings.entrySet()) {
            info += String.format("Interest Group Id:%d, Interest Group Name:%s, Category:%s, Leader Id:%d, Leader Name:%s, Capacity:%d;\n", interestGroupOffering.getKey(), interestGroups.get(interestGroupOffering.getKey())[0], interestGroups.get(interestGroupOffering.getKey())[1], interestGroupOffering.getValue()[0], person.get(interestGroupOffering.getValue()[0])[0]+" "+person.get(interestGroupOffering.getValue()[0])[1], interestGroupOffering.getValue()[1]);
        }
        System.out.println(info);
    }
    
    // instructor course results: instructorId, courseId, studentId, finalGrade
    public static void getInstructorCourseResultsInfo(Map<Integer, Object> instructorCourseResults) {
        ObjectMapper om = new ObjectMapper();
        String info = "Instructor Course Results List - \n";
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
                    info += String.format("%d;%d;%d;%d", instructorCourseResult.getKey(), courseGrade.getKey(), studentGrade.getKey(), studentGrade.getValue());
                }
            }
        }
        System.out.println(info);
    }
    
    public static void getInstructorCourseResultsInfo(Map<Integer, Object> instructorCourseResults, Integer instructorId) {
        ObjectMapper om = new ObjectMapper();
        String info = String.format("Instructor %d Course Results List - \n", instructorId); 
        
        Object objCourseGrades = instructorCourseResults.get(instructorId);
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
                info += String.format("Instructor Id:%d, Course Id:%d, Student Id:%d, Final Grade:%d;\n", instructorId, courseGrade.getKey(), studentGrade.getKey(), studentGrade.getValue());
            }
        }
        System.out.println(info);
    }
    
    public static void getInstructorCourseResultsInfo(Map<Integer, Object> instructorCourseResults, Integer instructorId, Integer courseId) {
        ObjectMapper om = new ObjectMapper();
        String info = String.format("Instructor %d Course %d Results List - \n", instructorId, courseId); 
        
        Object objCourseGrades = instructorCourseResults.get(instructorId);
        Map<Integer, Object> courseGrades = om.convertValue(
                objCourseGrades,
                new TypeReference<Map<Integer, Object>>(){}
        );
        Object objStudentGrades = courseGrades.get(courseId);
        Map<Integer, Integer> studentGrades = om.convertValue(
                objStudentGrades,
                new TypeReference<Map<Integer, Integer>>(){}
        );
        for (Map.Entry<Integer, Integer> studentGrade : studentGrades.entrySet()) {
            info += String.format("Instructor Id:%d, Course Id:%d, Student Id:%d, Final Grade:%d;\n", instructorId, courseId, studentGrade.getKey(), studentGrade.getValue());
        }
        System.out.println(info);
    }
    
    public static void getInstructorCourseResultsInfo(Map<Integer, Object> instructorCourseResults, Integer instructorId, Integer courseId, Integer studentId) {
        ObjectMapper om = new ObjectMapper();
        String info = String.format("Instructor %d Course %d Student %d Results List - \n", instructorId, courseId, studentId); 
        
        Object objCourseGrades = instructorCourseResults.get(instructorId);
        Map<Integer, Object> courseGrades = om.convertValue(
                objCourseGrades,
                new TypeReference<Map<Integer, Object>>(){}
        );
        Object objStudentGrades = courseGrades.get(courseId);
        Map<Integer, Integer> studentGrades = om.convertValue(
                objStudentGrades,
                new TypeReference<Map<Integer, Integer>>(){}
        );
        info += String.format("Instructor Id:%d, Course Id:%d, Student Id:%d, Final Grade:%d;\n", instructorId, courseId, studentId, studentGrades.get(studentId));
        System.out.println(info);
    }
    
    public static void main(String[] args) {
        
    }
    
}
