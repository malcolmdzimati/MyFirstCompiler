public class Parser{
    Token tokens[];
    int length;
    int current=0;
    Token currentToken;

    public Token getNextToken(){
        currentToken = tokens[current++];
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
        if(getNextToken().getContent().equals(" ")){
            getNextToken();
            return;
        }

        parse_PD();

        if(currentToken.equals(",")){
            parse_ProcDefs();
        }else{
            String error = "Procedure parse_ProcDefs() expected a ',' token " + "but received: " + currentToken.getContent();
        }
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
}
