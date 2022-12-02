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
import java.text.SimpleDateFormat;
import java.util.Date;
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
public class Person {
    private int id;
    private String fName;
    private String lName;
    private Date dob;
    private String addr;
    private String role;
    // student or instructor register an interest group, it will be approved by the coordinator or not;
    
    // interest groups - the data structure is interestGroupId, leaderId, capacity
    // ex. 1, 1, 5; 2, 3, 10; 
    private static Map<Integer, Object[]> interestGroupOfferings;    
    
    // interest groups enrollments - the data structure is interestGroupId, person Id list
    // ex. 1={1, 3, 4, 7, 9}, 2={1, 2, 3, 7}
    private static Map<Integer, TreeSet<Integer>> interestGroupEnrollments;
    
    // person interest group results - the data structure is personId, interstGroupId, isApproved, isEnrolled
    // ex. 1={1={true, true}, 3={true, true}}, 2={2={false, false}}
    // interest group description for above example: 1 - "Movie", 2 - "Cycling", 3 - "Hockey"
    private static Map<Integer, Object> interestGroupResults;
    
    // the data sructure is interestGroupId, approved, enrolled
    // ex. 1, True, True; 2, True, False; 3, False, False;
    // interest group description for above example: 1 - "Movie", 2 - "Cycling", 3 - "Hockey"
    private Map<Integer, Object[]> interestGroups = new TreeMap<Integer, Object[]>();
    
    private Scanner scan = new Scanner(System.in);
    
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    
    public Person(String _role, boolean directlyAssign, String... params) throws ParseException, IOException {
        // String... params - String _id, String _fName, String _lName, String _dob, String _addr
        if (directlyAssign) {
            role = _role;
            id = Integer.parseInt(params[0]);
            fName = params[1];
            lName = params[2];
            dob = sdf.parse(params[3]);
            addr = params[4];
        } else {
            role = _role;
            Scanner scan = new Scanner(System.in);
            System.out.print(String.format("Enter %s ID:", _role));
            id = Integer.parseInt(scan.nextLine());
            System.out.print(String.format("Enter %s First Name:", _role));
            fName = scan.nextLine();
            System.out.print(String.format("Enter %s Last Name:", _role));
            lName = scan.nextLine();
            System.out.print(String.format("Enter %s Date of Birth in the format 'yyyy-MM-dd':", _role));
            dob = sdf.parse(scan.nextLine());
            System.out.print(String.format("Enter %s Address:", _role));
            addr = scan.nextLine();
        }
        
        // populate interest group offerings
        interestGroupOfferings = DataHandler.loadInterestGroupOfferings();
        
        // populate interest group enrollments
        interestGroupEnrollments = DataHandler.loadInterestGroupEnrollments();
        
        // populate interest group results
        interestGroupResults = DataHandler.loadPersonInterestGroupResults();
    }
    
    public void registerInterestGroup() throws UnsupportedEncodingException, IOException {
        while (true) {
            System.out.print("Continue with interest group registration?(Y/n)");
            String op = scan.nextLine();
            if (!(op.equals("Y") || op.equals("y") || op.equals(""))) {
                break;
            }
            int personId = this.id;
            if (interestGroupOfferings.keySet().isEmpty()) {
                System.out.println("No interest group offering exists.");
                continue;
            }
            Utils.getInterestGroupOfferingsInfo(interestGroupOfferings);
            System.out.print("Input the interest group id to register:");
            String interestGroupIdIn = scan.nextLine();
            if (interestGroupIdIn.equals("") || !Pattern.matches("[1-9][0-9]*", interestGroupIdIn)) {
                    System.out.println("Interest Group Id is not correct!");
                    continue;
            }
            int interestGroupId = Integer.parseInt(interestGroupIdIn);
            if (!interestGroupOfferings.keySet().contains(interestGroupId)) {
                System.out.println(String.format("Interest Group %d is not offered.", interestGroupId));
                continue;
            }
            boolean interestGroupRegistered = false;
            ObjectMapper om = new ObjectMapper();
            Map<Integer, Object[]> recordToSave = new TreeMap<Integer, Object[]>();
            for (Map.Entry<Integer, Object> interestGroupResult : interestGroupResults.entrySet()) {
                Object obj = interestGroupResult.getValue();
                Map<Integer, Object[]> records = om.convertValue(
                        obj,
                        new TypeReference<Map<Integer, Object[]>>(){}
                );
                if (interestGroupResult.getKey() == personId) {
                    recordToSave = records;
                    for (Map.Entry<Integer, Object[]> record : records.entrySet()) {
                        if (record.getKey() == interestGroupId) {
                            interestGroupRegistered = true;
                            break;
                        }
                    }
                }
            }
            recordToSave.put(interestGroupId, new Object[]{false, false});
            if (interestGroupRegistered) {
                System.out.println("Interest Group Already registered! Update it?(Y/n)");
                op = scan.nextLine();
                if (!(op.equals("Y") || op.equals("y") || op.equals(""))) {
                    continue;
                }
                // personId, interestGroupId, isApproved, isEnrolled
                interestGroupResults.put(personId, recordToSave);
                DataHandler.savePersonInterestGroupResults(interestGroupResults);
                System.out.println("Person Interest Group Registration updated successfully!");
                continue;
            }
            interestGroupResults.put(personId, recordToSave);
            DataHandler.savePersonInterestGroupResults(interestGroupResults);
            System.out.println("Person Interest Group Registration added successfully!");   
        }
        
    }
    
