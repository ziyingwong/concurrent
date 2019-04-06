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

// GETTER
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
        sortCurrentAvailableDoctorListByTotalPatientNumber(this.currentAvailableDoctorList);
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

// Sorting
    // TODO : SORTING DOCTOR LIST!
//    public void sortCurrentAvailableDoctorList() {
//        Collections.sort(this.currentAvailableDoctorList,(Doctor o1, Doctor o2)->{
//            o1.getTotalNumberOfPatient().com
//        }});
//    }
// Sorting
    public void sortCurrentAvailableDoctorListByTotalPatientNumber(ArrayList<Doctor> docList) {
        for (int i = 0; i < docList.size(); i++) {
            for (int j = 0; j < docList.size() - 1; j++) {
                if (docList.get(j).getTotalNumberOfPatient() > docList.get(j + 1).getTotalNumberOfPatient()) {
                    Doctor temp = docList.get(j);
                    docList.set(j, docList.get(j + 1));
                    docList.set(j + 1, temp);
                }
            }
        }
    }

    public void sortMinPatientDoctorListByWaitingListNumber(ArrayList<Doctor> docList) {
        for (int i = 0; i < docList.size(); i++) {
            for (int j = 0; j < docList.size() - 1; j++) {
                if (docList.get(j).getWaitingList().size() > docList.get(j + 1).getWaitingList().size()) {
                    Doctor temp = docList.get(j);
                    docList.set(j, docList.get(j + 1));
                    docList.set(j + 1, temp);
                }
            }
        }
    }

// Calculation
        
    //MIN TOTAL NUM OF PATIENT
    public ArrayList<Doctor> findMinTotalPatientNumberDoctorList(int index) {
        ArrayList<Doctor> doctorListHavingMinTotalNumberPatient = new ArrayList<>();
        doctorListHavingMinTotalNumberPatient.add(this.currentAvailableDoctorList.get(index));
        for (int i = index + 1; i < this.currentAvailableDoctorList.size(); i++) {
            if (currentAvailableDoctorList.get(i).getTotalNumberOfPatient() == doctorListHavingMinTotalNumberPatient.get(0).getTotalNumberOfPatient()) {
                doctorListHavingMinTotalNumberPatient.add(this.currentAvailableDoctorList.get(i));
            } else {
                break;
            }
        }
        return doctorListHavingMinTotalNumberPatient;
    }
    
    //DOCTOR RANGE DIFFERENCE
    public boolean isRangeDifferenceInThree(int patienNum) {
        int mean = this.currentAvailableDoctorList.get((this.currentAvailableDoctorList.size() + 1) / 2).getTotalNumberOfPatient();
        int difference = mean - patienNum;
        return Math.abs(difference) < 3 && Math.abs(difference) >= 0;
    }

// Operation
    public void doctorOperation(Doctor doc, Patient p) throws Exception { // Should doctor lock ?
        lock.lock();
        try {
            while (!doc.getWaitingList().isEmpty()) {
                doctorWaitingListPatient.signal();
                patientLeaveCondition.await();
                doc.incrementTotalConsultationTime(p.getConsultationTime() * 1000);
            }
        } finally {
            lock.unlock();
        }
    }

    public void waitingListPatientOperation(Doctor doc, Patient p) throws Exception { // should waiting patient lock ?
        doctorWaitingListPatient.await();
        p.setEndOfWaitingTime(System.currentTimeMillis());
        Thread.sleep(p.getConsultationTime() * 1000);
        patientLeaveCondition.signal();
    }

    public void commonListPatientOperation(Patient p) throws Exception {
        patientLeaveCondition.await();
        assignPatient(p);
    }

    public void assignPatient(Patient p) throws Exception {
        lock.lock();
        try {
            getNewAvailableDoctorListOfTheDay();  // this.currentAvailableDoctorList ady sorted
            if (isAllClinicWaitingListFull() == true) {
                this.commonWaitingList.add(p); // all doctor waiting list is full, patient added to common // waiting list
                commonListPatientOperation(p);
            } else {
                int searchIndex = 0;
                while (true) {
                    ArrayList<Doctor> doctorListHavingMinTotalNumberPatient = findMinTotalPatientNumberDoctorList(searchIndex);
                    if (doctorListHavingMinTotalNumberPatient.size() == 1) {
                        if (doctorListHavingMinTotalNumberPatient.get(0).getWaitingList().size() < 3 && isRangeDifferenceInThree(doctorListHavingMinTotalNumberPatient.get(0).getTotalNumberOfPatient())) {
                            Doctor inchargeDoctor = doctorListHavingMinTotalNumberPatient.get(0);
                            inchargeDoctor.addPatient(p);
                            waitingListPatientOperation(inchargeDoctor, p);
                        } else {
                            searchIndex++;
                        }
                    } else {
                        sortMinPatientDoctorListByWaitingListNumber(doctorListHavingMinTotalNumberPatient); // Sort the doctor list that hv min patient by waiting list num
                        for (int i = 0; i < doctorListHavingMinTotalNumberPatient.size(); i++) {
                            if (isRangeDifferenceInThree(doctorListHavingMinTotalNumberPatient.get(0).getTotalNumberOfPatient())) {
                                if (doctorListHavingMinTotalNumberPatient.get(i).getWaitingList().size() < 3) {
                                    Doctor inchargeDoctor = doctorListHavingMinTotalNumberPatient.get(0);
                                    inchargeDoctor.addPatient(p);
                                    waitingListPatientOperation(inchargeDoctor, p);
                                    break;
                                }
                            }else{
                                break;
                            }
                        }
                        searchIndex += doctorListHavingMinTotalNumberPatient.size();
                    }
                }
            }
        } finally {
            lock.unlock();
        }
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
