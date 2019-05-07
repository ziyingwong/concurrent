package cpassignment;

import java.util.ArrayList;

public class Doctor implements Runnable {

    HospitalManagement hospital;
    private final String doctorID;
    private boolean isAvailable = true;
    private ArrayList<Patient> patientList = new ArrayList<>();
    private ArrayList<Patient> waitingList = new ArrayList<>();
    private int totalNumberOfPatient;
    private long totalConsultationTime;
    private boolean consultingStatus;

    public Doctor(String doctorID, HospitalManagement hospital) {
        this.doctorID = doctorID;
        this.hospital = hospital;
        this.isAvailable = true;
        this.totalNumberOfPatient = 0;
        this.totalConsultationTime = 0;
        this.consultingStatus = false;
    }

    // I changed something here
    public int addPatient(Patient patient) {
        this.patientList.add(patient);
        this.totalNumberOfPatient++;
        this.waitingList.add(patient);
        return this.totalNumberOfPatient;
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

    public boolean isConsultingStatus() {
        return consultingStatus;
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

    public void setConsultingStatus(boolean consultingStatus) {
        this.consultingStatus = consultingStatus;
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
        long timeDiff = System.currentTimeMillis() - hospital.getStartTime();
        while (System.currentTimeMillis() - timeDiff != 240 || !this.hospital.getCommonWaitingList().isEmpty()) {
            try {
                this.hospital.doctorOperation(this);
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            //ori
            if (getTotalNumberOfPatient() % 8 == 0 && getTotalNumberOfPatient() != 0) {
                // I added

//            int doctorTotalNumberOfPatient = getTotalNumberOfPatient();
//            if ( doctorTotalNumberOfPatient >= 8&& doctorTotalNumberOfPatient % 8 == 0) {
//                System.out.println("[Patient Count == " + getTotalNumberOfPatient() + "] Stop Assigning patient to " + this.doctorID +"[Is not available]");

                // This need to be commented because I will set the available status at assign patient when the patient is ady can be divided by 8
                // ori (but I wish to commented it)
                setIsAvailable(false);

                while (!getWaitingList().isEmpty()) {
                    System.out.println("[ " + this.doctorID + " currently waiting to finished own waiting list and going to Rest]");
                }
                try {
                    System.out.println(" " + this.doctorID + " Start Rest]: Doctor: " + this.doctorID);
                    Thread.sleep(15);
                    System.out.println("[ " + this.doctorID + " End Rest]: Doctor: " + this.doctorID + " is available");
                    //I added
//                    setIsAvailable(true);
                } catch (Exception e) {
                }
            }
// This need to be commented because I will set the available status at assign patient when the patient is ady can be divided by 8
// ori
            setIsAvailable(true);
        }
    }

}
