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

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

/**
 * 
 */
@TaskName("element")
public class ElementTask extends Task {
    private static final XeleneseLogger LOG = XeleneseLogger.getLogger(ElementTask.class);
    private String id;
    private String className;
    private String cssSelector;
    private String xpath;

    public ElementTask(Task parent) {
        super(parent);
    }

    @Override
    public String getTaskName() {
        return "element";
    }

    @Override
    public void execute() throws XeleneseException {
        LOG.debug("execute()");
        for (Task task : getTaskList()) {
            task.execute();
        }
    }

    @XmlElements({
        @XmlElement(name="click", type=ClickTask.class),
        @XmlElement(name="keys", type=KeysTask.class),
        @XmlElement(name="verify", type=VerifyTask.class),
        @XmlElement(name="assert", type=AssertTask.class)
    })
    @Override
    public List<Task> getTaskList() {
        return super.getTaskList();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCssSelector() {
        return cssSelector;
    }

    public void setCssSelector(String cssSelector) {
        this.cssSelector = cssSelector;
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

}
