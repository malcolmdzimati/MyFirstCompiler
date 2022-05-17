

public class Lexer{
    String[] code;
    int codeSize;
    Token[] tokens = new Token[10000];
    int tokenLen=0;
    Automata DFA = new Automata();

    public Lexer(String[] code, int size){
        this.code = code;
        codeSize = size;
    }

    public Token[] getTokens(){
        return tokens;
    }

    public int getSize(){
        return tokenLen;
    }

    public void printToken(){
        /*LexicalAnalysis();
        for(int i=0; i<tokenLen; i++){
            System.out.print("Accept: ");
            System.out.print(tokens[i]);
        }*/
    }

    public void lexicalAnalysis(){
        for(int i=0; i<tokenLen; i++){
            try{
                DFA.check(tokens[i]);
                //System.out.println(tokens[i]);
            }catch (LexerErrorException e){
               System.err.println(e);
            }
        }
    }


    public void tokenize(){
        String ml = ":{}[],;()";
        String qm = "\"";
        int numToken=0;

        for(int i=0; i<codeSize; i++){
            String tk = "";
            boolean nBetweenB = false;

            if(nBetweenB){
                //Throw Error
            }

            for(int j=0; j<code[i].length(); j++){
                char ct = code[i].charAt(j);

                if(nBetweenB){ //In between ShortString
                    tk+=ct;
                    if(qm.indexOf(ct)!=-1){
                        nBetweenB=false;
                        if(tk.length()>17){
                            //Throw Error
                        }else{
                            tokens[numToken] = new Token(numToken, "ShortString", tk);
                            numToken++;
                            tk="";
                        }
                    }
                }else if(ct==' ' || ct=='\t' || ct=='\r'){//Not in ShortString and breakage of Line
                    if(tk.length()!=0){
                        tokens[numToken] = new Token(numToken, "userDefinedName", tk);
                        tk="";
                        numToken++;
                    }
                }else if(ml.indexOf(ct) != -1 || qm.indexOf(ct) != -1){//Things that don't need a space between the character
                    if(tk.length()!=0){
                        tokens[numToken] = new Token(numToken, "userDefinedName", tk);
                        tk="";
                        numToken++;
                    }
                    //System.out.println("debug: "+ct);

                    if(qm.indexOf(ct)!=-1){//If It is a Quotation mark
                        //First Quotation Mark
                        nBetweenB=true;
                        tk="\"";
                        //numToken--;
                    }else if(ml.indexOf(ct)==0){
                        if(code[i].charAt(j+1) == '='){
                            tokens[numToken] = new Token(numToken, "Assign", ":=");
                            tk="";
                            numToken++;
                            j++;
                        }else{
                            //Throw Error
                        }
                    }else{
                        tokens[numToken] = new Token(numToken, "Separator", ct+"");
                        numToken++;
                    }
                }else{
                    //if(ct!='\r'){
                        tk+=ct;
                    //}
                }
            }
            if(tk.length()!=0){
                tokens[numToken] = new Token(numToken, "userDefinedName", tk);
                numToken++;
            }
        }
        tokenLen = numToken;
    }

}
