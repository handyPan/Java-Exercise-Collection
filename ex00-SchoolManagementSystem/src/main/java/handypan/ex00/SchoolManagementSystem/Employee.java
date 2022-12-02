/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package handypan.ex00.SchoolManagementSystem;

/**
 *
 * @author handyPan
 * This is to practice "abstract class" - it's not allowed to instantiate "abstract" class
 */
public abstract class Employee {
    private String employementType;
    private int hoursPerWeek;
    private String payMethod;

    public Employee(String employementType, int hoursPerWeek, String payMethod) {
        this.employementType = employementType;
        this.hoursPerWeek = hoursPerWeek;
        this.payMethod = payMethod;
    }
    
    public int getHoursPerWeek() {
        return hoursPerWeek;
    }
    
    public String getPayMethod() {
        return payMethod;
    }

    public void payWeekly() {
        System.out.println(String.format("Pay by %s every week.", payMethod));
    }
    
    // abstract method to be implemented in the subclass
    public abstract void payMonthly();
    
}
