/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.predic8.wsdl.*;
import com.predic8.wstool.creator.RequestTemplateCreator;
import com.predic8.wstool.creator.SOARequestCreator;
import com.sun.xml.internal.ws.api.server.SDDocument.WSDL;
import groovy.xml.MarkupBuilder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import obj.WSDLData;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * @author hui
 */
public class ConfigHandler {

    private String wsdlPath;
    public static final String FOLDER = "./WSDLTest/projects/soap_test/test_scripts/";
    public static final String PYFOLDER = "./WSDLTest/";

    public ConfigHandler(String path) {
        this.wsdlPath = path;
    }

    public Definitions parseWSDL(String wsdlPath) {
        WSDLParser parser = new WSDLParser();
        Definitions defs = null;
        try {
            defs = parser.parse(wsdlPath);
        } catch (Exception e) {
            System.out.println("parse failure");
        }
        

        return defs;
    }

    public ConfigHandler() {
    }

    public List<PortType> getPortTypes(String wsdlPath) {
        List<PortType> result = new ArrayList<PortType>();

        Definitions wsdl = parseWSDL(wsdlPath);

        if (wsdl == null) {
            return null;
        }
        result = wsdl.getPortTypes();
        

        return result;
    }

    public List<Operation> getOperations(PortType pt) {
        List<Operation> result = new ArrayList<Operation>();

        if (pt == null) {
            return null;
        }
        result = pt.getOperations();
        return result;
    }

    public List<Binding> getBindings(String wsdlPath) {
        List<Binding> result = new ArrayList<Binding>();
        Definitions wsdl = parseWSDL(wsdlPath);

        if (wsdl == null) {
            return null;
        }
        result = wsdl.getBindings();
        
        return result;

    }

    public boolean generateXML(String wsdlPath) {
        Definitions wsdl = parseWSDL(wsdlPath);
        for(Service s : wsdl.getServices()){
            for(Port p: s.getPorts()){
                System.out.print("XXXXX"+s.getName()+p.getName()+p.getAddress().getName());
            }
        }
        ArrayList<PortType> ptList = (ArrayList<PortType>) getPortTypes(wsdlPath);
        ArrayList<Binding> bdList = (ArrayList<Binding>) getBindings(wsdlPath);
        for(PortType b : ptList){
            System.out.println("address "+wsdl.getServices().get(0).getPorts().get(0).getAddress().getLocation());
        }
        for (PortType pt : ptList) {
            for (Operation op : pt.getOperations()) {
                try {
                    generateXMLFile(PYFOLDER + op.getName() + ".xml", wsdl, pt, op, bdList.get(0));
                    generatePy(FOLDER + op.getName() + ".py", op.getName() + ".xml", wsdl.getServices().get(0).getPorts().get(0).getAddress().getLocation());
                    
                } catch (IOException ex) {
                    ex.printStackTrace();
                    return false;
                }
            }
        }

        return true;
    }

    public void generateXMLFile(String filePath, Definitions wsdl, PortType pt, Operation op, Binding bd) throws IOException {
        StringWriter writer = new StringWriter();

        //SOAPRequestCreator constructor: SOARequestCreator(Definitions, Creator, MarkupBuilder)
        SOARequestCreator creator = new SOARequestCreator(wsdl, new RequestTemplateCreator(), new MarkupBuilder(writer));

        //creator.createRequest(PortType name, Operation name, Binding name);
        creator.createRequest(pt.getName(), op.getName(), bd.getName());

        System.out.println(writer);

        FileUtils.writeStringToFile(new File(filePath), writer.toString());

    }

    public ArrayList<String> getParaForOp(WSDLData data) {
        String req = null;
        Definitions def = parseWSDL(data.getWsdlPath());
        try {
            req = getRequest(def, data.getPt(), data.getOp(), data.getBd());
        } catch (IOException ex) {
            return new ArrayList<String>();
        }
        return getParaFromRequest(req);

    }

    private ArrayList<String> getParaFromRequest(String req) {
        ArrayList<String> result = new ArrayList<String>();
        Document d = null;
        try {
            d = loadXMLFromString(req);
        } catch (Exception ex) {
            return result;
        }
        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();
        XPathExpression expr = null;
        try {

            expr = xpath.compile("//*");
        } catch (XPathExpressionException ex) {
            return result;
        }
        NodeList nl = null;
        try {
            nl = (NodeList) expr.evaluate(d, XPathConstants.NODESET);
        } catch (XPathExpressionException ex) {
            return result;
        }
        if (nl == null) {
            System.out.println("return");

            return result;

        }
        for (int i = 0; i < nl.getLength(); i++) {
            Node childNode = nl.item(i);

            NodeList children = childNode.getChildNodes();
            StringBuffer buf = new StringBuffer();
            for (int j = 0; j < children.getLength(); j++) {
                Node textChild = children.item(j);
                if (textChild.getNodeType() != Node.TEXT_NODE) {
                    //System.err.println("Mixed content! Skipping child element " + textChild.getNodeName());
                    continue;
                }
                buf.append(textChild.getNodeValue());
            }


            // here would be a good place to put your application logic
            // and do something base upon node type
            if (buf.toString().equals("?XXX?")) {
                result.add(childNode.getNodeName());
                System.out.println("node name = " + childNode.getNodeName());
            }
            //traverseNodes(childNode);
        }



        return result;

    }

    

