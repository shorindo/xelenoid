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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

import com.shorindo.xelenese.task.SuiteTask;
import com.shorindo.xelenese.task.Task;
import com.shorindo.xelenese.task.TestTask;

/**
 * 
 */
@RunWith(XeleneseRunner.class)
public class XeleneseRunner extends Runner {
    private static final XeleneseLogger LOG = XeleneseLogger.getLogger(XeleneseRunner.class);
    private String suiteTitle;
    private SuiteTask suite;
    private Map<String,Task> taskMap = new HashMap<String,Task>();
    private Class<?> caseClass;

    /**
     * 
     * @param caseClass
     * @throws Exception
     */
    public XeleneseRunner(Class<?> caseClass) throws InitializationError {
        this.caseClass = caseClass;
        String fileName = System.getProperty("xelenese.testcase");
        XeleneseOptions opts = caseClass.getAnnotation(XeleneseOptions.class);
        if (opts != null) {
            fileName = opts.value();
        }
        if (fileName == null) {
            throw new InitializationError("XeleneseCase annotation or system property[xelenese.testcase] not set.");
        }
        try (InputStream is = new FileInputStream(new File(fileName))) {
            Xelenese xelenese = new Xelenese(is);
            suite = (SuiteTask)xelenese.getRoot();
            suite.init();
            suiteTitle = suite.getTitle();
        } catch (Exception e) {
            new InitializationError(e);
        }
    }

    /**
     * 
     */
    @Override
    public Description getDescription() {
        Description desc = Description.createSuiteDescription(suiteTitle);
        for (Task task : suite.getTaskList()) {
            if (task instanceof TestTask) {
                desc.addChild(Description.createTestDescription(suiteTitle, task.getTitle()));
                taskMap.put(task.getTitle(), task);
            }
        }
        return desc;
    }

    /**
     * 
     */
    @Override
    public void run(RunNotifier notifier) {
        try {
            Object caseObject = caseClass.newInstance();
            for (Method method : caseClass.getMethods()) {
                BeforeClass beforeClass = method.getAnnotation(BeforeClass.class);
                if (beforeClass != null) {
                    method.invoke(caseObject);
                }
            }
        } catch (InstantiationException e) {
            LOG.error(e);
        } catch (IllegalAccessException e) {
            LOG.error(e);
        } catch (IllegalArgumentException e) {
            LOG.error(e);
        } catch (InvocationTargetException e) {
            LOG.error(e);
        }
        Description desc = getDescription();
        for (Description child : desc.getChildren()) {
            notifier.fireTestStarted(child);
            try {
                Task task = taskMap.get(child.getMethodName());
                if (!task.execute()) {
                    notifier.fireTestFailure(new Failure(child, new AssertionError()));
                }
            } catch (Exception e) {
                notifier.fireTestFailure(new Failure(child, e));
            } finally {
                notifier.fireTestFinished(child);
            }
        }
    }

}
