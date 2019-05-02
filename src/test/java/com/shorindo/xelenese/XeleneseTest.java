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

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;

import org.junit.Test;

import com.shorindo.xelenese.task.SuiteTask;
import com.shorindo.xelenese.task.Task;

/**
 * 
 */
public class XeleneseTest {

    @Test
    public void testSuite() throws Exception {
        Task suite = load("<suite></suite>");
        assertEquals(SuiteTask.class, suite.getClass());
        assertEquals("suite", suite.getTaskName());
    }

    @Test
    public void testSuiteTitle() throws Exception {
        Task suite = load("<suite title='title'></suite>");
        assertEquals("title", suite.getTitle());
    }

    @Test
    public void testSuiteText() throws Exception {
        Task suite = load("<suite>text</suite>");
        assertEquals("text", suite.getText());
    }

    @Test
    public void testSuiteOnError() throws Exception {
        Task suite = load("<suite onError='ignore'></suite>");
        assertEquals("ignore", suite.getOnError());
    }

    @Test
    public void testSuiteUnknownAttr() throws Exception {
        Task suite = load("<suite foo='bar'></suite>");
    }

    @Test
    public void testUnkownTag() throws Exception {
        Task suite = load("<foo></foo>");
    }

    private Task load(String suite) throws Exception {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(suite.getBytes())) {
            Xelenese xelenese = new Xelenese(bais);
            return xelenese.getRoot();
        }
    }
}
