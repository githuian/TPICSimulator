/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package obj;

import com.predic8.wsdl.Binding;
import com.predic8.wsdl.Operation;
import com.predic8.wsdl.PortType;
import java.util.ArrayList;

/**
 *
 * @author hui
 */
public class WSDLData {
    String wsdlPath;
    PortType pt;
    Operation op;
    Binding bd;
    ArrayList<String> argList = new ArrayList<String>();
    boolean argIsSet = false;

    public ArrayList<String> getArgList() {
        return argList;
    }

    public void setArgList(ArrayList<String> argList) {
        this.argList = argList;
    }

    public boolean isArgIsSet() {
        return argIsSet;
    }

    public void setArgIsSet(boolean argIsSet) {
        this.argIsSet = argIsSet;
    }
    
    public String getWsdlPath() {
        return wsdlPath;
    }

    public void setWsdlPath(String wsdlPath) {
        this.wsdlPath = wsdlPath;
    }

    public PortType getPt() {
        return pt;
    }

    public void setPt(PortType pt) {
        this.pt = pt;
    }

    public Operation getOp() {
        return op;
    }

    public void setOp(Operation op) {
        this.op = op;
    }

    public Binding getBd() {
        return bd;
    }

    public void setBd(Binding bd) {
        this.bd = bd;
    }
    public String toString(){
        
        return pt.getName()+":"+op.getName();
        
    }
    
    
    
}
