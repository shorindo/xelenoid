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
package com.shorindo.xelenese.task;

import org.openqa.selenium.By;

/**
 * 
 */
public abstract class LocatableTask extends Task {
    private String id;
    private String name;
    private String linkText;
    private String className;
    private String cssSelector;
    private String tagName;
    private String xpath;
    protected By by;

    /**
     * @param parent
     */
    public LocatableTask(Task parent) {
        super(parent);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        this.by = By.id(id);
    }

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }

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

}
