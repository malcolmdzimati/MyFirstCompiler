import java.util.ArrayList;

class SymbolNode{
    String scopeID;           //Scope
    SymbolNode parent;
    String type;              //Userdefined Function or type of var or array
    String value;             //value is like the name of var or func
    ArrayList<SymbolNode> children = new ArrayList<SymbolNode>();
    int currentID;           //also not used yet
    String symbolID;         //Low-key not used for now, value is the key,
    SymbolNode mid;          //Attribute for arrs
    boolean hasBeenUsed=false;

    public SymbolNode(SymbolNode par, String symbolId, String scopeId, String type, String value){
        currentID = 0;
        parent = par;

        this.symbolID = symbolId;
        this.scopeID = scopeId;
        this.type = type;
        this.value = value;
    }

    public void used(){
        hasBeenUsed=true;
    }

    public void addChild(SymbolNode k){
        children.add(k);
        currentID++;
    }

    public void setSymbolID(String id){
        symbolID = id;
    }

    public void setMid(SymbolNode m){
        mid = m;
    }

    public String getSymbolID(){
        return symbolID;
    }

    public void setScopeID(String id){
        scopeID = id;
    }

    public String getScopeID(){
        return scopeID;
    }

    public SymbolNode getParent(){
        return parent;
    }

    public String getType(){
        return type;
    }

    public void setType(String tp){
        type = tp;
    }

    public String getValue(){
        return value;
    }

    public void setValue(String v){
        value = v;
    }

    public ArrayList<SymbolNode> getChildren(){
        return children;
    }
}
