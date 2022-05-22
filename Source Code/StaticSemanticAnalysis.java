import java.io.*;
import java.util.*;

class StaticSemanticAnalysis{
    SyNode root;
    SyNode current;

    //ArrayList<Hashtable<String, SymbolNode>> symbolTable = new ArrayList<Hashtable<String, String>>();

    public StaticSemanticAnalysis(SyNode root) {
        this.root = root;
    }

    public void findProc(String procName, SyNode root){
        if(root==null){
            return;
        }

        if(root.getContents().equals(procName)){
            current = root;
        }

        for(SyNode kid : root.getChildren()){
            findProc(procName, kid);
        }
    }

    public SyNode getCurrent(String procName){
        findProc(procName, root);
        return current;
    }
}
