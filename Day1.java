package groupProject;

import java.util.ArrayList;

public class Day1 {

	public static void main(String[] args) {
		Reader read = new Reader();
		Thread t = new Thread(read);
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// create hospital,doctor,patient
		int numOfDoc = read.getNumOfDoc();
		ArrayList<String[]> readList = read.getPatientList();
		HospitalManagement hospital = new HospitalManagement(numOfDoc);
		ArrayList<Patient> patientList = new ArrayList<Patient>();
		for (int i = 0; i < readList.size(); i++) {
			String[] container = readList.get(i);
			String arrival_time = container[0];
			String patient_ID = container[1];
			String duration = container[2];
			Patient p = new Patient(arrival_time, patient_ID, duration, hospital);
			patientList.add(p);
		}

		hospital.setNumberOfPatientVisit(patientList.size());
		hospital.setNumberOfDoctorWorking(numOfDoc);

		// Hospital Open
		long startTime = System.currentTimeMillis();
		hospital.setStartTime(startTime);
		for (int i = 0; i < numOfDoc; i++) {
			Doctor doctor = new Doctor("Doctor " + (i+1), hospital);
			hospital.updateWorkingDoctorList(doctor);
			Thread tdoctor = new Thread(doctor);
			tdoctor.start();
		}
		for (int i = 0; i < patientList.size(); i++) {
			long arrivalTime = patientList.get(i).getArrivalTime();
			while (System.currentTimeMillis() - startTime < arrivalTime * 1000) {
				// do nothing
			}
			System.out.println("Arrival TIME : " + (System.currentTimeMillis() - startTime)/1000);
			Runnable patient = patientList.get(i);
			Thread walkIn = new Thread(patient);
			walkIn.start();
			System.out.println(patientList.get(i).getPatientID() + " walk in");
		}
	}
}
