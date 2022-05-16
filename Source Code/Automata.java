import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

interface State {
    public State next(Token word);
    String colon=":";
    String symbols="0{}()[],;";
    String smallAlpha="abcdefghijklmnopqrstuvwxyz";
    String bigAlpha="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    String wholeNumbers="0123456789";
    String naturalNumbers="123456789";
    String quotation="\"";
    String blank=" ";
    String assignment="=";
    String negative="-";
}

interface FinalState extends State {
}


enum AcceptingState implements FinalState {
    Accept {
        @Override
        public State next(Token word) {
            return Accept;
        }
    };
}

enum FailingState implements FinalState {
    Fail {
        @Override
        public State next(Token word) {
            return Fail;
        }
    };
}

enum States implements State {
    A {
        @Override
        public State next(Token word) {
            if (word.hasNext()) {
                char c = word.read();

                if(colon.indexOf(c)!=-1){
                    return B;
                }else if(symbols.indexOf(c)!=-1){
                    return C;
                }else if(smallAlpha.indexOf(c)!=-1){
                    //word.set_Class("userDefinedNames");
                    return D;
                }else if(quotation.indexOf(c)!=-1){
                    return E;
                }else if(naturalNumbers.indexOf(c)!=-1){
                    word.set_Class("Number");
                    return F;
                }else if(negative.indexOf(c)!=-1){
                    word.set_Class("Number");
                    return G;
                }
            }
            return FailingState.Fail;
        }
    },

    B {
        @Override
        public State next(Token word) {
            if (word.hasNext()) {
                if(assignment.indexOf(word.read())!=-1){
                    return C;
                }
            }
            return FailingState.Fail;
        }
    },

    C {
        @Override
        public State next(Token word) {
            return AcceptingState.Accept;
        }
    },

    D {
        @Override
        public State next(Token word) {
            if(word.hasNext()){
                char c = word.read();

                if(smallAlpha.indexOf(c)!=-1){
                    return D;
                }else if(wholeNumbers.indexOf(c)!=-1){
                    return D;
                }else{
                    return FailingState.Fail;
                }
            }
            return AcceptingState.Accept;
        }
    },

    E {
        @Override
        public State next(Token word) {
            if(word.hasNext()){
                char c = word.read();

                if(wholeNumbers.indexOf(c)!=-1){
                    return E;
                }else if(bigAlpha.indexOf(c)!=-1){
                    return E;
                }else if(blank.indexOf(c)!=-1){
                    return E;
                }else if(quotation.indexOf(c)!=-1){
                    return C;
                }
            }
            return FailingState.Fail;
        }
    },

    F {
        @Override
        public State next(Token word) {
            if(word.hasNext()){
                if(wholeNumbers.indexOf(word.read())!=-1){
                    return F;
                }else{
                    return FailingState.Fail;
                }
            }
            return AcceptingState.Accept;
        }
    },

    G {
        @Override
        public State next(Token word) {
            if(word.hasNext()){
                if(naturalNumbers.indexOf(word.read())!=-1){
                    return F;
                }else{
                    return FailingState.Fail;
                }
            }
            return AcceptingState.Accept;
        }
    };

    public abstract State next(Token word);
}

public class Automata {
    public boolean check(Token token){
        State s;

        for(s=States.A; !(s instanceof FinalState); s = s.next(token)){

        }
        return s.toString() == "Accept";
    }
}
