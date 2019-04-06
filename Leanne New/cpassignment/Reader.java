package cpassignment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Reader implements Runnable {

    public Reader() {

    }
    
    
    public void readFile() throws FileNotFoundException , IOException{
        String thisLine = null;
        BufferedReader br = new BufferedReader(new FileReader("D:\\testout.txt"));
        while ((thisLine = br.readLine()) != null) {
            // Operation of the input file
        }
        br.close();
        
    }

    @Override
    public void run() {
        try {
            readFile();
        } catch (IOException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}