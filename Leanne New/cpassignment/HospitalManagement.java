package cpassignment;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class HospitalManagement {

    private static Lock lock = new ReentrantLock();
    private final Condition patientLeaveCondition = lock.newCondition();
    private final Condition doctorWaitingListPatient = lock.newCondition();
    private final Condition doctorOperationCondition = lock.newCondition();

    private ArrayList<Patient> commonWaitingList = new ArrayList<>();
    private volatile ArrayList<Doctor> workingDoctorList = new ArrayList<>();
    private ArrayList<Doctor> sortedWorkingDoctorList = new ArrayList<>();
    private ArrayList<Doctor> currentAvailableDoctorList = new ArrayList<>();
    private final int numberOfDoctorWorking;
    private int numberOfPatientVisit;
    private long startTime;

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
        ArrayList<Doctor> availableDoctorList = new ArrayList<>();// create a list of available doctor
        ArrayList<Doctor> sortedWorkingDoctorList = new ArrayList<>();
        for (int i = 0; i < this.workingDoctorList.size(); i++) {
            if (this.workingDoctorList.get(i).getIsAvailable() == true) {
                availableDoctorList.add(this.workingDoctorList.get(i));
            }
            sortedWorkingDoctorList.add(this.workingDoctorList.get(i));
        }
        this.sortedWorkingDoctorList = sortCurrentAvailableDoctorListByTotalPatientNumber(sortedWorkingDoctorList);
        this.currentAvailableDoctorList = sortCurrentAvailableDoctorListByTotalPatientNumber(availableDoctorList);
    }

    public long getStartTime() {
        return this.startTime;
    }

    // SETTER
    public void setCommonWaitingList(ArrayList<Patient> commonWaitingList) {
        this.commonWaitingList = commonWaitingList;
    }

    public void updateWorkingDoctorList(Doctor doctor) {
        this.workingDoctorList.add(doctor);
    }

    public void setNumberOfPatientVisit(int numberOfPatientVisit) {
        this.numberOfPatientVisit = numberOfPatientVisit;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

// Sorting
    public ArrayList<Doctor> sortCurrentAvailableDoctorListByTotalPatientNumber(ArrayList<Doctor> docList) {
        for (int i = 0; i < docList.size(); i++) {
            for (int j = 0; j < docList.size() - 1; j++) {
                if (docList.get(j).getTotalNumberOfPatient() > docList.get(j + 1).getTotalNumberOfPatient()) {
                    Doctor temp = docList.get(j);
                    docList.set(j, docList.get(j + 1));
                    docList.set(j + 1, temp);
                }
            }
        }
        return docList;
    }

    public ArrayList<Doctor> sortMinPatientDoctorListByWaitingListNumber(ArrayList<Doctor> docList) {
        for (int i = 0; i < docList.size(); i++) {
            for (int j = 0; j < docList.size() - 1; j++) {
                if (docList.get(j).getWaitingList().size() > docList.get(j + 1).getWaitingList().size()) {
                    Doctor temp = docList.get(j);
                    docList.set(j, docList.get(j + 1));
                    docList.set(j + 1, temp);
                }
            }
        }
        return docList;
    }

// Calculation
// MIN TOTAL NUM OF PATIENT
    public ArrayList<Doctor> findMinTotalPatientNumberDoctorList(int index) {
        ArrayList<Doctor> doctorListHavingMinTotalNumberPatient = new ArrayList<>();
        doctorListHavingMinTotalNumberPatient.add(this.currentAvailableDoctorList.get(index));
        for (int i = index + 1; i < this.currentAvailableDoctorList.size(); i++) {
            if (currentAvailableDoctorList.get(i).getTotalNumberOfPatient() == doctorListHavingMinTotalNumberPatient
                    .get(0).getTotalNumberOfPatient()) {
                doctorListHavingMinTotalNumberPatient.add(this.currentAvailableDoctorList.get(i));
            } else {
                break;
            }
        }
        return doctorListHavingMinTotalNumberPatient;
    }

// DOCTOR RANGE DIFFERENCE
    public boolean isRangeDifferenceInThree(int patientNum) {
        int difference = patientNum - this.sortedWorkingDoctorList.get(0).getTotalNumberOfPatient();
        return Math.abs(difference) < 2 && Math.abs(difference) >= 0;
    }

// Operation
    public void doctorOperation(Doctor doc) throws Exception {
        lock.lock();
        try {
            while (!doc.getWaitingList().isEmpty()) {
                doctorWaitingListPatient.signalAll();
                doctorOperationCondition.awaitUninterruptibly();
            }
            if (doc.getTotalNumberOfPatient() % 8 == 0 && doc.getTotalNumberOfPatient() != 0
                    && doc.getWaitingList().isEmpty() && !doc.getIsAvailable()) {
                System.out.println("[ ------ " + doc.getDoctorID() + " START Resting -------]");
                lock.unlock();
                Thread.sleep(15 * 1000);
                lock.lock();
                doc.setIsAvailable(true);
                patientLeaveCondition.signalAll();
                System.out.println("[ ~~~~~~ " + doc.getDoctorID() + " END Rest ~~~~~ ]");
            }
        } finally {
            lock.unlock();
        }
    }

    public void waitingListPatientOperation(Doctor doc, Patient p) throws Exception { // should waiting patient lock ?
        lock.lock();
        try {
            while (doc.isConsulting()) {
                doctorWaitingListPatient.awaitUninterruptibly();
            }
            doc.getWaitingList().remove(p);
            doc.setIsConsulting(true);
            System.out.println("[ " + doc.getDoctorID() + " start consult] ; " + p.getPatientID() + " start consultation by " + doc.getDoctorID());
            p.setEndOfWaitingTime(System.currentTimeMillis() - startTime);
            lock.unlock();
            Thread.sleep(p.getConsultationTime() * 1000);
            lock.lock();
            System.out.println("[ " + doc.getDoctorID() + " finished giving consultation ] ; " + p.getPatientID() + " finish consulting by " + doc.getDoctorID());
            doc.setIsConsulting(false);
            doc.incrementTotalConsultationTime(p.getConsultationTime());
            doctorOperationCondition.signal();
            patientLeaveCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void commonListPatientOperation(Patient p) throws Exception {
        lock.lock();
        try {
            patientLeaveCondition.awaitUninterruptibly();
//            System.out.println("You been called : " + p.getPatientID());
            lock.unlock();
            assignPatient(p);
            lock.lock();
        } finally {
            lock.unlock();
        }
    }

    public void assignPatient(Patient p) throws Exception {
        lock.lock();
        try {
//            System.out.println("========= START ASSIGN PATIENT =========");
            boolean consulted = false;
            getNewAvailableDoctorListOfTheDay(); // this.currentAvailableDoctorList ady sorted according total patient number
            if (isAllClinicWaitingListFull() == true || (!this.commonWaitingList.isEmpty() && p.isIsFirstTime() == true)) {
                if (p.isIsFirstTime() == true) {
                    this.commonWaitingList.add(p);
                    System.out.println(p.getPatientID() + "enter common waiting list");
                    p.setIsFirstTime(false);
                }

                // PRINT COMMON WAITING LIST
//                System.out.println("\n The common waiting list : ");
//                this.commonWaitingList.stream().forEach((patient) -> {
//                    System.out.println(patient.getPatientID() + " , ");
//                });
                
                lock.unlock();
                commonListPatientOperation(p);
                lock.lock();
            } else {
                int searchIndex = 0;
                while (true) {
                    ArrayList<Doctor> doctorListHavingMinTotalNumberPatient = findMinTotalPatientNumberDoctorList(
                            searchIndex);

                    // Print DOCTOR THAT HAVE MIN PATIENT
//                    System.out.println("\n The doc list that have min patient : " + doctorListHavingMinTotalNumberPatient.size() + " ppl");
//                    doctorListHavingMinTotalNumberPatient.stream().forEach((doctor) -> {
//                        System.out.println(doctor.getDoctorID() + " , ");
//                    });
//                    System.out.println("");

                    //HAVE MIN TOTAL # PATIENT DR LIST == 1
                    if (doctorListHavingMinTotalNumberPatient.size() == 1) {

                        // IF DR WAITING LIST < 3 & DR. PATIENT # RANGE < 3 || COMMON WAITING LIST ! empty & PATIENT JUST WALK IN 
                        if (doctorListHavingMinTotalNumberPatient.get(0).getWaitingList().size() < 3
                                && isRangeDifferenceInThree(doctorListHavingMinTotalNumberPatient.get(0).getTotalNumberOfPatient())) {
                            Doctor inchargeDoctor = doctorListHavingMinTotalNumberPatient.get(0);
                            if (!p.isIsFirstTime()) {
                                this.commonWaitingList.remove(p);
                            } else {
                                p.setIsFirstTime(false);
                            }
                            inchargeDoctor.addPatient(p);

                            //Check and Update Dr availability status after add a patient
                            if (inchargeDoctor.getTotalNumberOfPatient() % 8 == 0) {
                                inchargeDoctor.setIsAvailable(false);
                            }
                            System.out.println(inchargeDoctor.getDoctorID() + " has min patient");
                            System.out.println(p.getPatientID() + " is assigned to " + inchargeDoctor.getDoctorID());
                            lock.unlock();
                            waitingListPatientOperation(inchargeDoctor, p);
                            lock.lock();
                            consulted = true;
                            break;
                        } else if (!isRangeDifferenceInThree(doctorListHavingMinTotalNumberPatient.get(0).getTotalNumberOfPatient())) {
                             // DR. PATIENT # RANGE > 3
//                            System.out.println("Opps Doctor reach the max quota! Wait for other available doctor");
                            if (p.isIsFirstTime()) {
                                this.commonWaitingList.add(p);
                                System.out.println(p.getPatientID() + "enter common waiting list");
                                p.setIsFirstTime(false);
                            }
                            lock.unlock();
                            commonListPatientOperation(p);
                            lock.lock();
                            break;
                        } else {
                             // DR. PATIENT # RANGE < 3 &  DR WAITING LIST < 3 
//                            System.out.println("Opps waiting list is full but Range in 3. Go find next doctor!!!!!!!");
                            if (searchIndex + 1 < this.currentAvailableDoctorList.size() && consulted == false) {
                                searchIndex += 1;
                            } else {
                                // THIS IS THE LAST AVAILABLE DOCTOR
                                if (consulted == false) {
                                    if (p.isIsFirstTime() == true) {
                                        this.commonWaitingList.add(p);
                                        System.out.println(p.getPatientID() + "enter common waiting list");
                                        p.setIsFirstTime(false);
                                    }
                                    lock.unlock();
                                    commonListPatientOperation(p);
                                    lock.lock();
                                }
                                break;
                            }
                        }
                    } else {
                        //HAVE MIN TOTAL # PATIENT DR LIST > 1

                        doctorListHavingMinTotalNumberPatient = sortMinPatientDoctorListByWaitingListNumber(
                                doctorListHavingMinTotalNumberPatient);
                        
                        if (isRangeDifferenceInThree(
                                doctorListHavingMinTotalNumberPatient.get(0).getTotalNumberOfPatient())) {
                            for (int i = 0; i < doctorListHavingMinTotalNumberPatient.size(); i++) {
                                if (doctorListHavingMinTotalNumberPatient.get(i).getWaitingList().size() < 3) {
                                    Doctor inchargeDoctor = doctorListHavingMinTotalNumberPatient.get(i);
                                    if (p.isIsFirstTime() == false) {
                                        this.commonWaitingList.remove(p);
                                    } else {
                                        p.setIsFirstTime(false);
                                    }
                                    inchargeDoctor.addPatient(p);
                                    if (inchargeDoctor.getTotalNumberOfPatient() % 8 == 0) {
                                        inchargeDoctor.setIsAvailable(false);
                                    }
                                    System.out.println(
                                            p.getPatientID() + " is assigned to " + inchargeDoctor.getDoctorID());
                                    lock.unlock();
                                    waitingListPatientOperation(inchargeDoctor, p);
                                    lock.lock();
                                    consulted = true;
                                    break;
                                }
                            }
                            if (searchIndex
                                    + doctorListHavingMinTotalNumberPatient.size() < this.currentAvailableDoctorList
                                    .size()
                                    && consulted == false) {
                                searchIndex += doctorListHavingMinTotalNumberPatient.size();
                            } else {
                                if (consulted == false) {
                                    if (p.isIsFirstTime() == true) {
                                        this.commonWaitingList.add(p);
                                        System.out.println(p.getPatientID() + "enter common waiting list");
                                        p.setIsFirstTime(false);
                                    }
                                    lock.unlock();
                                    commonListPatientOperation(p);
                                    lock.lock();
                                }
                                break;
                            }
                        } else {
                            //When all doctor reach max quota
                            if (p.isIsFirstTime()) {
                                this.commonWaitingList.add(p);
                                p.setIsFirstTime(false);
                            }
//                            System.out.println("Opps ALL Doctor reach the max quota! Wait for other available doctor");
                            lock.unlock();
                            commonListPatientOperation(p);
                            lock.lock();
                            break;
                        }
                    }
                }
            }
        } finally {
            lock.unlock();
        }
    }

}
