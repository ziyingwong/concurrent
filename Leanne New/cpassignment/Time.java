package cpassignment;

public class Time {

    private final long arrivalTime;
    private long waitingTime;
    private final long consultationTime;
    private long endOfWaitingTime;

    public Time(long arrivalTime,long consultationTime) {
        //Not system time
        this.arrivalTime = arrivalTime;
        this.consultationTime = consultationTime;
    }

    // Getter
    public long getWaitingTime() {
        calculatedWaitingTime();
        return waitingTime;
    }

    public long getConsultationTime() {
        return consultationTime;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    // Setter
//    public void setArrivalTime(long arrivalTime) {
//        this.arrivalTime = arrivalTime;
//    }
//
//    public void setConsultationTime(long consultationTime) {
//        this.consultationTime = consultationTime;
//    }

    public void setEndOfWaitingTime(long endOfWaitingTime) {
        this.endOfWaitingTime = endOfWaitingTime;
    }

    // Calculation
    private long calculatedWaitingTime() {
        this.waitingTime = endOfWaitingTime - arrivalTime * 1000;
        return waitingTime;
    }

}
