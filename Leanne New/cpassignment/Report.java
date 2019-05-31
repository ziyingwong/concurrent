/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpassignment;

/**
 *
 * @author Leanne
 */


import java.util.ArrayList;

public class Report {

    private final HospitalManagement hospital;
    public Report(HospitalManagement hospital) {
        this.hospital = hospital;
    }

    public void generateReport() {
        System.out.println("-----------------Report-----------------");
        printNumberOfPatientVisitHospital();
        printOverallAverageConsultationTime();
        printOverallAverageWaitingTimeOfPatient();
        printDoctorSummaryReport();
        System.out.println("---------------End Report------------");
    }

    public void printNumberOfPatientVisitHospital() {
        System.out.println("Number of patients visit: " + hospital.getNumberOfPatientVisit());
    }
    
    public void printDoctorSummaryReport() {
        ArrayList<Doctor> doctorList = hospital.getWorkingDoctorList();
        for (int i = 0; i < doctorList.size(); i++) {
            System.out.println("Doctor " + doctorList.get(i).getDoctorID());
            int totalNumberOfPatientForTheDoctor = doctorList.get(i).getTotalNumberOfPatient();
            long consultation = doctorList.get(i).getTotalConsultationTime();
            long wait = doctorList.get(i).getMyPatientWaitingTime();
            System.out.println("Number of Patients: " + totalNumberOfPatientForTheDoctor);
            System.out.printf("Average Consultation Time: %.2f minutes\n" ,(float)(consultation / totalNumberOfPatientForTheDoctor));
            System.out.printf("Average Waiting Time: %.2f minutes\n",(float)((wait/1000) / totalNumberOfPatientForTheDoctor) );
            System.out.println("");
        }
    }
    
    public void printOverallAverageConsultationTime() {
        ArrayList<Doctor> doctorList = hospital.getWorkingDoctorList();
        long totalConsultationTimeForAllPatient = 0;
        for (int i = 0; i < doctorList.size(); i++) {
            totalConsultationTimeForAllPatient += doctorList.get(i).getTotalConsultationTime();
        }
        float overallAverageConsultationTime = totalConsultationTimeForAllPatient / hospital.getNumberOfPatientVisit();
        System.out.printf("Overall Average Consultation Time of Patient Visited: %.2f minutes\n" ,overallAverageConsultationTime);
    }

    public void printOverallAverageWaitingTimeOfPatient() {
        ArrayList<Doctor> doctorList = hospital.getWorkingDoctorList();
        long totalWaitingTimeForAllPatient = 0;
        for (int i = 0; i < doctorList.size(); i++) {
            totalWaitingTimeForAllPatient += doctorList.get(i).getMyPatientWaitingTime();
        }
        float overallAverageWaitingTime = totalWaitingTimeForAllPatient / hospital.getNumberOfPatientVisit();
        System.out.printf("Overall Average Waiting Time of Patient Visited: %.2f minutes\n", overallAverageWaitingTime/1000);
    }
}

