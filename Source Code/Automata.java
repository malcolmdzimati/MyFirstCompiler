import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Automata {

    private enum State{
        //Defines Different states and defines them as accepting or not:
        S1(false), S2(true), S3(true), S4(true), S5(false), S6(false), S7(false), S8(false), S9(false), S10(false), S11(false), S12(false), S13(false), S14(false), S15(false), S16(false), S17(false), S18(false), S19(false), S20(false), S21(true), S22(true), S23(false), S24(false);

        //Parameter that defines whether a state is accepting or not:
        final boolean accept;

        //Construct for states:
        State(boolean accept){
            this.accept = accept;
         }

        //define Alphabet:
        State colon;
        State symbols;
        State quotation;
        State nNumbers;
        State negative;
        State wNumber;
        State smallCap;
        State bigCap;
        State space;
        State eq;

        //Define Transition States:
    }
}
