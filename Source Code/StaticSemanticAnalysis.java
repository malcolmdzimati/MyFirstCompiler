import java.io.*;
import java.util.*;

class StaticSemanticAnalysis{
    SymbolNode root;
    SymbolNode current;
    int currentSc;

    ArrayList<Hashtable<String, SymbolNode>> symbolTableVar = new ArrayList<Hashtable<String, SymbolNode>>();
    ArrayList<Hashtable<String, SymbolNode>> symbolTableFunc = new ArrayList<Hashtable<String, SymbolNode>>();
    Queue<SymbolNode> que=new LinkedList<>();

    public StaticSemanticAnalysis(SymbolNode root) {
        this.root = root;
    }

    public SymbolNode findProc(SymbolNode n, String procName)  {
        que.clear();
        que.add(n);       /* root node is added to the top of the queue */
        while (que.size() != 0){
            n = que.remove();
            for (SymbolNode kid : n.getChildren()) {
                if(kid.getValue().equals(procName)){
                    return kid.getParent();
                }else{
                    que.add(kid);
                }
            }
        }
        return null;
    }

    public SymbolNode findVar(SymbolNode n, String procName)  {
        que.clear();
        que.add(n);       /* root node is added to the top of the queue */
        while (que.size() != 0){
            n = que.remove();
            for (SymbolNode kid : n.getChildren()) {
                if(kid.getType().equals(procName)){
                    return kid;
                }else{
                    que.add(kid);
                }
            }
        }
        return null;
    }

    public SymbolNode getProc(String procName){
        findProc(root, procName);
        return current;
    }

    public void performScopeResolution(){
        SymbolNode man = findProc(root, "main");
        makeScopeVarSymbolTable(man, 0);

        SymbolNode tt = findVar(root, "Algorithm");
        for(SymbolNode k : tt.getChildren()){
            //System.out.println(k.getChildren().get(0).getValue()+" "+k.getType());
        }
        makeFunctionEntry(tt, 0);
    }

    public void makeScopeVarSymbolTable(SymbolNode beginProc, int scID){
        SymbolNode beginVar = findVar(beginProc, "VarDecl");
        makeVaraibleEntry(beginVar, scID);
    }

    public void makeVaraibleEntry(SymbolNode beg, int scID){
        if(beg.getChildren().isEmpty()){
            return;
        }else if (beg.getChildren().size()==2){
                SymbolNode dec = beg.getChildren().get(0);
                String type = dec.getChildren().get(0).getChildren().get(0).getValue();
                String value = dec.getChildren().get(1).getChildren().get(0).getValue();

                //System.out.println(type + " " +value);

                for(SymbolNode k : dec.getChildren()){
                    //System.out.println(k.getChildren().get(0).getValue()+" "+k.getType());
                }

                //makeVaraibleEntry(beg.getChildren().get(1), scID);
        }else{

        }
    }

    public  void makeFunctionEntry(SymbolNode beg, int scID){
        if(beg.getType().equals("Branch")

        }

        if (beg.getType().equals("Loop")){

        }

        if(beg.getType().equals("PD")){

        }

        if(beg.getValue().equals("pcall")){
            scopeFC(beg.getChildren().get(1).getValue());
        }
    }
}
