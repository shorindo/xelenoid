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

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebElement;

import com.shorindo.xelenese.XeleneseException;
import com.shorindo.xelenese.XeleneseLogger;
import com.shorindo.xelenese.annotation.ChildTasks;
import com.shorindo.xelenese.annotation.TaskName;

/**
 * 
 */
@TaskName("element")
@ChildTasks({"element", "click", "keys", "verify", "assert", "wait"})
public class ElementTask extends LocatableTask {
    private static final XeleneseLogger LOG = XeleneseLogger.getLogger(ElementTask.class);

    public ElementTask(Task parent) {
        super(parent);
    }

    @Override
    public boolean execute(Object...args) throws XeleneseException {
        boolean result = true;
        LOG.debug("execute()");
        WebElement element = getDriver().findElement(by);
        for (Task task : getTaskList()) {
            if (!task.execute(element)) {
                result = false;
            }
        }
        return result;
    }

    @Override
    public List<ValidationError> validate() throws XeleneseException {
        // TODO Auto-generated method stub
        return new ArrayList<ValidationError>();
    }

}
