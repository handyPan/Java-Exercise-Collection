/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package handypan.ex00.SchoolManagementSystem;

/**
 *
 * @author handyPan
 * This is to practice "enum" 
 * - all enum values are "public static final" and implemented in a class.
 * - each obj needs to implement the abstract method in an enum
 */

enum CourseResult {
    PASS, FAIL;
}

enum ImportantDates {
    ORIENTATION, LAST_WITHDRAW, MID_TERM, FINAL_EXAM, GRADE_SUBMISSION;
    
    // constructor of enum has to be private
    private ImportantDates() {
        System.out.println("This is constructor of enum " + ImportantDates.class);
    }
    
    public void getInfo() {
        System.out.println("This is a method of enum " + ImportantDates.class);
    }
}

enum Gender {
    MALE {
        public String getGender() {
            return "Male";
        }
    },
    FEMALE {
        
        public String getGender() {
            return "Female";
        }
    };
    public abstract String getGender();
}


public class EnumCollection {
    
    enum WeekDays {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY;
    } 
    
    public static void main(String[] args) {
        
        CourseResult cr = CourseResult.PASS;
        System.out.println(cr);
        
        // iteration of enum
        for (WeekDays w: WeekDays.values()) {
            System.out.println(w);
        }
        
        ImportantDates day = ImportantDates.MID_TERM;
        
        switch (day) {
            case ORIENTATION:
                System.out.println("Day for orientation.");
                break;
            case LAST_WITHDRAW:
                System.out.println("Day for last withdraw.");
                break;
            case MID_TERM:
                System.out.println("Day for mid term.");
                break;
            case FINAL_EXAM:
                System.out.println("Day for final exam.");
                break;
            case GRADE_SUBMISSION:
                System.out.println("Day for grade submission.");
                break;
            default:
                System.out.println("Other days.");
        }
        
        System.out.println(day);
        day.getInfo();
        
        ImportantDates[] days = ImportantDates.values();
        for (ImportantDates id : days) {
            System.out.println(id + " at index " + id.ordinal());
        }
        
        System.out.println(WeekDays.valueOf("WEDNESDAY"));
        
        for (Gender g : Gender.values()) {
            System.out.println(g.getGender());
        }
    }
}
