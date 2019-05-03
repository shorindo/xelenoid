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
package com.shorindo.xelenese.task;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;

import com.shorindo.xelenese.ExecutionError;
import com.shorindo.xelenese.ValidationError;
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
    private String driverName;
    private String driverExec;
    private String options;
    private String capabilities;

    public DriverTask(Task parent) {
        super(parent);
    }

    @Override
    public List<ExecutionError> execute(Object...args) throws XeleneseException {
        //LOG.debug("execute() - " + toString());
        List<ExecutionError> errors = new ArrayList<ExecutionError>();
        try {
            if (driverName != null && driverExec != null) {
                System.setProperty(driverName, driverExec);
            }
            WebDriver driver = (WebDriver)Class.forName(className).newInstance();
            Task parent = getParent();
            while (parent != null) {
                if (parent instanceof SuiteTask) {
                    ((SuiteTask)parent).setDriver(driver);
                    return errors;
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
        return errors;
    }

    @Override
    public List<ValidationError> validate() throws XeleneseException {
        // TODO
        return new ArrayList<ValidationError>();
    }

    @Override
    protected XeleneseLogger getLogger() {
        return LOG;
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

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverExec() {
        return driverExec;
    }

    public void setDriverExec(String driverExec) {
        this.driverExec = driverExec;
    }

}
