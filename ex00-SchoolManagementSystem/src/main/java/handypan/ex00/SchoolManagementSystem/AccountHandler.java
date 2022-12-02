/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package handypan.ex00.SchoolManagementSystem;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author handyPan
 */
public class AccountHandler {
    
    // accounts -  the data structure is id, username, password, personId
    // ex. 1, "admin", "1234", 1
    private Map<Integer, Object[]> accounts;
    private Map<Integer, Object[]> person;
    
    private Scanner scan = new Scanner(System.in);
    
    public AccountHandler() throws IOException {
        // load accounts from file or database
        accounts = DataHandler.loadAccounts();
        accounts.put(1, new Object[]{"admin", "1234", 1});  // the default account
        person = DataHandler.loadPerson();
    }
    
    public Account welcome() throws UnsupportedEncodingException, IOException {
        while (true) {
            System.out.print("Welcome to the School Management System.\nChoose options to proceed - [L]Login, [S]Singnup, [Q]Quit: ");
            String op = scan.nextLine();
            if (op.equals("L") || op.equals("l")) {
                System.out.print("To login, input the username:");
            } else if (op.equals("S") || op.equals("s")) {
                System.out.print("To signup, input the username:");
            } else if (op.equals("Q") || op.equals("q")) {
                System.out.print("Quit system.");
                System.exit(0);
            }else {
                System.out.println("Wrong selection. Program terminated!");
                System.exit(0);
            }

            String username = scan.nextLine();
            if (username.equals("")) {
                System.out.println("Username can't be empty!");
                continue;
            }
            
            // if to sign up, validate whether the username already exists
            if (op.equals("S") || op.equals("s")) {
                if (getUsernames().contains(username)) {
                    System.out.println("Username has been used. Try another name.");
                    continue;
                }
            }
            
            System.out.print("Input the password:");
            String password = scan.nextLine();
            if (password.equals("")) {
                System.out.println("Password can't be empty!");
                continue;
            }

            // if to sign up
            if (op.equals("S") || op.equals("s")) {
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
                
                // id, username, password, personId
                accounts.put(Collections.max(accounts.keySet())+1, new Object[]{username, password, 0});
                // write to data file
                DataHandler.saveAccounts(accounts);
                
                System.out.println("User account registered successfully!");
                continue;
            }
            
            // if to login in, verify whether the username, password exists
            // if exists, return
            int accountId, personId;
            String role = "Guest";
            for (Map.Entry<Integer, Object[]> acc : accounts.entrySet()) {
                if (acc.getValue()[0].equals(username) && acc.getValue()[1].equals(password)) {     
                    accountId = acc.getKey();
                    personId = (int) acc.getValue()[2];
                    if ((int)acc.getValue()[2] != 0) {
                        role = (String) person.get(acc.getValue()[2])[4];
                    }
                    System.out.println(String.format("Welcome %s - %s! Login successfully!", role.toUpperCase(), username)); 
                    return new Account(accountId, username, password, personId);
                }
            }
            
            // if not exist, do welcome again
            System.out.println("Username or password not correct! Login again!");
        }
        
    }
    
    public Set<String> getUsernames() {
        Set<String> usernames = new TreeSet<String>();
        for (Map.Entry<Integer, Object[]> acc : accounts.entrySet()) {
            usernames.add(acc.getValue()[0].toString());
        }
        return usernames;
    }
    
    public String getAccounts() {
        String info = "Accounts List - \n";
        for (Map.Entry<Integer, Object[]> acc : accounts.entrySet()) {
            info += String.format("Id:%d, Username:%s, Password:%s, PersonId:%d;\n", acc.getKey(), acc.getValue()[0], acc.getValue()[1], acc.getValue()[2]);
        }
        return info;
    }
    
    public static void main(String[] args) throws IOException {
        AccountHandler accHdl = new AccountHandler();
        accHdl.welcome();
        System.out.println(accHdl.getAccounts());
    }
    
}
