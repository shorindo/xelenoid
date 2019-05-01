/*
 * Copyright 2018-2019 Shorindo, Inc.
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

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
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
import com.shorindo.xelenese.task.BackTask;
import com.shorindo.xelenese.task.ClickTask;
import com.shorindo.xelenese.task.CloseTask;
import com.shorindo.xelenese.task.DriverTask;
import com.shorindo.xelenese.task.ElementTask;
import com.shorindo.xelenese.task.ForwardTask;
import com.shorindo.xelenese.task.GetTask;
import com.shorindo.xelenese.task.IncludeTask;
import com.shorindo.xelenese.task.KeysTask;
import com.shorindo.xelenese.task.QuitTask;
import com.shorindo.xelenese.task.RefreshTask;
import com.shorindo.xelenese.task.ScriptTask;
import com.shorindo.xelenese.task.SuiteTask;
import com.shorindo.xelenese.task.Task;
import com.shorindo.xelenese.task.TemplateTask;
import com.shorindo.xelenese.task.TestTask;
import com.shorindo.xelenese.task.TextTask;
import com.shorindo.xelenese.task.TitleTask;
import com.shorindo.xelenese.task.WaitTask;
import com.shorindo.xelenese.util.BeanUtil;
import com.shorindo.xelenese.util.BeanUtil.BeanNotFoundException;

/**
 * 
 */
public class Xelenese {
    private static final XeleneseLogger LOG = XeleneseLogger.getLogger(Xelenese.class);
    private Task suite;
    private Map<String, Class<? extends Task>> taskMap = new HashMap<String, Class<? extends Task>>() {
        private static final long serialVersionUID = 1L;
        {
            put("back", BackTask.class);
            put("click", ClickTask.class);
            put("close", CloseTask.class);
            put("driver", DriverTask.class);
            put("element", ElementTask.class);
            put("forward", ForwardTask.class);
            put("get", GetTask.class);
            put("include", IncludeTask.class);
            put("keys", KeysTask.class);
            put("quit", QuitTask.class);
            put("refresh", RefreshTask.class);
            put("script", ScriptTask.class);
            put("suite", SuiteTask.class);
            put("template", TemplateTask.class);
            put("test", TestTask.class);
            put("text", TextTask.class);
            put("title", TitleTask.class);
            put("wait", WaitTask.class);
        }
    };

    /**
     * 
     * @param is
     * @throws XeleneseException
     */
    public Xelenese(InputStream is) throws XeleneseException {
        load(is);
    }

    /**
     * 
     * @return
     */
    public Task getRoot() {
        return suite;
    }

    /**
     * 
     * @param is
     * @throws XeleneseException
     */
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

    /**
     * 
     * @param parent
     * @param node
     * @return
     */
    private Task parse(Task parent, Node node) {
        String taskName = node.getNodeName();
        Class<? extends Task> taskClass = taskMap.get(taskName);

        if (taskClass == null) {
            LOG.warn("[{}] is unknown.", taskName);
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

    /**
     * 
     * @throws XeleneseException
     */
    public void run() throws XeleneseException {
        suite.execute();
    }

//    /**
//     * 
//     * @param clazz
//     */
//    public void addTask(Class<Task> clazz) {
//        TaskName taskName = clazz.getAnnotation(TaskName.class);
//        taskMap.put(taskName.value(), clazz);
//    }

    /**
     * 
     * @param document
     * @return
     * @throws IOException
     */
    public static String dump(Document document) throws IOException {
        StringWriter writer = new StringWriter();
        JAXB.marshal(document, writer);
        return writer.toString();
    }

}
