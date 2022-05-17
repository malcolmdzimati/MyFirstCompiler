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

    public void parse_Loop(){
        if(getNextToken().getContent().equals("do")){
            if(getNextToken().getContent().equals("{")){
                parse_Algorithm();
                if(getNextToken().getContent().equals("}")){
                    if(getNextToken().getContent().equals("until")){
                        if(getNextToken().getContent().equals("(")){
                            parse_Expr();
                            if(getNextToken().getContent().equals(")")){

                            }else{
                                String error = "Procedure parse_Loop() expected a ')' token " + "but received: " + currentToken.getContent();
                            }
                        }else{
                            String error = "Procedure parse_Loop() expected a '(' token " + "but received: " + currentToken.getContent();
                        }
                    }else{
                        String error = "Procedure parse_Loop() expected a 'until' token " + "but received: " + currentToken.getContent();
                    }
                }else{
                    String error = "Procedure parse_Loop() expected a '}' token " + "but received: " + currentToken.getContent();
                }
            }else{
                String error = "Procedure parse_Loop() expected a '{' token " + "but received: " + currentToken.getContent();
            }
        }else if(currentToken.getContent().equals("while")){
            if(getNextToken().getContent().equals("(")){
                parse_Expr();
                if(getNextToken().getContent().equals(")")){
                    if(getNextToken().getContent().equals("do")){
                        if(getNextToken().getContent().equals("{")){
                            parse_Algorithm();
                            if(getNextToken().getContent().equals("}")){

                            }else{
                                String error = "Procedure parse_Loop() expected a '}' token " + "but received: " + currentToken.getContent();
                            }
                        }else{
                            String error = "Procedure parse_Loop() expected a '{' token " + "but received: " + currentToken.getContent();
                        }
                    }else{
                        String error = "Procedure parse_Loop() expected a 'do' token " + "but received: " + currentToken.getContent();
                    }
                }else{
                    String error = "Procedure parse_Loop() expected a ')' token " + "but received: " + currentToken.getContent();
                }
            }else{
                String error = "Procedure parse_Loop() expected a '(' token " + "but received: " + currentToken.getContent();
            }
        }else{
            String error = "Procedure parse_Loop() expected a 'while' or 'do' token " + "but received: " + currentToken.getContent();
        }
    }

    public void parse_LHS(){
        if(getNextToken().getContent().equals("output")){

        }
    }

    public void parse_Expr(){

    }

    public void parse_PCall(){
        if(getNextToken().getContent().equals("call")){
            if(getNextToken().get_Class().equals("userDefinedName")){

            }else{
                String error = "Procedure parse_PCall() expected a 'userDefinedName' token " + "but received: " + currentToken.get_Class();
            }
        }else{
            String error = "Procedure parse_PCall() expected a 'call' token " + "but received: " + currentToken.getContent();
        }
    }

    public void parse_Var(){
        if(getNextToken().get_Class().equals("userDefinedName")){

        }else{
            String error = "Procedure parse_PCall() expected a 'userDefinedName' token " + "but received: " + currentToken.getContent();
        }
    }

    public boolean ll_Var(){
        boolean ll = false;
        if(getNextToken().get_Class().equals("userDefinedName")){
            ll=true;
        }
        getPToken();
        return ll;
    }

    public void parse_Field(){
        if(getNextToken().get_Class().equals("userDefinedName")){
            if(getNextToken().getContent().equals("[")){
                if(ll_Var()){
                    parse_Var();
                }else {
                    parse_Const();
                }

                if(getNextToken().getContent().equals("[")){

                }else{
                    String error = "Procedure parse_PCall() expected a ']' token " + "but received: " + currentToken.getContent();
                }
            }else{
                String error = "Procedure parse_PCall() expected a '[' token " + "but received: " + currentToken.getContent();
            }
        }else{
            String error = "Procedure parse_PCall() expected a 'userDefinedName' token " + "but received: " + currentToken.get_Class();
        }
    }

    public void parse_Const(){
        if(getNextToken().get_Class().equals("ShortString")){

        }else if(getNextToken().get_Class().equals("Number")){

        }else if(getNextToken().getContent().equals("true")){

        }else if(getNextToken().getContent().equals("false")){

        }else{
            String error = "Procedure parse_PConst() expected a 'shortString' or 'Number' or 'true' or 'false' token " + "but received: " + currentToken.getContent();
        }
    }

    public void parse_UnOp(){
        if(getNextToken().getContent().equals("input")){
            if(getNextToken().getContent().equals("(")){
                parse_Var();
                if(getNextToken().getContent().equals(")")){

                }else{
                    String error =  "Procedure parse_UnOp() expected a ')' token " + "but received: " + currentToken.getContent();
                }
            }else{
                String error =  "Procedure parse_UnOp() expected a '(' token " + "but received: " + currentToken.getContent();
            }
        }else if(currentToken.getContent().equals("not")){
            if(getNextToken().getContent().equals("(")){
                parse_Expr();
                if(getNextToken().getContent().equals(")")){

                }else{
                    String error =  "Procedure parse_UnOp() expected a ')' token " + "but received: " + currentToken.getContent();
                }
            }else{
                String error =  "Procedure parse_UnOp() expected a '(' token " + "but received: " + currentToken.getContent();
            }
        }else{
            String error =  "Procedure parse_UnOp() expected a 'input' or 'not' token " + "but received: " + currentToken.getContent();
        }
    }

    public void parse_BinOp(){
        String binopgroup = "and or eq larger add sub mult";
        if(binopgroup.indexOf( getNextToken().getContent()) != -1){
            if(getNextToken().getContent().equals("(")){
                parse_Expr();
                if(getNextToken().getContent().equals(",")){
                    parse_Expr();
                    if(getNextToken().getContent().equals(")")){

                    }else{
                        String error =  "Procedure parse_BinOp() expected a ')' token " + "but received: " + currentToken.getContent();
                    }
                }else{
                    String error =  "Procedure parse_BinOp() expected a ',' token " + "but received: " + currentToken.getContent();
                }
            }else{
                String error =  "Procedure parse_BinOp() expected a '(' token " + "but received: " + currentToken.getContent();
            }
        }else{
            String error =  "Procedure parse_BinOp() expected a 'and' or 'or' 'eq' or 'larger' or 'add' or 'sub' or 'mult' token " + "but received: " + currentToken.getContent();
        }
    }

    public void parse_VarDecl(){
        String varDeclf = "}";
        if(varDeclf.indexOf( getNextToken().getContent()) != -1){
            return;
        }

        getPToken();
        parse_Dec();

        if(getNextToken().equals(";")){
            parse_VarDecl();
        }else{
            String error = "Procedure parse_ProcDefs() expected a ';' token " + "but received: " + currentToken.getContent();
        }

        //throw error
    }

    public void parse_Dec(){
        if(getNextToken().getContent().equals("arr")){
            parse_TYP();
            if(getNextToken().getContent().equals("[")){
                parse_Const();
                if(getNextToken().getContent().equals("]")){
                    parse_Var();
                }else{
                    String error = "Procedure parse_Dec() expected a ']' token " + "but received: " + currentToken.getContent();
                }
            }else{
                String error = "Procedure parse_Dec() expected a '[' token " + "but received: " + currentToken.getContent();
            }
        }else{
            parse_TYP();
            parse_Var();
        }
    }
}
