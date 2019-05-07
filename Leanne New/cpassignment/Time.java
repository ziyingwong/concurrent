package cpassignment;

public class Time {

    private long arrivalTime;
    private long waitingTime;
    private long consultationTime;
    private long endOfWaitingTime;

    public Time() {
        this.arrivalTime = System.currentTimeMillis();
    }

    // Getter
    public long getWaitingTime() {
        calcuatedWaitingTime();
        return waitingTime;
    }

    public long getConsultationTime() {
        return consultationTime;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    // Setter
    public void setArrivalTime(long arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setConsultationTime(long consultationTime) {
        this.consultationTime = consultationTime;
    }

    public void setEndOfWaitingTime(long endOfWaitingTime) {
        this.endOfWaitingTime = endOfWaitingTime;
    }

    // Calculation
    private long calcuatedWaitingTime() {
        this.waitingTime = endOfWaitingTime - arrivalTime;
        return waitingTime;
    }

}
