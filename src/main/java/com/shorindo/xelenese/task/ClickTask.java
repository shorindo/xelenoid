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
import com.shorindo.xelenese.annotation.TaskName;

/**
 * 
 */
@TaskName("click")
public class ClickTask extends Task {
    private static final XeleneseLogger LOG = XeleneseLogger.getLogger(ClickTask.class);

    public ClickTask(Task parent) {
        super(parent);
    }

    @Override
    public boolean execute(Object...args) {
        LOG.debug("execute(" + args[0] + ")");
        if (args != null && args.length > 0) {
            WebElement element = (WebElement)args[0];
            element.click();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<ValidationError> validate() throws XeleneseException {
        return new ArrayList<ValidationError>();
    }
}
