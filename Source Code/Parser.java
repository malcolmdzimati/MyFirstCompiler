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

    public void parse_SPLProgr() throws ParserErrorException {
        try{
            parse_ProcDefs();
        }catch(ParserErrorException e){
            throw e;
        }
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
                            throw new ParserErrorException(error);
                        }
                    }else{
                            String error = "Procedure parse_SPLProgr() expected a ';' token " + "but received: " + currentToken.getContent();
                            throw new ParserErrorException(error);
                    }
                }else{
                    String error = "Procedure parse_SPLProgr() expected a 'halt' token " + "but received: " + currentToken.getContent();
                    throw new ParserErrorException(error);
                }
            }else{
                String error = "Procedure parse_SPLProgr() expected a '{' token " + "but received: " + currentToken.getContent();
                throw new ParserErrorException(error);
            }
        }else{
            String error = "Procedure parse_SPLProgr() expected a 'main' token " + "but received: " + currentToken.getContent();
            throw new ParserErrorException(error);
        }
    }

    public void parse_ProcDefs() throws ParserErrorException{
        String procdefsfollows = "main return if do while call output";
        if(procdefsfollows.indexOf( getNextToken().getContent()) != -1 || currentToken.get_Class().equals("userDefinedName")){
            return;
        }

        getPToken();
        parse_PD();

        if(getNextToken().equals(",")){
            parse_ProcDefs();
            return;
        }else{
            String error = "Procedure parse_ProcDefs() expected a ',' token " + "but received: " + currentToken.getContent();
            throw new ParserErrorException(error);
        }

        //Throw an error
        //throw new ParserErrorException("Unexpected Token recieved: " + currentToken.getContent());
    }

    public void parse_PD() throws ParserErrorException{
        if(getNextToken().getContent().equals("proc")){
            if(getNextToken().get_Class().equals("userDefinedName")){
                if(getNextToken().getContent().equals("{")){
                    try{
                        parse_ProcDefs();
                        parse_Algorithm();
                    }catch(ParserErrorException e){
                        throw e;
                    }
                    if(getNextToken().getContent().equals("return")){
                        if(getNextToken().getContent().equals(";")){
                            parse_VarDecl();
                            if(getNextToken().getContent().equals("}")){
                                return;
                            }else{
                                String error = "Procedure parse_PD() expected a '}' token " + "but received: " + currentToken.getContent();
                                throw new ParserErrorException(error);
                            }
                        }else{
                            String error = "Procedure parse_PD() expected a ';' token " + "but received: " + currentToken.getContent();
                            throw new ParserErrorException(error);
                        }
                    }else{
                        String error = "Procedure parse_PD() expected a 'return' token " + "but received: " + currentToken.getContent();
                        throw new ParserErrorException(error);
                    }
                }else{
                    String error = "Procedure parse_PD() expected a '}' token " + "but received: " + currentToken.getContent();
                    throw new ParserErrorException(error);
                }
            }else{
                String error = "Procedure parse_PD() expected a 'userDefinedName' token " + "but received: " + currentToken.get_Class();
                throw new ParserErrorException(error);
            }
        }else{
            String error = "Procedure parse_PD() expected a 'proc' token " + "but received: " + currentToken.getContent();
            throw new ParserErrorException(error);
        }
    }

    public void parse_Algorithm() throws ParserErrorException{
        String procdefsfollows = "halt return }";
        if(procdefsfollows.indexOf( getNextToken().getContent()) != -1){
            return;
        }

        getPToken();
        parse_Instr();

        if(getNextToken().equals(";")){
            parse_Algorithm();
            return;
        }else{
            String error = "Procedure parse_Algorithm() expected a ';' token " + "but received: " + currentToken.getContent();
            throw new ParserErrorException(error);
        }
        //Throw Error
        //throw new ParserErrorException("Unexpected Token receieved: "+currentToken.getContent());
    }

    public void parse_Instr() throws ParserErrorException{
        if(getNextToken().getContent().equals("if")){
            getPToken();
            try{
                parse_Branch();
            }catch (ParserErrorException e){
                throw e;
            }
        }else if(currentToken.getContent().equals("do") || currentToken.getContent().equals("while")){
            getPToken();
            try{
                parse_Loop();
            }catch (ParserErrorException e){
                throw e;
            }
        }else if(currentToken.getContent().equals("call")){
            getPToken();
            parse_PCall();
        }else{
            getPToken();
            parse_Assign();
        }
    }

    public void parse_Assign() throws ParserErrorException{
        parse_LHS();
        if(getNextToken().getContent().equals(":=")){
            parse_Expr();
        }else{
            String error = "Procedure parse_Assign() expected a ':=' token " + "but received: " + currentToken.getContent();
            throw new ParserErrorException(error);
        }
    }

    public void parse_Branch() throws ParserErrorException{
        if(getNextToken().getContent().equals("if")){
            if(getNextToken().getContent().equals("(")){
                parse_Expr();
                if(getNextToken().getContent().equals(")")){
                    if(getNextToken().getContent().equals("then")){
                        if(getNextToken().getContent().equals("{")){
                            try{
                                parse_Algorithm();
                            }catch(ParserErrorException e){
                                throw e;
                            }
                            if(getNextToken().getContent().equals("}")){
                                parse_Alternate();
                            }else{
                                String error = "Procedure parse_Branch() expected a '}' token " + "but received: " + currentToken.getContent();
                                throw new ParserErrorException(error);
                            }
                        }else{
                            String error = "Procedure parse_Branch() expected a '{' token " + "but received: " + currentToken.getContent();
                            throw new ParserErrorException(error);
                        }
                    }else{
                        String error = "Procedure parse_Branch() expected a 'then' token " + "but received: " + currentToken.getContent();
                        throw new ParserErrorException(error);
                    }
                }else{
                    String error = "Procedure parse_Branch() expected a ')' token " + "but received: " + currentToken.getContent();
                    throw new ParserErrorException(error);
                }
            }else{
                String error = "Procedure parse_Branch() expected a '(' token " + "but received: " + currentToken.getContent();
                throw new ParserErrorException(error);
            }
        }else{
            String error = "Procedure parse_Branch() expected a 'if' token " + "but received: " + currentToken.getContent();
            throw new ParserErrorException(error);
        }
    }

    public void parse_Alternate() throws ParserErrorException{
        String procdefsfollows = ";";
        if(procdefsfollows.indexOf( getNextToken().getContent()) != -1){
            return;
        }

        //getPToken();

        if(currentToken.getContent().equals("else")){
            if(getNextToken().getContent().equals("{")){
                try{
                    parse_Algorithm();
                }catch(ParserErrorException e){
                    throw e;
                }
                if(getNextToken().getContent().equals("}")){

                }else{
                    String error = "Procedure parse_ALternate() expected a '}' token " + "but received: " + currentToken.getContent();
                    throw new ParserErrorException(error);
                }
            }else{
                String error = "Procedure parse_ALternate() expected a '{' token " + "but received: " + currentToken.getContent();
                throw new ParserErrorException(error);
            }
        }else{
            String error = "Procedure parse_ALternate() expected a 'else' token " + "but received: " + currentToken.getContent();
            throw new ParserErrorException(error);
        }
    }

    public void parse_Loop() throws ParserErrorException {
        if(getNextToken().getContent().equals("do")){
            if(getNextToken().getContent().equals("{")){
                try{
                    parse_Algorithm();
                }catch(ParserErrorException e){
                    throw e;
                }
                if(getNextToken().getContent().equals("}")){
                    if(getNextToken().getContent().equals("until")){
                        if(getNextToken().getContent().equals("(")){
                            parse_Expr();
                            if(getNextToken().getContent().equals(")")){

                            }else{
                                String error = "Procedure parse_Loop() expected a ')' token " + "but received: " + currentToken.getContent();
                                throw new ParserErrorException(error);
                            }
                        }else{
                            String error = "Procedure parse_Loop() expected a '(' token " + "but received: " + currentToken.getContent();
                            throw new ParserErrorException(error);
                        }
                    }else{
                        String error = "Procedure parse_Loop() expected a 'until' token " + "but received: " + currentToken.getContent();
                        throw new ParserErrorException(error);
                    }
                }else{
                    String error = "Procedure parse_Loop() expected a '}' token " + "but received: " + currentToken.getContent();
                    throw new ParserErrorException(error);
                }
            }else{
                String error = "Procedure parse_Loop() expected a '{' token " + "but received: " + currentToken.getContent();
                throw new ParserErrorException(error);
            }
        }else if(currentToken.getContent().equals("while")){
            if(getNextToken().getContent().equals("(")){
                parse_Expr();
                if(getNextToken().getContent().equals(")")){
                    if(getNextToken().getContent().equals("do")){
                        if(getNextToken().getContent().equals("{")){
                            try{
                                parse_Algorithm();
                            }catch(ParserErrorException e){
                                throw e;
                            }
                            if(getNextToken().getContent().equals("}")){

                            }else{
                                String error = "Procedure parse_Loop() expected a '}' token " + "but received: " + currentToken.getContent();
                                throw new ParserErrorException(error);
                            }
                        }else{
                            String error = "Procedure parse_Loop() expected a '{' token " + "but received: " + currentToken.getContent();
                            throw new ParserErrorException(error);
                        }
                    }else{
                        String error = "Procedure parse_Loop() expected a 'do' token " + "but received: " + currentToken.getContent();
                        throw new ParserErrorException(error);
                    }
                }else{
                    String error = "Procedure parse_Loop() expected a ')' token " + "but received: " + currentToken.getContent();
                    throw new ParserErrorException(error);
                }
            }else{
                String error = "Procedure parse_Loop() expected a '(' token " + "but received: " + currentToken.getContent();
                throw new ParserErrorException(error);
            }
        }else{
            String error = "Procedure parse_Loop() expected a 'while' or 'do' token " + "but received: " + currentToken.getContent();
            throw new ParserErrorException(error);
        }
    }

    public void parse_LHS() throws ParserErrorException{
        if(getNextToken().getContent().equals("output")){
            return;
        }

        if(currentToken.get_Class().equals("userDefinesName")){
            if(ll_Var()){
                getPToken();
                parse_Field();
                return;
            }else{
                getPToken();
                parse_Var();
                return;
            }
        }

        //Throw an Error
        throw new ParserErrorException("Unexpected Token: "+currentToken.getContent());
    }

    public void parse_Expr() throws ParserErrorException{
        String binopgroup = "and or eq larger add sub mult";
        String unopgroup = "input not";
        if(binopgroup.indexOf( getNextToken().getContent()) != -1){
            getPToken();
            parse_BinOp();
            return;
        }else if(unopgroup.indexOf( currentToken.getContent()) != -1){
            getPToken();
            parse_UnOp();
            return;
        }else if(currentToken.get_Class().equals("userDefinesName")){
            if(ll_Var()){
                getPToken();
                try{
                    parse_Field();
                }catch(ParserErrorException e){
                    throw e;
                }
                return;
            }else{
                getPToken();
                parse_Var();
                return;
            }
        }else{
            getPToken();
            parse_Const();
            return;
        }
        //Throw an error
    }

    public void parse_PCall() throws ParserErrorException{
        if(getNextToken().getContent().equals("call")){
            if(getNextToken().get_Class().equals("userDefinedName")){

            }else{
                String error = "Procedure parse_PCall() expected a 'userDefinedName' token " + "but received: " + currentToken.get_Class();
                throw new ParserErrorException(error);
            }
        }else{
            String error = "Procedure parse_PCall() expected a 'call' token " + "but received: " + currentToken.getContent();
            throw new ParserErrorException(error);
        }
    }

    public void parse_Var() throws ParserErrorException{
        if(getNextToken().get_Class().equals("userDefinedName")){

        }else{
            String error = "Procedure parse_PCall() expected a 'userDefinedName' token " + "but received: " + currentToken.getContent();
            throw new ParserErrorException(error);
        }
    }

    public boolean ll_Var(){
        boolean ll = false;
        if(tokens[current+1].get_Class().equals("userDefinedName")){
            ll=true;
        }
        return ll;
    }

    public void parse_Field() throws ParserErrorException{
        if(getNextToken().get_Class().equals("userDefinedName")){
            if(getNextToken().getContent().equals("[")){
                if(getNextToken().get_Class().equals("userDefinedName")){
                    getPToken();
                    parse_Var();
                }else {
                    getPToken();
                    parse_Const();
                }

                if(getNextToken().getContent().equals("]")){

                }else{
                    String error = "Procedure parse_PCall() expected a ']' token " + "but received: " + currentToken.getContent();
                    throw new ParserErrorException(error);
                }
            }else{
                String error = "Procedure parse_PCall() expected a '[' token " + "but received: " + currentToken.getContent();
                throw new ParserErrorException(error);
            }
        }else{
            String error = "Procedure parse_PCall() expected a 'userDefinedName' token " + "but received: " + currentToken.get_Class();
            throw new ParserErrorException(error);
        }
    }

    public void parse_Const() throws ParserErrorException{
        if(getNextToken().get_Class().equals("ShortString")){

        }else if(currentToken.get_Class().equals("Number")){

        }else if(currentToken.getContent().equals("true")){

        }else if(currentToken.getContent().equals("false")){

        }else{
            String error = "Procedure parse_PConst() expected a 'shortString' or 'Number' or 'true' or 'false' token " + "but received: " + currentToken.getContent();
            throw new ParserErrorException(error);
        }
    }

    public void parse_UnOp() throws ParserErrorException{
        if(getNextToken().getContent().equals("input")){
            if(getNextToken().getContent().equals("(")){
                parse_Var();
                if(getNextToken().getContent().equals(")")){

                }else{
                    String error =  "Procedure parse_UnOp() expected a ')' token " + "but received: " + currentToken.getContent();
                    throw new ParserErrorException(error);
                }
            }else{
                String error =  "Procedure parse_UnOp() expected a '(' token " + "but received: " + currentToken.getContent();
                throw new ParserErrorException(error);
            }
        }else if(currentToken.getContent().equals("not")){
            if(getNextToken().getContent().equals("(")){
                try{
                   parse_Expr();
                }catch(ParserErrorException e){
                    throw e;
                }
                if(getNextToken().getContent().equals(")")){

                }else{
                    String error =  "Procedure parse_UnOp() expected a ')' token " + "but received: " + currentToken.getContent();
                    throw new ParserErrorException(error);
                }
            }else{
                String error =  "Procedure parse_UnOp() expected a '(' token " + "but received: " + currentToken.getContent();
                throw new ParserErrorException(error);
            }
        }else{
            String error =  "Procedure parse_UnOp() expected a 'input' or 'not' token " + "but received: " + currentToken.getContent();
            throw new ParserErrorException(error);
        }
    }

    public void parse_BinOp() throws ParserErrorException{
        String binopgroup = "and or eq larger add sub mult";
        if(binopgroup.indexOf( getNextToken().getContent()) != -1){
            if(getNextToken().getContent().equals("(")){
                try{
                   parse_Expr();
                }catch(ParserErrorException e){
                    throw e;
                }
                if(getNextToken().getContent().equals(",")){
                    try{
                        parse_Expr();
                    }catch(ParserErrorException e){
                        throw e;
                    }
                    if(getNextToken().getContent().equals(")")){

                    }else{
                        String error =  "Procedure parse_BinOp() expected a ')' token " + "but received: " + currentToken.getContent();
                        throw new ParserErrorException(error);
                    }
                }else{
                    String error =  "Procedure parse_BinOp() expected a ',' token " + "but received: " + currentToken.getContent();
                    throw new ParserErrorException(error);
                }
            }else{
                String error =  "Procedure parse_BinOp() expected a '(' token " + "but received: " + currentToken.getContent();
                throw new ParserErrorException(error);
            }
        }else{
            String error =  "Procedure parse_BinOp() expected a 'and' or 'or' 'eq' or 'larger' or 'add' or 'sub' or 'mult' token " + "but received: " + currentToken.getContent();
            throw new ParserErrorException(error);
        }
    }

    public void parse_VarDecl() throws ParserErrorException{
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
            throw new ParserErrorException(error);
        }

        //throw error
    }

    public void parse_Dec() throws ParserErrorException{
        if(getNextToken().getContent().equals("arr")){
            parse_TYP();
            if(getNextToken().getContent().equals("[")){
                parse_Const();
                if(getNextToken().getContent().equals("]")){
                    parse_Var();
                }else{
                    String error = "Procedure parse_Dec() expected a ']' token " + "but received: " + currentToken.getContent();
                    throw new ParserErrorException(error);
                }
            }else{
                String error = "Procedure parse_Dec() expected a '[' token " + "but received: " + currentToken.getContent();
                throw new ParserErrorException(error);
            }
        }else{
            parse_TYP();
            parse_Var();
        }
    }

    public void parse_TYP() throws ParserErrorException{
        String typ = "num bool string";
        if(typ.indexOf( getNextToken().getContent()) != -1){
            return;
        }else{
           String error = "Procedure parse_TYP() expected a 'num' or 'bool' or 'string' token " + "but received: " + currentToken.getContent();
           throw new ParserErrorException(error);
        }
    }
}
