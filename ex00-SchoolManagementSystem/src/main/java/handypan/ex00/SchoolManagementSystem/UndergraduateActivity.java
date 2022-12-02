/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package handypan.ex00.SchoolManagementSystem;

/**
 *
 * @author handyPan
 */
public interface UndergraduateActivity extends LifeActivity, StudyActivity {
    public void doCapstone(String topic);
    public void doCoop(int hours);
}
