package groupProject;

public class Time {
	long arrivalTime;
	long waitingTime;
	long consultationTime;
	
	public Time() {
		this.arrivalTime = System.currentTimeMillis();
	}
	
	public long calcuatedWaitingTime() {
		this.waitingTime = System.currentTimeMillis()-arrivalTime;
		return waitingTime;
	}
	
	public long calculateConsultationTime() {
		this.consultationTime = System.currentTimeMillis()-waitingTime-arrivalTime;
		return consultationTime;
	}
}
