# Welcome To My Compiler

## author: Batsirai Malcolm Dzimati
## Student Number: u20456078

## Project Details
Language used was Java on archlinux.
Java used to test program on my side was:
```bash
openjdk 18.0.1.1 2022-04-22
OpenJDK Runtime Environment (build 18.0.1.1+2)
OpenJDK 64-Bit Server VM (build 18.0.1.1+2, mixed mode)
```

Imports Used throughout Project:
```java
import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
```

## Running

##General Desciription
Compiler was designed to be used vai command line and makefile is provided and all interaction with the program is done vaai command line
Please make sure that the input File is always of type '.txt'(As specified by Specs.
When running the code the user will be propmted to enter filename.
This means that:
1. entering a filename thats in the same directory as the execution file, Look at folder eg1
2. Entering the directory and filename to a file that is contained within a directory that is in the same directory as the execuation file, look at folder eg2


## Excution
Provided Three Folders for different verions of the same runable code: Executable(.class), Executable(.jar) and implementation(SourceCode).

## Executable(.class):
contains '.class' files that can be run in the terminal with command line and command:
```bash
make
```
or 
```bash
make run
```

Please do not move, alter or delete any of the files that are stored there(.'class') files)

## Executable(.jar):
contains a compiler.jar that can be run with the provided makefile by using command line and command:
```bash
make
```
or 
```bash
make run
```

## Implementation(SourceCode)
contains .Java files and actually implmementation of the code. a makefile is also provided to compile and run this coded if everthing else fails using provided command:
```bash
make
```
or 
```bash
make run
```


## After Execution
After a Succesful run the program will stop and if no errors are returned it means the file was successful parsered and the resultant .xml will be contained in the same location as the input file.
I repeat the resultant .xml file will be stored at the same place as the input file.
The .xml file will be be name 'InputFileName'TreeResult.xml, e.g if the input filename was 'input.txt' then the .xml file will be 'inputTreeResult.txt'. 
Please make sure that the only presence of '.txt' as at the end of the filename as the file Extension only.
To rerun program just follow the above mention sections.
If an error is encounted the program stops and description of error is provided. For more deatiled explantion of errors please look at the 'Possible Running Errors' section

# Possible Running Errors

## File not Found
If filename was not found program, program will return error:
```bash
ERROR: File not found. Please Make sure that the file exists and the filename and Directory you provided is correct.
```
please make sure you read the section: 'General Description' or look at folders 'eg1' and 'eg2' as examples of how to provide input file, and please make sure the file is a text file '.txt'


## [Lexer Error]
If there was a lexcial error in the file, an error like this will be returned:
```bash
[LEXICAL ERROR]LexerErrorException: Failed to Recoginze: [Line: 16] Token: '-a' of Class: 'Number' with ID: 108
```
The first part details the error type i.e 'Lexer Error' and the Second part deatils the Exception class return the error 'LexerErrorException' then the rest just gives a description of the error 'Failed to Recginze' followed by the line number  '[Line: 16]' and the actucal cause of the problem '-a' and then more toeken info
The line number specifies on which line the error occured with the first rowing being '1' and the second row being '2' 

## [SYNTAX Error]
If there was a parser error an error like:
```bash
[SYNTAX ERROR]ParserErrorException: Procedure parse_SPLProgr() expected a 'main' token but received: [Line: 1] Token: 'aa' of Class: 'userDefinedName' with ID: 0
```
The first part details the error type i.e 'Syntax Error' and the Second part deatils the Exception class return the error 'ParserErrorException' then the description of the error with the procedure being the part of the language that failed.
The Program will suggest a fix to the problem letting the user know what token was expected to parse that part, it also provides at which line the parse failed and as well at which token the parser failed.
The suggestion won't also be relevant to the exact problem for example, given file input.txt with part of the code being:
```bash
1. proc procd1
2. {
3.     output := 9652;
4.     lhsvar1 := var1wnum1;
5.     varfield1[var] := varassign1[45];
6.     constfield1[true] := not(input(invar));
7.     constfield2["SHORT STRING 69"] := larger(input(invar2), not(eq(eq1, eq2)));
8.
9.    iff (iffield[false]) then
10.    {
11. ....
```
it is clear that the error is at line 9 at the token 'iff' but the parse will return Error:
```Bash
[SYNTAX ERROR]ParserErrorException: Procedure parse_Assign() expected a ':=' token but received: [Line: 9] Token: '(' of Class: 'Separator' with ID: 58
```
meaning the parse read 'iff' as a valid user defined name and it expected an ':=" to assign a value to the userDefinedName to make the line parserable therefor incorrectly saying that token ')' is erranous because that is where the parser fails
The line number specifies on which line the parser failed, this might not always equate to where the error in code is but this is where the parser failed. it counts from 1, with the first line being 1
This is also true for tokens the returned token is where the parser fails and it is not necessarilie the errorneus token


