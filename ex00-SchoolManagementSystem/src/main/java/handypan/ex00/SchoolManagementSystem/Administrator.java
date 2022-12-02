/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package handypan.ex00.SchoolManagementSystem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Pattern;

/**
 *
 * @author handyPan
 */
public class Administrator extends Person {
    /*
        methods of administrator
        - register, delete, edit interest groups
        - register, delete, edit courses
        - register, delete, edit person
        - register, delete, edit accounts
    */
    
    // interest group - the data structure is: id, name, category, leaderId, capacity
    
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
    
    // List<String> roles = Arrays.asList("Administrator", "Student", "Coordinator", "Instructor");
    Map<String, String> roles = new TreeMap<String, String>() {
        {
            put("A", "Administrator");
            put("C", "Coordinator");
            put("I", "Instructor");
            put("S", "Student");
        }
    };
    
    private Scanner scan = new Scanner(System.in);
    
    public Administrator(String _role, boolean directlyAssign, String... params) throws ParseException, IOException {
        super(_role, directlyAssign, params);
        
        // populate person
        person = DataHandler.loadPerson();
        
        // populate accounts 
        accounts = DataHandler.loadAccounts();
        // accounts.put(1, new Object[]{"admin", "1234", 1});  // a default account
        
        // populate courses
        courses = DataHandler.loadCourses();
        
        // populate interestGroups
        interestGroups = DataHandler.loadInterestGroups();
    }
    
    // register person
    public void registerPerson() throws UnsupportedEncodingException, IOException {
        while (true) {
            System.out.print("Continue with person registration?(Y/n)");
            String op = scan.nextLine();
            if (!(op.equals("Y") || op.equals("y") || op.equals(""))) {
                break;
            }
            System.out.print("Input the first name:");
            String firstName = scan.nextLine();
            if (firstName.equals("")) {
                System.out.println("First name can't be empty!");
                continue;
            } 
            System.out.print("Input the last name:");
            String lastName = scan.nextLine();
            if (lastName.equals("")) {
                System.out.println("Last name can't be empty!");
                continue;
            }
            System.out.print("Input the birth date (yyyy-MM-dd):");
            String dob = scan.nextLine();
            if (dob.equals("") || !Utils.isValidDate(dob)) {
                System.out.println("Birth date not correct!");
                continue;
            }
            System.out.print("Input the address:");
            String address = scan.nextLine();
            if (address.equals("")) {
                System.out.println("Address can't be empty!");
                continue;
            }
            
            System.out.print("Input the role to assign (A - Administrator, C - Coordinator, I - Instructor, S - Student):");
            String role = scan.nextLine();
            role = role.toUpperCase();
            if (!role.equals("A") && !role.equals("C") && !role.equals("I") && !role.equals("S")) {
                System.out.println("Wrong selection!");
                continue;
            }
            
            // id, firstName, lastName, dob, address, role
            person.put(person.isEmpty()?1:Collections.max(person.keySet())+1, new Object[]{firstName, lastName, dob, address, roles.get(role)});
            // write to data file
            DataHandler.savePerson(person);

            System.out.println("Person registered successfully!");
            continue;                
        }
    }
    
