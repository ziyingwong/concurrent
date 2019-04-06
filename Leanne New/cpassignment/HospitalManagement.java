package cpassignment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


// TOFIX sorting using comparator
//class CustomComparator implements Comparator<Doctor> {
//    @Override
//    public int compare(Doctor o1, Doctor o2) {
//          return o1.getTotalNumberOfPatient().compareTo(o2.getTotalNumberOfPatient());
//           
//    }
//}

public class HospitalManagement { // ADD CONDITION FOR WAITWAITINGLIST & WAITCOMMONWATINGLIST

    private static Lock lock = new ReentrantLock();
    private final Condition patientLeaveCondition = lock.newCondition();
    private final Condition doctorWaitingListPatient = lock.newCondition();
    private ArrayList<Patient> commonWaitingList;
    private ArrayList<Doctor> workingDoctorList;
    private ArrayList<Doctor> currentAvailableDoctorList;
    private int numberOfDoctorWorking;
    private int numberOfPatientVisit;

    public HospitalManagement(int numberOfDoctorWorking) {
        this.numberOfDoctorWorking = numberOfDoctorWorking;
    }

// Status
    public boolean isAllClinicWaitingListFull() {
        boolean isFull = true; // check if all doctor waiting list is full
        for (int i = 0; i < this.currentAvailableDoctorList.size(); i++) {
            if (currentAvailableDoctorList.get(i).getWaitingList().size() < 3) {
                isFull = false;
                break;
            }
        }
        return isFull;
    }

    // TODO : SORTING DOCTOR LIST!
//    public void sortCurrentAvailableDoctorList() {
//        Collections.sort(this.currentAvailableDoctorList,(Doctor o1, Doctor o2)->{
//            o1.getTotalNumberOfPatient().com
//        }});
//    }
    
     // TODO: DOCTOR RANGE DIFFERENCE
    
    
    // TODO: MIN TOTAL NUM OF PATIENT

// 2GETTER
    public ArrayList<Patient> getCommonWaitingList() {
        return commonWaitingList;
    }

    public ArrayList<Doctor> getCurrentAvailableDoctorList() {
        return this.currentAvailableDoctorList;
    }

    public ArrayList<Doctor> getWorkingDoctorList() {
        return workingDoctorList;
    }

    public int getNumberOfDoctorWorking() {
        return numberOfDoctorWorking;
    }

    public int getNumberOfPatientVisit() {
        return this.numberOfPatientVisit;
    }

    public void getNewAvailableDoctorListOfTheDay() {
        ArrayList<Doctor> availableDoctorList = new ArrayList<>(); // create a list of available doctor
        for (int i = 0; i < this.workingDoctorList.size() - 1; i++) {
            if (this.workingDoctorList.get(i).getIsAvailable() == true) {
                availableDoctorList.add(this.workingDoctorList.get(i));
            }
        }
        this.currentAvailableDoctorList = availableDoctorList;
    }

//SETTER
    public void setCommonWaitingList(ArrayList<Patient> commonWaitingList) {
        this.commonWaitingList = commonWaitingList;
    }

    public void setWorkingDoctorList(ArrayList<Doctor> workingDoctorList) {
        this.workingDoctorList = workingDoctorList;
    }

    public void setNumberOfDoctorWorking(int numberOfDoctorWorking) {
        this.numberOfDoctorWorking = numberOfDoctorWorking;
    }

    public void setNumberOfPatientVisit(int numberOfPatientVisit) {
        this.numberOfPatientVisit = numberOfPatientVisit;
    }

 // Operation
    public void doctorOperation(Doctor doc, Patient p) throws Exception { // Should doctor lock ?
        lock.lock();
        try {
            while (!doc.getWaitingList().isEmpty()) {
                doctorWaitingListPatient.signal();
                patientLeaveCondition.await();
                doc.incrementTotalConsultationTime(p.getConsultationTime()*1000);
            }
        } finally {
            lock.unlock();
        }
    }

    public void waitingListPatientOperation(Doctor doc, Patient p) throws Exception { // should waiting patient lock ?
        doctorWaitingListPatient.await();
        p.setendOfWaitingTime(System.currentTimeMillis());
        Thread.sleep(p.getConsultationTime()*1000);
        patientLeaveCondition.signal(); 
    }
    
    public void commonListPatientOperation(Patient p) throws Exception {
        patientLeaveCondition.await();
        assignPatient(p);
    }
    
    
    public void assignPatient(Patient p) throws Exception {
        lock.lock();
        try {
            getNewAvailableDoctorListOfTheDay();
            if (isAllClinicWaitingListFull() == true) {
                this.commonWaitingList.add(p); // all doctor waiting list is full, patient added to common // waiting list
            } else {
                ArrayList<Integer> min = new ArrayList<>(); // find doctor with least total patient
                min.add(0);
                for (int i = 1; i < this.currentAvailableDoctorList.size(); i++) {
                    if (this.currentAvailableDoctorList.get(i).getTotalNumberOfPatient() < this.currentAvailableDoctorList
                            .get(min.get(0)).getTotalNumberOfPatient()) {
                        min.clear();
                        min.add(i);
                    } else if (currentAvailableDoctorList.get(i).getTotalNumberOfPatient() == currentAvailableDoctorList
                            .get(min.get(0)).getTotalNumberOfPatient()) {
                        min.add(i);
                    }
                }

                if (min.size() == 1) {
                    if (this.currentAvailableDoctorList.get(min.get(0)).getWaitingList().size() < 3) {
                        for (int i = 0; i < this.workingDoctorList.size(); i++) {
                            if (this.workingDoctorList.get(i).getDoctorID().equals(this.currentAvailableDoctorList.get(min.get(0)).getDoctorID())) {
                                Doctor inchargeDoctor = this.currentAvailableDoctorList.get(min.get(0));
                                inchargeDoctor.addPatient(p);
                                this.workingDoctorList.set(i, new Clinic(inchargeDoctor));
                            }
                        }
                    }
                }

            }
        } finally {
            lock.unlock();
        }
    }

    void assignPatient() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}




/* Discussed Operation on 5/4 */
//-assignDoc [end of the method - record patient StartWaitingTime]
//-doctor operation
//
//Doctor Operation method:
//-while (waitingList.size() != 0  ){ // keep consulting patient
////[see patient [flow]]
//-p.signalWaitingList() //signal patient waiting list
//-awaitLeave()
//
//Waiting List Patient
//- patient awaitWaitingList() // wait for doctor waitingListSIgnal()
//[record patient EndWaitingTime]
//[==After signal==]
//-patient sleep(consultationTime) 
//-signalLeave()
//[Increment doctor total consultationTime]
//
//Comon List
//- patient await() //wait for patient leaveSignal()
//- assignDoctor
//__________________________
//
//Another Class - ClockClass
//------------------
//Another  Class - readerClass has run method
//- while loop until end of file