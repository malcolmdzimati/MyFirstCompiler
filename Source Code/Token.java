public class Token{
    String id;                    //position of the token in the stream
    String _class;                 //token type
    String content;              //actual instance of the token type
    int current;

    public Token(int id, String _class, String content){
        this.content = content;
        if(content.equals("if") || content.equals("then") || content.equals("else") || content.equals("do") || content.equals("until") || content.equals("while") || content.equals("do") || content.equals("output") || content.equals("call") || content.equals("true") || content.equals("false") || content.equals("input") || content.equals("not") || content.equals("and") || content.equals("or") || content.equals("eq") || content.equals("larger") || content.equals("add") || content.equals("sub") || content.equals("mult") || content.equals("arr") || content.equals("num") || content.equals("bool") || content.equals("string") || content.equals("main") || content.equals("halt") || content.equals("proc") || content.equals("return")){
            this._class = "keyword";
        }else{
            this._class = _class;
        }
        this.id = Integer.toString(id);

        try {
            int d = Integer.parseInt(content);
            this._class = "Number";
        } catch (NumberFormatException nfe) {

        }
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

    @Override
    public String toString(){
        return "<ID: "+id+", Class: "+_class+", Content: "+content+">\n";
    }
}