    // edit person
    public void editPerson() throws UnsupportedEncodingException, IOException {
        while (true) {
            System.out.print("Continue with editing person?(Y/n)");
            String op = scan.nextLine();
            if (!(op.equals("Y") || op.equals("y") || op.equals(""))) {
                break;
            }
            if (person.keySet().isEmpty()) {
                System.out.println("No person exists.");
                continue;
            }
            getPersonInfo();
            System.out.print("Input the person id to edit:");
            String personIdIn = scan.nextLine();
            if (personIdIn.equals("") || !Pattern.matches("[1-9][0-9]*", personIdIn)) {
                    System.out.println("Person Id is not correct!");
                    continue;
            }
            int personId = Integer.parseInt(personIdIn);
            if (!person.keySet().contains(personId)) {
                System.out.println(String.format("Person %d does not exist.", personId));
                continue;
            }
            System.out.println(String.format("Person Id %d info: First Name - %s, Last Name - %s, Birth Date - %s, Address - %s, Role - %s", personId, person.get(personId)[0], person.get(personId)[1], person.get(personId)[2], person.get(personId)[3], person.get(personId)[4]));
            String updateFirstNameTo = (String) person.get(personId)[0];
            String updateLastNameTo = (String) person.get(personId)[1];
            String updateDobTo = (String) person.get(personId)[2];
            String updateAddrTo = (String) person.get(personId)[3];
            String updateRoleTo = (String) person.get(personId)[4];
            while (true) {
                System.out.print("Continue updating fields: [Y/n] ");
                op = scan.nextLine();
                if (!op.equals("Y") && !op.equals("y")) {
                    break;
                }
                System.out.print("Select the field to update (F - First Name, L - Last Name, B - Birth Date, A - Address, R - Role): ");
                op = scan.nextLine();
                if (!op.equals("F") && !op.equals("f") && !op.equals("L") && !op.equals("l") && !op.equals("B") && !op.equals("b") && !op.equals("A") && !op.equals("a") && !op.equals("R") && !op.equals("r")) {
                    continue;
                }
                switch (op.toUpperCase()) {
                    case "F":
                        System.out.print("Input the first name:");
                        String firstName = scan.nextLine();
                        if (firstName.equals("")) {
                            System.out.println("First name can't be empty!");
                            break;
                        }
                        updateFirstNameTo = firstName;
                        break;
                    case "L":
                        System.out.print("Input the last name:");
                        String lastName = scan.nextLine();
                        if (lastName.equals("")) {
                            System.out.println("Last name can't be empty!");
                            break;
                        }
                        updateLastNameTo = lastName;
                        break;
                    case "B":
                        System.out.print("Input the birth date (yyyy-MM-dd):");
                        String dob = scan.nextLine();
                        if (dob.equals("") || !Utils.isValidDate(dob)) {
                            System.out.println("Birth date not correct!");
                            break;
                        }
                        updateDobTo = dob;
                        break;
                    case "A":
                        System.out.print("Input the address:");
                        String address = scan.nextLine();
                        if (address.equals("")) {
                            System.out.println("Address can't be empty!");
                            break;
                        }
                        updateAddrTo = address;
                        break;
                    case "R":
                        System.out.print("Input the role to assign (A - Administrator, C - Coordinator, I - Instructor, S - Student):");
                        String role = scan.nextLine();
                        role = role.toUpperCase();
                        if (!role.equals("A") && !role.equals("C") && !role.equals("I") && !role.equals("S")) {
                            System.out.println("Wrong selection!");
                            break;
                        }
                        updateRoleTo = roles.get(role);
                        break;
                    default:
                        System.out.println("Wrong selection!");
                }

                // id, firstName, lastName, dob, address, role
                person.put(personId, new Object[]{updateFirstNameTo, updateLastNameTo, updateDobTo, updateAddrTo, updateRoleTo});
                // write to data file
                DataHandler.savePerson(person);

                System.out.println("Person updated successfully!");
            }
        }
    }
    
    // delete person
    public void deletePerson() throws UnsupportedEncodingException, IOException {
        while (true) {
            System.out.print("Continue with deleting person?(Y/n)");
            String op = scan.nextLine();
            if (!(op.equals("Y") || op.equals("y") || op.equals(""))) {
                break;
            }
            if (person.keySet().isEmpty()) {
                System.out.println("No person exists.");
                continue;
            }
            getPersonInfo();
            System.out.print("Input the person id to delete:");
            String personIdIn = scan.nextLine();
            if (personIdIn.equals("") || !Pattern.matches("[1-9][0-9]*", personIdIn)) {
                    System.out.println("Person Id is not correct!");
                    continue;
            }
            int personId = Integer.parseInt(personIdIn);
            if (!person.keySet().contains(personId)) {
                System.out.println(String.format("Person %d does not exist.", personId));
                continue;
            }
            System.out.println(String.format("Person Id %d info: First Name - %s, Last Name - %s, Birth Date - %s, Address - %s, Role - %s", personId, person.get(personId)[0], person.get(personId)[1], person.get(personId)[2], person.get(personId)[3], person.get(personId)[4]));
            System.out.print("Please confirm to delte this person: [y/N]");
            op = scan.nextLine();
            if (!op.equals("Y") && !op.equals("y")) {
                continue;
            }
            person.remove(personId);
            // if person id is to be deleted - to be implemented in later versions
            // update accounts.data 
            
            // update courseEnrollments.data
            
            // update courseOfferings.data
            
            // update instructorCourseResults.data
            
            // update interestGroupEnrollments.data
            
            // update interestGroupOfferings.data
            
            // update person.data
            
            // update personInterestGroupResults.data
            
            // update studentCourseResults.data
            
            
            // write to data file
            DataHandler.savePerson(person);
            System.out.println(String.format("Person %d deleted successfully!", personId));
        }
    }
    
