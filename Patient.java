package groupProject;

public class Patient {
	String patientID;
	Time time;
	
	public Patient(String patientID) {
		this.patientID = patientID;
		time = new Time();
	}
}
