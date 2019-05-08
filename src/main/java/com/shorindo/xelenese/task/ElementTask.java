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

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.shorindo.xelenese.ExecutionError;
import com.shorindo.xelenese.ValidationError;
import com.shorindo.xelenese.XeleneseException;
import com.shorindo.xelenese.XeleneseLogger;
import com.shorindo.xelenese.annotation.ChildTasks;
import com.shorindo.xelenese.annotation.TaskName;

/**
 * 
 */
@TaskName("element")
@ChildTasks({"element", "clear", "click", "keys", "verify", "assert", "wait"})
public class ElementTask extends Task {
    private static final XeleneseLogger LOG = XeleneseLogger.getLogger(ElementTask.class);
    private String id;
    private String name;
    private String linkText;
    private String className;
    private String cssSelector;
    private String tagName;
    private String xpath;
    private boolean present = true;
    protected By by;

    public ElementTask(Task parent) {
        super(parent);
    }

    @Override
    public List<ExecutionError> execute(Object...args) throws XeleneseException {
        //LOG.debug("execute() - " + toString());
        List<ExecutionError> errors = new ArrayList<ExecutionError>();
        List<WebElement> elements = getDriver().findElements(by);
        if (present) {
            if (elements.size() > 0) {
                for (WebElement element : elements) {
                    for (Task task : getTaskList()) {
                        errors.addAll(task.execute(element));
                    }
                }
            } else {
                ExecutionError e = new ExecutionError(this, "Specified element exists.");
                errors.add(e);
                if (!ON_ERROR_IGNORE.equals(getOnError())) {
                    throw e;
                }
            }
        } else {
            if (elements.size() > 0) {
                ExecutionError e = new ExecutionError(this, "Specified element exists.");
                errors.add(e);
                if (!ON_ERROR_IGNORE.equals(getOnError())) {
                    throw e;
                }
            }
        }
        return errors;
    }

    @Override
    public List<ValidationError> validate() throws XeleneseException {
        // TODO Auto-generated method stub
        return new ArrayList<ValidationError>();
    }

    @Override
    protected XeleneseLogger getLogger() {
        return LOG;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        this.by = By.id(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLinkText() {
        return linkText;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
        this.by = By.linkText(linkText);
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
        this.by = By.className(className);
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
        this.by = By.tagName(tagName);
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
        this.by = By.xpath(xpath);
    }

    public String getCssSelector() {
        return cssSelector;
    }

    public void setCssSelector(String cssSelector) {
        this.cssSelector = cssSelector;
        this.by = By.cssSelector(cssSelector);
    }

    public boolean getPresent() {
        return present;
    }

    public void setPresent(String present) {
        if ("true".equals(present)) {
            this.present = true;
        } else if ("false".equals(present)) {
            this.present = false;
        } else {
            LOG.warn("Invalid present property:{}", present);
        }
    }

    public String toString() {
        String byName = by.toString()
                .replaceAll(".*?([a-zA-Z0-9]+):\\s*(.*)$", "$1='$2'");
        StringBuilder sb = new StringBuilder("<" + getTaskName())
            .append(" " + byName)
            .append(" onError='" + getOnError() + "'")
            .append(" present='" + present + "'")
            .append(">");
        return sb.toString();
    }
}
