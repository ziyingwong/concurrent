package groupProject;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Patient implements Runnable {

	private final HospitalManagement hospital;
	private final String patientID;
	private Time time;
	private int index=0;

	public Patient(String arrivalTime, String patientID, String consultationTime, HospitalManagement hospital) {
		this.patientID = patientID;
		time = new Time();
		time.setArrivalTime(Long.parseLong(arrivalTime));
		time.setConsultationTime(Long.parseLong(consultationTime));
		this.hospital = hospital;
	}

	// Getter
	public String getPatientID() {
		return patientID;
	}
	
	public int getIndex() {
		return this.index;
	}

	public long getArrivalTime() {
		return time.getArrivalTime();
	}

	public long getConsultationTime() {
		return time.getConsultationTime();
	}

	public long getWaitingTime() {
		return time.getWaitingTime();
	}

	// Setter
	public void setEndOfWaitingTime(long endOfWaitingTime) {
		this.time.setEndOfWaitingTime(endOfWaitingTime);
	}
	
	public void incrementIndex() {
		this.index++;
	}

	@Override
	public void run() {
		try {
			this.hospital.assignPatient(this);
		} catch (Exception ex) {
			Logger.getLogger(Patient.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
