import java.io.*;
import java.util.*;

class StaticSemanticAnalysis{
    SymbolNode root;
    SymbolNode current;
    int currentSc;

    ArrayList<Hashtable<String, SymbolNode>> symbolTableVar = new ArrayList<Hashtable<String, SymbolNode>>();
    ArrayList<Hashtable<String, SymbolNode>> symbolTableFunc = new ArrayList<Hashtable<String, SymbolNode>>();

    public StaticSemanticAnalysis(SymbolNode root) {
        this.root = root;
    }

    /*public void findProc(String procName, SyNode rot, boolean isFound){
        if(rot==null){
            return;
        }

        if(rot.getContents().equals(procName) && !isFound){
            current = rot.getParent();
            if(rot.getClassName().equals(rot.getContents())){
                current = rot;
                //System.out.println(rot.getChildren().;
            }
            //System.out.println(current.getChildren())i;
            isFound = true;
            return;
        }

        for(SyNode kid : rot.getChildren()){
            //System.out.println(kid);
            findProc(procName, kid, isFound);
        }
    }

    public SyNode getProc(String procName){
        findProc(procName, root);
        return current;
    }

    public void performScopeResolution(){
        findProc("main", root, false);
        SyNode man = current;
        makeScopeSymbolTable(man, 0);
    }

    public void makeScopeSymbolTable(SyNode beginProc, int scID){
        boolean isFound = false;
        findProc("VarDecl", beginProc, isFound);
        SyNode beginVar = current;
        makeVaraibleTable(beginVar, scID);
    }

    public void makeVaraibleTable(SyNode beg, int scID){
        ArrayList<SyNode> kids = beg.getChildren();

        for(SyNode kid : kids){
            System.out.println(kid.getContents());
        }

        if(kids.size()==1){
            System.out.println("boom");
            return;
        }

        SyNode dec = kids.get(0);
        int i=0;
        boolean isrr = false;

        if(dec.getChildren().get(0).getContents().equals("arr")){
            i++;
            isrr=true;
        }else{

        }


        //String type = dec.getChildren().get(0).get(0).getContents();
        //String var = dec.getChildren().get(1).get(0).getContents();

        System.out.println(dec.getChildren().get(0).getContents());

        //SymbolNodeVar node = new SymbolNodeVar(type, var, scID+"");

        makeVaraibleTable(kids.get(2), scID);
    }*/
}