    public static Document loadXMLFromString(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }

    private String getRequest(Definitions wsdl, PortType pt, Operation op, Binding bd) throws IOException {
        StringWriter writer = new StringWriter();

        //SOAPRequestCreator constructor: SOARequestCreator(Definitions, Creator, MarkupBuilder)
        SOARequestCreator creator = new SOARequestCreator(wsdl, new RequestTemplateCreator(), new MarkupBuilder(writer));

        //creator.createRequest(PortType name, Operation name, Binding name);
        creator.createRequest(pt.getName(), op.getName(), bd.getName());

        return writer.toString();

    }

    public String getRequestFromFile(String filePath, String request) {
        return null;
    }

    public void generateConfig(String path, List<String> operationList,
            int runtime, int rampup, int interval, int threads) {
        String s1 = "[global]\n"
                + "run_time: " + runtime + "\n"
                + "rampup: " + rampup + "\n"
                + "console_logging: off\n"
                + "results_ts_interval: " + interval + "\n"
                + "\n";
        StringBuilder s2Builder = new StringBuilder();
        int count = 1;
        for (String s : operationList) {
            s2Builder.append("[user_group-" + count + "]\n");
            s2Builder.append("threads: " + threads + "\n");
            s2Builder.append("script: " + s + ".py" + "\n");
            count++;
        }
        String s2 = s2Builder.toString();
        try {
            FileUtils.writeStringToFile(new File(path), s1 + s2);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    protected void generatePy(String path, String xmlFile, String url) {
        try {
            FileUtils.writeStringToFile(new File(path), getPy(xmlFile, url));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void generatePyForOp(WSDLData data) {
        generatePy(FOLDER + data.getOp().getName() + ".py", data.getOp().getName() + ".xml", data.getWsdlPath());
    }

    public void generatePyWithArg(String path, String xmlFile, String url, String argPath) {
        try {
            FileUtils.writeStringToFile(new File(path), getPyWithArgFile(xmlFile, url, argPath));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void generatePyWithArgForOp(WSDLData data, String argPath) {
        generatePyWithArg(FOLDER + data.getOp().getName() + ".py", data.getOp().getName() + ".xml", data.getWsdlPath(), argPath);
    }

    private String getPy(String xmlFile, String url) {
        return "import urllib2\n"
                + "\n"
                + "\n"
                + "class Transaction(object):\n"
                + "    def __init__(self):\n"
                + "        with open('" + xmlFile + "') as f:\n"
                + "            self.soap_body = f.read()\n"
                + "    \n"
                + "    def run(self):\n"
                + "        req = urllib2.Request(url='" + url + "', data=self.soap_body)\n"
                + "        req.add_header('Content-Type', 'text/xml')\n"
                + "        resp = urllib2.urlopen(req)\n"
                + "        content = resp.read()";
    }

    private String getPyWithArgFile(String xmlFile, String url, String argFile) {
        return "import urllib2\n"
                + "import csv,random\n"
                + "from itertools import islice"
                + "\n"
                + "\n"
                + "class Transaction(object):\n"
                + "    def __init__(self):\n"
                + "        with open('" + xmlFile + "') as f:\n"
                + "            self.soap_body = f.read()\n"
                + "        temp = open('" + argFile + "')\n"
                + "        self.count = len(temp.readlines())\n"
                + "        temp.close()\n"
                + "        b = open('" + argFile + "')\n"
                + "        self.soap_arg1 = csv.reader(b)\n"
                + "        self.soap_arg = list(self.soap_arg1)\n"
                + "    \n"
                + "    def run(self):\n"
                + "        ran = random.randrange(1,self.count-2)\n"
                + "        li = list(self.soap_arg)[ran]\n"
                + "        for item in li:\n"
                + "            self.soap_body = self.soap_body.replace('?XXX?',item,1)\n"
                + "        req = urllib2.Request(url='" + url + "', data=self.soap_body)\n"
                + "        req.add_header('Content-Type', 'text/xml')\n"
                + "        resp = urllib2.urlopen(req)\n"
                + "        content = resp.read()";


    }
//    private String generatePySoapBody(String soapTemplete, String resourceFile){
//        StringBuffer buffer = new StringBuffer();
//        
//        "        with open('GetWeather.xml') as f:\n" +
//"            self.soap_body = f.read()"
//                ""
//                
//                
//        
//        
//        return buffer.toString();
//    }
}
