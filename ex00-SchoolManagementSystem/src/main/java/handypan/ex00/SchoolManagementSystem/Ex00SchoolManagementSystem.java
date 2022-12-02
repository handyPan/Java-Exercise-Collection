/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package handypan.ex00.SchoolManagementSystem;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Pattern;

/**
 *
 * @author handyPan
 */
public class Ex00SchoolManagementSystem {
    
    public static void runMethods(String role, Account acc, Object obj, Map<Integer, ArrayList<String>> objMethods) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        while (true) {
            System.out.println(String.format("Select the option to proceed for %s - %s", role, acc.getUsername()));
            System.out.println("0. Quit");
            for (Map.Entry<Integer, ArrayList<String>> objMethod : objMethods.entrySet()) {
                System.out.println(String.format("%d. %s", objMethod.getKey(), objMethod.getValue().get(0)));
            }
            System.out.print("Choose the operation to proceed:");
            Scanner scan = new Scanner(System.in);
            String op = scan.nextLine();
            if (op.equals("") || !Pattern.matches("^[0-9]$|^[1-9][0-9]*$", op)) {
                System.out.println("Input is not correct!");
                continue;
            }
            if (op.equals("0")) {
                System.out.println(String.format("Quit operations for %s - %s", role, acc.getUsername()));
                break;
            }
            if (!objMethods.keySet().contains(Integer.parseInt(op))) {
                System.out.println("Option not available, input again.");
                continue;
            }
            // perform the selected object operation
            Method selectedMethod = obj.getClass().getMethod(objMethods.get(Integer.parseInt(op)).get(1));
            selectedMethod.invoke(obj);
        }
    }

    public static void main(String[] args) throws IOException, ParseException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        
        Map<Integer, ArrayList<String>> administratorMethods = new TreeMap<Integer, ArrayList<String>>() {
            {
                put(1, new ArrayList<String>(Arrays.asList(new String[]{"Register Person", "registerPerson"})));
                put(2, new ArrayList<String>(Arrays.asList(new String[]{"Edit Person", "editPerson"})));
                put(3, new ArrayList<String>(Arrays.asList(new String[]{"Delete Person", "deletePerson"})));
                put(4, new ArrayList<String>(Arrays.asList(new String[]{"Register Account", "registerAccount"})));
                put(5, new ArrayList<String>(Arrays.asList(new String[]{"Edit Account", "editAccount"})));
                put(6, new ArrayList<String>(Arrays.asList(new String[]{"Delete Account", "deleteAccount"})));
                put(7, new ArrayList<String>(Arrays.asList(new String[]{"Register Course", "registerCourse"})));
                put(8, new ArrayList<String>(Arrays.asList(new String[]{"Edit Course", "editCourse"})));
                put(9, new ArrayList<String>(Arrays.asList(new String[]{"Delete Course", "deleteCourse"})));
                put(10, new ArrayList<String>(Arrays.asList(new String[]{"Register Interest Group", "registerInterestGroup"})));
                put(11, new ArrayList<String>(Arrays.asList(new String[]{"Edit Interest Group", "editInterestGroup"})));
                put(12, new ArrayList<String>(Arrays.asList(new String[]{"Delete Interest Group", "deleteInterestGroup"})));
            }
        };
        Map<Integer, ArrayList<String>> coordinatorMethods = new TreeMap<Integer, ArrayList<String>>() {
            {
                put(1, new ArrayList<String>(Arrays.asList(new String[]{"Offer Course", "offerCourse"})));
                put(2, new ArrayList<String>(Arrays.asList(new String[]{"De-offer Course", "deofferCourse"})));
                put(3, new ArrayList<String>(Arrays.asList(new String[]{"Approve Course Registration", "approveCourseRegistration"})));
                put(4, new ArrayList<String>(Arrays.asList(new String[]{"Offer Interest Group", "offerInterestGroup"})));
                put(5, new ArrayList<String>(Arrays.asList(new String[]{"De-offer Interest Group", "deofferInterestGroup"})));
                put(6, new ArrayList<String>(Arrays.asList(new String[]{"Approve Interest Group Registration", "approveInterestGroupRegistration"})));
            }
        };
        Map<Integer, ArrayList<String>> instructorMethods = new TreeMap<Integer, ArrayList<String>>() {
            {
                put(1, new ArrayList<String>(Arrays.asList(new String[]{"Report Course Result", "reportCourseResult"})));
            }
        };
        Map<Integer, ArrayList<String>> studentMethods = new TreeMap<Integer, ArrayList<String>>() {
            {
                put(1, new ArrayList<String>(Arrays.asList(new String[]{"Register Course", "registerCourse"})));
                put(2, new ArrayList<String>(Arrays.asList(new String[]{"Drop Course", "dropCourse"})));
            }
        };
        
        while (true) {
            AccountHandler accHdl = new AccountHandler();
            Account acc = accHdl.welcome();
            System.out.println(acc.toString());

            if (acc.getPersonId() == 0) {
                System.out.println("Account is not linked to person.");
                continue;
            }

            Map<Integer, Object[]> person = DataHandler.loadPerson();
            String id = Integer.toString(acc.getPersonId());
            String firstName = (String) person.get(acc.getPersonId())[0];
            String lastName = (String) person.get(acc.getPersonId())[1];
            String dob = (String) person.get(acc.getPersonId())[2];
            String addr = (String) person.get(acc.getPersonId())[3];
            String role = (String) person.get(acc.getPersonId())[4];
            
            Scanner scan = new Scanner(System.in);

            switch (role) {
                case "Administrator":
                    Administrator admin = new Administrator(role, true, id, firstName, lastName, dob, addr);
                    runMethods(role, acc, admin, administratorMethods);
                    break;
                case "Coordinator":
                    Coordinator coo = new Coordinator(role, true, id, firstName, lastName, dob, addr);
                    runMethods(role, acc, coo, coordinatorMethods);
                    break;
                case "Instructor":
                    Instructor ins = new Instructor(role, true, id, firstName, lastName, dob, addr);
                    runMethods(role, acc, ins, instructorMethods);
                    break;
                case "Student":
                    Student stu = new Student(role, true, id, firstName, lastName, dob, addr);
                    runMethods(role, acc, stu, studentMethods);

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
                    break;
                default:
                    System.out.println("Error, exit program!");
                    System.exit(0);
            }
            
            System.out.print("Continue to use the system?(Y/n)");
            String op = scan.nextLine();
            if (!(op.equals("Y") || op.equals("y") || op.equals(""))) {
                System.out.println("Exit program!");
                System.exit(0);
            }
        }
        
    }
}
