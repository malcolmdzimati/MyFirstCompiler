import java.io.File;
import java.util.ArrayList;

public class Parser{
    ArrayList<Token> tokens;
    int current=0;
    Token currentToken;
    String tree="";

    public Token getNextToken(){
        current++;
        if(current==tokens.size()){
            return null;
        }
        currentToken = tokens.get(current);
        return currentToken;
    }

    public Token getPToken(){
        current--;
        currentToken = tokens.get(current);
        return currentToken;
    }

    public Parser(ArrayList<Token> tokens){
        this.tokens=tokens;
        currentToken = tokens.get(0);
    }

    public void syntaxAnalysis() throws ParserErrorException{
        parse_SPLProgr();
        System.out.println(tree);
    }

    public void parse_SPLProgr() throws ParserErrorException {
        tree+="<SPLProgr>\n";
        try{
            parse_ProcDefs();
        }catch(ParserErrorException e){
            throw e;
        }
        if(currentToken.getContent().equals("main")){
            tree+=getNextToken();
            if(currentToken.getContent().equals("{")){
                tree+=getNextToken();
                parse_Algorithm();
                if(currentToken.getContent().equals("halt")){
                    tree+=getNextToken();
                    if(currentToken.getContent().equals(";")){
                        tree+=getNextToken();
                        parse_VarDecl();
                        if(currentToken.getContent().equals("}")){
                            tree+=getNextToken();
                            tree+="</SPLProgr>";
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
        String tk = currentToken.getContent();
        if(tk.equals("output") || tk.equals("call") || tk.equals("main") || tk.equals("return") || tk.equals("if") || tk.equals("do") || tk.equals("while") || currentToken.get_Class().equals("userDefinedName")){

            return;
        }

        parse_PD();

        if(currentToken.getContent().equals(",")){
            getNextToken();
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
        if(currentToken.getContent().equals("proc")){
            getNextToken();
            if(currentToken.get_Class().equals("userDefinedName")){
                getNextToken();
                if(currentToken.getContent().equals("{")){
                    getNextToken();
                    try{
                        parse_ProcDefs();
                        parse_Algorithm();
                    }catch(ParserErrorException e){
                        throw e;
                    }
                    if(currentToken.getContent().equals("return")){
                        getNextToken();
                        if(currentToken.getContent().equals(";")){
                            getNextToken();
                            parse_VarDecl();
                            if(currentToken.getContent().equals("}")){
                                getNextToken();
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
        String  tk =currentToken.getContent();
        if(tk.equals("halt") || tk.equals("return") || tk.equals("}")){
            //getNextToken();
            return;
        }

        parse_Instr();

        if(currentToken.getContent().equals(";")){
            getNextToken();
            parse_Algorithm();
        }else{
            String error = "Procedure parse_Algorithm() expected a ';' token " + "but received: " + currentToken.getContent();
            throw new ParserErrorException(error);
        }
        //Throw Error
        //throw new ParserErrorException("Unexpected Token receieved: "+currentToken.getContent());
    }

    public void parse_Instr() throws ParserErrorException{
        if(currentToken.getContent().equals("if")){
            try{
                parse_Branch();
            }catch (ParserErrorException e){
                throw e;
            }
        }else if(currentToken.getContent().equals("do") || currentToken.getContent().equals("while")){
            try{
                parse_Loop();
            }catch (ParserErrorException e){
                throw e;
            }
        }else if(currentToken.getContent().equals("call")){
            parse_PCall();
        }else{
            parse_Assign();
        }
    }

    public void parse_Assign() throws ParserErrorException{
        parse_LHS();
        if(currentToken.getContent().equals(":=")){
            getNextToken();
            parse_Expr();
        }else{
            String error = "Procedure parse_Assign() expected a ':=' token " + "but received: " + currentToken.getContent();
            throw new ParserErrorException(error);
        }
    }

    public void parse_Branch() throws ParserErrorException{
        if(currentToken.getContent().equals("if")){
            getNextToken();
            if(currentToken.getContent().equals("(")){
                getNextToken();
                parse_Expr();
                if(currentToken.getContent().equals(")")){
                    getNextToken();
                    if(currentToken.getContent().equals("then")){
                        getNextToken();
                        if(currentToken.getContent().equals("{")){
                            getNextToken();
                            try{
                                parse_Algorithm();
                            }catch(ParserErrorException e){
                                throw e;
                            }
                            if(currentToken.getContent().equals("}")){
                                getNextToken();
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
        if(procdefsfollows.indexOf( currentToken.getContent()) != -1){
            //getNextToken();
            return;
        }

        //getPToken();

        if(currentToken.getContent().equals("else")){
            getNextToken();
            if(currentToken.getContent().equals("{")){
                getNextToken();
                try{
                    parse_Algorithm();
                }catch(ParserErrorException e){
                    throw e;
                }
                if(currentToken.getContent().equals("}")){
                    getNextToken();
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
        if(currentToken.getContent().equals("do")){
            getNextToken();
            if(currentToken.getContent().equals("{")){
                getNextToken();
                parse_Algorithm();
                if(currentToken.getContent().equals("}")){
                    getNextToken();
                    if(currentToken.getContent().equals("until")){
                        getNextToken();
                        if(currentToken.getContent().equals("(")){
                            getNextToken();
                            parse_Expr();
                            if(currentToken.getContent().equals(")")){
                                getNextToken();
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
                    String error = "Procedure parse_Loop() expected a '}' token " + "but received: " + currentToken.getContent() +" class:" + currentToken.get_Class();
                    throw new ParserErrorException(error);
                }
            }else{
                String error = "Procedure parse_Loop() expected a '{' token " + "but received: " + currentToken.getContent();
                throw new ParserErrorException(error);
            }
        }else if(currentToken.getContent().equals("while")){
            getNextToken();
            if(currentToken.getContent().equals("(")){
                getNextToken();
                parse_Expr();
                if(currentToken.getContent().equals(")")){
                    getNextToken();
                    if(currentToken.getContent().equals("do")){
                        getNextToken();
                        if(currentToken.getContent().equals("{")){
                            getNextToken();
                            try{
                                parse_Algorithm();
                            }catch(ParserErrorException e){
                                throw e;
                            }
                            if(currentToken.getContent().equals("}")){
                                getNextToken();
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
        if(currentToken.getContent().equals("output")){
            getNextToken();
            return;
        }

        if(currentToken.get_Class().equals("userDefinedName")){
            if(ll_Var()){
                parse_Field();
                return;
            }else{
                parse_Var();
                return;
            }
        }

        //Throw an Error
        throw new ParserErrorException("Unexpected Token: "+currentToken.getContent());
    }

    public void parse_Expr() throws ParserErrorException{
        String tk = currentToken.getContent();
        if(tk.equals("sub") || tk.equals("and") || tk.equals("or") || tk.equals("eq") || tk.equals("larger") || tk.equals("add") || tk.equals("mult")){
            parse_BinOp();
            return;
        }else if(tk.equals("input") || tk.equals("not")){
            parse_UnOp();
            return;
        }else if(currentToken.get_Class().equals("userDefinedName")){
            if(ll_Var()){
                try{
                    parse_Field();
                }catch(ParserErrorException e){
                    throw e;
                }
            }else{
                parse_Var();
                return;
            }
        }else{
            parse_Const();
            return;
        }
        //Throw an error
    }

    public void parse_PCall() throws ParserErrorException{
        if(currentToken.getContent().equals("call")){
            getNextToken();
            if(currentToken.get_Class().equals("userDefinedName")){
                getNextToken();
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
        if(currentToken.get_Class().equals("userDefinedName")){
            getNextToken();
        }else{
            String error = "Procedure parse_PCall() expected a 'userDefinedName' token " + "but received: " + currentToken.getContent();
            throw new ParserErrorException(error);
        }
    }

    public boolean ll_Var(){
        boolean ll = false;
        if(tokens.get(current+1).getContent().equals("[")){
            ll=true;
        }
        return ll;
    }

    public void parse_Field() throws ParserErrorException{
        if(currentToken.get_Class().equals("userDefinedName")){
            getNextToken();
            if(currentToken.getContent().equals("[")){
                getNextToken();
                if(currentToken.get_Class().equals("userDefinedName")){
                    parse_Var();
                }else {
                    parse_Const();
                }
                if(currentToken.getContent().equals("]")){
                    getNextToken();
                }else{
                    String error = "Procedure parse_Field() expected a ']' token " + "but received: " + currentToken.getContent();
                    throw new ParserErrorException(error);
                }
            }else{
                String error = "Procedure parse_Field() expected a '[' token " + "but received: " + currentToken.getContent();
                throw new ParserErrorException(error);
            }
        }else{
            String error = "Procedure parse_Field() expected a 'userDefinedName' token " + "but received: " + currentToken.get_Class();
            throw new ParserErrorException(error);
        }
    }

    public void parse_Const() throws ParserErrorException{
        if(currentToken.get_Class().equals("ShortString")){
            getNextToken();
        }else if(currentToken.get_Class().equals("Number")){
            getNextToken();
        }else if(currentToken.getContent().equals("true")){
            getNextToken();
        }else if(currentToken.getContent().equals("false")){
            getNextToken();
        }else{
            String error = "Procedure parse_Const() expected a 'shortString' or 'Number' or 'true' or 'false' token " + "but received: " + currentToken.getContent() + " Class: " + currentToken.get_Class();
            throw new ParserErrorException(error);
        }
    }

    public void parse_UnOp() throws ParserErrorException{
        if(currentToken.getContent().equals("input")){
            getNextToken();
            if(currentToken.getContent().equals("(")){
                getNextToken();
                parse_Var();
                if(currentToken.getContent().equals(")")){
                    getNextToken();
                }else{
                    String error =  "Procedure parse_UnOp() expected a ')' token " + "but received: " + currentToken.getContent();
                    throw new ParserErrorException(error);
                }
            }else{
                String error =  "Procedure parse_UnOp() expected a '(' token " + "but received: " + currentToken.getContent();
                throw new ParserErrorException(error);
            }
        }else if(currentToken.getContent().equals("not")){
            getNextToken();
            if(currentToken.getContent().equals("(")){
                getNextToken();
                try{
                   parse_Expr();
                }catch(ParserErrorException e){
                    throw e;
                }
                if(currentToken.getContent().equals(")")){
                    getNextToken();
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
        String tk = currentToken.getContent();
        if(tk.equals("sub") || tk.equals("and") || tk.equals("or") || tk.equals("eq") || tk.equals("larger") || tk.equals("add") || tk.equals("mult")){
            getNextToken();
            if(currentToken.getContent().equals("(")){
                getNextToken();
                try{
                   parse_Expr();
                }catch(ParserErrorException e){
                    throw e;
                }
                if(currentToken.getContent().equals(",")){
                    getNextToken();
                    try{
                        parse_Expr();
                    }catch(ParserErrorException e){
                        throw e;
                    }
                    if(currentToken.getContent().equals(")")){
                        getNextToken();
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
        if(varDeclf.indexOf( currentToken.getContent()) != -1){
            //getNextToken();
            return;
        }

        parse_Dec();

        if(currentToken.getContent().equals(";")){
            getNextToken();
            parse_VarDecl();
        }else{
            String error = "Procedure parse_VarDecl() expected a ';' token " + "but received: " + currentToken.getContent();
            throw new ParserErrorException(error);
        }

        //throw error
    }

    public void parse_Dec() throws ParserErrorException{
        if(currentToken.getContent().equals("arr")){
            getNextToken();
            parse_TYP();
            if(currentToken.getContent().equals("[")){
                getNextToken();
                parse_Const();
                if(currentToken.getContent().equals("]")){
                    getNextToken();
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
        String tk = currentToken.getContent();
        if(tk.equals("num") || tk.equals("bool") || tk.equals("string")){
            getNextToken();
            return;
        }else{
           String error = "Procedure parse_TYP() expected a 'num' or 'bool' or 'string' token " + "but received: " + currentToken.getContent();
           throw new ParserErrorException(error);
        }
    }
}
