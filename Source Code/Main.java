import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner


public class Main{
    //Function to Scanner Size of File
    public int static readFileSize(String filename){
        int size = 0;
        try{
            File myFile = new File(filename);
            Scanner myReader = new Scanner(myFile);

            while(myReader.hasNextLine()){
                size++;
            }
            myReader.close();
        } catch (FileNotFoundException e){
            return -1;
        }
        return size;
    }

    //Function to read code in file and put it into  a string array(2d char array)
    public String[] static readFile(String filename){
        int size = readFileSize(filename);
        String code[size] = new String[];

        try{
            File myFile = new File(filename);
            Scanner myReader = new Scanner(myFile);

            for(int i = 0; i < Size; i++){
                code[i] = myReader.nextLine();
            }
            myReader.close();
        } catch (FileNotFoundException e){
            return null;
        }
    }

    public static void main(String[] args) {

    }
}