    // assign person id to account
    public void assignPersonIdToAccount(int personId, int accountId) throws UnsupportedEncodingException, IOException {
        // determine whether person id exists
        if (!person.keySet().contains(personId)) {
            System.out.println(String.format("Person Id %d does not exist", personId));
            return;
        }
        // id, username, password, personId
        accounts.put(accountId, new Object[]{accounts.get(accountId)[0], accounts.get(accountId)[1], personId});
        // write to data file
        DataHandler.saveAccounts(accounts);
        System.out.println(String.format("Person Id updated for account Id %d", accountId));
    }
    
    public void getPersonInfo() {
        String info = "Person List - \n";
        for (Map.Entry<Integer, Object[]> p : person.entrySet()) {
            info += String.format("Id:%d, First Name:%s, Last Name:%s, Birth Date:%s, Address:%s, Role:%s;\n", p.getKey(), p.getValue()[0], p.getValue()[1], p.getValue()[2], p.getValue()[3], p.getValue()[4]);
        }
        System.out.println(info);
    }
    
    
    
    // register account
    public void registerAccount() throws UnsupportedEncodingException, IOException {
        while (true) {
            System.out.print("Continue with account registration?(Y/n)");
            String op = scan.nextLine();
            if (!(op.equals("Y") || op.equals("y") || op.equals(""))) {
                break;
            }
            System.out.print("Input the username:");
            String username = scan.nextLine();
            if (username.equals("")) {
                System.out.println("Username can't be empty!");
                continue;
            }
            if (getUsernames().contains(username)) {
                    System.out.println("Username has been used. Try another name.");
                continue;
            }
            System.out.print("Input the password:");
            String password = scan.nextLine();
            if (password.equals("")) {
                System.out.println("Password can't be empty!");
                continue;
            }
            System.out.print("Input the password again:");
            String password2 = scan.nextLine();
            if (password2.equals("")) {
                System.out.println("Password can't be empty!");
                continue;
            }
            if (!password2.equals(password)) {
                System.out.println("Passwords do not match!");
                continue;
            }
            
            System.out.print("Assign person id to the account?[Y/n] ");
            String personIdIn;
            String personId = "0";
            op = scan.nextLine();
            if (op.equals("Y") || op.equals("y") || op.equals("")) {
                System.out.print("Input the person id to assign: ");
                personIdIn = scan.nextLine();
                if (Pattern.matches("[1-9][0-9]*", personIdIn)) {
                    if (person.keySet().contains(Integer.parseInt(personIdIn))) {
                        personId = personIdIn;
                    }
                }
            }
            
            // id, username, password, personId
            accounts.put(accounts.isEmpty()?1:Collections.max(accounts.keySet())+1, new Object[]{username, password, Integer.parseInt(personId)});
            // write to data file
            DataHandler.saveAccounts(accounts);
            System.out.println("User account registered successfully!");
        }
        
    }
    
