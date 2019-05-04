package groupProject2;

import java.util.ArrayList;

public class Report {
	private HospitalManagement hospital;

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
		System.out.println("Number of Patients: " + hospital.getNumberOfPatientVisit());
	}

	// Number of patient seen by each doctor
	// Average consultationTime by each doctor = totalConsultationTime of the doctor
	// Average waitingTime of patient by each doctor = totalPatientWaitingTime of the doctor / totalPatientOf the doctor
	public void printDoctorSummaryReport() {
		ArrayList<Doctor> doctorList = new ArrayList<Doctor>();
		doctorList = hospital.getWorkingDoctorList();
		for (int i = 1; i <= doctorList.size(); i++) {
			System.out.println("Doctor " + i + " : ");
			int total = doctorList.get(i).getTotalNumberOfPatient();
			long consultation = doctorList.get(i).getTotalConsultationTime();
			long wait = doctorList.get(i).getMyPatientWaitingTime();
			System.out.println("Number of Patients: " + total);
			System.out.println("Average Consultation Time: " + (consultation / total));
			System.out.println("Average Waiting Time: " + (wait / total));
			System.out.println("");
		}

	}

	// sum of all patient Consultation Time/sum of total patient number for all doctor
	public void printOverallAverageConsultationTime() {
		ArrayList<Patient> patientList = new ArrayList<Patient>();
		long totalConsultationTime = 0;
		for (int i = 0; i < patientList.size(); i++) {
			totalConsultationTime += patientList.get(i).getConsultationTime();
		}
		long overallAverageConsultationTime = totalConsultationTime / patientList.size();
		System.out.println("Overall Average Consultation Time: " + overallAverageConsultationTime);
	}

	// sum of all patient Waiting Time /sum of total patient number for all doctor
	public void printOverallAverageWaitingTimeOfPatient() {
		ArrayList<Patient> patientList = new ArrayList<Patient>();
		long totalWaitingTime = 0;
		for (int i = 0; i < patientList.size(); i++) {
			totalWaitingTime += patientList.get(i).getWaitingTime();
		}
		long overallAverageWaitingTime = totalWaitingTime / patientList.size();
		System.out.println("Overall Average Waiting Time: " + overallAverageWaitingTime);
	}

}
