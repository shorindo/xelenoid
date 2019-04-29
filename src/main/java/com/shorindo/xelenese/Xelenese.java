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
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXB;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.shorindo.xelenese.annotation.TaskName;
import com.shorindo.xelenese.task.SuiteTask;
import com.shorindo.xelenese.task.Task;
import com.shorindo.xelenese.util.BeanUtil;
import com.shorindo.xelenese.util.BeanUtil.BeanNotFoundException;

/**
 * 
 */
public class Xelenese {
    private static final XeleneseLogger LOG = XeleneseLogger.getLogger(Xelenese.class);
    private Map<String, Class<Task>> taskMap = new HashMap<String, Class<Task>>();
    private Task suite;

    public Xelenese(InputStream is) throws XeleneseException {
        setup();
        load(is);
        //LOG.debug(suite.toString());
    }

    public Task getRoot() {
        return suite;
    }

    @SuppressWarnings("unchecked")
    private void setup() {
        String path = getClass().getName().replaceAll("\\.", "/") + ".class";
        URL url = getClass().getClassLoader().getResource(path);
        int prefix = url.getFile().length() - path.length();
        File dir = new File(new File(url.getFile()).getParentFile(), "task");
        for (File file : dir.listFiles()) {
            try {
                String absPath = file.toURI().toURL().getFile();
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
                        LOG.warn("[{0}] has no TaskName annotation.", clazz.getName());
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
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
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
            LOG.warn("[{0}] is unknown.", taskName);
            return null;
        }

        try {
            Task task = taskClass
                    .getConstructor(Task.class)
                    .newInstance(parent);

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
                    Node child = childNodes.item(i);
                    switch (child.getNodeType()) {
                    case Node.ELEMENT_NODE:
                        parse(task, child);
                        break;
                    case Node.CDATA_SECTION_NODE:
                    case Node.TEXT_NODE:
                        task.addText(child.getNodeValue());
                        break;
                    default:
                    }
                }
            }

            return task;
        } catch (InstantiationException e) {
            LOG.error(e);
        } catch (IllegalAccessException e) {
            LOG.error(e);
        } catch (IllegalArgumentException e) {
            LOG.error(e);
        } catch (InvocationTargetException e) {
            LOG.error(e);
        } catch (NoSuchMethodException e) {
            LOG.error(e);
        } catch (SecurityException e) {
            LOG.error(e);
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

    public static String dump(Document document) throws IOException {
        StringWriter writer = new StringWriter();
        JAXB.marshal(document, writer);
        return writer.toString();
    }

}
