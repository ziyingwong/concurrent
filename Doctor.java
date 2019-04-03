package groupProject;

import java.util.ArrayList;

public class Doctor {
	String doctorID;
	boolean isAvailable;
	ArrayList<Patient> patientList;
	int totalNumberOfPatient;
	long totalConsultationTime;
	ArrayList<Patient> waitingList;

	
	public Doctor(String doctorID) {
		this.doctorID = doctorID;
		this.isAvailable = true;
		this.totalNumberOfPatient=0;
		this.totalConsultationTime=0;
	}
	
	public void addPatient(Patient patient) {
		this.patientList.add(patient);
		this.totalNumberOfPatient++;
		this.waitingList.add(patient);
	}
	
}
