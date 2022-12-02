/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package handypan.ex00.SchoolManagementSystem;

/**
 *
 * @author handyPan
 */
public class InterestGroup {
    private Integer id;
    private String name;
    private String category;

    public InterestGroup(Integer id, String name, String category) {
        this.id = id;
        this.name = name;
        this.category = category;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "InterestGroup{" + "id=" + id + ", name=" + name + ", category=" + category + '}';
    }
    
    public String getInfo() {
        return String.format("Interest Group Information - ID: %d, Name: %s, Category: %s", id, name, name, category);
    }
    
    public static void main(String[] args) {
        InterestGroup ig1 = new InterestGroup(1, "Hiking", "Sports");
        System.out.println(ig1.toString());
        System.out.println(ig1.getInfo());
    }
}
