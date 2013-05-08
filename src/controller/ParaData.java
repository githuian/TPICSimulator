/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.ArrayList;
import javax.swing.AbstractListModel;

/**
 *
 * @author hui
 */
public class ParaData extends AbstractListModel{
    ArrayList<String> data = new ArrayList<String>();

    @Override
    public int getSize() {
        return data.size();//To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getElementAt(int index) {
        return data.get(index);//To change body of generated methods, choose Tools | Templates.
    }
    public void add(String s ){
        data.add(s);
        fireContentsChanged(this, 0, getSize());
    }
    public void clear(){
        data.clear();
        fireContentsChanged(this, 0, getSize());
    }
}