    // edit account, reset password to "1234" or change person id
    public void editAccount() throws UnsupportedEncodingException, IOException {
        while (true) {
            System.out.print("Continue with editing account?(Y/n)");
            String op = scan.nextLine();
            if (!(op.equals("Y") || op.equals("y") || op.equals(""))) {
                break;
            }
            if (accounts.keySet().isEmpty()) {
                System.out.println("No account exists.");
                continue;
            }
            getAccountsInfo();
            System.out.print("Input the account id to edit:");
            String accountIdIn = scan.nextLine();
            if (accountIdIn.equals("") || !Pattern.matches("[1-9][0-9]*", accountIdIn)) {
                    System.out.println("Account Id is not correct!");
                    continue;
            }
            int accountId = Integer.parseInt(accountIdIn);
            if (!accounts.keySet().contains(accountId)) {
                System.out.println(String.format("Account %d does not exist.", accountId));
                continue;
            }
            System.out.println(String.format("Account Id %d info: Username - %s, Person Id - %d", accountId, accounts.get(accountId)[0], accounts.get(accountId)[2]));
            String updateUsernameTo = (String) accounts.get(accountId)[0];
            String updatePasswordTo = (String) accounts.get(accountId)[1];
            int updatePersonIdTo = (int) accounts.get(accountId)[2];
            while (true) {
                System.out.print("Continue updating fields: [Y/n] ");
                op = scan.nextLine();
                if (!op.equals("Y") && !op.equals("y")) {
                    break;
                }
                System.out.print("Select the field to update (U - Username, P - Person Id): ");
                op = scan.nextLine();
                if (!op.equals("U") && !op.equals("u") && !op.equals("P") && !op.equals("p")) {
                    continue;
                }
                switch (op.toUpperCase()) {
                    case "U":
                        System.out.print("Input the username:");
                        String username = scan.nextLine();
                        if (username.equals("")) {
                            System.out.println("Username can't be empty!");
                            break;
                        }
                        updateUsernameTo = username;
                        break;
                    case "P":
                        System.out.print("Input the person id:");
                        String personIdIn = scan.nextLine();
                        if (!Pattern.matches("[1-9][0-9]*", personIdIn)) {
                            System.out.println("Person id not correct!");
                            break;
                        }
                        if (!person.keySet().contains(Integer.parseInt(personIdIn))) {
                            System.out.println("Person id does not exist!");
                            break;
                        }
                        updatePersonIdTo = Integer.parseInt(personIdIn);
                        break;
                    default:
                        System.out.println("Wrong selection!");
                }
                
                System.out.print("Reset the password?[y/N]:");
                op = scan.nextLine();
                if ((op.equals("Y") || op.equals("y"))) {
                    updatePasswordTo = "1234";
                    System.out.println("Password is reset to 1234!");
                }

                // id, username, password, personId
                accounts.put(accountId, new Object[]{updateUsernameTo, updatePasswordTo, updatePersonIdTo});
                // write to data file
                DataHandler.saveAccounts(accounts);
                System.out.println("Account updated successfully!");
            }
        }
    }
    
    // delete account
    public void deleteAccount() throws UnsupportedEncodingException, IOException {
        while (true) {
            System.out.print("Continue with deleting account?(Y/n)");
            String op = scan.nextLine();
            if (!(op.equals("Y") || op.equals("y") || op.equals(""))) {
                break;
            }
            if (accounts.keySet().isEmpty()) {
                System.out.println("No account exists.");
                continue;
            }
            getAccountsInfo();
            System.out.print("Input the account id to delete:");
            String accountIdIn = scan.nextLine();
            if (accountIdIn.equals("") || !Pattern.matches("[1-9][0-9]*", accountIdIn)) {
                    System.out.println("Account Id is not correct!");
                    continue;
            }
            int accountId = Integer.parseInt(accountIdIn);
            if (!accounts.keySet().contains(accountId)) {
                System.out.println(String.format("Account %d does not exist.", accountId));
                continue;
            }
            System.out.println(String.format("Account Id %d info: Username - %s, Person Id - %d", accountId, accounts.get(accountId)[0], accounts.get(accountId)[2]));
            System.out.print("Please confirm to delte this account: [y/N]");
            op = scan.nextLine();
            if (!op.equals("Y") && !op.equals("y")) {
                continue;
            }
            accounts.remove(accountId);
            // write to data file
            DataHandler.saveAccounts(accounts);
            System.out.println(String.format("Account %d deleted successfully!", accountId));
        }
    }
    
    public void getAccountsInfo() {
        String info = "Accounts List - \n";
        for (Map.Entry<Integer, Object[]> acc : accounts.entrySet()) {
            info += String.format("Id:%d, Username:%s, Person Id:%d;\n", acc.getKey(), acc.getValue()[0], acc.getValue()[2]);
        }
        System.out.println(info);
    }
    
    public Set<String> getUsernames() {
        Set<String> usernames = new TreeSet<String>();
        for (Map.Entry<Integer, Object[]> acc : accounts.entrySet()) {
            usernames.add(acc.getValue()[0].toString());
        }
        return usernames;
    }
    
