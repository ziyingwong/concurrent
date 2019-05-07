package cpassignment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Reader implements Runnable {

    private ArrayList<String[]> readList;
    private int numOfDoc;

    public void readFile() throws FileNotFoundException, IOException {
        String thisLine = null;
        BufferedReader br = new BufferedReader(new FileReader("testdata.txt"));
        this.numOfDoc = Integer.parseInt(br.readLine().trim());
        this.readList = new ArrayList<>();

        //create read list
        while ((thisLine = br.readLine()) != null) {
            String line = thisLine;
            String[] container = line.trim().split(";");
            this.readList.add(container);
        }
        br.close();
    }

    //Getter
    public int getNumOfDoc() {
        return this.numOfDoc;
    }

//	public ArrayList<Doctor> getDoctorList(){
//		return this.doctorList;
//	}
    public ArrayList<String[]> getPatientList() {
        return this.readList;
    }

//	public HospitalManagement getHospital() {
//		return this.hospital;
//	}
    @Override
    public void run() {
        try {
            readFile();
        } catch (IOException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
