import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Automata {

    /*private enum State{
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
        State wNumbers;
        State smallCap;
        State bigCap;
        State space;
        State eq;

        static{
            //Defines all Transition States:
            /**S1.colon=s2; S1.symbols=S3; S1.smallCap=S4; S1.quotation=S5, S1.nNumbers=S21; S1.negative=S23;          //Transitions From S1
            S2.assignment=S3;                                                                                       //Transitions from S2
            S3;                                                                                                     //Transitions from S3
            S4.smallCap=S4; S4.wNumber=S4;                                                                          //Transitions from S4
            S5.wNumbers=S6; S5.bigCap=S6; S5.space=S6; S5.quotation=S22;                                            //Transitions from S5
            S6.wNumbers=S7; S6.bigCap=S7; S6.space=S7; S6.quotation=S22;                                            //Transitions from S6
            S7.wNumbers=S8; S7.bigCap=S8; S7.space=S8; S7.quotation=S22;                                            //Transitions from S7
            S8.wNumbers=S9; S8.bigCap=S9;                                                                           //Transitions from S8
            S9.wNumbers=S10; S9.bigCap=S10;                                                                         //Transitions from S9
            S10.wNumbers=S11; S10.bigCap=S11;                                                                       //Transitions from S10
            S11.wNumbers=S12; S11.bigCap=S12;                                                                       //Transitions from S11
            S12.wNumbers=S13; S12.bigCap=S13;                                                                       //Transitions from S12
            S13.wNumbers=S14;                                                                                       //Transitions from S13
            S14.wNumbers=S15;                                                                                       //Transitions from S14
            S15.wNumbers=S16;                                                                                       //Transitions from S15
            S16.wNumbers=S17;                                                                                       //Transitions from S16
            S17.wNumbers=S18;                                                                                       //Transitions from S17
            S18.wNumbers=S19;                                                                                       //Transitions from S18
            S19.wNumbers=S20;                                                                                       //Transitions from S19
            S20.quotation=S21;                                                                                      //Transitions from s20
            S21.wNumbers=S21;                                                                                       //Transitions from S21
            S22;                                                                                                    //Transitions from S22
            S23.nNumbers=S21;                                                                                 //Transitions frm S23
        }

        //Defines a single transition from a state to another state
        State transition(char ch){
            char colon=':';
           // char quotation='\';
            char space=' ';
            char eq='=';
            char negative='-';
            String symbols="0{}()[],;";
            String smallCap="qwertyuiopasdfghjklzxcvbnm";
            String bigCap="QWERTYUIOPASDFGHJKLZXCVBNM";
            String wNumbers="0987654321";
            String nNumbers="123456789";

    }*/
}
