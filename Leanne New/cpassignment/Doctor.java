package cpassignment;

import java.util.ArrayList;

public class Doctor implements Runnable {

    HospitalManagement hospital;
    private final String doctorID;
    private boolean isAvailable;
    private ArrayList<Patient> patientList;
    private ArrayList<Patient> waitingList;
    private int totalNumberOfPatient;
    private long totalConsultationTime;
//    private long restingTime;

    public Doctor(String doctorID, HospitalManagement hospital) {
        this.doctorID = doctorID;
        this.hospital = hospital;
        this.isAvailable = true;
        this.totalNumberOfPatient = 0;
        this.totalConsultationTime = 0;
    }

    public void addPatient(Patient patient) {
        this.patientList.add(patient);
        this.totalNumberOfPatient++;
        this.waitingList.add(patient);
    }

    public boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public ArrayList<Patient> getPatientList() {
        return this.patientList;
    }

    public void setPatientList(ArrayList<Patient> patientList) {
        this.patientList = patientList;
    }

    public int getTotalNumberOfPatient() {
        return this.totalNumberOfPatient;
    }

    public void setTotalNumberOfPatient(int totalNumberOfPatient) {
        this.totalNumberOfPatient = totalNumberOfPatient;
    }

    public String getDoctorID() {
        return this.doctorID;
    }

    public long getTotalConsultationTime() {
        return this.totalConsultationTime;
    }

    public void incrementTotalConsultationTime(long totalConsultationTime) {
        this.totalConsultationTime += totalConsultationTime;
    }

    public ArrayList<Patient> getWaitingList() {
        return this.waitingList;
    }

    public void setWaitingList(ArrayList<Patient> waitingList) {
        this.waitingList = waitingList;
    }

    // For Report Used
    public long getMyPatientWaitingTime(){
        long totalWaitingTime = 0;
        for (Patient p : this.patientList) {
            totalWaitingTime += p.getWaitingTime();
        }
        return totalWaitingTime;
    }

    @Override
    public void run() { // 
        long currentTime = System.currentTimeMillis();
        long timeDiff = currentTime - 0; // I dunno how to loop time
        while (System.currentTimeMillis() - timeDiff != 240) {
            if (getTotalNumberOfPatient() % 8 == 0 && getWaitingList().isEmpty()) {
                setIsAvailable(false);
                try {
                    Thread.sleep(15);
                } catch (Exception e) {
                }
            } else if (getTotalNumberOfPatient() % 8 == 0) {
                setIsAvailable(true);
                continue;
            }
            setIsAvailable(true);
        }
    }

    //got one method
    public void doctorOperation(Patient p) throws Exception { // Dunno where to put this doctor Operation 
        this.hospital.doctorOperation(this, p);
    }

}
