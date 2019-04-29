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
package com.shorindo.xelenese.task;

import org.openqa.selenium.WebDriver;

import com.shorindo.xelenese.XeleneseException;
import com.shorindo.xelenese.XeleneseLogger;
import com.shorindo.xelenese.annotation.TaskName;

/**
 * 
 */
@TaskName("driver")
public class DriverTask extends Task {
    private static final XeleneseLogger LOG = XeleneseLogger.getLogger(DriverTask.class);
    private String className;
    private String options;
    private String capabilities;

    public DriverTask(Task parent) {
        super(parent);
    }

    @Override
    public boolean execute(Object...args) throws XeleneseException {
        LOG.debug("execute()");
        try {
            WebDriver driver = (WebDriver)Class.forName(className).newInstance();
            Task parent = getParent();
            while (parent != null) {
                if (parent instanceof SuiteTask) {
                    ((SuiteTask)parent).setDriver(driver);
                    return true;
                }
                parent = parent.getParent();
            }
        } catch (InstantiationException e) {
            throw new XeleneseException(e);
        } catch (IllegalAccessException e) {
            throw new XeleneseException(e);
        } catch (ClassNotFoundException e) {
            throw new XeleneseException(e);
        }
        return false;
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
