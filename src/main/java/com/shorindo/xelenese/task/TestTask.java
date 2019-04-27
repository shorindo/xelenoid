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

import com.shorindo.xelenese.XeleneseException;
import com.shorindo.xelenese.XeleneseLogger;
import com.shorindo.xelenese.annotation.ChildTasks;
import com.shorindo.xelenese.annotation.TaskName;

/**
 * 
 */
@TaskName("test")
@ChildTasks({"get", "element", "forward", "back", "click",
    "close", "quit", "refresh", "script" })
public class TestTask extends Task {
    private static final XeleneseLogger LOG = XeleneseLogger.getLogger(TestTask.class);
    private String depends;

    public TestTask(Task parent) {
        super(parent);
    }

    @Override
    public void execute(Object...args) throws XeleneseException {
        LOG.debug("execute()");
        try {
            for (Task task : getTaskList()) {
                task.execute();
            }
        } catch (Exception e) {
            if (ON_ERROR_CONTINUE.equals(getOnError())) {
                LOG.error(e);
            } else {
                throw e;
            }
        }
    }

    public String getDepends() {
        return depends;
    }

    public void setDepends(String depends) {
        this.depends = depends;
    }

}
