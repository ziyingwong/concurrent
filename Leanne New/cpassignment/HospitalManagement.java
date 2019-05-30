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
//    private ArrayList<Doctor> workingDoctorList = new ArrayList<>();
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

//    public void setNumberOfDoctorWorking(int numberOfDoctorWorking) {
//        this.numberOfDoctorWorking = numberOfDoctorWorking;
//    }
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
//        int difference = patientNum - this.currentAvailableDoctorList.get(0).getTotalNumberOfPatient();
//        int mean = this.currentAvailableDoctorList.get((this.currentAvailableDoctorList.size() + 1) / 2)
//                .getTotalNumberOfPatient();
//        int difference = mean - patienNum;
//        System.out.println("Difference:" + difference);
        return Math.abs(difference) < 2 && Math.abs(difference) >= 0; // Less than 2 is because, it must be one thn 1 can allow patient and now become 2
    }

    // Operation
    public void doctorOperation(Doctor doc) throws Exception { // Should doctor lock ?
        //ori
//        lock.lock();
//        try {
//            while (!doc.getWaitingList().isEmpty()) {
//                doctorWaitingListPatient.signal();
//                ArrayList<Patient> list = doc.getWaitingList();
//                Patient p = list.get(0);
//                list.remove(0);
//                doc.setWaitingList(list);
//                patientLeaveCondition.await();
//                doc.incrementTotalConsultationTime(p.getConsultationTime() * 1000);
//            }
//        } finally {
//            lock.unlock();
//        }

        lock.lock();
        try {
            while (!doc.getWaitingList().isEmpty()) {
                doctorWaitingListPatient.signal();
                ArrayList<Patient> list = doc.getWaitingList();
                Patient p = list.get(0);
                list.remove(0);
                doc.setWaitingList(list);
                doctorOperationCondition.await();
                doc.incrementTotalConsultationTime(p.getConsultationTime() * 1000);
                System.out.println("\n" + doc.getDoctorID() + " waiting list : ");

                // Print WAITING LIST THAT HAVE MIN PATIENT
                doc.getWaitingList().stream().forEach((patient) -> {
                    System.out.println(patient.getPatientID() + " , ");
                });
                System.out.println("");

            }
            if (doc.getTotalNumberOfPatient() % 8 == 0 && doc.getTotalNumberOfPatient() != 0
                    && doc.getWaitingList().isEmpty() && !doc.getIsAvailable()) {

//                System.out.println(doc.getDoctorID() + " not available!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("[ " + doc.getDoctorID() + " START Resting  !!!!!!!!!!!!!!!!!!!!!]");
//                try {
                lock.unlock();
                Thread.sleep(15000);
                lock.lock();
//                } catch (Exception e) {
//                }

//                System.out.println(doc.getDoctorID() + " is now available!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                doc.setIsAvailable(true);
                patientLeaveCondition.signalAll();
                System.out.println("[ " + doc.getDoctorID() + " END Rest !!!!!!!!!!!!!!!!!!!!!");
//				System.out.println("signal 111111111111");
//				patientLeaveCondition.signal();
//				System.out.println("signal 222222222222");
//				patientLeaveCondition.signal();
//				System.out.println("signal 33333333333");
            }
        } finally {
            lock.unlock();
        }
    }

    public void waitingListPatientOperation(Doctor doc, Patient p) throws Exception { // should waiting patient lock ?
        lock.lock();
        try {
            while (doc.isConsulting()) {
                doctorWaitingListPatient.await();
            }
            doc.setIsConsulting(true);
            System.out.println("[ " + doc.getDoctorID() + " start consult] ; " + p.getPatientID() + " start consultation by " + doc.getDoctorID());
            p.setEndOfWaitingTime(System.currentTimeMillis() - startTime);
            lock.unlock();
            Thread.sleep(p.getConsultationTime() * 1000);
            lock.lock();
            System.out.println("[ " + doc.getDoctorID() + " finished giving consultation ] ; " + p.getPatientID() + " finish consulting by " + doc.getDoctorID());
            doc.setIsConsulting(false);
            doctorOperationCondition.signal();
            // TODO: WHICH ONE BETTER ? SignalAll or Signal?
            patientLeaveCondition.signalAll();
//            patientLeaveCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    public void commonListPatientOperation(Patient p) throws Exception {
        lock.lock();
        try {
            patientLeaveCondition.await();
            System.out.println("You been called : " + p.getPatientID());
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
            System.out.println("========= LOCK ASSIGN PATIENT =========");
            boolean consulted = false;
            getNewAvailableDoctorListOfTheDay(); // this.currentAvailableDoctorList ady sorted
            if (isAllClinicWaitingListFull() == true || (!this.commonWaitingList.isEmpty() && p.isIsFirstTime() == true)) {
                if (p.isIsFirstTime() == true) { /// first arrived patient
                    this.commonWaitingList.add(p); // all doctor waiting list is full, patient added to common waiting list
                    System.out.println(p.getPatientID() + "enter common waiting list");
                    p.setIsFirstTime(false);
                }
                System.out.println("\n The common waiting list : ");
                this.commonWaitingList.stream().forEach((patient) -> {
                    System.out.println(patient.getPatientID() + " , ");
                });
                lock.unlock();
                commonListPatientOperation(p);
                lock.lock();
            } else {
                int searchIndex = 0;
                while (true) {
                    ArrayList<Doctor> doctorListHavingMinTotalNumberPatient = findMinTotalPatientNumberDoctorList(
                            searchIndex);

                    // Print DOCTOR THAT HAVE MIN PATIENT
                    System.out.println("\n The doc list that have min patient : " + doctorListHavingMinTotalNumberPatient.size() + " ppl");
                    doctorListHavingMinTotalNumberPatient.stream().forEach((doctor) -> {
                        System.out.println(doctor.getDoctorID() + " , ");
                    });
                    System.out.println("");

                    if (doctorListHavingMinTotalNumberPatient.size() == 1) {
                        if (doctorListHavingMinTotalNumberPatient.get(0).getWaitingList().size() < 3
                                && isRangeDifferenceInThree(doctorListHavingMinTotalNumberPatient.get(0).getTotalNumberOfPatient())) {
                            Doctor inchargeDoctor = doctorListHavingMinTotalNumberPatient.get(0);
                            if (!p.isIsFirstTime()) {
                                this.commonWaitingList.remove(p);
                            } else {
                                //ADDED
                                p.setIsFirstTime(false);
                            }
                            inchargeDoctor.addPatient(p);

                            //Update Dr availability status after add a patient
                            if (inchargeDoctor.getTotalNumberOfPatient() % 8 == 0) {
                                inchargeDoctor.setIsAvailable(false);
                            }

                            System.out.println(inchargeDoctor.getDoctorID() + " has min patient");
                            System.out.println(p.getPatientID() + " is assigned to " + inchargeDoctor.getDoctorID());
//                            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! UNLOCK !!!!!!!!!!!!");
                            lock.unlock();
                            waitingListPatientOperation(inchargeDoctor, p);
                            lock.lock();
                            consulted = true;
                            break;
                        } else if (!isRangeDifferenceInThree(doctorListHavingMinTotalNumberPatient.get(0).getTotalNumberOfPatient())) {
                            System.out.println("Opps Doctor reach the max quota! Wait for other available doctor");
//                            Added
                              if (p.isIsFirstTime()) {
                                this.commonWaitingList.add(p);
                                System.out.println(p.getPatientID() + "enter common waiting list");
                                p.setIsFirstTime(false);
                            }
                            lock.unlock();
                            commonListPatientOperation(p);
                            lock.lock();
                            break;//(If the range exit thn can break ady)
                        }
                        // TODO: SOLVE THE LOCK PROBLEM
//                        searchIndex++;// Search for next one that have min num [Seems like have the release a unlock problem]
                    } else {
                        doctorListHavingMinTotalNumberPatient = sortMinPatientDoctorListByWaitingListNumber(
                                doctorListHavingMinTotalNumberPatient);
                        // Sort the doctor list that hv min patient by waiting list num
                        if (isRangeDifferenceInThree(
                                doctorListHavingMinTotalNumberPatient.get(0).getTotalNumberOfPatient())) {
                            for (int i = 0; i < doctorListHavingMinTotalNumberPatient.size(); i++) {
                                if (doctorListHavingMinTotalNumberPatient.get(i).getWaitingList().size() < 3) {
                                    Doctor inchargeDoctor = doctorListHavingMinTotalNumberPatient.get(i);
                                    if (p.isIsFirstTime() == false) {
                                        //need to update the common list by removing it
                                        this.commonWaitingList.remove(p);
                                    } else {
                                        //ADDED
                                        p.setIsFirstTime(false);
                                    }
                                    inchargeDoctor.addPatient(p);
                                    if (inchargeDoctor.getTotalNumberOfPatient() % 8 == 0) {
                                        inchargeDoctor.setIsAvailable(false);
                                    }
                                    System.out.println(
                                            p.getPatientID() + " is assigned to " + inchargeDoctor.getDoctorID());
//                                    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! UNLOCK !!!!!!!!!!!!");
                                    lock.unlock();
                                    waitingListPatientOperation(inchargeDoctor, p);
                                    lock.lock();
                                    consulted = true;
                                    break;
                                }
                            }
                            //ori
                            if (searchIndex
                                    + doctorListHavingMinTotalNumberPatient.size() < this.currentAvailableDoctorList
                                    .size()
                                    && consulted == false) {
                                searchIndex += doctorListHavingMinTotalNumberPatient.size();
                            } else{ // TODO: WHAT IS THE ACTION FOR THE PATIENT (ELSE THE THREAD MISSING) ?? CORRECT?
                               //ADDED that cause the fail
//                            if (consulted == false) {
//                                if (p.isIsFirstTime() == true) {
//                                    this.commonWaitingList.add(p);
//                                    System.out.println(p.getPatientID() + "enter common waiting list");
//                                    p.setIsFirstTime(false);
//                                }
//                                lock.unlock();
//                                commonListPatientOperation(p);
//                                lock.lock();
                                break;
                            }
                        } else {
                            //When all doctor reach max quota
                            //ADDED
                            if (p.isIsFirstTime()) {
                                this.commonWaitingList.add(p);
                                p.setIsFirstTime(false);
                            }
                            System.out.println("Opps ALL Doctor reach the max quota! Wait for other available doctor");
                            lock.unlock();
                            commonListPatientOperation(p);
                            lock.lock();
                            break;
                        }
                    }
                }
            }
        } finally {
//            System.out.println("========= UNLOCK ASSIGN PATIENT LOCK ============");
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
