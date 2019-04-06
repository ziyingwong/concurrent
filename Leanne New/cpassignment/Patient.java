package cpassignment;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Patient implements Runnable {

    private final HospitalManagement hospital;
    private final String patientID;
    private Time time;
//    private PatientStatus patientStatus = PatientStatus.QUEUE;
//
//    public enum PatientStatus {
//        QUEUE,WAITING_AT_COMMONLIST, WAITING_AT_WAITINGLIST, CONSULTATION, EXIT;
//    }

    public Patient(String patientID, HospitalManagement hospital) {
        this.patientID = patientID;
        time = new Time();
        this.hospital = hospital;
    }

    // Getter
    public String getPatientID() {
        return patientID;
    }

    public long getArrivalTime() {
        return time.getArrivalTime();
    }

    public long getConsultationTime() {
        return time.getConsultationTime();
    }
    
    public long getWaitingTime() {
        return time.getWaitingTime();
    }

//    public PatientStatus getPatientStatus(){
//        return this.patientStatus;
//    }
    
    // Setter
    public void setEndOfWaitingTime(long endOfWaitingTime) {
        this.time.setEndOfWaitingTime(endOfWaitingTime);
    }

//    public void setPatientStatus(PatientStatus patientStatus){
//        this.patientStatus = patientStatus;
//    }
    
    @Override
    public void run() {
        try {
            //        while (getPatientStatus()!= PatientStatus.EXIT) {
//            hospital.assignPatient(this); // AssignPatient de ending is wait()[commonlist / waiting list] if wait
//
//        }
            this.hospital.assignPatient(this);
        } catch (Exception ex) {
            Logger.getLogger(Patient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
