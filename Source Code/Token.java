public class Token{
    String id;                    //position of the token in the stream
    String _class;                 //token type
    String content;              //actual instance of the token type
    int current;

    public Token(int id, String _class, String content){
        this.id=Integer.toString(id);
        this._class=_class;
        this.content=content;
        current=0;
    }

    //setter for Token ID
    public void setID(String position){
        id = position;
    }

    //setter for Token Class
    public void set_Class(String c){
        _class = c;
    }

    public char read(){
        if(hasNext()){
            char ret = content.charAt(current);
            current++;
            return ret;
        }
        return '*';
    }

    public boolean hasNext(){
        return current < content.length();
    }

    //gsetter for Token Contents
    public void setContent(String content){
        this.content = content;
    }

    //getter for Token ID
    public String getID(){
        return id;
    }

    //getter for Token Class
    public String get_Class(){
        return _class;
    }

    //getter for Token Contents
    public String getContent(){
        return content;
    }
}
