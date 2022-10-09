/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package ex01atm;

/**
 *
 * @author yoyo.yao
 */
public class Ex01ATM {

    public static void main(String[] args) {
        // System.out.println("Hello World!");
        
        Start startAtm = new Start();
        startAtm.setVisible(true);
        
        try {
            for (int i = 0; i <= 100; i++) {
                Thread.sleep(100);
                startAtm.getPbrLoading().setValue(i);
            }
        } catch (Exception e) {
            
        }
        
        Login loginAtm = new Login();
        startAtm.dispose();
        loginAtm.setVisible(true);
        
    }
}
