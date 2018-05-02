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
package com.shorindo.xelenese;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

/**
 * 
 */
public class TestTask extends Task {

    public TestTask() {
    }

    @Override
    public String getName() {
        return "test";
    }

    @XmlElements({
        @XmlElement(name="get", type=GetTask.class),
        @XmlElement(name="element", type=ElementTask.class),
        @XmlElement(name="forward", type=ForwardTask.class),
        @XmlElement(name="back", type=BackTask.class),
        @XmlElement(name="click", type=ClickTask.class),
        @XmlElement(name="close", type=CloseTask.class),
        @XmlElement(name="quit", type=QuitTask.class),
        @XmlElement(name="refresh", type=RefreshTask.class)
    })
    @Override
    public List<Task> getTaskList() {
        return super.getTaskList();
    }

}
