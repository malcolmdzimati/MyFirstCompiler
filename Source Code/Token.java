public class Token{
    String id;                    //position of the token in the stream
    String _class;                 //token type
    String contents;              //actual instance of the token type

    public Token(){

    }

    //setter for Token ID
    public void setID(String position){
        id = position;
    }

    //setter for Token Class
    public void setClass(String c){
        _class = c;
    }

    //gsetter for Token Contents
    public void setContents(String content){
        contents = content;
    }

    //getter for Token ID
    public String getID(){
        return id;
    }

    //getter for Token Class
    public String getClass(){
        return _class;
    }

    //getter for Token Contents
    public String getContents(){
        return contents;
    }
}
