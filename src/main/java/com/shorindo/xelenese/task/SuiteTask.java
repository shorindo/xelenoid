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

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import com.shorindo.xelenese.ExecutionError;
import com.shorindo.xelenese.ValidationError;
import com.shorindo.xelenese.XeleneseException;
import com.shorindo.xelenese.XeleneseLogger;
import com.shorindo.xelenese.annotation.ChildTasks;
import com.shorindo.xelenese.annotation.TaskName;

/**
 * 
 */
@TaskName("suite")
@ChildTasks({"driver", "template", "test"})
public class SuiteTask extends Task {
    private static final XeleneseLogger LOG = XeleneseLogger.getLogger(SuiteTask.class);
    private WebDriver driver;

    public SuiteTask(Task parent) {
        super(parent);
    }

    @Override
    public List<ExecutionError> execute(Object...args) throws XeleneseException {
        //LOG.debug("execute() - " + toString());
        List<ExecutionError> errors = new ArrayList<ExecutionError>();
        try {
            init();
            run();
        } catch (Throwable th) {
            errors.add(new ExecutionError(this, th));
        }
        return errors;
    }

    @Override
    public List<ValidationError> validate() throws XeleneseException {
        // TODO Auto-generated method stub
        return new ArrayList<ValidationError>();
    }

    @Override
    protected XeleneseLogger getLogger() {
        return LOG;
    }

    public void init() throws XeleneseException {
        for (Task task : getTaskList()) {
            if (!(task instanceof TestTask)) {
                task.evaluate();
            }
        }
    }
    public void run() throws XeleneseException {
        for (Task task : getTaskList()) {
            if (task instanceof TestTask) {
                task.evaluate();
            }
        }
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

}
