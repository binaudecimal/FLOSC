package tokenizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FileReader {
    private File inputFile;
    private PushbackReader r;
    private char currentChar;
//    private char onhold = '\0';
    private int row =1, col=1;

    public FileReader(String location) throws FileNotFoundException{
        inputFile = new File(location);
        r = new PushbackReader(new BufferedReader(new InputStreamReader(new FileInputStream(inputFile))));
    }
    public int getRow(){
        return row;
    }
    
    public int getCol(){
        return col;
    }
    
    public char getNextChar(){ 
        if(currentChar=='\n') {
            row++;
            col = 0;
        }
        else col++;
     

        return currentChar;
    }
    
    public boolean hasNext(){
        int x = 0;
            try{
                x= r.read();
//                System.out.println("Hasnext function " + x + " row " + getRow() + " col " + getCol());
            }
            catch(IOException e){System.err.println("Illegal read");
            }
            if(x==-1) return false;
            else{
                currentChar = (char)x;
                return true;
            } 
    }

    public void backread(){
        try {
            col--;
            //System.out.println("Inside backread..." + (int)currentChar);
            r.unread((int)currentChar);
            
        } catch (IOException ex) {
            Logger.getLogger(FileReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void skip(){
        try {
            r.skip(1);
        } catch (IOException ex) {
            System.out.println("Reached end of file");
        }
    }
    
}
