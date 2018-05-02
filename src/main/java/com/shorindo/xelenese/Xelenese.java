/*
 * Copyright 2018 Shorindo, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.shorindo.xelenese;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.openqa.selenium.WebDriver;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Xelenese extends DefaultHandler {
    private Document document;
    private WebDriver driver;

    public Xelenese() {
    }

    public static void main(String[] args) {
        InputStream is = null;
        Xelenese xelenese = new Xelenese();
        try {
            is = Xelenese.class.getClassLoader().getResourceAsStream("com/shorindo/xelenoid/sample.xml");
            Task task = foo(is);
            System.out.println(task);
//            Document d = xelenese.load(is);
//            System.out.println(dump(d));
//        } catch (IOException e) {
//            e.printStackTrace();
        } finally {
            if (is != null)
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    public static Task foo(InputStream is) {
        return JAXB.unmarshal(is, SuiteTask.class);
    }

    public Document load(InputStream is) throws IOException {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = builder.parse(is);
            return document;
        } catch (ParserConfigurationException e) {
            throw new IOException(e);
        } catch (SAXException e) {
            throw new IOException(e);
        }
    }

    public static String dump(Document document) throws IOException {
        String xslt =
                "<?xml version='1.0' encoding='UTF-8'?>" +
                "<xsl:stylesheet version='1.0'" +
                "     xmlns:xsl='http://www.w3.org/1999/XSL/Transform'" +
                "     xmlns:xalan='http://xml.apache.org/xslt'>" +
                "   <xsl:output method='xml' encoding='UTF-8'" +
                "      indent='yes' xalan:indent-amount='4'/>" +
                "   <xsl:template match='/'>" +
                "      <xsl:copy-of select='.'/>" +
                "   </xsl:template>" +
                "</xsl:stylesheet>";
        ByteArrayInputStream bais = new ByteArrayInputStream(xslt.getBytes());
        StreamSource xsltSource = new StreamSource(bais);
        try {
            Transformer t = TransformerFactory.newInstance().newTransformer(xsltSource);
            DOMSource xmlSource = new DOMSource(document);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            StreamResult result = new StreamResult(baos);
            t.transform(xmlSource, result);
            return new String(baos.toByteArray(), "UTF-8");
        } catch (TransformerConfigurationException e) {
            throw new IOException(e);
        } catch (TransformerFactoryConfigurationError e) {
            throw new IOException(e);
        } catch (TransformerException e) {
            throw new IOException(e);
        }
    }

    public void run(Node node) throws XeleneseException {
        switch (node.getNodeName()) {
        case "driver":
            doDriver(node);
            break;
        default:
        }
    }

    private void doDriver(Node node) throws XeleneseException {
        Node attr = node.getAttributes().getNamedItem("className");
        try {
            Class clazz = Class.forName(attr.getNodeValue());
            driver = (WebDriver)clazz.newInstance();
        } catch (ClassNotFoundException e) {
            throw new XeleneseException(e);
        } catch (DOMException e) {
            throw new XeleneseException(e);
        } catch (InstantiationException e) {
            throw new XeleneseException(e);
        } catch (IllegalAccessException e) {
            throw new XeleneseException(e);
        }
    }

//    public static String dump(Tag node, int depth) {
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < depth * 4; i++) {
//            sb.append(' ');
//        }
//        sb.append("<" + node.getName() + ">\n");
//        for (Tag tag : node.getChildren()) {
//            sb.append(dump(tag, depth + 1));
//        }
//        return sb.toString();
//    }

//    private Document document;
//    private Node curr;
//
//    public Document parse(InputStream is) throws XeleneseException {
//        try {
//            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
//            parser.parse(is, this);
//            return document;
//        } catch (ParserConfigurationException e) {
//            throw new XeleneseException(e);
//        } catch (SAXException e) {
//            throw new XeleneseException(e);
//        } catch (IOException e) {
//            throw new XeleneseException(e);
//        }
//    }
//
//    @Override
//    public void startDocument() throws SAXException {
//        DocumentBuilder builder;
//        try {
//            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//            document = builder.newDocument();
//        } catch (ParserConfigurationException e) {
//            throw new SAXException(e);
//        }
//    }
//
//    @Override
//    public void startElement(String uri, String localName, String qName,
//            Attributes attributes) throws SAXException {
//        Node child = null;
//        switch(qName) {
//        case "suite":
//            child = new SuiteTag(document);
//            break;
//        case "driver":
//            child = new DriverTag(document);
//            break;
//        case "template":
//            child = new TemplateTag(document);
//            break;
//        case "test":
//            child = new TestTag(document);
//        default:
//        }
//        if (curr == null) {
//            curr = child;
//            document.appendChild(child);
//        } else {
//            curr.appendChild(child);
//            curr = child;
//        }
//    }
//
//    @Override
//    public void endElement(String uri, String localName, String qName)
//            throws SAXException {
//        super.endElement(uri, localName, qName);
//    }
//
//    @Override
//    public void characters(char[] ch, int start, int length)
//            throws SAXException {
//        // TODO Auto-generated method stub
//        super.characters(ch, start, length);
//    }
}
