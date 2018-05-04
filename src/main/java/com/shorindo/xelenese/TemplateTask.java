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
@TaskName("template")
public class TemplateTask extends Task {
    private static final XeleneseLogger LOG = XeleneseLogger.getLogger(TemplateTask.class);
    private String name;

    public TemplateTask(Task parent) {
        super(parent);
    }

    @Override
    public String getTaskName() {
        return "template";
    }

    @XmlElements({
        @XmlElement(name="get", type=GetTask.class),
        @XmlElement(name="element", type=ElementTask.class)
    })
    @Override
    public List<Task> getTaskList() {
        return super.getTaskList();
    }

    @Override
    public void execute() {
        LOG.debug("execute()");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
