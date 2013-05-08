/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.predic8.schema.ComplexType;
import com.predic8.schema.Element;
import com.predic8.wsdl.Binding;
import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.Operation;
import com.predic8.wsdl.Part;
import com.predic8.wsdl.PortType;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;
import obj.WSDLData;

/**
 *
 * @author hui
 */
public class OperationData extends AbstractListModel {

    private ArrayList<WSDLData> dataList = new ArrayList<WSDLData>();

    public ArrayList<WSDLData> getDataList() {
        return dataList;
    }

    @Override
    public int getSize() {
        return dataList.size();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public OperationData() {
    }

    @Override
    public Object getElementAt(int index) {
        
        return dataList.get(index);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void addOperation(WSDLData o) {

        dataList.add(o);
        fireContentsChanged(this, 0, getSize());

    }
    
    public void removeOperation(WSDLData o){
        
        dataList.remove(o);
        fireContentsChanged(this, 0, getSize());
    }
    
    
    public WSDLData getAt(int index){
        return dataList.get(index);
    }

    public void build(String path) {
        clear();
        ConfigHandler ch = new ConfigHandler(path);
        ArrayList<Binding> bdList = (ArrayList<Binding>) ch.getBindings(path);
        ArrayList<PortType> ptList = (ArrayList<PortType>) ch.getPortTypes(path);
        for (PortType pt : ptList) {
            ArrayList<Operation> opList = (ArrayList<Operation>) ch.getOperations(pt);
            for (Operation op : opList) {
                WSDLData data = new WSDLData();
                
                data.setBd(bdList.get(0));
                data.setOp(op);
                
                data.setPt(pt);
                data.setWsdlPath(path);
                
                data.setArgList(ch.getParaForOp(data));
                dataList.add(data);
                
                
                for(Part p : op.getInput().getMessage().getParts()){
                    System.out.println(op.getName()+":"+p.getName() + " " + p.getElement());
                }
                
                
            }

        }
        fireContentsChanged(this, 0, getSize());
    }
    
    public void clear(){
        dataList.clear();
        fireContentsChanged(this, 0, getSize());
    }
    
    public List<Part> getParts(Operation o){
        return o.getInput().getMessage().getParts();
    }
    
    private static void listParameters(Element element) {
        ComplexType ct = (ComplexType) element.getEmbeddedType();
        
        if(ct == null) {
            ct = (ComplexType) element.getSchema().getType(element.getType());
        }
        if(ct.getSequence()==null)
            return;
        for (Element e : ct.getSequence().getElements()) {
            System.out.println(e.getName() + " " + e.getType());
        }
}
    
    
     
}
