import java.util.ArrayList;

class SyNode{
    String scID;
    SyNode parent;
    String _class;
    String content;
    ArrayList<SyNode> children = new ArrayList<SyNode>();
    int currentID;
    String tkID;

    public SyNode(SyNode par, String tkId, String scId, String _class, String content){
        int currentID = 0;
        parent = par;

        this.tkID = tkId;
        this.scID = scId;
        this._class = _class;
        this.content = content;
    }

    public void addChild(SyNode k){
        children.add(k);
        currentID++;
    }

    public void setTKID(String tk){
        tkID = tk;
    }

    public String getTKID(){
        return tkID;
    }

    public void setSCID(String sc){
        scID = sc;
    }

    public String getSCID(){
        return scID;
    }

    public SyNode getParent(){
        return parent;
    }

    public String getClassName(){
        return _class;
    }

    public String getContents(){
        return content;
    }

    public String getLevel(){
        return scID;
    }

    public ArrayList<SyNode> getChildren(){
        return children;
    }
}
