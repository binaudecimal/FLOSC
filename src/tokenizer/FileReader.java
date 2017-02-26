package tokenizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Scanner;

public class FileReader {
    private File inputFile;
    private Reader r;
    private char currentChar;
    private char onhold;
    private int row, col;

    public FileReader(String location) throws FileNotFoundException{
        inputFile = new File(location);
        r = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));
    }
    public int getRow(){
        return row;
    }
    
    public int getCol(){
        return col;
    }
    
    public char getNextChar(){ 
        if(currentChar=='\n') row++;
        else col++;

        return currentChar;
    }
    
    public boolean hasNext(){
        int x = 0;
        if(onhold=='\0'){
            try{
                x= r.read();
            }
            catch(IOException e){System.err.println("Illegal read");
            }
            if(x==-1) return false;
            else{
                currentChar = (char)x;
                return true;
            } 
        }
        else{
            currentChar=getHeld();
            return true;
        }
    }
    public void putHold(char ch){
        onhold = ch;
    }
    public char getHeld(){
        char cha = onhold;
        onhold='\0';
        return cha;
    }
}
