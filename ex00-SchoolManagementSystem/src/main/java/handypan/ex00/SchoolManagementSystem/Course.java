/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package handypan.ex00.SchoolManagementSystem;

/**
 *
 * @author handyPan
 */
public class Course {
    private Integer id;
    private String name;
    private String department;
    private Integer hours;
    private Double credits;

    public Course(Integer id, String name, String department, Integer hours, Double credits) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.hours = hours;
        this.credits = credits;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public Double getCredits() {
        return credits;
    }

    public void setCredits(Double credits) {
        this.credits = credits;
    }

    @Override
    public String toString() {
        return "Course{" + "id=" + id + ", name=" + name + ", department=" + department + ", hours=" + hours + ", credits=" + credits + '}';
    }
    
    public String getInfo() {
        return String.format("Course Information - ID: %d, Name: %s, Department: %s, Hours: %d, Credits: %.1f", id, name, department, hours, credits);
    }
    
    public static void main(String[] args) {
        Course c1 = new Course(1, "Machine Learning", "Computer Science", 30, 1.0);
        System.out.println(c1.toString());
        System.out.println(c1.getInfo());
    }
}