    public void dropInterestGroup() throws UnsupportedEncodingException, IOException {
        while (true) {
            System.out.print("Continue with dropping interest group?(Y/n)");
            String op = scan.nextLine();
            if (!(op.equals("Y") || op.equals("y") || op.equals(""))) {
                break;
            }
            int personId = this.id;
            ObjectMapper om = new ObjectMapper();
            Object obj = interestGroupResults.get(personId);
            Map<Integer, Object[]> records = om.convertValue(
                    obj,
                    new TypeReference<Map<Integer, Object[]>>(){}
            );
            if (records.keySet().isEmpty()) {
                System.out.println(String.format("No interest group exists for person %d.", personId));
                continue;
            }
            Utils.getInterestGroupRegisrationInfo(interestGroupResults, personId);
            System.out.print("Input the interst group id to drop:");
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
            System.out.println(String.format("Interest Group Id %d registration info: Is Approved - %b, Is Enrolled - %b", interestGroupId, records.get(interestGroupId)[0], records.get(interestGroupId)[1]));
            System.out.print("Please confirm to delete this course: [y/N]");
            op = scan.nextLine();
            if (!op.equals("Y") && !op.equals("y")) {
                continue;
            }
            records.remove(interestGroupId);
            interestGroupResults.put(personId, records);
            interestGroupEnrollments.remove(interestGroupId);
            // write to data file
            DataHandler.savePersonInterestGroupResults(interestGroupResults);
            DataHandler.saveInterestGroupEnrollments(interestGroupEnrollments);
            System.out.println(String.format("Interest Group %d dropped successfully!", interestGroupId));
        }
    } 

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }
    
    public Map<Integer, Object[]> getPersonInterestGroups() {
        return interestGroups;
    }
    
    public Set getInterests() {
        return interestGroups.keySet();
    }
    
    public void registerInterestGroup(Integer interestGroupId) {
        // ex. 4, False, False;
        // 4 - "Hiking"
        interestGroups.put(interestGroupId, new Object[]{false, false});
    }
    
    public void dropInterestGroup(Integer interestGroupId) {
        interestGroups.remove(interestGroupId);
    }
    
    public Object[] getInterestGroupEnrollResult(Integer interestGroupId) {
        return interestGroups.get(interestGroupId);
    }

    @Override
    public String toString() {
        return "Person{" + "id=" + id + ", fName=" + fName + ", lName=" + lName + ", dob=" + dob + ", addr=" + addr + ", role=" + role + ", interestGroups=" + interestGroups + ", sdf=" + sdf + '}';
    }

    public String getInfo() {
        String info =  String.format("Info - ID:%d, Name: %s %s, Dob: %s, Address: %s, Role: %s", id, fName, lName, sdf.format(dob), addr, role);
        // traverse interestGroups = map
        info += ", Interest Groups: ";
        for (Map.Entry<Integer, Object[]> interestGroup : interestGroups.entrySet()) {
            info += interestGroup.getKey() + ", " + interestGroup.getValue()[0].toString() + ", " + interestGroup.getValue()[1].toString() + ";";
        }
        return info;
    }
    
    public static void main(String[] args) throws ParseException, IOException {
        // test the class
        // String... params - String _id, String _fName, String _lName, String _dob, String _addr
        Person p1 = new Person("Student", true, "0001", "John", "Doe", "1985-11-12", "Ottawa, Canada");
        System.out.println(p1.toString());
        System.out.println(p1.getInfo());
        System.out.println("Interests - " + p1.getInterests());
        System.out.println("Interest Groups - " + p1.getPersonInterestGroups());
        
        Person p2 = new Person("Student", false);
        System.out.println(p2.toString());
        System.out.println(p2.getInfo());
        System.out.println("Interests - " + p2.getInterests());
        System.out.println("Interest Groups - " + p2.getPersonInterestGroups());
        
        Scanner scan = new Scanner(System.in);
        
        p1.registerInterestGroup();
        p1.dropInterestGroup();
        p2.registerInterestGroup();
        p2.dropInterestGroup();
        
        System.out.println(p2.toString());
        System.out.println(p2.getInfo());
        System.out.println("Interests - " + p2.getInterests());
        System.out.println("Interest Groups - " + p2.getPersonInterestGroups());
        
        int ig = 3;
        if (p2.getInterests().contains(ig)) {
            p2.dropInterestGroup(ig);
            System.out.println(String.format("Interest group %d is dropped.", ig));
        }
        System.out.println(p2.toString());
        System.out.println(p2.getInfo());
        System.out.println("Interests - " + p2.getInterests());
        System.out.println("Interest Groups - " + p2.getPersonInterestGroups());
        
    }
}
