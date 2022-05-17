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
                            getNextToken();
                            return;
                        }else{
                            String error = "Procedure parse_SPLProgr() expected a 'main' token " + "but received: " + currentToken.getContent();
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
}
