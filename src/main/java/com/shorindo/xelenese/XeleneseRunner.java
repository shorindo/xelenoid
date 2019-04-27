/*
 * Copyright 2019 Shorindo, Inc.
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

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

import com.shorindo.xelenese.task.SuiteTask;
import com.shorindo.xelenese.task.Task;
import com.shorindo.xelenese.task.TestTask;

/**
 * 
 */
@RunWith(XeleneseRunner.class)
public class XeleneseRunner extends Runner {
    private String suiteName;
    private SuiteTask suite;
    private Map<String,Task> taskMap = new HashMap<String,Task>();

    public XeleneseRunner(Class<?> testCase) throws Exception {
        suiteName = testCase.getSimpleName();
        String fileName = System.getProperty("xelenese.testcase");
        if (fileName == null) {
            throw new XeleneseException("System property[xelenese.testcase] not set.");
        } else {
            InputStream is = new FileInputStream(fileName);
            Xelenese xelenese = new Xelenese(is);
            suite = (SuiteTask)xelenese.getRoot();
            suite.init();
        }
    }

    @Override
    public Description getDescription() {
        Description desc = Description.createSuiteDescription(suiteName);
        for (Task task : suite.getTaskList()) {
            if (task instanceof TestTask) {
                desc.addChild(Description.createTestDescription(suiteName, task.getName()));
                taskMap.put(task.getName(), task);
            }
        }
        return desc;
    }

    @Override
    public void run(RunNotifier notifier) {
        Description desc = getDescription();
        for (Description child : desc.getChildren()) {
            notifier.fireTestStarted(child);
            try {
                Task task = taskMap.get(child.getMethodName());
                task.execute();
            } catch (Exception e) {
                notifier.fireTestFailure(new Failure(child, e));
                throw new RuntimeException(e);
            } finally {
                notifier.fireTestFinished(child);
            }
        }
    }

}
