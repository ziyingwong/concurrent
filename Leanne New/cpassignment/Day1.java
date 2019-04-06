package cpassignment;

import java.util.ArrayList;

public class Day1 {

    public static void main(String[] args) {
        HospitalManagement PPUM = new HospitalManagement(9);
        ArrayList<Doctor> workingDoctorList = new ArrayList<Doctor>();
        // Create doctor List [Should be at Reader class]
        for (int i = 0; i < PPUM.getNumberOfDoctorWorking(); i++) {
            Doctor doctor = new Doctor("d" + i, PPUM);
            workingDoctorList.add(doctor);
        }
        PPUM.setWorkingDoctorList(workingDoctorList);
        
        

        Thread patient = new Thread() {
            public void run() {
//                Patient patient1 = new Patient("p1");

//                ArrayList<Doctor> availableDoctor = new ArrayList<Doctor>(); // create a list of available doctor
//                for (int i = 0; i < PPUM.clinicList.size() - 1; i++) {
//                    if (PPUM.clinicList.get(i).doctor.isAvailable == true) {
//                        availableDoctor.add(PPUM.clinicList.get(i).doctor);
//                    }
//                }
//                boolean isFull = true; // check if all doctor waiting list is full
//                for (int i = 0; i < availableDoctor.size(); i++) {
//                    if (availableDoctor.get(i).waitingList.size() < 3) {
//                        isFull = false;
//                        break;
//                    }
//                }

//                if (isFull == true) {
//                    PPUM.commonWaitingList.add(patient1); // all doctor waiting list is full, patient added to common
//                    // waiting list
//                } else {
//                    ArrayList<Integer> min = new ArrayList<Integer>(); // find doctor with least total patient
//                    min.add(0);
//                    for (int i = 1; i < availableDoctor.size(); i++) {
//                        if (availableDoctor.get(i).totalNumberOfPatient < availableDoctor
//                                .get(min.get(0)).totalNumberOfPatient) {
//                            min.clear();
//                            min.add(i);
//                        } else if (availableDoctor.get(i).totalNumberOfPatient == availableDoctor
//                                .get(min.get(0)).totalNumberOfPatient) {
//                            min.add(i);
//                        }
//                    }
//
//                    if (min.size() == 1) {
//                        if (availableDoctor.get(min.get(0)).waitingList.size() < 3) {
//                            for (int i = 0; i < PPUM.clinicList.size(); i++) {
//                                if (PPUM.clinicList.get(i).doctor.doctorID.equals(availableDoctor.get(min.get(0)).doctorID)) {
//                                    Doctor inchargeDoctor = availableDoctor.get(min.get(0));
//                                    inchargeDoctor.addPatient(patient1);
//                                    PPUM.clinicList.set(i, new Clinic(inchargeDoctor));
//                                }
//                            }
//                        }
//                    }
//
//                }
            }
        };
    }

}
