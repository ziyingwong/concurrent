package cpassignment;

import java.util.ArrayList;

public class Doctor implements Runnable {

    HospitalManagement hospital;
    private final String doctorID;
    private boolean isAvailable = true;
    private boolean isConsulting;
    private volatile ArrayList<Patient> patientList = new ArrayList<>();
    private ArrayList<Patient> waitingList = new ArrayList<>();
    private int totalNumberOfPatient;
    private long totalConsultationTime;

    public Doctor(String doctorID, HospitalManagement hospital) {
        this.doctorID = doctorID;
        this.hospital = hospital;
        this.isAvailable = true;
        this.totalConsultationTime = 0;
        this.isConsulting = false;
    }

    public void addPatient(Patient patient) {
        this.totalNumberOfPatient++;
        this.patientList.add(patient);
        this.waitingList.add(patient);
    }

    public void incrementTotalConsultationTime(long totalConsultationTime) {
        this.totalConsultationTime += totalConsultationTime;
    }

    //getter
    public String getDoctorID() {
        return this.doctorID;
    }

    public boolean getIsAvailable() {
        return isAvailable;
    }

    public ArrayList<Patient> getPatientList() {
        return this.patientList;
    }

    public ArrayList<Patient> getWaitingList() {
        return this.waitingList;
    }

    public int getTotalNumberOfPatient() {
        return this.totalNumberOfPatient;
    }

    public long getTotalConsultationTime() {
        return this.totalConsultationTime;
    }

    public boolean isConsulting() {
        return this.isConsulting;
    }

    // setter
    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public void setPatientList(ArrayList<Patient> patientList) {
        this.patientList = patientList;
    }

    public void setWaitingList(ArrayList<Patient> waitingList) {
        this.waitingList = waitingList;
    }

    public void setIsConsulting(boolean isConsulting) {
        this.isConsulting = isConsulting;
    }

    // For Report Used
    public long getMyPatientWaitingTime() {
        long totalWaitingTime = 0;
        for (Patient p : this.patientList) {
            totalWaitingTime += p.getWaitingTime();
        }
        return totalWaitingTime;
    }

    @Override
    public void run() {
        while (System.currentTimeMillis() - hospital.getStartTime() < 240*1000 || !waitingList.isEmpty() || !this.hospital.getCommonWaitingList().isEmpty()) {
            try {
                this.hospital.doctorOperation(this);
            } catch (Exception e1) {
                System.out.println("Exception : " + e1);
            }
        }
        System.out.println(this.doctorID + " Balik Kampung.");
    }

}
