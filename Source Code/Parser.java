import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Parser{
    ArrayList<Token> tokens;
    int current=0;
    Token currentToken;
    String filename;
    int DEPTH_XML;
    SyNode root;

    DocumentBuilderFactory dbFactory;
    DocumentBuilder dBuilder;
    Document doc;

    private void printNode(NodeList nodeList, int level, SyNode parent){
        level++;

      if (nodeList != null && nodeList.getLength() > 0) {
          for (int i = 0; i < nodeList.getLength(); i++) {
              Node node = nodeList.item(i);
              if (node.getNodeType() == Node.ELEMENT_NODE) {
                  Element e = (Element)node;
                  String tkid = e.getAttribute("ID");
                  String _class = node.getNodeName();
                  String content = e.getAttribute("Content");

                  SyNode kid = new SyNode(parent, tkid, level+"", _class, content);
                  parent.addChild(kid);

                  String result = String.format("%" + level * 5 + "s : [%s]%n", node.getNodeName()+" "+e.getAttribute("Content"), level);
                  //System.out.print(result);

                  printNode(node.getChildNodes(), level, kid);

                  // how depth is it?
                  if (level > DEPTH_XML) {
                      DEPTH_XML = level;
                  }

              }

          }
      }
    }

    public Token getNextToken(Element pr){
        Element terminal = doc.createElement(currentToken.get_Class());
        pr.appendChild(terminal);
        Attr id = doc.createAttribute("ID");
        terminal.setAttributeNode(id);
        Attr content = doc.createAttribute("Content");
        terminal.setAttributeNode(content);
        id.setValue(currentToken.getID());
        content.setValue(currentToken.getContent());

        current++;
        if(current==tokens.size()){
            return null;
        }
        currentToken = tokens.get(current);
        return currentToken;
    }

    public Token getPToken(){
        current--;
        currentToken = tokens.get(current);
        return currentToken;
    }

    public Parser(ArrayList<Token> tokens, String filename){
        this.tokens=tokens;
        currentToken = tokens.get(0);
        this.filename = filename;
    }

    public void apNul(Element pr){
        Element nul = doc.createElement("Nullable");
        pr.appendChild(nul);
        Attr descr = doc.createAttribute("Descitpion");
        nul.setAttributeNode(descr);
        descr.setValue("This Tag Represents Epsilon");
    }

    /*public void appendTerminal(Element pr){
        Element terminal = doc.createElement(currentToken.get_Class());
        pr.appendChild(terminal);
        Attr id = doc.createAttribute("ID");
        terminal.setAttributeNode(id);
        Attr content = doc.createAttribute("Content");
        terminal.setAttributeNode(content);
        id.setValue(currentToken.getID());
        content.setValue(currentToken.getContent());
    }*/

    public void syntaxAnalysis() throws Exception{
        filename = filename.replace(".txt", "TreeResult.xml");
        dbFactory = DocumentBuilderFactory.newInstance();
        dBuilder = dbFactory.newDocumentBuilder();
        doc = dBuilder.newDocument();
        parse_SPLProgr();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(filename));
        transformer.transform(source, result);

         DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

         try (InputStream is = new FileInputStream(filename)) {
             DocumentBuilder db = dbf.newDocumentBuilder();
             Document doc = db.parse(is);
             // get all elements
             NodeList childNodes = doc.getChildNodes();
             Node r = childNodes.item(0);
             root=new SyNode(null, "0", "1", r.getNodeName(), "");
             printNode(r.getChildNodes(), 0, root);
             System.out.println("Depth of XML : " + DEPTH_XML);
         } catch (SAXException | IOException e) {
             e.printStackTrace();
         }
         printTree(root);
    }

    private void printTree(SyNode root) {
        int lvl = Integer.parseInt(root.getLevel());
        String result=String.format("%" + lvl*5 + "s: [%s]%n", root.getClassName(), lvl);
        System.out.print(result);

        if (root.getContents()!=null) {
            ArrayList<SyNode> nList=root.getChildren();

            for (int i=0; i<nList.size(); i++)
                //printTree(nList.get(i));
        }
    }

    public SyNode getAST(){
        return root;
    }

    public void parse_SPLProgr() throws ParserErrorException {
        Element SPLProgr = doc.createElement("SPLProgr");
        try{
            doc.appendChild(SPLProgr);
            parse_ProcDefs(SPLProgr);
        }catch(ParserErrorException e){
            throw e;
        }
        if(currentToken.getContent().equals("main")){
            //appendTerminal(SPLProgr);
            getNextToken(SPLProgr);
            if(currentToken.getContent().equals("{")){
                //appendTerminal(SPLProgr);
                getNextToken(SPLProgr);
                parse_Algorithm(SPLProgr);
                if(currentToken.getContent().equals("halt")){
                    //appendTerminal(SPLProgr);
                    getNextToken(SPLProgr);
                    if(currentToken.getContent().equals(";")){
                        //appendTerminal(SPLProgr);
                        getNextToken(SPLProgr);
                        parse_VarDecl(SPLProgr);
                        if(currentToken.getContent().equals("}")){
                            //appendTerminal(SPLProgr);
                            getNextToken(SPLProgr);
                            return;
                        }else{
                            String error = "Procedure parse_SPLProgr() expected a '}' token " + "but received: " + currentToken;
                            throw new ParserErrorException(error);
                        }
                    }else{
                            String error = "Procedure parse_SPLProgr() expected a ';' token " + "but received: " + currentToken;
                            throw new ParserErrorException(error);
                    }
                }else{
                    String error = "Procedure parse_SPLProgr() expected a 'halt' token " + "but received: " + currentToken;
                    throw new ParserErrorException(error);
                }
            }else{
                String error = "Procedure parse_SPLProgr() expected a '{' token " + "but received: " + currentToken;
                throw new ParserErrorException(error);
            }
        }else{
            String error = "Procedure parse_SPLProgr() expected a 'main' token " + "but received: " + currentToken;
            throw new ParserErrorException(error);
        }
    }

    public void parse_ProcDefs(Element par) throws ParserErrorException{
        Element pr = doc.createElement("ProcDefs");
        par.appendChild(pr);

        String tk = currentToken.getContent();
        if(tk.equals("output") || tk.equals("call") || tk.equals("main") || tk.equals("return") || tk.equals("if") || tk.equals("do") || tk.equals("while") || currentToken.get_Class().equals("userDefinedName")){
            apNul(pr);
            return;
        }

        parse_PD(pr);

        if(currentToken.getContent().equals(",")){
            //appendTerminal(pr);
            getNextToken(pr);
            parse_ProcDefs(pr);
            return;
        }else{
            String error = "Procedure parse_ProcDefs() expected a ',' token " + "but received: " + currentToken;
            throw new ParserErrorException(error);
        }

        //Throw an error
        //throw new ParserErrorException("Unexpected Token recieved: " + currentToken.getContent());
    }

    public void parse_PD(Element par) throws ParserErrorException{
        Element pD = doc.createElement("PD");
        par.appendChild(pD);

        if(currentToken.getContent().equals("proc")){
            //appendTerminal(pD);
            getNextToken(pD);
            if(currentToken.get_Class().equals("userDefinedName")){
                //appendTerminal(pD);
                getNextToken(pD);
                if(currentToken.getContent().equals("{")){
                    //appendTerminal(pD);
                    getNextToken(pD);
                    try{
                        parse_ProcDefs(pD);
                        parse_Algorithm(pD);
                    }catch(ParserErrorException e){
                        throw e;
                    }
                    if(currentToken.getContent().equals("return")){
                        //appendTerminal(pD);
                        getNextToken(pD);
                        if(currentToken.getContent().equals(";")){
                            //appendTerminal(pD);
                            getNextToken(pD);
                            parse_VarDecl(pD);
                            if(currentToken.getContent().equals("}")){
                                //appendTerminal(pD);
                                getNextToken(pD);
                                return;
                            }else{
                                String error = "Procedure parse_PD() expected a '}' token " + "but received: " + currentToken;
                                throw new ParserErrorException(error);
                            }
                        }else{
                            String error = "Procedure parse_PD() expected a ';' token " + "but received: " + currentToken;
                            throw new ParserErrorException(error);
                        }
                    }else{
                        String error = "Procedure parse_PD() expected a 'return' token " + "but received: " + currentToken;
                        throw new ParserErrorException(error);
                    }
                }else{
                    String error = "Procedure parse_PD() expected a '}' token " + "but received: " + currentToken;
                    throw new ParserErrorException(error);
                }
            }else{
                String error = "Procedure parse_PD() expected a 'userDefinedName' token " + "but received: " + currentToken;
                throw new ParserErrorException(error);
            }
        }else{
            String error = "Procedure parse_PD() expected a 'proc' token " + "but received: " + currentToken;
            throw new ParserErrorException(error);
        }
    }

    public void parse_Algorithm(Element par) throws ParserErrorException{
        Element algo = doc.createElement("Algorithm");
        par.appendChild(algo);

        String  tk =currentToken.getContent();
        if(tk.equals("halt") || tk.equals("return") || tk.equals("}")){
            apNul(algo);
            return;
        }

        parse_Instr(algo);

        if(currentToken.getContent().equals(";")){
            //appendTerminal(algo);
            getNextToken(algo);
            parse_Algorithm(algo);
        }else{
            String error = "Procedure parse_Algorithm() expected a ';' token " + "but received: " + currentToken;
            throw new ParserErrorException(error);
        }
        //Throw Error
        //throw new ParserErrorException("Unexpected Token receieved: "+currentToken.getContent());
    }

    public void parse_Instr(Element par) throws ParserErrorException{
        Element inst = doc.createElement("Instr");
        par.appendChild(inst);

        if(currentToken.getContent().equals("if")){
            try{
                parse_Branch(inst);
            }catch (ParserErrorException e){
                throw e;
            }
        }else if(currentToken.getContent().equals("do") || currentToken.getContent().equals("while")){
            try{
                parse_Loop(inst);
            }catch (ParserErrorException e){
                throw e;
            }
        }else if(currentToken.getContent().equals("call")){
            parse_PCall(inst);
        }else{
            parse_Assign(inst);
        }
    }

    public void parse_Assign(Element par) throws ParserErrorException{
        Element ass = doc.createElement("Assign");
        par.appendChild(ass);

        parse_LHS(ass);

        if(currentToken.getContent().equals(":=")){
            //appendTerminal(ass);
            getNextToken(ass);
            parse_Expr(ass);
        }else{
            String error = "Procedure parse_Assign() expected a ':=' token " + "but received: " + currentToken;
            throw new ParserErrorException(error);
        }
    }

    public void parse_Branch(Element par) throws ParserErrorException{
        Element br = doc.createElement("Branch");
        par.appendChild(br);

        if(currentToken.getContent().equals("if")){
            //appendTerminal(br);
            getNextToken(br);
            if(currentToken.getContent().equals("(")){
                //appendTerminal(br);
                getNextToken(br);
                parse_Expr(br);
                if(currentToken.getContent().equals(")")){
                    //appendTerminal(br);
                    getNextToken(br);
                    if(currentToken.getContent().equals("then")){
                        //appendTerminal(br);
                        getNextToken(br);
                        if(currentToken.getContent().equals("{")){
                            //appendTerminal(br);
                            getNextToken(br);
                            try{
                                parse_Algorithm(br);
                            }catch(ParserErrorException e){
                                throw e;
                            }
                            if(currentToken.getContent().equals("}")){
                                //appendTerminal(br);
                                getNextToken(br);
                                parse_Alternate(br);
                            }else{
                                String error = "Procedure parse_Branch() expected a '}' token " + "but received: " + currentToken;
                                throw new ParserErrorException(error);
                            }
                        }else{
                            String error = "Procedure parse_Branch() expected a '{' token " + "but received: " + currentToken;
                            throw new ParserErrorException(error);
                        }
                    }else{
                        String error = "Procedure parse_Branch() expected a 'then' token " + "but received: " + currentToken;
                        throw new ParserErrorException(error);
                    }
                }else{
                    String error = "Procedure parse_Branch() expected a ')' token " + "but received: " + currentToken;
                    throw new ParserErrorException(error);
                }
            }else{
                String error = "Procedure parse_Branch() expected a '(' token " + "but received: " + currentToken;
                throw new ParserErrorException(error);
            }
        }else{
            String error = "Procedure parse_Branch() expected a 'if' token " + "but received: " + currentToken;
            throw new ParserErrorException(error);
        }
    }

    public void parse_Alternate(Element par) throws ParserErrorException{
        Element alt = doc.createElement("Alternate");
        par.appendChild(alt);

        String procdefsfollows = ";";
        if(procdefsfollows.indexOf( currentToken.getContent()) != -1){
            apNul(alt);
            return;
        }

        //getPToken();

        if(currentToken.getContent().equals("else")){
            //appendTerminal(alt);
            getNextToken(alt);
            if(currentToken.getContent().equals("{")){
                //appendTerminal(alt);
                getNextToken(alt);
                try{
                    parse_Algorithm(alt);
                }catch(ParserErrorException e){
                    throw e;
                }
                if(currentToken.getContent().equals("}")){
                    //appendTerminal(alt);
                    getNextToken(alt);
                }else{
                    String error = "Procedure parse_ALternate() expected a '}' token " + "but received: " + currentToken;
                    throw new ParserErrorException(error);
                }
            }else{
                String error = "Procedure parse_ALternate() expected a '{' token " + "but received: " + currentToken;
                throw new ParserErrorException(error);
            }
        }else{
            String error = "Procedure parse_ALternate() expected a 'else' token " + "but received: " + currentToken;
            throw new ParserErrorException(error);
        }
    }

    public void parse_Loop(Element par) throws ParserErrorException {
        Element loop = doc.createElement("Loop");
        par.appendChild(loop);

        if(currentToken.getContent().equals("do")){
            //appendTerminal(loop);
            getNextToken(loop);
            if(currentToken.getContent().equals("{")){
                //appendTerminal(loop);
                getNextToken(loop);
                parse_Algorithm(loop);
                if(currentToken.getContent().equals("}")){
                    //appendTerminal(loop);
                    getNextToken(loop);
                    if(currentToken.getContent().equals("until")){
                        //appendTerminal(loop);
                        getNextToken(loop);
                        if(currentToken.getContent().equals("(")){
                            //appendTerminal(loop);
                            getNextToken(loop);
                            parse_Expr(loop);
                            if(currentToken.getContent().equals(")")){
                                //appendTerminal(loop);
                                getNextToken(loop);
                            }else{
                                String error = "Procedure parse_Loop() expected a ')' token " + "but received: " + currentToken;
                                throw new ParserErrorException(error);
                            }
                        }else{
                            String error = "Procedure parse_Loop() expected a '(' token " + "but received: " + currentToken;
                            throw new ParserErrorException(error);
                        }
                    }else{
                        String error = "Procedure parse_Loop() expected a 'until' token " + "but received: " + currentToken;
                        throw new ParserErrorException(error);
                    }
                }else{
                    String error = "Procedure parse_Loop() expected a '}' token " + "but received: " + currentToken;
                    throw new ParserErrorException(error);
                }
            }else{
                String error = "Procedure parse_Loop() expected a '{' token " + "but received: " + currentToken;
                throw new ParserErrorException(error);
            }
        }else if(currentToken.getContent().equals("while")){
            //appendTerminal(loop);
            getNextToken(loop);
            if(currentToken.getContent().equals("(")){
                //appendTerminal(loop);
                getNextToken(loop);
                parse_Expr(loop);
                if(currentToken.getContent().equals(")")){
                   //appendTerminal(loop);
                    getNextToken(loop);
                    if(currentToken.getContent().equals("do")){
                        //appendTerminal(loop);
                        getNextToken(loop);
                        if(currentToken.getContent().equals("{")){
                            //appendTerminal(loop);
                            getNextToken(loop);
                            try{
                                parse_Algorithm(loop);
                            }catch(ParserErrorException e){
                                throw e;
                            }
                            if(currentToken.getContent().equals("}")){
                                //appendTerminal(loop);
                                getNextToken(loop);
                            }else{
                                String error = "Procedure parse_Loop() expected a '}' token " + "but received: " + currentToken;
                                throw new ParserErrorException(error);
                            }
                        }else{
                            String error = "Procedure parse_Loop() expected a '{' token " + "but received: " + currentToken;
                            throw new ParserErrorException(error);
                        }
                    }else{
                        String error = "Procedure parse_Loop() expected a 'do' token " + "but received: " + currentToken;
                        throw new ParserErrorException(error);
                    }
                }else{
                    String error = "Procedure parse_Loop() expected a ')' token " + "but received: " + currentToken;
                    throw new ParserErrorException(error);
                }
            }else{
                String error = "Procedure parse_Loop() expected a '(' token " + "but received: " + currentToken;
                throw new ParserErrorException(error);
            }
        }else{
            String error = "Procedure parse_Loop() expected a 'while' or 'do' token " + "but received: " + currentToken;
            throw new ParserErrorException(error);
        }
    }

    public void parse_LHS(Element par) throws ParserErrorException{
        Element lh = doc.createElement("LHS");
        par.appendChild(lh);

        if(currentToken.getContent().equals("output")){
            //appendTerminal(lh);
            getNextToken(lh);
            return;
        }

        if(currentToken.get_Class().equals("userDefinedName")){
            if(ll_Var()){
                parse_Field(lh);
                return;
            }else{
                parse_Var(lh);
                return;
            }
        }

        //Throw an Error
        throw new ParserErrorException("Unexpected Token: "+currentToken);
    }

    public void parse_Expr(Element par) throws ParserErrorException{
        Element exp = doc.createElement("Expr");
        par.appendChild(exp);

        String tk = currentToken.getContent();
        if(tk.equals("sub") || tk.equals("and") || tk.equals("or") || tk.equals("eq") || tk.equals("larger") || tk.equals("add") || tk.equals("mult")){
            parse_BinOp(exp);
            return;
        }else if(tk.equals("input") || tk.equals("not")){
            parse_UnOp(exp);
            return;
        }else if(currentToken.get_Class().equals("userDefinedName")){
            if(ll_Var()){
                try{
                    parse_Field(exp);
                }catch(ParserErrorException e){
                    throw e;
                }
            }else{
                parse_Var(exp);
                return;
            }
        }else{
            parse_Const(exp);
            return;
        }
        //Throw an error
    }

    public void parse_PCall(Element par) throws ParserErrorException{
        Element pcall = doc.createElement("PCall");
        par.appendChild(pcall);

        if(currentToken.getContent().equals("call")){
            //appendTerminal(pcall);
            getNextToken(pcall);
            if(currentToken.get_Class().equals("userDefinedName")){
                //appendTerminal(pcall);
                getNextToken(pcall);
            }else{
                String error = "Procedure parse_PCall() expected a 'userDefinedName' token " + "but received: " + currentToken;
                throw new ParserErrorException(error);
            }
        }else{
            String error = "Procedure parse_PCall() expected a 'call' token " + "but received: " + currentToken;
            throw new ParserErrorException(error);
        }
    }

    public void parse_Var(Element par) throws ParserErrorException{
        Element vr = doc.createElement("Var");
        par.appendChild(vr);

        if(currentToken.get_Class().equals("userDefinedName")){
            //appendTerminal(vr);
            getNextToken(vr);
        }else{
            String error = "Procedure parse_PCall() expected a 'userDefinedName' token " + "but received: " + currentToken;
            throw new ParserErrorException(error);
        }
    }

    public boolean ll_Var(){
        boolean ll = false;
        if(tokens.get(current+1).getContent().equals("[")){
            ll=true;
        }
        return ll;
    }

    public void parse_Field(Element par) throws ParserErrorException{
        Element fd = doc.createElement("Field");
        par.appendChild(fd);

        if(currentToken.get_Class().equals("userDefinedName")){
            //appendTerminal(fd);
            getNextToken(fd);
            if(currentToken.getContent().equals("[")){
                //appendTerminal(fd);
                getNextToken(fd);
                if(currentToken.get_Class().equals("userDefinedName")){
                    parse_Var(fd);
                }else {
                    parse_Const(fd);
                }
                if(currentToken.getContent().equals("]")){
                    //appendTerminal(fd);
                    getNextToken(fd);
                }else{
                    String error = "Procedure parse_Field() expected a ']' token " + "but received: " + currentToken;
                    throw new ParserErrorException(error);
                }
            }else{
                String error = "Procedure parse_Field() expected a '[' token " + "but received: " + currentToken;
                throw new ParserErrorException(error);
            }
        }else{
            String error = "Procedure parse_Field() expected a 'userDefinedName' token " + "but received: " + currentToken;
            throw new ParserErrorException(error);
        }
    }

    public void parse_Const(Element par) throws ParserErrorException{
        Element con = doc.createElement("Const");
        par.appendChild(con);

        if(currentToken.get_Class().equals("ShortString")){
            //appendTerminal(con);
            getNextToken(con);
        }else if(currentToken.get_Class().equals("Number")){
            //appendTerminal(con);
            getNextToken(con);
        }else if(currentToken.getContent().equals("true")){
            //appendTerminal(con);
            getNextToken(con);
        }else if(currentToken.getContent().equals("false")){
            //appendTerminal(con);
            getNextToken(con);
        }else{
            String error = "Procedure parse_Const() expected a 'shortString' or 'Number' or 'true' or 'false' token " + "but received: " + currentToken;
            throw new ParserErrorException(error);
        }
    }

    public void parse_UnOp(Element par) throws ParserErrorException{
        Element uop = doc.createElement("UnOp");
        par.appendChild(uop);

        if(currentToken.getContent().equals("input")){
            //appendTerminal(uop);
            getNextToken(uop);
            if(currentToken.getContent().equals("(")){
                //appendTerminal(uop);
                getNextToken(uop);
                parse_Var(uop);
                if(currentToken.getContent().equals(")")){
                    //appendTerminal(uop);
                    getNextToken(uop);
                }else{
                    String error =  "Procedure parse_UnOp() expected a ')' token " + "but received: " + currentToken;
                    throw new ParserErrorException(error);
                }
            }else{
                String error =  "Procedure parse_UnOp() expected a '(' token " + "but received: " + currentToken;
                throw new ParserErrorException(error);
            }
        }else if(currentToken.getContent().equals("not")){
            //appendTerminal(uop);
            getNextToken(uop);
            if(currentToken.getContent().equals("(")){
                //appendTerminal(uop);
                getNextToken(uop);
                try{
                   parse_Expr(uop);
                }catch(ParserErrorException e){
                    throw e;
                }
                if(currentToken.getContent().equals(")")){
                    //appendTerminal(uop);
                    getNextToken(uop);
                }else{
                    String error =  "Procedure parse_UnOp() expected a ')' token " + "but received: " + currentToken;
                    throw new ParserErrorException(error);
                }
            }else{
                String error =  "Procedure parse_UnOp() expected a '(' token " + "but received: " + currentToken;
                throw new ParserErrorException(error);
            }
        }else{
            String error =  "Procedure parse_UnOp() expected a 'input' or 'not' token " + "but received: " + currentToken;
            throw new ParserErrorException(error);
        }
    }

    public void parse_BinOp(Element par) throws ParserErrorException{
        Element binop = doc.createElement("BinaryOp");
        par.appendChild(binop);

        String tk = currentToken.getContent();
        if(tk.equals("sub") || tk.equals("and") || tk.equals("or") || tk.equals("eq") || tk.equals("larger") || tk.equals("add") || tk.equals("mult")){
            //appendTerminal(binop);
            getNextToken(binop);
            if(currentToken.getContent().equals("(")){
                //appendTerminal(binop);
                getNextToken(binop);
                try{
                   parse_Expr(binop);
                }catch(ParserErrorException e){
                    throw e;
                }
                if(currentToken.getContent().equals(",")){
                    //appendTerminal(binop);
                    getNextToken(binop);
                    try{
                        parse_Expr(binop);
                    }catch(ParserErrorException e){
                        throw e;
                    }
                    if(currentToken.getContent().equals(")")){
                        //appendTerminal(binop);
                        getNextToken(binop);
                    }else{
                        String error =  "Procedure parse_BinOp() expected a ')' token " + "but received: " + currentToken;
                        throw new ParserErrorException(error);
                    }
                }else{
                    String error =  "Procedure parse_BinOp() expected a ',' token " + "but received: " + currentToken;
                    throw new ParserErrorException(error);
                }
            }else{
                String error =  "Procedure parse_BinOp() expected a '(' token " + "but received: " + currentToken;
                throw new ParserErrorException(error);
            }
        }else{
            String error =  "Procedure parse_BinOp() expected a 'and' or 'or' 'eq' or 'larger' or 'add' or 'sub' or 'mult' token " + "but received: " + currentToken;
            throw new ParserErrorException(error);
        }
    }

    public void parse_VarDecl(Element par) throws ParserErrorException{
        Element vd = doc.createElement("VarDecl");
        par.appendChild(vd);

        String varDeclf = "}";
        if(varDeclf.indexOf( currentToken.getContent()) != -1){
            apNul(vd);
            return;
        }

        parse_Dec(vd);

        if(currentToken.getContent().equals(";")){
            //appendTerminal(vd);
            getNextToken(vd);
            parse_VarDecl(vd);
        }else{
            String error = "Procedure parse_VarDecl() expected a ';' token " + "but received: " + currentToken;
            throw new ParserErrorException(error);
        }

        //throw error
    }

    public void parse_Dec(Element par) throws ParserErrorException{
        Element de = doc.createElement("Dec");
        par.appendChild(de);

        if(currentToken.getContent().equals("arr")){
            //appendTerminal(de);
            getNextToken(de);
            parse_TYP(de);
            if(currentToken.getContent().equals("[")){
                //appendTerminal(de);
                getNextToken(de);
                parse_Const(de);
                if(currentToken.getContent().equals("]")){
                    //appendTerminal(de);
                    getNextToken(de);
                    parse_Var(de);
                }else{
                    String error = "Procedure parse_Dec() expected a ']' token " + "but received: " + currentToken;
                    throw new ParserErrorException(error);
                }
            }else{
                String error = "Procedure parse_Dec() expected a '[' token " + "but received: " + currentToken;
                throw new ParserErrorException(error);
            }
        }else{
            parse_TYP(de);
            parse_Var(de);
        }
    }

    public void parse_TYP(Element par) throws ParserErrorException{
        Element typ = doc.createElement("Type");
        par.appendChild(typ);

        String tk = currentToken.getContent();
        if(tk.equals("num") || tk.equals("bool") || tk.equals("string")){
            //appendTerminal(typ);
            getNextToken(typ);
            return;
        }else{
           String error = "Procedure parse_TYP() expected a 'num' or 'bool' or 'string' token " + "but received: " + currentToken;
           throw new ParserErrorException(error);
        }
    }
}
