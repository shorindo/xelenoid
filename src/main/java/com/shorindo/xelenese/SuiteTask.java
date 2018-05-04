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
import javax.xml.bind.annotation.XmlTransient;

import org.openqa.selenium.WebDriver;

/**
 * 
 */
@TaskName("suite")
public class SuiteTask extends Task {
    private static final XeleneseLogger LOG = XeleneseLogger.getLogger(SuiteTask.class);
    private WebDriver driver;

    public SuiteTask(Task parent) {
        super(parent);
    }

    @Override
    public String getTaskName() {
        return "suite";
    }

    @XmlElements({
        @XmlElement(name="driver", type=DriverTask.class),
        @XmlElement(name="include", type=IncludeTask.class),
        @XmlElement(name="template", type=TemplateTask.class),
        @XmlElement(name="test", type=TestTask.class)
    })
    @Override
    public List<Task> getTaskList() {
        return super.getTaskList();
    }

    @Override
    public void execute() throws XeleneseException {
        LOG.debug("execute()");
        for (Task task : getTaskList()) {
            task.execute();
        }
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

}
