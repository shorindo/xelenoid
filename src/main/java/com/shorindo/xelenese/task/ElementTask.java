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

import org.openqa.selenium.By.ById;
import org.openqa.selenium.WebElement;

import com.shorindo.xelenese.ExecutionError;
import com.shorindo.xelenese.ValidationError;
import com.shorindo.xelenese.XeleneseException;
import com.shorindo.xelenese.XeleneseLogger;
import com.shorindo.xelenese.annotation.ChildTasks;
import com.shorindo.xelenese.annotation.TaskName;

/**
 * 
 */
@TaskName("element")
@ChildTasks({"element", "clear", "click", "keys", "verify", "assert", "wait"})
public class ElementTask extends LocatableTask {
    private static final XeleneseLogger LOG = XeleneseLogger.getLogger(ElementTask.class);
    private boolean present = true;

    public ElementTask(Task parent) {
        super(parent);
    }

    @Override
    public List<ExecutionError> execute(Object...args) throws XeleneseException {
        LOG.debug("execute() - " + toString());
        List<ExecutionError> errors = new ArrayList<ExecutionError>();
        try {
            List<WebElement> elements = getDriver().findElements(by);
            if (present) {
                if (elements.size() > 0) {
                    for (WebElement element : elements) {
                        for (Task task : getTaskList()) {
                            errors.addAll(task.execute(element));
                        }
                    }
                } else {
                    throw new ExecutionError(this, "Specified element not exist.");
                }
            } else {
                if (elements.size() > 0) {
                    ExecutionError e = new ExecutionError(this, "Specified element exists.");
                    LOG.error(e);
                    errors.add(e);
                    if (!ON_ERROR_IGNORE.equals(getOnError())) {
                        throw e;
                    }
                } else {
                    
                }
            }
        } catch (Throwable th) {
            LOG.error(th);
            errors.add(new ExecutionError(this, th));
            if (!ON_ERROR_IGNORE.equals(getOnError())) {
                throw th;
            }
        }
        return errors;
    }

    @Override
    public List<ValidationError> validate() throws XeleneseException {
        // TODO Auto-generated method stub
        return new ArrayList<ValidationError>();
    }

    public boolean getPresent() {
        return present;
    }

    public void setPresent(String present) {
        if ("true".equals(present)) {
            this.present = true;
        } else if ("false".equals(present)) {
            this.present = false;
        } else {
            LOG.warn("Invalid present property:{}", present);
        }
    }

    public String toString() {
        String byName = by.toString()
                .replaceAll(".*?([a-zA-Z0-9]+):\\s*(.*)$", "$1='$2'");
        StringBuilder sb = new StringBuilder("<" + getTaskName())
            .append(" " + byName)
            .append(" onError='" + getOnError() + "'")
            .append(" present='" + present + "'")
            .append(">");
        return sb.toString();
    }
}
