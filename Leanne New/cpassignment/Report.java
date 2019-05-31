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
        printDoctorSummaryReport();
        printOverallAverageConsultationTime();
        printOverallAverageWaitingTimeOfPatient();
        System.out.println("----------------------------------------");
    }

    // sum of total patient number for all doctor
    public void printNumberOfPatientVisitHospital() {
        System.out.println("[Hospital] Total number of patients: " + hospital.getNumberOfPatientVisit());
    }

    // Number of patient seen by each doctor
    // Average consultationTime by each doctor = totalConsultationTime of the doctor
    // Average waitingTime of patient by each doctor = totalPatientWaitingTime of the doctor / totalPatientOf the doctor
    public void printDoctorSummaryReport() {
        ArrayList<Doctor> doctorList = hospital.getWorkingDoctorList();
        for (int i = 0; i < doctorList.size(); i++) {
            System.out.println("Doctor " + doctorList.get(i).getDoctorID());
            int totalNumberOfPatientForTheDoctor = doctorList.get(i).getTotalNumberOfPatient();
            long consultation = doctorList.get(i).getTotalConsultationTime();
            long wait = doctorList.get(i).getMyPatientWaitingTime();
            System.out.println("Number of Patients: " + totalNumberOfPatientForTheDoctor);
//            System.out.println("Total Consultation Time: " + (consultation));
//            System.out.println("Total Waiting Time: " + (wait));
            System.out.println("Average Consultation Time: " + (consultation / totalNumberOfPatientForTheDoctor));
            System.out.println("Average Waiting Time: " + (wait / totalNumberOfPatientForTheDoctor));
            System.out.println("");
        }

    }

    // sum of all patient Consultation Time/sum of total patient number for all doctor
    // Take every doctor de consultation time / total number of patient
    public void printOverallAverageConsultationTime() {
        ArrayList<Doctor> doctorList = hospital.getWorkingDoctorList();
        long totalConsultationTimeForAllPatient = 0;
        for (int i = 0; i < doctorList.size(); i++) {
            totalConsultationTimeForAllPatient += doctorList.get(i).getTotalConsultationTime();
        }
        long overallAverageConsultationTime = totalConsultationTimeForAllPatient / hospital.getNumberOfPatientVisit();
        System.out.println("Overall Average Consultation Time: " + overallAverageConsultationTime);
    }

    // sum of all patient Waiting Time /sum of total patient number for all doctor
    public void printOverallAverageWaitingTimeOfPatient() {
        ArrayList<Doctor> doctorList = hospital.getWorkingDoctorList();
        ArrayList<Patient> patientList;
        long totalWaitingTimeForAllPatient = 0;
        for (int i = 0; i < doctorList.size(); i++) {
            patientList = doctorList.get(i).getPatientList();
            for (int j = 0; j < patientList.size(); j++) {
                totalWaitingTimeForAllPatient += patientList.get(j).getWaitingTime();
            }
        }
        long overallAverageWaitingTime = totalWaitingTimeForAllPatient / hospital.getNumberOfPatientVisit();
        System.out.println("Overall Average Waiting Time: " + overallAverageWaitingTime);
    }
}
