/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractListModel;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

/**
 *
 * @author hui
 */
public class ResultData extends AbstractListModel{
    public static final String PATH = "./WSDLTest/projects/soap_test/results/";
    private ArrayList<String> dataList = new ArrayList<String>();
    @Override
    public int getSize() {
        return dataList.size();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getElementAt(int index) {
        return dataList.get(index);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void refresh(){
        dataList.clear();
        List<File>  temp = readDirList(PATH);
        for(File f : temp){
            dataList.add(f.getName());
            dataList.remove("results");
        }
        Collections.sort(dataList);
        fireContentsChanged(this, 0, getSize());
    }
    
    public List<File> readDirList(String dir){
        List<File> b =  (List<File>) FileUtils.listFilesAndDirs(new File(dir), 
                new NotFileFilter(TrueFileFilter.INSTANCE), 
                DirectoryFileFilter.DIRECTORY);
        for(File f : b){
            System.out.println(f.getName());
        }
        return b;
    }
    
}
