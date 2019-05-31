package cpassignment;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Doctor implements Runnable {

//    private AtomicInteger totalNumberOfPatient;
    HospitalManagement hospital;
    private final String doctorID;
    private boolean isAvailable = true;
    private boolean isConsulting;
    private volatile ArrayList<Patient> patientList = new ArrayList<>();
//    private ArrayList<Patient> patientList = new ArrayList<>();
    private ArrayList<Patient> waitingList = new ArrayList<>();
    private int totalNumberOfPatient;
    private long totalConsultationTime;

    public Doctor(String doctorID, HospitalManagement hospital) {
        this.doctorID = doctorID;
        this.hospital = hospital;
        this.isAvailable = true;
//        this.totalNumberOfPatient = new AtomicInteger(0);

        this.totalConsultationTime = 0;
        this.isConsulting = false;
    }

    // I changed something here
//    public int addPatient(Patient patient) {
    public void addPatient(Patient patient) {
//        boolean update = false;
//        int expectedTotalNumber = this.totalNumberOfPatient.get();
//        while (!update) {
//            expectedTotalNumber = this.totalNumberOfPatient.get();
//            update = this.totalNumberOfPatient.compareAndSet(expectedTotalNumber, expectedTotalNumber + 1);
//        }
        this.totalNumberOfPatient++;
        this.patientList.add(patient);
        this.waitingList.add(patient);
//        return expectedTotalNumber;
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
//        return this.totalNumberOfPatient.get();
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
//        long timeDiff = System.currentTimeMillis() - hospital.getStartTime();
        
        while (System.currentTimeMillis() - hospital.getStartTime() < 240*1000 || !waitingList.isEmpty() || !this.hospital.getCommonWaitingList().isEmpty()) {
            try {
                this.hospital.doctorOperation(this);
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            
            
            //own testing
            /*           try {


                               if (this.isAvailable == true) {
//                    System.out.println("!!!!!!!!!!!! availability: " + this.isAvailable + "  !!!!!! patient num:" + this.totalNumberOfPatient);
                    this.hospital.doctorOperation(this);
                }
                // old update
//                int doctorTotalNumberOfPatient = getTotalNumberOfPatient();
//                if (doctorTotalNumberOfPatient >= 8 && doctorTotalNumberOfPatient % 8 == 0) {
                //new update 
                if (this.isAvailable == false) {
                    System.out.println("[Patient Count == " + getTotalNumberOfPatient() + "] Stop Assigning patient to " + this.doctorID + "[Is not available]");

                    // This need to be commented because I will set the available status at assign patient when the patient is ady can be divided by 8
                    // ori (but I wish to commented it)
//                setIsAvailable(false);
                    while (!getWaitingList().isEmpty()) {
                        this.hospital.doctorOperation(this);
                        System.out.println("[ " + this.doctorID + " currently waiting to finished own waiting list and going to Rest]");
                    }
                    System.out.println(" " + this.doctorID + " Start Rest]: Doctor: " + this.doctorID);
                    Thread.sleep(1500);
                    System.out.println("[ " + this.doctorID + " End Rest]: Doctor: " + this.doctorID + " is available");
                    //I added
                    setIsAvailable(true);

                }
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }*/
            //ori
//            if (getTotalNumberOfPatient() % 8 == 0 && getTotalNumberOfPatient() != 0) {
            // I added

//            int doctorTotalNumberOfPatient = getTotalNumberOfPatient();
//            if ( doctorTotalNumberOfPatient >= 8&& doctorTotalNumberOfPatient % 8 == 0) {
//                System.out.println("[Patient Count == " + getTotalNumberOfPatient() + "] Stop Assigning patient to " + this.doctorID +"[Is not available]");
//
//                // This need to be commented because I will set the available status at assign patient when the patient is ady can be divided by 8
//                // ori (but I wish to commented it)
////                setIsAvailable(false);
//
//                while (!getWaitingList().isEmpty()) {
//                    System.out.println("[ " + this.doctorID + " currently waiting to finished own waiting list and going to Rest]");
//                }
//                try {
//                    System.out.println(" " + this.doctorID + " Start Rest]: Doctor: " + this.doctorID);
//                    Thread.sleep(15);
//                    System.out.println("[ " + this.doctorID + " End Rest]: Doctor: " + this.doctorID + " is available");
//                    //I added
//                    setIsAvailable(true);
//                } catch (Exception e) {
//                }
//            }
// This need to be commented because I will set the available status at assign patient when the patient is ady can be divided by 8
// ori
//            setIsAvailable(true);
        }
        System.out.println(this.doctorID +" Yeah 放工！");
    }

}
