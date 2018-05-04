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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Unmarshaller.Listener;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.shorindo.xelenese.BeanUtil.BeanNotFoundException;

/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Xelenese {
    private static final XeleneseLogger LOG = XeleneseLogger.getLogger(Xelenese.class);
    private Map<String, Class<Task>> taskMap = new HashMap<String, Class<Task>>();
    private Task suite;

    public Xelenese(InputStream is) throws XeleneseException {
        init();
        load(is);
//        try {
//            JAXBContext jc = JAXBContext.newInstance(SuiteTask.class);
//            Unmarshaller unmarshaller = jc.createUnmarshaller();
//            SuiteUnmarshallListener pul = new SuiteUnmarshallListener();
//            unmarshaller.setListener(pul);
//            suite = SuiteTask.class.cast(unmarshaller.unmarshal(is));
//            LOG.debug(suite.toString());
//        } catch (JAXBException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
    }

    private void init() {
        String path = getClass().getName().replaceAll("\\.", "/") + ".class";
        URL url = getClass().getClassLoader().getResource(path);
        int prefix = url.getFile().length() - path.length();
        File dir = new File(url.getFile()).getParentFile();
        for (File file : dir.listFiles()) {
            String absPath;
            try {
                absPath = file.toURI().toURL().getFile();
                if (absPath.endsWith(".class")) {
                    String className = absPath
                            .substring(prefix)
                            .replaceAll(".class$", "")
                            .replaceAll("/", ".");
                    Class<?> clazz = getClass().getClassLoader().loadClass(className);
                    if (Task.class == clazz) {
                        continue;
                    }
                    if (!Task.class.isAssignableFrom(clazz)) {
                        continue;
                    }
                    TaskName taskName = clazz.getAnnotation(TaskName.class);
                    if (taskName == null) {
                        LOG.warn(clazz.getName() + " has no TaskName annotation.");
                        continue;
                    }
                    taskMap.put(taskName.value(), (Class<Task>)clazz);
                }
            } catch (MalformedURLException e) {
                LOG.error(e);
            } catch (ClassNotFoundException e) {
                LOG.error(e);
            }
        }
    }

    private void load(InputStream is) throws XeleneseException {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(is);
            suite = (SuiteTask)parse(null, document.getDocumentElement());
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new XeleneseException(e);
        }
    }

    private Task parse(Task parent, Node node) {
        String taskName = node.getNodeName();
        Class<Task> taskClass = taskMap.get(taskName);

        if (taskClass == null) {
            return null;
        }

        try {
            Task task = taskClass.getConstructor(Task.class).newInstance(parent);

            if (node.hasAttributes() && task != null) {
                NamedNodeMap map = node.getAttributes();
                for (int i = 0; i < map.getLength(); i++) {
                    Node attr = map.item(i);
                    try {
                        BeanUtil.setValue(task, attr.getNodeName(), attr.getNodeValue());
                    } catch (DOMException e) {
                        LOG.error(e);
                    } catch (BeanNotFoundException e) {
                        LOG.error(e);
                    }
                }
            }

            if (node.hasChildNodes()) {
                NodeList childNodes = node.getChildNodes();
                for (int i = 0; i < childNodes.getLength(); i++) {
                    parse(task, childNodes.item(i));
                }
            }

            return task;
        } catch (InstantiationException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IllegalArgumentException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (InvocationTargetException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (NoSuchMethodException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (SecurityException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        return null;
    }

    public void run() throws XeleneseException {
        suite.execute();
    }

    public void addTask(Class<Task> clazz) {
        TaskName taskName = clazz.getAnnotation(TaskName.class);
        taskMap.put(taskName.value(), clazz);
    }

//    public static void main(String[] args) {
//        InputStream is = null;
//        Xelenese xelenese = new Xelenese();
//        try {
//            is = Xelenese.class.getClassLoader().getResourceAsStream("com/shorindo/xelenoid/sample.xml");
//            Task task = foo(is);
//            System.out.println(task);
////            Document d = xelenese.load(is);
////            System.out.println(dump(d));
////        } catch (IOException e) {
////            e.printStackTrace();
//        } finally {
//            if (is != null)
//                try {
//                    is.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//        }
//    }

//    public Document load(InputStream is) throws IOException {
//        try {
//            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//            document = builder.parse(is);
//            return document;
//        } catch (ParserConfigurationException e) {
//            throw new IOException(e);
//        } catch (SAXException e) {
//            throw new IOException(e);
//        }
//    }

    public static String dump(Document document) throws IOException {
        StringWriter writer = new StringWriter();
        JAXB.marshal(document, writer);
        return writer.toString();
//        String xslt =
//                "<?xml version='1.0' encoding='UTF-8'?>" +
//                "<xsl:stylesheet version='1.0'" +
//                "     xmlns:xsl='http://www.w3.org/1999/XSL/Transform'" +
//                "     xmlns:xalan='http://xml.apache.org/xslt'>" +
//                "   <xsl:output method='xml' encoding='UTF-8'" +
//                "      indent='yes' xalan:indent-amount='4'/>" +
//                "   <xsl:template match='/'>" +
//                "      <xsl:copy-of select='.'/>" +
//                "   </xsl:template>" +
//                "</xsl:stylesheet>";
//        ByteArrayInputStream bais = new ByteArrayInputStream(xslt.getBytes());
//        StreamSource xsltSource = new StreamSource(bais);
//        try {
//            Transformer t = TransformerFactory.newInstance().newTransformer(xsltSource);
//            DOMSource xmlSource = new DOMSource(document);
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            StreamResult result = new StreamResult(baos);
//            t.transform(xmlSource, result);
//            return new String(baos.toByteArray(), "UTF-8");
//        } catch (TransformerConfigurationException e) {
//            throw new IOException(e);
//        } catch (TransformerFactoryConfigurationError e) {
//            throw new IOException(e);
//        } catch (TransformerException e) {
//            throw new IOException(e);
//        }
    }

//    public void run(Node node) throws XeleneseException {
//        switch (node.getNodeName()) {
//        case "driver":
//            doDriver(node);
//            break;
//        default:
//        }
//    }

//    private void doDriver(Node node) throws XeleneseException {
//        Node attr = node.getAttributes().getNamedItem("className");
//        try {
//            Class clazz = Class.forName(attr.getNodeValue());
//            driver = (WebDriver)clazz.newInstance();
//        } catch (ClassNotFoundException e) {
//            throw new XeleneseException(e);
//        } catch (DOMException e) {
//            throw new XeleneseException(e);
//        } catch (InstantiationException e) {
//            throw new XeleneseException(e);
//        } catch (IllegalAccessException e) {
//            throw new XeleneseException(e);
//        }
//    }

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
    
    public static class SuiteUnmarshallListener extends Listener {
        @Override
        public void beforeUnmarshal(Object target, Object parent) {
            super.beforeUnmarshal(target, parent);
            if (Task.class.isAssignableFrom(target.getClass())) {
                ((Task)target).setParent((Task)parent);
            }
        }
    }
}
