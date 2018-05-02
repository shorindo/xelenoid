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
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 */
@XmlRootElement(name="suite")
public class SuiteTask extends Task {

    public SuiteTask() {
    }

    @Override
    public String getName() {
        return "suite";
    }

    @XmlElements({
        @XmlElement(name="driver", type=DriverTask.class),
        @XmlElement(name="include", type=IncludeTask.class),
        @XmlElement(name="template", type=TemplateTask.class),
        @XmlElement(name="test", type=TestTask.class)
    })
    @Override
    public List<Task> getTaskList() {
        return super.getTaskList();
    }

}
