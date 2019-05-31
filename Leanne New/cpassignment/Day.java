package cpassignment;

import java.util.ArrayList;

public class Day {
 
    public static void dayOperation(String filename) throws InterruptedException{
        Reader read = new Reader(filename);
        Thread t = new Thread(read);
        t.start();
        t.join();
        
        // create Hospital, Doctor, Patient
        int numOfDoc = read.getNumOfDoc();
        ArrayList<String[]> readList = read.getPatientList();
        HospitalManagement hospital = new HospitalManagement(numOfDoc);
        ArrayList<Patient> patientList = new ArrayList<>();
        for (int i = 0; i < readList.size(); i++) {
            String arrival_time = readList.get(i)[0];
            String patient_ID = readList.get(i)[1];
            String duration = readList.get(i)[2];
            Patient p = new Patient(arrival_time, patient_ID, duration, hospital);
            patientList.add(p);
        }

        hospital.setNumberOfPatientVisit(patientList.size());

        // Hospital Open
        long startTime = System.currentTimeMillis();
        hospital.setStartTime(startTime);
        
        // create doctor thread
        ArrayList<Thread> doctorThreadList = new ArrayList<>();
        for (int i = 0; i < numOfDoc; i++) {
            Doctor doctor = new Doctor("Doctor " + (i + 1), hospital);
            hospital.updateWorkingDoctorList(doctor);
            Thread tdoctor = new Thread(doctor);
            doctorThreadList.add(tdoctor);
            tdoctor.start();
        }
        
        //create patient thread
        for (int i = 0; i < patientList.size(); i++) {
            long arrivalTime = patientList.get(i).getArrivalTime();
            while ((System.currentTimeMillis() - startTime) < arrivalTime *1000) {
                // Do nothing wait for patient walk in
            }
            System.out.println("\n##### Arrival TIME : " + (System.currentTimeMillis() - startTime) / 1000 + " #####");
            Runnable patient = patientList.get(i);
            Thread walkIn = new Thread(patient);
            walkIn.start();
            System.out.println("====== " + patientList.get(i).getPatientID() + " walk in ======\n");
        }
        
        
        // Wait for all doctor end task 
        for (int i = 0; i < numOfDoc; i++) {
            doctorThreadList.get(i).join();
        }
        
        // Generate Report
        Report report = new Report(hospital);
        report.generateReport();
    }
    
    public static void main(String[] args) throws InterruptedException {
        //create Reader thread read input file
//        - TextFile1: Normal.txt
//        - TextFile2: BestCase.txt
//        - TextFile3: WorstCase.txt
        dayOperation("Normal.txt");
//        dayOperation("BestCase.txt");
//        dayOperation("WorstCase.txt");
    }
}