    // register course
    public void registerCourse() throws UnsupportedEncodingException, IOException {
        while (true) {
            System.out.print("Continue with course registration?(Y/n)");
            String op = scan.nextLine();
            if (!(op.equals("Y") || op.equals("y") || op.equals(""))) {
                break;
            }
            System.out.print("Input the course name:");
            String courseName = scan.nextLine();
            if (courseName.equals("")) {
                System.out.println("Course name can't be empty!");
                continue;
            } 
            System.out.print("Input the department:");
            String department = scan.nextLine();
            if (department.equals("")) {
                System.out.println("Department can't be empty!");
                continue;
            }
            System.out.print("Input the hours:");
            String hours = scan.nextLine();
            if (hours.equals("") || !Pattern.matches("[1-9][0-9]*", hours)) {
                System.out.println("Hours should be positive integer!");
                continue;
            }
            System.out.print("Input the credits:");
            String credits = scan.nextLine();
            if (credits.equals("") || !Pattern.matches("^[1-9].[0,5]$|^[0].[5]$|[1-5]$", credits)) {
                System.out.println("Credits should be 0.5 to 5.0 with 0.5 as increments.");
                continue;
            }
            
            // check whether the course already exists
            boolean courseExists = false;
            for (Map.Entry<Integer, Object[]> course : courses.entrySet()) {
                if (course.getValue()[0].equals(courseName) && course.getValue()[1].equals(department) && (int) course.getValue()[2] == Integer.parseInt(hours) && (double) course.getValue()[3] == Double.parseDouble(credits)) {
                    courseExists = true;
                    break;
                }
            }
            if (courseExists) {
                System.out.println("Course already exists!");
                continue;
            }
            
            // id, name, department, hours, credits
            courses.put(courses.isEmpty()?1:Collections.max(courses.keySet())+1, new Object[]{courseName, department, Integer.parseInt(hours), Double.parseDouble(credits)});
            // write to data file
            DataHandler.saveCourses(courses);
            System.out.println("Course registered successfully!");
            
        }
    }
    
    // edit course
    public void editCourse() throws UnsupportedEncodingException, IOException {
        while (true) {
            System.out.print("Continue with editing course?(Y/n)");
            String op = scan.nextLine();
            if (!(op.equals("Y") || op.equals("y") || op.equals(""))) {
                break;
            }
            if (courses.keySet().isEmpty()) {
                System.out.println("No course exists.");
                continue;
            }
            Utils.getCoursesInfo(courses);
            System.out.print("Input the course id to edit:");
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
            System.out.println(String.format("Course Id %d info: Name - %s, Department - %s, Hours - %d, Credits - %f", courseId, courses.get(courseId)[0], courses.get(courseId)[1], courses.get(courseId)[2], courses.get(courseId)[3]));
            String updateNameTo = (String) courses.get(courseId)[0];
            String updateDepartmentTo = (String) courses.get(courseId)[1];
            Integer updateHoursTo = (Integer) courses.get(courseId)[2];
            Double updateCreditsTo = (Double) courses.get(courseId)[3];
            while (true) {
                System.out.print("Continue updating fields: [Y/n] ");
                op = scan.nextLine();
                if (!op.equals("Y") && !op.equals("y")) {
                    break;
                }
                System.out.print("Select the field to update (N - Name, D - Department, H - Hours, C - Credits): ");
                op = scan.nextLine();
                if (!op.equals("N") && !op.equals("n") && !op.equals("D") && !op.equals("d") && !op.equals("H") && !op.equals("h") && !op.equals("C") && !op.equals("c")) {
                    continue;
                }
                switch (op.toUpperCase()) {
                    case "N":
                        System.out.print("Input the course name:");
                        String courseName = scan.nextLine();
                        if (courseName.equals("")) {
                            System.out.println("Course name can't be empty!");
                            break;
                        }
                        updateNameTo = courseName;
                        break;
                    case "D":
                        System.out.print("Input the department name:");
                        String departmentName = scan.nextLine();
                        if (departmentName.equals("")) {
                            System.out.println("Department name can't be empty!");
                            break;
                        }
                        updateDepartmentTo = departmentName;
                        break;
                    case "H":
                        System.out.print("Input the hours:");
                        String hours = scan.nextLine();
                        if (hours.equals("") || !Pattern.matches("[1-9][0-9]*", hours)) {
                            System.out.println("Hours should be positive integer!");
                            break;
                        }
                        updateHoursTo = Integer.parseInt(hours);
                        break;
                    case "C":
                        System.out.print("Input the credits:");
                        String credits = scan.nextLine();
                        if (credits.equals("") || !Pattern.matches("^[1-9].[0,5]$|^[0].[5]$|[1-5]$", credits)) {
                            System.out.println("Credits should be 0.5 to 5.0 with 0.5 as increments.");
                            break;
                        }   
                        updateCreditsTo = Double.parseDouble(credits);
                        break;
                    default:
                        System.out.println("Wrong selection!");
                }

                // id, name, department, hours, credits
                courses.put(courseId, new Object[]{updateNameTo, updateDepartmentTo, updateHoursTo, updateCreditsTo});
                // write to data file
                DataHandler.saveCourses(courses);

                System.out.println("Course updated successfully!");
            }
        }
    }
    
