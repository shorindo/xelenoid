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

import com.shorindo.xelenese.XeleneseException;
import com.shorindo.xelenese.XeleneseLogger;
import com.shorindo.xelenese.annotation.TaskName;

/**
 * 
 */
@TaskName("forward")
public class ForwardTask extends Task {
    private static final XeleneseLogger LOG = XeleneseLogger.getLogger(ForwardTask.class);

    public ForwardTask(Task parent) {
        super(parent);
    }

    @Override
    public List<ExecutionError> execute(Object...args) {
        LOG.debug("execute()");
        List<ExecutionError> errors = new ArrayList<ExecutionError>();
        try {
            getDriver().navigate().forward();
        } catch (Exception e) {
            errors.add(new ExecutionError(this, e));
        }
        return errors;
    }

    @Override
    public List<ValidationError> validate() throws XeleneseException {
        return new ArrayList<ValidationError>();
    }
}
