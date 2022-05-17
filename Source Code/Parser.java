public class Parser{
    Token tokens[];
    int length;
    int current=0;
    Token currentToken;

    public Token getNextToken(){
        currentToken = tokens[current++];
        return currentToken;
    }

    public Token getPToken(){
        currentToken = tokens[--current];
        return currentToken;
    }

    public Parser(Token tokens[], int length){
        this.tokens=tokens;
        this.length = length;
    }

    public void parse_SPLProgr(){
        parse_ProcDefs();
        if(getNextToken().getContent().equals("main")){
            if(getNextToken().getContent().equals("{")){
                parse_Algorithm();
                if(getNextToken().getContent().equals("halt")){
                    if(getNextToken().getContent().equals(";")){
                        parse_VarDecl();
                        if(getNextToken().getContent().equals("}")){
                            return;
                        }else{
                            String error = "Procedure parse_SPLProgr() expected a '}' token " + "but received: " + currentToken.getContent();
                        }
                    }else{
                            String error = "Procedure parse_SPLProgr() expected a ';' token " + "but received: " + currentToken.getContent();
                    }
                }else{
                    String error = "Procedure parse_SPLProgr() expected a 'halt' token " + "but received: " + currentToken.getContent();
                }
            }else{
                String error = "Procedure parse_SPLProgr() expected a '{' token " + "but received: " + currentToken.getContent();
            }
        }else{
            String error = "Procedure parse_SPLProgr() expected a 'main' token " + "but received: " + currentToken.getContent();
        }
    }

    public void parse_ProcDefs(){
        String procdefsfollows = "main return if do while call output userDefinedName";
        if(procdefsfollows.indexOf( getNextToken().getContent()) != -1){
            return;
        }

        getPToken();
        parse_PD();

        if(getNextToken().equals(",")){
            parse_ProcDefs();
        }else{
            String error = "Procedure parse_ProcDefs() expected a ',' token " + "but received: " + currentToken.getContent();
        }

        //Throw an error
    }

    public void parse_PD(){
        if(getNextToken().getContent().equals("proc")){
            if(getNextToken().get_Class().equals("userDefinedName")){
                if(getNextToken().getContent().equals("{")){
                    parse_ProcDefs();
                    parse_Algorithm();
                    if(getNextToken().getContent().equals("return")){
                        if(getNextToken().getContent().equals(";")){
                            parse_VarDecl();
                            if(getNextToken().getContent().equals("}")){
                                return;
                            }else{
                                String error = "Procedure parse_PD() expected a '}' token " + "but received: " + currentToken.getContent();
                            }
                        }else{
                            String error = "Procedure parse_PD() expected a ';' token " + "but received: " + currentToken.getContent();
                        }
                    }else{
                        String error = "Procedure parse_PD() expected a 'return' token " + "but received: " + currentToken.getContent();
                    }
                }else{
                    String error = "Procedure parse_PD() expected a '}' token " + "but received: " + currentToken.getContent();
                }
            }else{
                String error = "Procedure parse_PD() expected a 'userDefinedName' token " + "but received: " + currentToken.get_Class();
            }
        }else{
            String error = "Procedure parse_PD() expected a 'proc' token " + "but received: " + currentToken.getContent();
        }
    }

    public void parse_Algorithm(){
        String procdefsfollows = "halt return }";
        if(procdefsfollows.indexOf( getNextToken().getContent()) != -1){
            return;
        }

        getPToken();
        parse_Instr();

        if(getNextToken().equals(";")){
            parse_Algorithm();
        }else{
            String error = "Procedure parse_Algorithm() expected a ';' token " + "but received: " + currentToken.getContent();
        }
        //Throw Error
    }

    public void parse_Instr(){

    }

    public void parse_Assign(){
        parse_LHS();
        if(getNextToken().getContent().equals(":=")){
            parse_Expr();
        }else{
            String error = "Procedure parse_Assign() expected a ':=' token " + "but received: " + currentToken.getContent();
        }
    }

    public void parse_Branch(){
        if(getNextToken().getContent().equals("if")){
            if(getNextToken().getContent().equals("(")){
                parse_Expr();
                if(getNextToken().getContent().equals(")")){
                    if(getNextToken().getContent().equals("then")){
                        if(getNextToken().getContent().equals("{")){
                            parse_Algorithm();
                            if(getNextToken().getContent().equals("}")){
                                parse_Alternate();
                            }else{
                                String error = "Procedure parse_Branch() expected a '}' token " + "but received: " + currentToken.getContent();
                            }
                        }else{
                            String error = "Procedure parse_Branch() expected a '{' token " + "but received: " + currentToken.getContent();
                        }
                    }else{
                        String error = "Procedure parse_Branch() expected a 'then' token " + "but received: " + currentToken.getContent();
                    }
                }else{
                    String error = "Procedure parse_Branch() expected a ')' token " + "but received: " + currentToken.getContent();
                }
            }else{
                String error = "Procedure parse_Branch() expected a '(' token " + "but received: " + currentToken.getContent();
            }
        }else{
            String error = "Procedure parse_Branch() expected a 'if' token " + "but received: " + currentToken.getContent();
        }
    }

    public void parse_Alternate(){
        String procdefsfollows = ";";
        if(procdefsfollows.indexOf( getNextToken().getContent()) != -1){
            return;
        }

        getPToken();

        if(getNextToken().getContent().equals("else")){
            if(getNextToken().getContent().equals("{")){
                parse_Algorithm();
                if(getNextToken().getContent().equals("}")){

                }else{
                    String error = "Procedure parse_ALternate() expected a '}' token " + "but received: " + currentToken.getContent();
                }
            }else{
                String error = "Procedure parse_ALternate() expected a '{' token " + "but received: " + currentToken.getContent();
            }
        }else{
            String error = "Procedure parse_ALternate() expected a 'else' token " + "but received: " + currentToken.getContent();
        }
    }
}