    // delete course
    public void deleteCourse() throws UnsupportedEncodingException, IOException {
        while (true) {
            System.out.print("Continue with deleting course?(Y/n)");
            String op = scan.nextLine();
            if (!(op.equals("Y") || op.equals("y") || op.equals(""))) {
                break;
            }
            if (courses.keySet().isEmpty()) {
                System.out.println("No course exists.");
                continue;
            }
            Utils.getCoursesInfo(courses);
            System.out.print("Input the course id to delete:");
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
            System.out.println(String.format("Course Id %d info: Name - %s, Department - %s, Hours - %d, Credits - %f", courseId, courses.get(courseId)[0], courses.get(courseId)[1], courses.get(courseId)[2], courses.get(courseId)[3]));
            System.out.print("Please confirm to delte this course: [y/N]");
            op = scan.nextLine();
            if (!op.equals("Y") && !op.equals("y")) {
                continue;
            }
            courses.remove(courseId);
            // write to data file
            DataHandler.saveCourses(courses);
            System.out.println(String.format("Course %d deleted successfully!", courseId));
        }
    }
    
    // register interest group
    public void registerInterestGroup() throws UnsupportedEncodingException, IOException {
        while (true) {
            System.out.print("Continue with interest group registration?(Y/n)");
            String op = scan.nextLine();
            if (!(op.equals("Y") || op.equals("y") || op.equals(""))) {
                break;
            }
            System.out.print("Input the interest group name:");
            String interestGroupName = scan.nextLine();
            if (interestGroupName.equals("")) {
                System.out.println("Interest group name can't be empty!");
                continue;
            } 
            System.out.print("Input the category:");
            String category = scan.nextLine();
            if (category.equals("")) {
                System.out.println("Category can't be empty!");
                continue;
            }
            
            // check whether the interest group already exists
            boolean interestGroupExists = false;
            for (Map.Entry<Integer, Object[]> interestGroup : interestGroups.entrySet()) {
                if (interestGroup.getValue()[0].equals(interestGroupName) && interestGroup.getValue()[1].equals(category)) {
                    interestGroupExists = true;
                    break;
                }
            }
            if (interestGroupExists) {
                System.out.println("Interest group already exists!");
                continue;
            }
            
            // id, name, category
            interestGroups.put(interestGroups.isEmpty()?1:Collections.max(interestGroups.keySet())+1, new Object[]{interestGroupName, category});
            // write to data file
            DataHandler.saveInterestGroups(interestGroups);
            System.out.println("Interest group registered successfully!");
            
        }
    }
    
