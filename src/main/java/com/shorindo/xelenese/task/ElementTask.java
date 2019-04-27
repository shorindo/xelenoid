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

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.shorindo.xelenese.XeleneseException;
import com.shorindo.xelenese.XeleneseLogger;
import com.shorindo.xelenese.annotation.ChildTasks;
import com.shorindo.xelenese.annotation.TaskName;

/**
 * 
 */
@TaskName("element")
@ChildTasks({"element", "click", "keys", "verify", "assert"})
public class ElementTask extends Task {
    private static final XeleneseLogger LOG = XeleneseLogger.getLogger(ElementTask.class);
    private By by;
    private String id;
    private String name;
    private String className;
    private String tagName;
    private String xpath;
    private String cssSelector;
    private String linkText;

    public ElementTask(Task parent) {
        super(parent);
    }

    @Override
    public void execute(Object...args) throws XeleneseException {
        LOG.debug("execute()");
        WebElement element = getDriver().findElement(by);
        for (Task task : getTaskList()) {
            task.execute(element);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        this.by = By.id(id);
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
        this.by = By.className(className);
    }

    public String getCssSelector() {
        return cssSelector;
    }

    public void setCssSelector(String cssSelector) {
        this.cssSelector = cssSelector;
        this.by = By.cssSelector(cssSelector);
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
        this.by = By.xpath(xpath);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.by = By.name(name);
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getLinkText() {
        return linkText;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
        this.by = By.linkText(linkText);
    }

}
