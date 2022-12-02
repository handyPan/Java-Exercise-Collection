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
 */
public class Graduate extends Student {
    
    private String yearLength;
    private String studyStyle;

    public Graduate(String _role, boolean directlyAssign, String... params) throws ParseException, IOException {
        super(_role, directlyAssign, params);
        yearLength = "2 years";
        studyStyle = "research based";
    }
    
    @Override
    public String getInfo() {
        return "Graduate " +
                super.getInfo() + 
                "\nYear length - " + yearLength + 
                "\nStudy style - " + studyStyle
                ;
    }
    
    
}