    // edit interest group
    public void editInterestGroup() throws UnsupportedEncodingException, IOException {
        while (true) {
            System.out.print("Continue with editing interest group?(Y/n)");
            String op = scan.nextLine();
            if (!(op.equals("Y") || op.equals("y") || op.equals(""))) {
                break;
            }
            if (interestGroups.keySet().isEmpty()) {
                System.out.println("No interest group exists.");
                continue;
            }
            getInterestGroupsInfo();
            System.out.print("Input the interest group id to edit:");
            String interestGroupIdIn = scan.nextLine();
            if (interestGroupIdIn.equals("") || !Pattern.matches("[1-9][0-9]*", interestGroupIdIn)) {
                    System.out.println("Interest group Id is not correct!");
                    continue;
            }
            int interestGroupId = Integer.parseInt(interestGroupIdIn);
            if (!interestGroups.keySet().contains(interestGroupId)) {
                System.out.println(String.format("Interest Group %d does not exist.", interestGroupId));
                continue;
            }
            System.out.println(String.format("Interest Group Id %d info: Name - %s, Category - %s",interestGroupId, interestGroups.get(interestGroupId)[0], interestGroups.get(interestGroupId)[1]));
            String updateNameTo = (String) interestGroups.get(interestGroupId)[0];
            String updateCategoryTo = (String) interestGroups.get(interestGroupId)[1];
            while (true) {
                System.out.print("Continue updating fields: [Y/n] ");
                op = scan.nextLine();
                if (!op.equals("Y") && !op.equals("y")) {
                    break;
                }
                System.out.print("Select the field to update (N - Name, C - Category): ");
                op = scan.nextLine();
                if (!op.equals("N") && !op.equals("n") && !op.equals("C") && !op.equals("c")) {
                    continue;
                }
                switch (op.toUpperCase()) {
                    case "N":
                        System.out.print("Input the interest group name:");
                        String interestGroupName = scan.nextLine();
                        if (interestGroupName.equals("")) {
                            System.out.println("Interest group name can't be empty!");
                            break;
                        }
                        updateNameTo = interestGroupName;
                        break;
                    case "C":
                        System.out.print("Input the category:");
                        String categoryName = scan.nextLine();
                        if (categoryName.equals("")) {
                            System.out.println("Category name can't be empty!");
                            break;
                        }
                        updateCategoryTo = categoryName;
                        break;
                    default:
                        System.out.println("Wrong selection!");
                }

                // id, name, category
                interestGroups.put(interestGroupId, new Object[]{updateNameTo, updateCategoryTo});
                // write to data file
                DataHandler.saveInterestGroups(interestGroups);

                System.out.println("Interest group updated successfully!");
            }
        }
    }
    
    // delete interest group
    public void deleteInterestGroup() throws UnsupportedEncodingException, IOException {
        while (true) {
            System.out.print("Continue with deleting interest group?(Y/n)");
            String op = scan.nextLine();
            if (!(op.equals("Y") || op.equals("y") || op.equals(""))) {
                break;
            }
            if (interestGroups.keySet().isEmpty()) {
                System.out.println("No interest group exists.");
                continue;
            }
            getInterestGroupsInfo();
            System.out.print("Input the interest group id to delete:");
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
            System.out.println(String.format("Interest Group Id %d info: Name - %s, Category - %s",interestGroupId, interestGroups.get(interestGroupId)[0], interestGroups.get(interestGroupId)[1]));
            System.out.print("Please confirm to delte this interest group: [y/N]");
            op = scan.nextLine();
            if (!op.equals("Y") && !op.equals("y")) {
                continue;
            }
            interestGroups.remove(interestGroupId);
            // write to data file
            DataHandler.saveInterestGroups(interestGroups);
            System.out.println(String.format("Interest group  %d deleted successfully!", interestGroupId));
        }
    }
    
    public void getInterestGroupsInfo() {
        String info = "Interest Groups List - \n";
        for (Map.Entry<Integer, Object[]> interestGroup : interestGroups.entrySet()) {
            info += String.format("Id:%d, Name:%s, Category:%s;", interestGroup.getKey(), interestGroup.getValue()[0], interestGroup.getValue()[1]);
        }
        System.out.println(info);
    }
    
    public static void main(String[] args) throws ParseException, IOException {
        Administrator admin = new Administrator("Student", true, "0001", "John", "Doe", "1985-11-12", "Ottawa, Canada");
        admin.registerPerson();
        admin.editPerson();
        admin.deletePerson();
        admin.registerAccount();
        admin.editAccount();
        admin.deleteAccount();
        admin.registerCourse();
        admin.editCourse();
        admin.deleteCourse();
        admin.registerInterestGroup();
        admin.editInterestGroup();
        admin.deleteInterestGroup();
    }
}
