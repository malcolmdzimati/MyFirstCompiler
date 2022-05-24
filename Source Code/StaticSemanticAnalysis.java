import java.io.*;
import java.util.*;

class StaticSemanticAnalysis{
    SymbolNode root;
    int current=-1;
    SymbolNode prevFun;
    String allErrors="";

    ArrayList<Hashtable<String, SymbolNode>> symbolTableVar = new ArrayList<Hashtable<String, SymbolNode>>();
    ArrayList<Hashtable<String, SymbolNode>> symbolTableFunc = new ArrayList<Hashtable<String, SymbolNode>>();
    Queue<SymbolNode> que=new LinkedList<>();

    public StaticSemanticAnalysis(SymbolNode root) {
        this.root = root;
        prevFun = root;
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

    public void performScopeResolution(){
        enterNewScope();
        SymbolNode man = findProc(root, "main");
        makeScopeVarSymbolTable(man);

        //SymbolNode tt = findVar(root, "Algorithm");
        //for(SymbolNode k : tt.getChildren()){
            //System.out.println(k.getChildren().get(0).getValue()+" "+k.getType());
        //}
        makeFunctionEntry(root.getChildren().get(0));
    }

    public void makeScopeVarSymbolTable(SymbolNode beginProc){
        SymbolNode beginVar = findVar(beginProc, "VarDecl");
        makeVaraibleEntry(beginVar);
    }

    public void makeVaraibleEntry(SymbolNode beg){
        if(beg.getChildren().isEmpty()){
            return;
        }

        if (beg.getChildren().size()==2){
                SymbolNode dec = beg.getChildren().get(0);
                if(dec.getChildren().size()==2){
                    String type = dec.getChildren().get(0).getChildren().get(0).getValue();
                    SymbolNode var = dec.getChildren().get(1).getChildren().get(0);
                    var.setType(type);
                    var.setScopeID(current+"");

                    if(!getCurrentScA().containsKey(var.getValue())){
                        getCurrentScA().put(var.getValue(), dec.getChildren().get(1));
                    }else{
                        String error = "[SEMANTIC ERROR]Conflicting Declaration, varaible has already been delcared";
                    }

                    for(SymbolNode k : beg.getChildren()){
                        //System.out.println(k.getChildren().get(0).getValue()+" "+k.getType());
                    }
                }else{
                    for(SymbolNode k : dec.getChildren()){
                        //System.out.println(k.getValue()+" "+k.getType());
                    }
                    String type = dec.getChildren().get(0).getValue()+"["+dec.getChildren().get(1).getChildren().get(0).getValue()+"]";
                    SymbolNode mid = dec.getChildren().get(2).getChildren().get(0);
                    SymbolNode var = dec.getChildren().get(3).getChildren().get(0);
                    var.setType(type);
                    var.setScopeID(current+"");
                    var.setMid(mid);
                    var.setValue(var.getValue()+"["+mid.getType()+"]");

                    //System.out.println(var.getType()+" "+var.getValue());
                    if(!getCurrentScA().containsKey(var.getValue())){
                        getCurrentScA().put(var.getValue(), dec.getChildren().get(1));
                    }else{
                        String error = "[SEMANTIC ERROR]Conflicting Declaration, varaible has already been delcared";
                        System.out.println(error);
                    }
                }

                makeVaraibleEntry(beg.getChildren().get(1));
        }
    }

    public  void makeFunctionEntry(SymbolNode beg){
        if(beg.getType().equals("Branch")){
            enterNewScope();
            //scopeBranch(beg);
        }

        if (beg.getType().equals("Loop")){
            enterNewScope();
            scopeLoop(beg);
        }

        if(beg.getType().equals("ProcDefs")){
            enterNewScope();
            scopeFC(beg);
        }

        if(beg.getValue().equals("pcall")){
            SymbolNode called = beg.getChildren().get(1);
            called.used();
            if(!getCurrentScF().containsKey(called.getValue()) && !called.getValue().equals(prevFun.getValue())){
                String error = "[APPL-DECL ERROR] Function '"+called.getValue()+"' is not within Scope";
            }
            enterNewScope();
            //scopeFC(beg.getChildren().get(1).getValue());
        }

    }

    public void scopeLoop(SymbolNode beg){
        for(SymbolNode k : beg.getChildren().get(0).getChildren()){
            System.out.println(k.getValue()+" "+k.getType());
        }
    }

    public SymbolNode checkExistanceVar(SymbolNode th){
        for(int i=0; i<current; i++){
            if(symbolTableVar.get(current-i).contains(th.getValue())){
                return symbolTableVar.get(current-i).get(th.getValue());
            }
        }
        String error = "[APPL-DECL ERROR] Variable: '"+th.getValue()+"' was never declared.";
        return null;
    }

    public void scopeFC(SymbolNode beg){
        if(beg.getChildren().isEmpty())
            return;

        makeScopeVarSymbolTable(beg);

        SymbolNode func = beg.getChildren().get(0).getChildren().get(1);
        func.setType("UserDefinedProcedure");
        func.setScopeID((current-1)+"");

        if(func.getValue().equals("main")){
            String error = "[SEMANTIC ERROR]User Defined Procedure can not be named 'main'";
            //error
        }

        if(func.getValue().equals(prevFun.getValue())){
            String error = "[SEMANTIC ERROR]Cannot define a child-procedure with the same name as parent procedure.";
        }

        if(symbolTableFunc.containsKey(func.getValue())){
            String error = "[SEMANTIC ERROR]No two children procedures of the same scope can have the same name: '"+func.getValue()+"' already exits";
        }else{
            symbolTableFunc.get(current-1).put(func.getValue(), func);
        }
        //System.out.println(func.getValue()+" "+func.getType()+" "+func.getScopeID());

        prevFun = func;
        makeFunctionEntry(beg.getChildren().get(0).getChildren().get(2));
        revertScope();

        for(SymbolNode k : beg.getChildren().get(0).getChildren()){
            //System.out.println(k.getValue()+" "+k.getType());
        }

        scopeFC(beg.getChildren().get(1));
    }

    public void enterNewScope(){
        current++;
        symbolTableVar.add(new Hashtable<>());
        symbolTableFunc.add(new Hashtable<>());
    }

    public Hashtable<String, SymbolNode> getCurrentScF(){
        return symbolTableFunc.get(current);
    }

    public Hashtable<String, SymbolNode> getCurrentScA(){
        return symbolTableVar.get(current);
    }

    public void revertScope(){
        current--;
    }

    public SymbolNode lookUp(boolean isFunc, String value){
        if(isFunc){
            for(int i=0; i<=current; i++){
                if(symbolTableFunc.get(current-i).containsKey(value))
                    return symbolTableFunc.get(current).get(value);
            }
        }

        for(int i=0; i<=current; i++){
            if(symbolTableVar.get(current-i).containsKey(value))
                return symbolTableVar.get(current).get(value);
        }

        return null;
    }
}
