package groupProject;

import java.util.ArrayList;

public class Hospital {

	ArrayList<Patient> commonWaitingList;
	int numberOfDoctorWorking;
	int numberOfPatientVisit;
	ArrayList<Clinic> clinicList;
	
	public Hospital(int numberOfDoctorWorking) {
		this.numberOfDoctorWorking = numberOfDoctorWorking;
	}
	
}
