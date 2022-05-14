import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner


public class Main{
    public int static readFileSize(String filename){
        int size = 0;
        try{
            File myFile = new File(filename);
            Scanner myReader = new Scanner(myFile);

            while(myReader.hasNextLine()){
                size++;
            }
        } catch (FileNotFoundException e){
            return -1;
        }
        myReader.close();
        return size;
    }

    public static void main(String[] args) {

    }
}
