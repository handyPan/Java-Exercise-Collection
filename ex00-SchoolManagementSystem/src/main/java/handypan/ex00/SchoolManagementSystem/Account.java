/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package handypan.ex00.SchoolManagementSystem;

/**
 *
 * @author handyPan
 */
public class Account {
    private int id;
    private String username;
    private String password;
    private int personId;

    public Account(int id, String username, String password, int personId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.personId = personId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    @Override
    public String toString() {
        return "Account{" + "id=" + id + ", username=" + username + ", password=" + password + ", personId=" + personId + '}';
    }
    
    public static void main(String[] args) {
        Account acc = new Account(1, "admin", "1234", 1);
        System.out.println(acc.toString());
    }
}
