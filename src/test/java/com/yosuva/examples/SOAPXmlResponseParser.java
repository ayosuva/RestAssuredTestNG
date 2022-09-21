package com.yosuva.examples;

import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

public class SOAPXmlResponseParser {
    public String getResponseValue(Response response, String node) {
        ResponseBody body = response.getBody();
        String xmlValue = body.asString();
        Node parentChild =null ;
        Document doc = convertStringToXMLDocument(xmlValue);
        if (xmlValue.contains("soap:") ) {
            parentChild = doc.getFirstChild().getFirstChild().getFirstChild();
        }else
        {
            parentChild = doc.getFirstChild();

        }
        NodeList nodeList = parentChild.getChildNodes();
        String nodeValue=null;
        for (int j = 0; j < nodeList.getLength(); j++) {
            Node childNode = nodeList.item(j);
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                if(childNode.getNodeName().equals(node)){
                    if(null!=childNode.getFirstChild()) {
                        nodeValue = childNode.getFirstChild().getNodeValue();
                        System.out.println("Node [%s] is present and has a value [%s]"+childNode.getNodeName()+" "+nodeValue);
                    }else{
                        System.out.println("Empty node is present");
                    }
                    break;
                }
            }else{
                System.out.println("Node is not present");
            }
        }
        return nodeValue;
    }

    private  Document convertStringToXMLDocument(String xmlString)
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try
        {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
            return doc;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
