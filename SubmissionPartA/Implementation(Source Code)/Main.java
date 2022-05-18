import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Main{
    //Function to Scanner Size of File
    public static int readFileSize(String filename){
        int size = 0;
        try{
            File myFile = new File(filename);
            Scanner myReader = new Scanner(myFile);

            while(myReader.hasNextLine()){
                String line = myReader.nextLine();
                size++;
            }
            myReader.close();
        } catch (FileNotFoundException e){
            return -1;
        }
        return size;
    }

    //Function to read code in file and put it into  a string array(2d char array)
    public static String[] readFile(String filename, int fileSize){
        int size = fileSize;
        String[] code = new String[size];

        try{
            File myFile = new File(filename);
            Scanner myReader = new Scanner(myFile);

            for(int i = 0; i < size; i++){
                code[i] = myReader.nextLine();
            }
            myReader.close();
        } catch (FileNotFoundException e){
            return null;
        }
        return code;
    }

    //Function to promt user for Filename and returns it as a string
    public static String readFileName(){
        System.out.println("Make Sure You have Familiarized yourself with ALL README.md(or README.txt) files.");
        System.out.println("Please Enter the Name of File, if no Directory is Specified it is Assumed that file is in the Current Directory.");
        System.out.print("Enter The Name of The File: ");

        Scanner read = new Scanner(System.in);
        String filename = read.nextLine();

        read.close();
        return filename;
    }

    public static void main(String[] args) {
        String fileName = readFileName();
        int fileSize = readFileSize(fileName);

        //Error message if file is not found.
        if(fileSize == -1){
            System.out.println("ERROR: File not found. Please Make sure that the file exists and the filename and Directory you provided is correct.");
            return;
        }

        String[] fileContent = readFile(fileName, fileSize);

        //Test Code to make Sure read in line correctly: Passed Test Reads in line properly
        /*for(String line : fileContent){
            System.out.println(line.length());
        }*/

        //Make Lexer and perform Lexical Analysis
        Lexer lexer = new Lexer(fileContent, fileSize);
        try{
            lexer.tokenize();
            lexer.lexicalAnalysis();

            Parser parser = new Parser(lexer.getTokens(), fileName);

            try {
                parser.syntaxAnalysis();
                //lexer.printToken();
            } catch (Exception e) {
                System.err.println("[SYNTAX ERROR]"+e);
            }

        }catch (LexerErrorException e){
            System.err.println("[LEXICAL ERROR]"+e);
        }
    }

}
