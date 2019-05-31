package cpassignment;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Patient implements Runnable {

    private final HospitalManagement hospital;
    private final String patientID;
    private final Time time;
    private boolean isFirstTime = true;

    public Patient(String arrivalTime, String patientID, String consultationTime, HospitalManagement hospital) {
        this.patientID = patientID;
        time = new Time(Long.parseLong(arrivalTime),Long.parseLong(consultationTime));
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

    public boolean isIsFirstTime() {
        return isFirstTime;
    }

    // Setter
    public void setEndOfWaitingTime(long endOfWaitingTime) {
        this.time.setEndOfWaitingTime(endOfWaitingTime);
    }
    
    public void setIsFirstTime(boolean isFirstTime) {
        this.isFirstTime = isFirstTime;
    }

    @Override
    public void run() {
        try {
            this.hospital.assignPatient(this);
        } catch (Exception ex) {
            Logger.getLogger(Patient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
