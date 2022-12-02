/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package handypan.ex00.SchoolManagementSystem;

/**
 *
 * @author handyPan
 */
public class Salary extends Employee {
    private double salaryPerHour;

    public Salary(double salaryPerHour, String employementType, int hoursPerWeek, String payMethod) {
        super(employementType, hoursPerWeek, payMethod);
        this.salaryPerHour = salaryPerHour;
    }
    
    public double getPayPerWeek() {
        return this.salaryPerHour * super.getHoursPerWeek();
    }
    
    public double getPayPerMonth() {
        return 4 * getPayPerWeek();
    }
    
    public void payWeekly() {
        System.out.println(String.format("Pay %f by %s every week.", getPayPerWeek(), super.getPayMethod()));
    }
    
    public void payMonthly() {
        System.out.println(String.format("Pay %f by %s every month.", getPayPerMonth(), super.getPayMethod()));
    }
    
    public static void main(String[] args) {
        Salary s = new Salary(15.0, "Fulltime", 40, "Cheque");
        s.payWeekly();
        s.payMonthly();
        Employee e = new Salary(12.0, "Parttime", 20, "E-transfer");
        e.payWeekly();
        e.payMonthly();
    }
}
