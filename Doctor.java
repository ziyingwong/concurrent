package groupProject;

import java.util.ArrayList;


public class Doctor implements Runnable {

	HospitalManagement hospital;
	private final String doctorID;
	private boolean isAvailable = true;
	private boolean isConsulting = false;
	private volatile ArrayList<Patient> patientList = new ArrayList<Patient>();
	private ArrayList<Patient> waitingList = new ArrayList<Patient>();
	private int totalNumberOfPatient;
	private long totalConsultationTime;

	

	public Doctor(String doctorID, HospitalManagement hospital) {
		this.doctorID = doctorID;
		this.hospital = hospital;
		this.isAvailable = true;
		this.totalNumberOfPatient = 0;
		this.totalConsultationTime = 0;
	}

	public void addPatient(Patient patient) {
		this.patientList.add(patient);
		this.totalNumberOfPatient++;
		this.waitingList.add(patient);
	}
	public boolean isConsultingStatus() {
		return this.isConsulting;
	}

	public void setConsultingStatus(boolean isConsulting) {
		this.isConsulting = isConsulting;
	}

	public boolean getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public ArrayList<Patient> getPatientList() {
		return this.patientList;
	}
	

	public void setPatientList(ArrayList<Patient> patientList) {
		this.patientList = patientList;
	}

	public int getTotalNumberOfPatient() {
		return this.totalNumberOfPatient;
	}

	public void setTotalNumberOfPatient(int totalNumberOfPatient) {
		this.totalNumberOfPatient = totalNumberOfPatient;
	}

	public String getDoctorID() {
		return this.doctorID;
	}

	public long getTotalConsultationTime() {
		return this.totalConsultationTime;
	}

	public void incrementTotalConsultationTime(long totalConsultationTime) {
		this.totalConsultationTime += totalConsultationTime;
	}

	public ArrayList<Patient> getWaitingList() {
		return this.waitingList;
	}

	public void setWaitingList(ArrayList<Patient> waitingList) {
		this.waitingList = waitingList;
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
		while (System.currentTimeMillis() - timeDiff != 240 || this.hospital.getCommonWaitingList().size() != 0||this.waitingList.size()!=0) {
			try {
				this.hospital.doctorOperation(this);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		System.out.println(this.doctorID +" bang ganggggggggggggggg");
	}

}
