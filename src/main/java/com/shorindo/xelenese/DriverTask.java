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

import javax.xml.bind.annotation.XmlAttribute;

import org.openqa.selenium.WebDriver;

/**
 * 
 */
@TaskName("driver")
public class DriverTask extends Task {
    private static final XeleneseLogger LOG = XeleneseLogger.getLogger(DriverTask.class);
    private String className;
    private String options;
    private String capabilities;
    private WebDriver driver;

    public DriverTask(Task parent) {
        super(parent);
    }

    @Override
    public String getTaskName() {
        return "driver";
    }

    @Override
    public void execute() {
        LOG.debug("execute()");
        try {
            driver = (WebDriver)Class.forName(className).newInstance();
            Task parent = getParent();
            while (true) {
                if (parent == null) {
                    return;
                } else if (parent instanceof SuiteTask) {
                    ((SuiteTask)parent).setDriver(driver);
                    return;
                }
            }
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getClassName() {
        return className;
    }

    public String getOptions() {
        return options;
    }

    public String getCapabilities() {
        return capabilities;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public void setCapabilities(String capabilities) {
        this.capabilities = capabilities;
    }

}
