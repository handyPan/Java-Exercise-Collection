/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package handypan.ex00.SchoolManagementSystem;

import java.io.IOException;
import java.text.ParseException;

/**
 *
 * @author handyPan
 * This is to practice Polymorphism
 */
public class Undergraduate extends Student implements UndergraduateActivity {
    
    private String yearLength;
    private String studyStyle;

    public Undergraduate(String _role, boolean directlyAssign, String... params) throws ParseException, IOException {
        super(_role, directlyAssign, params);
        yearLength = "4 years";
        studyStyle = "course based";
    }
    
    @Override
    public String getInfo() {
        return "Undergraduate " + 
                super.getInfo() + 
                "\nYear length - " + yearLength + 
                "\nStudy style - " + studyStyle
                ;
    }
    
    public static void show(Student stu) {
        // determine the class type
        if (stu instanceof Undergraduate) {
            Undergraduate uStu = (Undergraduate) stu;
            System.out.println(uStu.getInfo());
        } else if (stu instanceof Graduate) {
            Graduate gStu = (Graduate) stu;
            System.out.println(gStu.getInfo());
        } else {
            
        }
    }

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
    
    public static void main(String[] args) throws ParseException, IOException {
        show(new Undergraduate("Student", true, "0001", "John", "Doe", "1985-11-12", "Toronto, Canada"));
        show(new Graduate("Student", true, "0002", "Jane", "Dawn", "1991-10-19", "Newyork, USA"));
        
        // test converting Undergraduate to Student
        Student stu1 = new Undergraduate("Student", true, "0003", "Jerry", "Don", "1995-7-12", "Paris, France");
        System.out.println(stu1.getInfo());
        
        // test implementation of the interface methods
        Undergraduate uStu = new Undergraduate("Student", true, "0004", "Jackie", "Daug", "1975-3-28", "Boston, USA");
        uStu.doAssignments(4);
        uStu.doCapstone("Application of Machine Learning");
        uStu.doCoop(200);
        uStu.doSports();
        uStu.goShopping();
        uStu.haveMeals();
        uStu.takeLectures(60);
        uStu.writeExams(2);
    }

}
