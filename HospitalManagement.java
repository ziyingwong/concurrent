package groupProject;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class HospitalManagement {

	private static Lock lock = new ReentrantLock();
	private final Condition patientLeaveCondition = lock.newCondition();
	private final Condition doctorWaitingListPatient = lock.newCondition();
	private final Condition doctorOperationCondition = lock.newCondition();
	private ArrayList<Patient> commonWaitingList = new ArrayList<Patient>();
	private volatile ArrayList<Doctor> workingDoctorList = new ArrayList<Doctor>();
	private ArrayList<Doctor> currentAvailableDoctorList = new ArrayList<Doctor>();
	private int numberOfDoctorWorking;
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
		ArrayList<Doctor> availableDoctorList = new ArrayList<>(); // create a list of available doctor
		for (int i = 0; i < this.workingDoctorList.size(); i++) {
			if (this.workingDoctorList.get(i).getIsAvailable() == true) {
				availableDoctorList.add(this.workingDoctorList.get(i));

			}
		}
		this.currentAvailableDoctorList = sortDoctorListByTotalPatientNumber(availableDoctorList);
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

	public void setNumberOfDoctorWorking(int numberOfDoctorWorking) {
		this.numberOfDoctorWorking = numberOfDoctorWorking;
	}

	public void setNumberOfPatientVisit(int numberOfPatientVisit) {
		this.numberOfPatientVisit = numberOfPatientVisit;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

// Sorting
	public ArrayList<Doctor> sortDoctorListByTotalPatientNumber(ArrayList<Doctor> docList) {
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
		for (int i = index; i < this.currentAvailableDoctorList.size(); i++) {
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
	public boolean isRangeDifferenceInThree(int patienNum) {
		this.currentAvailableDoctorList = sortDoctorListByTotalPatientNumber(this.currentAvailableDoctorList);
		int min = this.currentAvailableDoctorList.get(0).getTotalNumberOfPatient();
		int difference = min - patienNum;
		if (difference <= 3) {
			return true;
		} else {
			return false;
		}
	}

	// Operation
	public void doctorOperation(Doctor doc) throws Exception {
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
				for (int i = 0; i < doc.getWaitingList().size(); i++) {
					System.out.print(doc.getWaitingList().get(i).getPatientID() + " , ");
				}
				System.out.println("");

			}
			if (doc.getTotalNumberOfPatient() % 8 == 0 && doc.getTotalNumberOfPatient() != 0
					&& doc.getWaitingList().isEmpty() && !doc.getIsAvailable()) {

				System.out.println(doc.getDoctorID() + " not available!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				try {
					lock.unlock();
					Thread.sleep(15000);
					lock.lock();
				} catch (Exception e) {
				}
				System.out
						.println(doc.getDoctorID() + " is now available!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				doc.setIsAvailable(true);
				patientLeaveCondition.signalAll();
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
		try {
			lock.lock();
			while (doc.isConsultingStatus()) {
				doctorWaitingListPatient.await();
			}
			doc.setConsultingStatus(true);
			System.out.println(p.getPatientID() + " start consultation by " + doc.getDoctorID());
			p.setEndOfWaitingTime(System.currentTimeMillis() - startTime);
			lock.unlock();
			Thread.sleep(p.getConsultationTime() * 1000);
			lock.lock();
			System.out.println(p.getPatientID() + " finish consulting by " + doc.getDoctorID());
			doc.setConsultingStatus(false);
			doctorOperationCondition.signal();
			patientLeaveCondition.signalAll();
		} finally {
			lock.unlock();
		}

	}

	public void commonListPatientOperation(Patient p) throws Exception {
		patientLeaveCondition.await();
		assignPatient(p);
	}

	public void assignPatient(Patient p) throws Exception {
		lock.lock();
		try {
			boolean consulted = false;
			getNewAvailableDoctorListOfTheDay(); // this.currentAvailableDoctorList ady sorted
			if (isAllClinicWaitingListFull() == true||(!this.commonWaitingList.isEmpty()&&p.getIndex()==0)) {
				this.commonWaitingList.add(p); // all doctor waiting list is full, patient added to common waiting list
				System.out.println(p.getPatientID() + "enter common waiting list");
				p.incrementIndex();
				commonListPatientOperation(p);
			} else {
				int searchIndex = 0;
				while (true) {
					ArrayList<Doctor> doctorListHavingMinTotalNumberPatient = findMinTotalPatientNumberDoctorList(
							searchIndex);
					if (doctorListHavingMinTotalNumberPatient.size() == 1) {
						if (doctorListHavingMinTotalNumberPatient.get(0).getWaitingList().size() < 3
								&& isRangeDifferenceInThree(
										doctorListHavingMinTotalNumberPatient.get(0).getTotalNumberOfPatient())) {
							Doctor inchargeDoctor = doctorListHavingMinTotalNumberPatient.get(0);
							inchargeDoctor.addPatient(p);
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
						} else {
							searchIndex++;
						}
					} else {
						doctorListHavingMinTotalNumberPatient = sortMinPatientDoctorListByWaitingListNumber(
								doctorListHavingMinTotalNumberPatient);
						// Sort the doctor list that hv min patient by waiting list num
						if (isRangeDifferenceInThree(
								doctorListHavingMinTotalNumberPatient.get(0).getTotalNumberOfPatient())) {
							for (int i = 0; i < doctorListHavingMinTotalNumberPatient.size(); i++) {
								if (doctorListHavingMinTotalNumberPatient.get(i).getWaitingList().size() < 3) {
									Doctor inchargeDoctor = doctorListHavingMinTotalNumberPatient.get(i);
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
								break;
							}
						} else {
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
