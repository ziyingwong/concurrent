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
public class Report {

    private HospitalManagement hospital;

    public Report(HospitalManagement hospital) {
        this.hospital = hospital;
    }

    public void generateReport() {
        //Add header
        printNumberOfPatientVisitHospital();
        printDoctorSummaryReport();
        printOverallAverageConsultationTime();
        printOverallAverageWaitingTimeOfPatient();
        //Add footer
    }

    public void printNumberOfPatientVisitHospital() {
        // sum of total patient number for all doctor
    }

    public void printDoctorSummaryReport() {
        //Number of patient seen by each doctor
        //Average consultationTime by each doctor = totalConsultationTime of the doctor / totalPatientOf the doctor
        //Average waitingTime of patient by each doctor = totalPatientWaitingTime of the doctor / / totalPatientOf the doctor
    }

    public void printOverallAverageConsultationTime() {
//        sum of all patient Consultation Time/sum of total patient number for all doctor
    }

    public void printOverallAverageWaitingTimeOfPatient() {
//        sum of all patient Waiting Time /sum of total patient number for all doctor
    }

}
