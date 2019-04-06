package cpassignment;

public class Patient implements Runnable {

    private final HospitalManagement hospital;
    private final String patientID;
    private Time time;
//    private PatientStatus patientStatus = PatientStatus.QUEUE;
//
//    public enum PatientStatus {
//        QUEUE,WAITING_AT_COMMONLIST, WAITING_AT_WAITINGLIST, CONSULTATION, EXIT;
//    }

    public Patient(String patientID, HospitalManagement hospital) {
        this.patientID = patientID;
        time = new Time();
        this.hospital = hospital;
    }

    // Getter
    public String getPatientID() {
        return patientID;
    }

    public long getArrivalTime() {
        return time.getArrivalTime();
    }

    public long getConsultationTime() {
        return time.getConsultationTime();
    }

//    public PatientStatus getPatientStatus(){
//        return this.patientStatus;
//    }
    
    
    // Setter
    public void setendOfWaitingTime(long endOfWaitingTime) {
        this.time.setEndOfWaitingTime(endOfWaitingTime);
    }

//    public void setPatientStatus(PatientStatus patientStatus){
//        this.patientStatus = patientStatus;
//    }
    
    
    public void run() {
//        while (getPatientStatus()!= PatientStatus.EXIT) {
//            hospital.assignPatient(this); // AssignPatient de ending is wait()[commonlist / waiting list] if wait 
//            
//        }

    }

}
